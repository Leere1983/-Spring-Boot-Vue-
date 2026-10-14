<template>
    <div class="views-dingdanfahuo-updt">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 添加订单发货 </span>
                    </div>
                </template>

                <el-form :model="form" ref="formModel" :label-width="labelWidth" status-icon validate-on-rule-change>
                    <el-form-item v-if="isRead" label="商品编号 " prop="shangpinbianhao"> {{ form.shangpinbianhao }} </el-form-item>

                    <el-form-item v-if="isRead" label="商品名称 " prop="shangpinmingcheng"> {{ form.shangpinmingcheng }} </el-form-item>

                    <el-form-item v-if="isRead" label="商品图片 " prop="shangpintupian">
                        <div style="width: 100%; max-width: 120px">
                            <e-img :src="form.shangpintupian" type="list"></e-img>
                        </div>
                    </el-form-item>

                    <el-form-item v-if="isRead" label="竞拍人 " prop="jingpairen"> {{ form.jingpairen }} </el-form-item>

                    <el-form-item v-if="isRead" label="发布人 " prop="faburen"> {{ form.faburen }} </el-form-item>

                    <el-form-item v-if="isRead" label="分类 " prop="fenlei">
                        <e-select-view module="shangpinfenlei" :value="form.fenlei" select="id" show="fenleimingcheng"></e-select-view>
                    </el-form-item>

                    <el-form-item v-if="isRead" label="订单编号 " prop="dingdanbianhao"> {{ form.dingdanbianhao }} </el-form-item>

                    <el-form-item label="物流公司 " prop="wuliugongsi" required :rules="[{required:true, message:'请填写物流公司'}]">
                        <el-input type="text" placeholder="输入物流公司" style="width: 450px" v-model="form.wuliugongsi" />
                    </el-form-item>

                    <el-form-item label="物流单号 " prop="wuliudanhao" required :rules="[{required:true, message:'请填写物流单号'}]">
                        <el-input type="text" placeholder="输入物流单号" style="width: 450px" v-model="form.wuliudanhao" />
                    </el-form-item>

                    <el-form-item label="发货备注 " prop="fahuobeizhu"> <el-input type="textarea" v-model="form.fahuobeizhu"></el-input> </el-form-item>

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
    import { useDingdanfahuoFindById, canDingdanfahuoFindById, canDingdanfahuoUpdate, canJingpaidingdanFindById } from "@/module";
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
    const form = useDingdanfahuoFindById(props.id);
    const emit = defineEmits(["success"]);
    const formModel = ref();
    const loading = ref(false);

    const submit = () => {
        formModel.value.validate().then((res) => {
            if (loading.value) return;
            loading.value = true;
            canDingdanfahuoUpdate(form).then(
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
    .views-dingdanfahuo-updt {
    }
</style>
