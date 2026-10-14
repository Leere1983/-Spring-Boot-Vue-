<template>
    <div class="views-jingpaidingdan-detail">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 竞拍订单详情 </span>
                    </div>
                </template>

                <div id="printdetail">
                    <el-descriptions class="margin-top" :column="3" border>
                        <el-descriptions-item label="商品编号"> {{ map.shangpinbianhao }} </el-descriptions-item>
                        <el-descriptions-item label="商品名称"> {{ map.shangpinmingcheng }} </el-descriptions-item>
                        <el-descriptions-item label="当前竞拍价"> {{ map.dangqianjingpaijia }} </el-descriptions-item>
                        <el-descriptions-item label="竞拍人"> {{ map.jingpairen }} </el-descriptions-item>
                        <el-descriptions-item label="发布人"> {{ map.faburen }} </el-descriptions-item>
                        <el-descriptions-item label="分类">
                            <e-select-view module="shangpinfenlei" :value="map.fenlei" select="id" show="fenleimingcheng"></e-select-view>
                        </el-descriptions-item>
                        <el-descriptions-item label="订单编号"> {{ map.dingdanbianhao }} </el-descriptions-item>
                        <el-descriptions-item label="订单状态"> {{ map.dingdanzhuangtai }} </el-descriptions-item>
                        <el-descriptions-item label="支付金额"> {{ map.zhifujine }} </el-descriptions-item>
                        <el-descriptions-item label="手续费"> {{ map.shouxufei }} </el-descriptions-item>
                        <el-descriptions-item label="联系人"> {{ map.lianxiren }} </el-descriptions-item>
                        <el-descriptions-item label="联系电话"> {{ map.lianxidianhua }} </el-descriptions-item>
                        <el-descriptions-item label="联系地址"> {{ map.lianxidizhi }} </el-descriptions-item>
                        <el-descriptions-item label="添加时间"> {{ map.addtime }} </el-descriptions-item>
                    </el-descriptions>

                    <el-descriptions direction="vertical" class="margin-top" :column="1" border>
                        <el-descriptions-item label="商品图片"> <e-images :src="map.shangpintupian" type="detail"></e-images> </el-descriptions-item>
                    </el-descriptions>
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
    import { useJingpaidingdanFindById, canJingpaidingdanFindById } from "@/module";

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
     * @type {EJingpaidingdan}
     */
    const map = useJingpaidingdanFindById(props.id);
    watch(
        () => props.id,
        (id) => {
            canJingpaidingdanFindById(id).then((res) => {
                extend(map, res);
            });
        }
    );
    // end 获取详情页面的一行数据
</script>

<style scoped lang="scss">
    .views-jingpaidingdan-detail {
    }
</style>
