<template>
    <div class="views-shangjia-add">
        <div>
            <el-card class="box-card">

                <template #header>
                    <template v-if="$session.cx == '管理员'">
                        <div class="clearfix">
                            <span class="title"> 添加商家 </span>
                        </div>
                    </template>

                </template>

                <el-form :model="form" ref="formModel" :label-width="labelWidth" status-icon validate-on-rule-change>
                    <el-form-item
                        label="账号 "
                        prop="zhanghao"
                        required
                        :rules="[{required:true, message:'请填写账号'}, {validator:rule.checkRemote, message:'内容重复了', checktype:'insert', module:'shangjia', col:'zhanghao', trigger:'blur'}]"
                    >
                        <el-input type="text" placeholder="输入账号" style="width: 450px" v-model="form.zhanghao" />
                    </el-form-item>

                    <el-form-item label="密码 " prop="mima" required :rules="[{required:true, message:'请填写密码'}]">
                        <el-input type="password" placeholder="输入密码" style="width: 450px" v-model="form.mima" />
                    </el-form-item>

                    <el-form-item label="店铺名称 " prop="dianpumingcheng" required :rules="[{required:true, message:'请填写店铺名称'}]">
                        <el-input type="text" placeholder="输入店铺名称" style="width: 450px" v-model="form.dianpumingcheng" />
                    </el-form-item>

                    <el-form-item label="负责人 " prop="fuzeren" required :rules="[{required:true, message:'请填写负责人'}]">
                        <el-input type="text" placeholder="输入负责人" style="width: 450px" v-model="form.fuzeren" />
                    </el-form-item>

                    <el-form-item
                        label="身份证 "
                        prop="shenfenzheng"
                        required
                        :rules="[{required:true, message:'请填写身份证'}, {validator:rule.checkCard, message:'请输入有效身份证号码'}]"
                    >
                        <el-input type="text" placeholder="输入身份证" style="width: 450px" v-model="form.shenfenzheng" />
                    </el-form-item>

                    <el-form-item label="电话 " prop="dianhua" required :rules="[{required:true, message:'请填写电话'}, {validator:rule.checkPhone, message:'请输入正确手机号码'}]">
                        <el-input type="text" placeholder="输入电话" style="width: 450px" v-model="form.dianhua" />
                    </el-form-item>

                    <el-form-item label="邮箱 " prop="youxiang" required :rules="[{required:true, message:'请填写邮箱'}, {type:'email', message:'请输入正确邮箱地址'}]">
                        <el-input type="text" placeholder="输入邮箱" style="width: 450px" v-model="form.youxiang" />
                    </el-form-item>

                    <el-form-item label="地址 " prop="dizhi" required :rules="[{required:true, message:'请填写地址'}]">
                        <el-input type="text" placeholder="输入地址" style="width: 450px" v-model="form.dizhi" />
                    </el-form-item>

                    <el-form-item label="简介 " prop="jianjie" required :rules="[{required:true, message:'请填写简介'}]">
                        <el-input type="textarea" v-model="form.jianjie"></el-input>
                    </el-form-item>

                    <el-form-item label="资质证书 " prop="zizhizhengshu" required :rules="[{required:true, message:'请填写资质证书'}]">
                        <e-upload-image v-model="form.zizhizhengshu" is-paste></e-upload-image>
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

    import { ref, reactive, computed } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { ElMessage, ElMessageBox } from "element-plus";
    import { useShangjiaCreateForm, canShangjiaInsert } from "@/module";
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
    const { form } = useShangjiaCreateForm();
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
                    canShangjiaInsert(form).then(
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
    .views-shangjia-add {
    }
</style>
