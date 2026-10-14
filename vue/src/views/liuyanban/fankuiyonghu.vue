<template>
    <div class="views-liuyanban-list">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 留言反馈查询 </span>
                    </div>
                </template>

                <el-table border :data="lists" style="width: 100%" @sort-change="sortChange" highlight-current-row>
                    <el-table-column type="index" label="#"></el-table-column>
                    <!-- 序号 -->

                    <el-table-column prop="xingming" label="姓名" width="180">
                        <template #default="{ row }"> {{ row.xingming }} </template>
                    </el-table-column>
                    <el-table-column prop="lianxidianhua" label="联系电话" width="180">
                        <template #default="{ row }"> {{ row.lianxidianhua }} </template>
                    </el-table-column>
                    <el-table-column prop="fankuineirong" label="反馈内容">
                        <template #default="{ row }"> {{ row.fankuineirong }} </template>
                    </el-table-column>
                    <el-table-column prop="fankuiyonghu" label="反馈用户" width="180">
                        <template #default="{ row }"> {{ row.fankuiyonghu }} </template>
                    </el-table-column>
                    <el-table-column prop="fankuizhuangtai" label="反馈状态" width="180">
                        <template #default="{ row }"> {{ row.fankuizhuangtai }} </template>
                    </el-table-column>
                    <el-table-column prop="addtime" label="反馈时间">
                        <template #default="{ row }"> {{ row.addtime.substring(0, 19) }} </template>
                    </el-table-column>
                    <el-table-column prop="fankuihuifu" label="反馈回复">
                        <template #default="{ row }"> {{ row.fankuihuifu }} </template>
                    </el-table-column>
                    <el-table-column prop="huifushijian" label="回复时间" width="180">
                        <template #default="{ row }"> {{ row.huifushijian }} </template>
                    </el-table-column>

                    <el-table-column label="操作" fixed="right" width="250">
                        <template #default="{ row }">
                            <el-button-group>
                                <el-tooltip effect="dark" content="删除" placement="top-start"><el-button type="danger" :icon="Delete" size="small" @click="deleteItems(row.id)">删除</el-button> </el-tooltip>
                            </el-button-group>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="e-pages" style="margin-top: 10px; text-align: center">
                    <el-pagination @current-change="loadList" :page-sizes="[12, 24, 36, 48, 60]" v-model:current-page="search.page" v-model:page-size="search.pagesize" @size-change="sizeChange" layout="total, sizes, prev, pager, next" :total="totalCount"> </el-pagination>
                </div>
            </el-card>
        </div>
    </div>
</template>

<script setup>
    import http from "@/utils/ajax/http";
    import DB from "@/utils/db";
    import router from "@/router";

    import { ref, reactive, watch, unref, onBeforeMount } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { canLiuyanbanSelect, useLiuyanbanSelect, canLiuyanbanDelete } from "@/module";
    import { extend } from "@/utils/extend";
    import { ElMessageBox, ElMessage } from "element-plus";
    import { Delete } from "@element-plus/icons-vue";

    const route = useRoute();
    const search = reactive({
        page: 1, // 当前页
        pagesize: 12, // 每页行数
        orderby: "id", // 排序字段
        sort: "desc", // 排序类型
    });
    extend(search, route.query);
    // 链接参数变化时更新这些内容
    watch(
        () => route.query,
        () => {
            extend(search, route.query);
            loadList(1);
        },
        { deep: true }
    );

    // 总行数
    const totalCount = ref(0);
    /**
     * 列表数据
     * @type {ELiuyanban[]}
     */
    const lists = ref([]);
    // 加载状态
    const loading = ref(false);

    // 排序操作
    const sortChange = (e) => {
        console.log(e);
        if (e.order == null) {
            search.orderby = "id";
            search.sort = "desc";
        } else {
            search.orderby = e.prop;
            search.sort = e.order == "ascending" ? "asc" : "desc";
        }
        loadList(1);
    };
    // 设置页数多少
    const sizeChange = (e) => {
        search.pagesize = e;
        loadList(1);
    };

    const deleteItems = (ids) => {
        return new Promise((resolve, reject) => {
            ElMessageBox.confirm("确定删除？")
                .then((res) => {
                    canLiuyanbanDelete(ids).then((res) => {
                        if (res.code == 0) {
                            ElMessage.success("删除成功");
                            loadList(search.page);
                            resolve(res.data);
                        } else {
                            ElMessage.error(res.msg);
                        }
                    });
                })
                .catch((err) => {
                    reject(err);
                });
        });
    };

    // 加载留言反馈列表方法
    const loadList = (page) => {
        // 加载
        if (unref(loading)) return;
        loading.value = true;
        search.page = page;

        http.post("/api/liuyanban/selectFankuiyonghu", search).then(
            (res) => {
                loading.value = false;
                if (res.code == 0) {
                    var data = res.data;
                    lists.value = data.lists.records;
                    totalCount.value = data.lists.total;
                }
            },
            (err) => {
                loading.value = false;
                ElMessage.error(err.message);
            }
        );
    };

    onBeforeMount(() => {
        loadList(1);
    });
</script>

<style scoped lang="scss">
    .views-liuyanban-list {
    }
</style>
