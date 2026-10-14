<template>
    <div class="views-shangpinxinxi-add">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 添加商品信息 </span>
                    </div>
                </template>

                <el-form :model="form" ref="formModel" :label-width="labelWidth" status-icon validate-on-rule-change>
                    <el-form-item label="商品编号 " prop="shangpinbianhao" :rules="[{required:true, message:'请填写商品编号'}]">
                        <el-input type="text" placeholder="系统自动生成" style="width: 450px" v-model="form.shangpinbianhao" readonly />
                    </el-form-item>

                    <el-form-item label="分类 " prop="fenlei" required :rules="[{required:true, message:'请填写分类'}]">
                        <el-select v-model="form.fenlei"><e-select-option type="option" module="shangpinfenlei" value="id" label="fenleimingcheng"></e-select-option></el-select>
                    </el-form-item>

                    <el-form-item label="商品名称 " prop="shangpinmingcheng" required :rules="[{required:true, message:'请填写商品名称'}]">
                        <el-input type="text" placeholder="输入商品名称" style="width: 450px" v-model="form.shangpinmingcheng" />
                    </el-form-item>

                    <el-form-item label="商品图片 " prop="shangpintupian" required :rules="[{required:true, message:'请填写商品图片'}]">
                        <e-upload-images v-model="form.shangpintupian" is-paste></e-upload-images>
                    </el-form-item>

                    <el-form-item
                        label="起拍价 "
                        prop="qipaijia"
                        required
                        :rules="[{required:true, message:'请填写起拍价'}, {validator:rule.checkNumber, message:'输入一个有效数字'}]"
                    >
                        <el-input type="number" placeholder="输入起拍价" style="width: 450px" v-model.number="form.qipaijia" />
                    </el-form-item>

                    <el-form-item
                        label="加价幅度 "
                        prop="jiajiafudu"
                        required
                        :rules="[{required:true, message:'请填写加价幅度'}, {validator:rule.checkNumber, message:'输入一个有效数字'}, {validator:rule.checkMin, value:1}]"
                    >
                        <el-input type="number" placeholder="输入加价幅度" style="width: 450px" v-model.number="form.jiajiafudu" />
                    </el-form-item>

                    <el-form-item label="起拍时间 " prop="qipaishijian" required :rules="[{required:true, message:'请填写起拍时间'}]">
                        <el-date-picker v-model="form.qipaishijian" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择日期"> </el-date-picker>
                    </el-form-item>

                    <el-form-item label="结束时间 " prop="jieshushijian" required :rules="[{required:true, message:'请填写结束时间'}]">
                        <el-date-picker v-model="form.jieshushijian" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择日期"> </el-date-picker>
                    </el-form-item>

                    <el-form-item label="商品详情 " prop="shangpinxiangqing">
                        <e-editor v-model="form.shangpinxiangqing" @getContent="getshangpinxiangqingContent"></e-editor>
                    </el-form-item>

                    <el-form-item label="竞拍人 " prop="jingpairen"> <el-input v-model="form.jingpairen" readonly style="width: 250px"></el-input> </el-form-item>

                    <el-form-item label="发布人 " prop="faburen"> <el-input v-model="form.faburen" readonly style="width: 250px"></el-input> </el-form-item>

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
    import { useShangpinxinxiCreateForm, canShangpinxinxiInsert } from "@/module";
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
    const { form } = useShangpinxinxiCreateForm();
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
                    canShangpinxinxiInsert(form).then(
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

    const getshangpinxiangqingContent = (v) => {
        form.shangpinxiangqing = v;
    };
</script>

<style scoped lang="scss">
    .views-shangpinxinxi-add {
    }
</style>
