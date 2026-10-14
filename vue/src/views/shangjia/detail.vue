<template>
    <div class="views-shangjia-detail">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 商家详情 </span>
                    </div>
                </template>

                <div id="printdetail">
                    <el-descriptions class="margin-top" :column="3" border>
                        <el-descriptions-item label="账号"> {{ map.zhanghao }} </el-descriptions-item>
                        <el-descriptions-item label="密码"> {{ map.mima }} </el-descriptions-item>
                        <el-descriptions-item label="店铺名称"> {{ map.dianpumingcheng }} </el-descriptions-item>
                        <el-descriptions-item label="钱包余额"> {{ map.qianbaoyue }} </el-descriptions-item>
                        <el-descriptions-item label="负责人"> {{ map.fuzeren }} </el-descriptions-item>
                        <el-descriptions-item label="身份证"> {{ map.shenfenzheng }} </el-descriptions-item>
                        <el-descriptions-item label="电话"> {{ map.dianhua }} </el-descriptions-item>
                        <el-descriptions-item label="邮箱"> {{ map.youxiang }} </el-descriptions-item>
                        <el-descriptions-item label="地址"> {{ map.dizhi }} </el-descriptions-item>
                        <el-descriptions-item label="资质证书"> <e-img :src="map.zizhizhengshu" class="detail-image" style="max-width: 120px" /> </el-descriptions-item>
                    </el-descriptions>

                    <el-descriptions direction="vertical" class="margin-top" :column="1" border>
                        <el-descriptions-item label="简介"> {{ map.jianjie }} </el-descriptions-item>
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
    import { useShangjiaFindById, canShangjiaFindById } from "@/module";

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
     * @type {EShangjia}
     */
    const map = useShangjiaFindById(props.id);
    watch(
        () => props.id,
        (id) => {
            canShangjiaFindById(id).then((res) => {
                extend(map, res);
            });
        }
    );
    // end 获取详情页面的一行数据
</script>

<style scoped lang="scss">
    .views-shangjia-detail {
    }
</style>
