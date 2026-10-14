<template>
    <div class="views-xiaoxi-add">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 添加消息 </span>
                    </div>
                </template>

                <el-form :model="form" ref="formModel" :label-width="labelWidth" status-icon validate-on-rule-change>
                    <el-form-item v-if="isRead" label="编号 " prop="bianhao"> {{ form.bianhao }} </el-form-item>

                    <el-form-item label="内容 " prop="neirong"> <e-editor v-model="form.neirong" @getContent="getneirongContent"></e-editor> </el-form-item>

                    <el-form-item label="发送人 " prop="fasongren"> <el-input v-model="form.fasongren" readonly style="width: 250px"></el-input> </el-form-item>

                    <el-form-item label="发送时间 " prop="fasongshijian" :rules="[{required:true, message:'请填写发送时间'}]">
                        <el-date-picker v-model="form.fasongshijian" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择日期"> </el-date-picker>
                    </el-form-item>

                    <el-form-item label="是否查看 " prop="shifouzhakan">
                        <el-input type="text" placeholder="输入是否查看" style="width: 450px" v-model="form.shifouzhakan" />
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
    import EEditor from "@/components/EEditor.vue";

    import { ref, reactive, computed } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { ElMessage, ElMessageBox } from "element-plus";
    import { useXiaoxiCreateForm, canXiaoxiInsert } from "@/module";

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
    const { form, readMap } = useXiaoxiCreateForm(props.id);
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
                    canXiaoxiInsert(form).then(
                        (res) => {
                            loading.value = false;
                            if (res.code == 0) {
                                emit("success", res.data);
                                if (props.isHouxu) {
                                    ElMessage.success("添加成功");
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

    const getneirongContent = (v) => {
        form.neirong = v;
    };
</script>

<style scoped lang="scss">
    .views-xiaoxi-add {
    }
</style>
