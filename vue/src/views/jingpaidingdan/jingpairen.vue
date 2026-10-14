<template>
    <div class="views-jingpaidingdan-list">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 竞拍订单查询 </span>
                    </div>
                </template>

                <div class="form-search">
                    <el-form @submit.prevent.stop :inline="true" size="small">
                        <el-form-item label="商品名称">
                            <el-input v-model="search.shangpinmingcheng"></el-input>
                        </el-form-item>
                        <el-form-item label="分类">
                            <el-select v-model="search.fenlei"
                            >
                                <el-option label="请选择" value=""></el-option
                                >
                                <e-select-option type="option" module="shangpinfenlei" value="id"
                                                 label="fenleimingcheng"></e-select-option
                                >
                            </el-select>
                        </el-form-item>
                        <el-form-item label="订单编号">
                            <el-input v-model="search.dingdanbianhao"></el-input>
                        </el-form-item>

                        <el-form-item>
                            <el-button type="primary" @click="searchSubmit" icon="el-icon-search">查询</el-button>
                        </el-form-item>
                    </el-form>
                </div>

                <el-table border :data="lists" style="width: 100%" @sort-change="sortChange" highlight-current-row>
                    <el-table-column type="index" label="#"></el-table-column>
                    <!-- 序号 -->

                    <el-table-column prop="shangpinmingcheng" label="商品名称"width="180">
                        <template #default="{row}"> {{ row.shangpinmingcheng }}</template>
                    </el-table-column>
                    <el-table-column prop="shangpintupian" label="商品图片" width="180">
                        <template #default="{row}">
                            <div style="width: 100%; max-width: 120px">
                                <e-img :src="row.shangpintupian" type="list"></e-img>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="dangqianjingpaijia" label="当前竞拍价" width="125">
                        <template #default="{row}"> {{ row.dangqianjingpaijia }}</template>
                    </el-table-column>

                    <el-table-column prop="fenlei" label="分类" width="180">
                        <template #default="{row}">
                            <e-select-view module="shangpinfenlei" :value="row.fenlei" select="id"
                                           show="fenleimingcheng"></e-select-view>
                        </template>
                    </el-table-column>

                    <el-table-column prop="dingdanzhuangtai" label="订单状态" width="180">
                        <template #default="{row}"> {{ row.dingdanzhuangtai }}</template>
                    </el-table-column>
                    <el-table-column prop="zhifujine" label="支付金额" width="180">
                        <template #default="{row}"> {{ row.zhifujine }}</template>
                    </el-table-column>
                    <el-table-column prop="shouxufei" label="手续费" width="180">
                        <template #default="{row}"> {{ row.shouxufei }}</template>
                    </el-table-column>
                    <el-table-column prop="lianxiren" label="联系人" width="180">
                        <template #default="{row}"> {{ row.lianxiren }}</template>
                    </el-table-column>
                    <el-table-column prop="lianxidianhua" label="联系电话" width="180">
                        <template #default="{row}"> {{ row.lianxidianhua }}</template>
                    </el-table-column>
                    <el-table-column prop="lianxidizhi" label="联系地址"width="180">
                        <template #default="{row}"> {{ row.lianxidizhi }}</template>
                    </el-table-column>


                    <el-table-column width="180" label="审核">
                        <template #default="{row}"> {{ row.issh }}</template>
                    </el-table-column>
                    <el-table-column label="是否支付" width="120">
                        <template #default="{row}">
                            {{ row.iszf }}
                            <el-button
                                size="small"
                                type="primary"
                                @click="$goto({path:'/admin/pay' , query:{biao:'jingpaidingdan' , id:row.id,ordersn:row.dingdanbianhao,zongji:row.zhifujine,isShaxiang:'true',bank:1} })"
                                v-if="row.iszf=='否'"
                            >
                                去支付
                            </el-button>
                        </template>
                    </el-table-column>

                    <el-table-column label="操作" fixed="right" width="250">
                        <template #default="{row}">
                            <el-button-group>


                                <el-button type="primary" :icon="Plus" size="small"     v-if="row.dingdanzhuangtai == '已完成'"
                                           @click="$router.push('/admin/dingdanpingjia/add?id='+row.id)">评价
                                </el-button>


                                <el-button type="primary" :icon="Plus" size="small"      v-if="row.dingdanzhuangtai == '待收货'"
                                           @click="$router.push('/admin/querenshouhuo/add?id='+row.id)">收货
                                </el-button>


                                <el-tooltip effect="dark" content="详情" placement="top-start"
                                >
                                    <el-button type="info" :icon="InfoFilled" size="small"
                                               @click="$router.push('/admin/jingpaidingdan/detail?id='+row.id)">详情
                                    </el-button>
                                </el-tooltip>

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

import {ref, reactive, watch, unref, onBeforeMount} from "vue";
import {useRoute} from "vue-router";
import {session} from "@/utils/utils";
import {
    canJingpaidingdanSelect,
    useJingpaidingdanSelect,
    canJingpaidingdanDelete,
    canJingpaidingdanCheckIssh
} from "@/module";
import {extend} from "@/utils/extend";
import {ElMessageBox, ElMessage} from "element-plus";
import {Plus, Search, InfoFilled, Edit, Delete} from "@element-plus/icons-vue";

const route = useRoute();
const search = reactive({
    shangpinxinxiid: "",
    shangpinmingcheng: "",
    fenlei: "",
    dingdanbianhao: "",
    dingdanzhuangtai: "",
    lianxiren: "",
    lianxidianhua: "",
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
    {deep: true}
);

// 总行数
const totalCount = ref(0);
/**
 * 列表数据
 * @type {EJingpaidingdan[]}
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
                canJingpaidingdanDelete(ids).then((res) => {
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

// 加载竞拍订单列表方法
const loadList = (page) => {
    // 加载
    if (unref(loading)) return;
    loading.value = true;
    search.page = page;

    http.post("/api/jingpaidingdan/selectJingpairen", search).then(
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
.views-jingpaidingdan-list {
}
</style>
