<template>
    <div class="views-dingdanpingjia-updt">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 添加订单评价 </span>
                    </div>
                </template>

                <el-form :model="form" ref="formModel" :label-width="labelWidth" status-icon validate-on-rule-change>
                    <el-form-item v-if="isRead" label="商品编号 " prop="shangpinbianhao"> {{ form.shangpinbianhao }} </el-form-item>

                    <el-form-item v-if="isRead" label="商品名称 " prop="shangpinmingcheng"> {{ form.shangpinmingcheng }} </el-form-item>

                    <el-form-item v-if="isRead" label="分类 " prop="fenlei">
                        <e-select-view module="shangpinfenlei" :value="form.fenlei" select="id" show="fenleimingcheng"></e-select-view>
                    </el-form-item>

                    <el-form-item v-if="isRead" label="发布人 " prop="faburen"> {{ form.faburen }} </el-form-item>

                    <el-form-item v-if="isRead" label="订单编号 " prop="dingdanbianhao"> {{ form.dingdanbianhao }} </el-form-item>

                    <el-form-item v-if="isRead" label="竞拍人 " prop="jingpairen"> {{ form.jingpairen }} </el-form-item>

                    <el-form-item label="评分 " prop="pingfen" required :rules="[{required:true, message:'请填写评分'}]"> <el-rate v-model="form.pingfen" /> </el-form-item>

                    <el-form-item label="评价 " prop="pingjia" required :rules="[{required:true, message:'请填写评价'}]">
                        <el-input type="text" placeholder="输入评价" style="width: 450px" v-model="form.pingjia" />
                    </el-form-item>

                    <el-form-item v-if="btnText">
                        <el-button type="primary" @click="submit">{{ btnText }}</el-button>
                    </el-form-item>
                </el-form></el-card
            >
        </div>
    </div>
</template>

<script setup>
    import http from "@/utils/ajax/http";
    import DB from "@/utils/db";
    import rule from "@/utils/rule";
    import router from "@/router";

    import { ref, reactive, computed, watch } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { ElMessage, ElMessageBox } from "element-plus";
    import { useDingdanpingjiaFindById, canDingdanpingjiaFindById, canDingdanpingjiaUpdate, canJingpaidingdanFindById } from "@/module";
    import { extend } from "@/utils/extend";

    const route = useRoute();
    const props = defineProps({
        id: [String, Number],
        btnText: {
            type: String,
            default: "保存",
        },
        isRead: {
            type: Boolean,
            default: true,
        },
        isHouxu: {
            type: Boolean,
            default: true,
        },
        labelWidth: {
            type: String,
            default: "140px",
        },
    });
    const form = useDingdanpingjiaFindById(props.id);
    const emit = defineEmits(["success"]);
    const formModel = ref();
    const loading = ref(false);

    const submit = () => {
        formModel.value.validate().then((res) => {
            if (loading.value) return;
            loading.value = true;
            canDingdanpingjiaUpdate(form).then(
                (res) => {
                    loading.value = false;
                    if (res.code == 0) {
                        emit("success", res.data);
                        if (props.isHouxu) {
                            ElMessage.success("更新成功");
                            router.go(-1);
                        }
                    } else {
                        ElMessageBox.alert(res.msg);
                    }
                },
                (err) => {
                    loading.value = false;
                    ElMessageBox.alert(err.message);
                }
            );
        });
    };

    const readMap = reactive({});
    watch(
        () => form.jingpaidingdanid,
        (id) => {
            canJingpaidingdanFindById(id).then((res) => {
                extend(readMap, res);
            });
        }
    );
</script>

<style scoped lang="scss">
    .views-dingdanpingjia-updt {
    }
</style>
