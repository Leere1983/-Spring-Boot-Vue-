<template>
    <div class="views-jingpaidingdan-web-add">
        <div>
            <e-container>
                <el-card class="box-card">
                    <template #header>
                        <div class="clearfix">
                            <span class="title"> 添加竞拍订单 </span>
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

                        <el-form-item v-if="isRead" label="当前竞拍价 " prop="dangqianjingpaijia" :rules="[{validator:rule.checkNumber, message:'输入一个有效数字'}]">
                            {{form.dangqianjingpaijia}}
                        </el-form-item>

                        <el-form-item v-if="isRead" label="竞拍人 " prop="jingpairen"> {{ form.jingpairen }} </el-form-item>

                        <el-form-item v-if="isRead" label="发布人 " prop="faburen"> {{ form.faburen }} </el-form-item>

                        <el-form-item v-if="isRead" label="分类 " prop="fenlei">
                            <e-select-view module="shangpinfenlei" :value="form.fenlei" select="id" show="fenleimingcheng"></e-select-view>
                        </el-form-item>

                        <el-form-item label="联系人 " prop="lianxiren" required :rules="[{required:true, message:'请填写联系人'}]">
                            <el-input type="text" placeholder="输入联系人" style="width: 450px" v-model="form.lianxiren" />
                        </el-form-item>

                        <el-form-item
                            label="联系电话 "
                            prop="lianxidianhua"
                            required
                            :rules="[{required:true, message:'请填写联系电话'}, {validator:rule.checkPhone, message:'请输入正确手机号码'}]"
                        >
                            <el-input type="text" placeholder="输入联系电话" style="width: 450px" v-model="form.lianxidianhua" />
                        </el-form-item>

                        <el-form-item label="联系地址 " prop="lianxidizhi" required :rules="[{required:true, message:'请填写联系地址'}]">
                            <el-input type="text" placeholder="输入联系地址" style="width: 450px" v-model="form.lianxidizhi" />
                        </el-form-item>

                        <el-form-item v-if="btnText">
                            <el-button type="primary" @click="submit">{{ btnText }}</el-button>
                        </el-form-item>
                    </el-form></el-card
                >
            </e-container>
        </div>
    </div>
</template>

<script setup>
    import http from "@/utils/ajax/http";
    import DB from "@/utils/db";
    import rule from "@/utils/rule";
    import router from "@/router";

    import { ref, reactive, computed } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { ElMessage, ElMessageBox } from "element-plus";
    import { useJingpaidingdanCreateForm, canJingpaidingdanInsert } from "@/module";

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
    const { form, readMap } = useJingpaidingdanCreateForm(props.id);
    const emit = defineEmits(["success"]);
    const formModel = ref();
    const loading = ref(false);
    var submit = () => {
        return new Promise((resolve, reject) => {
            formModel.value
                .validate()
                .then((res) => {
                    if (loading.value) return;
                    loading.value = true;
                    canJingpaidingdanInsert(form).then(
                        (res) => {
                            loading.value = false;
                            if (res.code == 0) {
                                emit("success", res.data);
                                if (props.isHouxu) {
                                    const orderNo = res.data?.dingdanbianhao || "";
                                    ElMessage.success(orderNo ? `添加成功，订单编号：${orderNo}` : "添加成功");
                                    router.go(-1);
                                }
                            } else {
                                ElMessageBox.alert(res.msg);
                            }
                            resolve(res);
                        },
                        (err) => {
                            loading.value = false;
                            ElMessageBox.alert(err.message);
                            reject(err);
                        }
                    );
                })
                .catch((err) => {
                    reject(err);
                });
        });
    };
</script>

<style scoped lang="scss">
    .views-jingpaidingdan-web-add {
    }
</style>
