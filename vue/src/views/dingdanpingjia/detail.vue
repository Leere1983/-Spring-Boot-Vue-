<template>
    <div class="views-dingdanpingjia-detail">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 订单评价详情 </span>
                    </div>
                </template>

                <div id="printdetail">
                    <el-descriptions class="margin-top" :column="3" border>
                        <el-descriptions-item label="商品编号"> {{ map.shangpinbianhao }} </el-descriptions-item>
                        <el-descriptions-item label="商品名称"> {{ map.shangpinmingcheng }} </el-descriptions-item>
                        <el-descriptions-item label="分类">
                            <e-select-view module="shangpinfenlei" :value="map.fenlei" select="id" show="fenleimingcheng"></e-select-view>
                        </el-descriptions-item>
                        <el-descriptions-item label="发布人"> {{ map.faburen }} </el-descriptions-item>
                        <el-descriptions-item label="订单编号"> {{ map.dingdanbianhao }} </el-descriptions-item>
                        <el-descriptions-item label="竞拍人"> {{ map.jingpairen }} </el-descriptions-item>
                        <el-descriptions-item label="评分"> <el-rate v-model="map.pingfen" disabled /> </el-descriptions-item>
                        <el-descriptions-item label="评价"> {{ map.pingjia }} </el-descriptions-item>
                        <el-descriptions-item label="评价时间"> {{ map.addtime }} </el-descriptions-item>
                    </el-descriptions>

                    <el-descriptions direction="vertical" class="margin-top" :column="1" border> </el-descriptions>
                </div>
                <div class="no-print" v-if="isShowBtn">
                    <el-button @click="$router.go(-1)">返回</el-button>
                    <el-button @click="$print('#printdetail')">打印</el-button>
                </div>
            </el-card>
        </div>
    </div>
</template>

<script setup>
    import http from "@/utils/ajax/http";
    import DB from "@/utils/db";

    import { ref, reactive, watch, computed } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { extend } from "@/utils/extend";
    import { useDingdanpingjiaFindById, canDingdanpingjiaFindById } from "@/module";

    const route = useRoute();
    const props = defineProps({
        id: {
            type: [Number, String],
        },
        isShowBtn: {
            type: Boolean,
            default: true,
        },
    });

    /**
     * 获取详情页面的一行数据,当url参数id变更时，当url参数id变更时，自动更新map中的数据
     * @type {EDingdanpingjia}
     */
    const map = useDingdanpingjiaFindById(props.id);
    watch(
        () => props.id,
        (id) => {
            canDingdanpingjiaFindById(id).then((res) => {
                extend(map, res);
            });
        }
    );
    // end 获取详情页面的一行数据
</script>

<style scoped lang="scss">
    .views-dingdanpingjia-detail {
    }
</style>
