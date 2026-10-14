<template>
    <div class="views-liuyanban-web-add">
        <div>
            <e-container>
                <el-card class="box-card">
                    <template #header>
                        <div class="clearfix">
                            <span class="title"> 留言反馈 </span>
                        </div>
                    </template>

                    <el-form :model="form" ref="formModel" :label-width="labelWidth" status-icon validate-on-rule-change>
                        <el-form-item label="姓名 " prop="xingming" required :rules="[{ required: true, message: '请填写姓名' }]"> <el-input type="text" placeholder="输入姓名" style="width: 450px" v-model="form.xingming" /> </el-form-item>

                        <el-form-item
                            label="联系电话 "
                            prop="lianxidianhua"
                            required
                            :rules="[
                                { required: true, message: '请填写联系电话' },
                                { validator: rule.checkPhone, message: '请输入正确手机号码' },
                            ]"
                        >
                            <el-input type="text" placeholder="输入联系电话" style="width: 450px" v-model="form.lianxidianhua" />
                        </el-form-item>

                        <el-form-item label="反馈内容 " prop="fankuineirong" required :rules="[{ required: true, message: '请填写反馈内容' }]"> <el-input type="textarea" v-model="form.fankuineirong"></el-input> </el-form-item>

                        <el-form-item label="反馈用户 " prop="fankuiyonghu"> <el-input v-model="form.fankuiyonghu" readonly style="width: 250px"></el-input> </el-form-item>

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
    import { useLiuyanbanCreateForm, canLiuyanbanInsert } from "@/module";
    import { extend } from "vue-design-plugin";

    const route = useRoute();
    const props = defineProps({
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
    const { form } = useLiuyanbanCreateForm();
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
                    canLiuyanbanInsert(form).then(
                        (res) => {
                            loading.value = false;
                            if (res.code == 0) {
                                emit("success", res.data);
                                if (props.isHouxu) {
                                    ElMessage.success("添加成功");
                                    formModel.value.resetFields();
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
    .views-liuyanban-web-add {
    }
</style>
