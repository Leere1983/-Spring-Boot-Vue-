<template>
    <div class="views-sytotal">
        <div>
            <e-container>
                <el-form action="javascript:;" @submit="searchSubmit" inline>
                    <el-form-item label="添加时间">
                        <div style="display: flex">
                            <el-date-picker v-model="search.addtime_start" type="date" value-format="YYYY-MM-DD" placeholder="选择开始日期" />
                            -
                            <el-date-picker v-model="search.addtime_end" type="date" value-format="YYYY-MM-DD" placeholder="选择结束日期" />
                        </div>
                    </el-form-item>
                    <el-form-item label="分类">
                        <el-select v-model="search.fenlei"
                            ><el-option label="请选择" value=""></el-option
                            ><e-select-option type="option" module="shangpinfenlei" value="id" label="fenleimingcheng"></e-select-option
                        ></el-select> </el-form-item
                ></el-form>
            </e-container>
        </div>
        <div>
            <e-container>
                <div style="padding: 10px; background: #ffffff">
                    <el-table :data="jingpaidingdanList">
                        <el-table-column prop="fenleimingcheng" label="分类名称" width="180" />
                        <el-table-column prop="sum_zhifujine" label="支付金额" sortable />
                        <el-table-column prop="sum_shouxufei" label="手续费" sortable />
                    </el-table>
                </div>
            </e-container>
        </div>
        <div>
            <e-container>
                <v3-echarts :options="barOptions" width="100%" height="300px"></v3-echarts>
            </e-container>
        </div>
    </div>
</template>

<script setup>
    import http from "@/utils/ajax/http";
    import DB from "@/utils/db";

    import { ref, reactive, watch, computed, unref } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { extend } from "@/utils/extend";

    const route = useRoute();

    const search = reactive({ addtime_start: "", addtime_end: "", fenlei: "" });
    extend(search, route.query);
    const serachSubmit = () => {
        loadDatabaseList();
    };
    var timer;
    watch(
        () => search,
        () => {
            clearTimeout(timer);
            timer = setTimeout(() => {
                serachSubmit();
            }, 200);
        },
        { deep: true }
    );
    const jingpaidingdanList = ref([]);
    const loadDatabaseList = async () => {
        var where = " 1=1 ";


        if("商家" == session('cx')){

            where += " AND faburen='"+session('username')+"' ";

        }

        if (search.addtime_start) {
            where += ` AND fl.addtime>='${search.addtime_start}' `;
        }
        if (search.addtime_end) {
            where += ` AND fl.addtime<='${search.addtime_end}' `;
        }
        if (search.fenlei) {
            where += ` AND fl.fenlei='${search.fenlei}' `;
        }
        jingpaidingdanList.value = await DB.name("jingpaidingdan")
            .alias("fl")
            .joinLeft("shangpinfenlei spf", "fl.fenlei=spf.id")
            .field("spf.fenleimingcheng")
            .group("spf.fenleimingcheng")
            .field("sum(fl.zhifujine) sum_zhifujine")
            .field("sum(fl.shouxufei) sum_shouxufei")
            .where(where)
            .select();
    };

    loadDatabaseList();
    const barOptions = computed(() => {
        var list = unref(jingpaidingdanList);
        return {
            title: {
                text: "",
            },
            tooltip: {
                trigger: "axis",
                axisPointer: {
                    type: "shadow",
                },
            },
            grid: {
                left: "3%",
                right: "4%",
                bottom: "3%",
                containLabel: true,
            },
            xAxis: [
                {
                    type: "category",
                    data: list.map((s) => s.fenleimingcheng),
                    axisTick: {
                        alignWithLabel: true,
                    },
                },
            ],
            yAxis: [
                {
                    type: "value",
                },
            ],
            series: [
                {
                    name: "支付金额",
                    type: "bar",
                    barWidth: "20px",
                    data: list.map((s) => parseFloat(s.sum_zhifujine)),
                },
                {
                    name: "手续费",
                    type: "bar",
                    barWidth: "20px",
                    data: list.map((s) => parseFloat(s.sum_shouxufei)),
                },
            ],
        };
    });
</script>

<style scoped lang="scss">
    .views-sytotal {
    }
</style>
