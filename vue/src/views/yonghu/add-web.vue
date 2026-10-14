<template>
    <div class="views-yonghu-web-add">
        <div>
            <e-container>
                <el-card class="box-card">
                    <template #header>
                        <div class="clearfix">
                            <span class="title"> 添加用户 </span>
                        </div>
                    </template>

                    <el-form :model="form" ref="formModel" :label-width="labelWidth" status-icon validate-on-rule-change>
                        <el-form-item
                            label="用户名 "
                            prop="yonghuming"
                            required
                            :rules="[{required:true, message:'请填写用户名'}, {validator:rule.checkRemote, message:'内容重复了', checktype:'insert', module:'yonghu', col:'yonghuming', trigger:'blur'}]"
                        >
                            <el-input type="text" placeholder="输入用户名" style="width: 450px" v-model="form.yonghuming" />
                        </el-form-item>

                        <el-form-item label="密码 " prop="mima" required :rules="[{required:true, message:'请填写密码'}]">
                            <el-input type="password" placeholder="输入密码" style="width: 450px" v-model="form.mima" />
                        </el-form-item>

                        <el-form-item label="姓名 " prop="xingming" required :rules="[{required:true, message:'请填写姓名'}]">
                            <el-input type="text" placeholder="输入姓名" style="width: 450px" v-model="form.xingming" />
                        </el-form-item>

                        <el-form-item label="性别 " prop="xingbie" required :rules="[{required:true, message:'请填写性别'}]">
                            <el-select v-model="form.xingbie"
                                ><el-option label="男" value="男"></el-option>
                                <el-option label="女" value="女"></el-option>
                            </el-select>
                        </el-form-item>

                        <el-form-item
                            label="手机 "
                            prop="shouji"
                            required
                            :rules="[{required:true, message:'请填写手机'}, {validator:rule.checkPhone, message:'请输入正确手机号码'}]"
                        >
                            <el-input type="text" placeholder="输入手机" style="width: 450px" v-model="form.shouji" />
                        </el-form-item>

                        <el-form-item label="邮箱 " prop="youxiang" required :rules="[{required:true, message:'请填写邮箱'}, {type:'email', message:'请输入正确邮箱地址'}]">
                            <el-input type="text" placeholder="输入邮箱" style="width: 450px" v-model="form.youxiang" />
                        </el-form-item>

                        <el-form-item
                            label="身份证 "
                            prop="shenfenzheng"
                            required
                            :rules="[{required:true, message:'请填写身份证'}, {validator:rule.checkCard, message:'请输入有效身份证号码'}]"
                        >
                            <el-input type="text" placeholder="输入身份证" style="width: 450px" v-model="form.shenfenzheng" />
                        </el-form-item>

                        <el-form-item label="头像 " prop="touxiang"> <e-upload-image v-model="form.touxiang" is-paste></e-upload-image> </el-form-item>

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
    import { useYonghuCreateForm, canYonghuInsert } from "@/module";
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
    const { form } = useYonghuCreateForm();
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
                    canYonghuInsert(form).then(
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
    .views-yonghu-web-add {
    }
</style>
