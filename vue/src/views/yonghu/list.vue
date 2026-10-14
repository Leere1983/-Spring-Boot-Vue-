<template>
    <div class="views-yonghu-list">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 用户查询 </span>
                    </div>
                </template>

                <div class="form-search">
                    <el-form @submit.prevent.stop :inline="true" size="small">
                        <el-form-item label="用户名">
                            <el-input v-model="search.yonghuming"></el-input>
                        </el-form-item>
                        <el-form-item label="姓名">
                            <el-input v-model="search.xingming"></el-input>
                        </el-form-item>
                        <el-form-item label="性别">
                            <el-select v-model="search.xingbie"
                                ><el-option label="请选择" value=""></el-option><el-option label="男" value="男"></el-option>
                                <el-option label="女" value="女"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="手机">
                            <el-input v-model="search.shouji"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="searchSubmit" icon="el-icon-search">查询</el-button>
                        </el-form-item>
                    </el-form>
                </div>

                <el-table border :data="lists" style="width: 100%" @sort-change="sortChange" highlight-current-row>
                    <el-table-column type="index" label="#"></el-table-column>
                    <!-- 序号 -->

                    <el-table-column prop="yonghuming" label="用户名" width="180">
                        <template #default="{row}"> {{ row.yonghuming }} </template>
                    </el-table-column>
                    <el-table-column prop="mima" label="密码">
                        <template #default="{row}"> {{ row.mima }} </template>
                    </el-table-column>
                    <el-table-column prop="xingming" label="姓名" width="180">
                        <template #default="{row}"> {{ row.xingming }} </template>
                    </el-table-column>
                    <el-table-column prop="xingbie" label="性别" width="180">
                        <template #default="{row}"> {{ row.xingbie }} </template>
                    </el-table-column>
                    <el-table-column sortable="custom" prop="qianbaoyue" label="钱包余额" width="180">
                        <template #default="{row}"> {{row.qianbaoyue}} </template>
                    </el-table-column>
                    <el-table-column prop="shouji" label="手机" width="180">
                        <template #default="{row}"> {{ row.shouji }} </template>
                    </el-table-column>
                    <el-table-column prop="youxiang" label="邮箱" width="180">
                        <template #default="{row}"> {{ row.youxiang }} </template>
                    </el-table-column>
                    <el-table-column prop="shenfenzheng" label="身份证" width="180">
                        <template #default="{row}"> {{ row.shenfenzheng }} </template>
                    </el-table-column>
                    <el-table-column prop="touxiang" label="头像" width="180">
                        <template #default="{row}"> <e-img :src="row.touxiang" style="max-width: 120px" /> </template>
                    </el-table-column>

                    <el-table-column label="操作" fixed="right" width="250">
                        <template #default="{row}">
                            <el-button-group>
                                <template v-if="$session.cx == '用户'">

                                    <el-button type="primary" :icon="Plus" size="small" @click="$router.push('/admin/yonghutixian/add?id='+row.id)">提现</el-button>

                                </template>


                                <template v-if="$session.cx == '管理员'">

                                    <el-tooltip effect="dark" content="编辑" placement="top-start"
                                    ><el-button type="success" :icon="Edit" size="small" @click="$router.push('/admin/yonghu/updt?id='+row.id)">编辑</el-button>
                                    </el-tooltip>
                                    <el-tooltip effect="dark" content="删除" placement="top-start"
                                    ><el-button type="danger" :icon="Delete" size="small" @click="deleteItems(row.id)">删除</el-button>
                                    </el-tooltip>

                                </template>



                            </el-button-group>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="e-pages" style="margin-top: 10px; text-align: center">
                    <el-pagination
                        @current-change="loadList"
                        :page-sizes="[12, 24, 36, 48,60]"
                        v-model:current-page="search.page"
                        v-model:page-size="search.pagesize"
                        @size-change="sizeChange"
                        layout="total, sizes, prev, pager, next"
                        :total="totalCount"
                    >
                    </el-pagination>
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
    import { canYonghuSelect, useYonghuSelect, canYonghuDelete } from "@/module";
    import { extend } from "@/utils/extend";
    import { ElMessageBox, ElMessage } from "element-plus";
    import { Plus, Search, Edit, Delete } from "@element-plus/icons-vue";

    const route = useRoute();
    const search = reactive({
        yonghuming: "",
        xingming: "",
        xingbie: "",
        shouji: "",
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
     * @type {EYonghu[]}
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
                    canYonghuDelete(ids).then((res) => {
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

    // 加载用户列表方法
    const loadList = (page) => {
        // 加载
        if (unref(loading)) return;
        loading.value = true;
        search.page = page;

        http.post("/api/yonghu/selectPages", search).then(
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
    const searchSubmit = () => {
        loadList(1);
    };
</script>

<style scoped lang="scss">
    .views-yonghu-list {
    }
</style>
