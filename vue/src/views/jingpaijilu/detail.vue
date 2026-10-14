<template>
    <div class="views-jingpaijilu-detail">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 竞拍记录详情 </span>
                    </div>
                </template>

                <div id="printdetail">
                    <el-descriptions class="margin-top" :column="3" border>
                        <el-descriptions-item label="商品编号"> {{ map.shangpinbianhao }} </el-descriptions-item>
                        <el-descriptions-item label="分类">
                            <e-select-view module="shangpinfenlei" :value="map.fenlei" select="id" show="fenleimingcheng"></e-select-view>
                        </el-descriptions-item>
                        <el-descriptions-item label="商品名称"> {{ map.shangpinmingcheng }} </el-descriptions-item>
                        <el-descriptions-item label="起拍价"> {{ map.qipaijia }} </el-descriptions-item>
                        <el-descriptions-item label="发布人"> {{ map.faburen }} </el-descriptions-item>
                        <el-descriptions-item label="加价幅度"> {{ map.jiajiafudu }} </el-descriptions-item>
                        <el-descriptions-item label="竞拍价"> {{ map.jingpaijia }} </el-descriptions-item>
                        <el-descriptions-item label="竞拍人"> {{ map.jingpairen }} </el-descriptions-item>
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
    import { useJingpaijiluFindById, canJingpaijiluFindById } from "@/module";

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
     * @type {EJingpaijilu}
     */
    const map = useJingpaijiluFindById(props.id);
    watch(
        () => props.id,
        (id) => {
            canJingpaijiluFindById(id).then((res) => {
                extend(map, res);
            });
        }
    );
    // end 获取详情页面的一行数据
</script>

<style scoped lang="scss">
    .views-jingpaijilu-detail {
    }
</style>
