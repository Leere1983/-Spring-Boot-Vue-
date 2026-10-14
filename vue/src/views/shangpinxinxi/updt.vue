<template>
    <div class="views-shangpinxinxi-updt">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 添加商品信息 </span>
                    </div>
                </template>

                <el-alert v-if="form.shangpinzhuangtai === '竞拍中'" title="竞拍中的商品只能修改商品名称、图片和详情，不能修改价格和时间" type="warning" :closable="false" style="margin-bottom: 20px" />
                <el-alert v-if="form.shangpinzhuangtai === '已结束' || form.shangpinzhuangtai === '已成交' || form.shangpinzhuangtai === '已流拍'" title="已结束的商品不能修改" type="error" :closable="false" style="margin-bottom: 20px" />

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
                        <el-input type="number" placeholder="输入起拍价" style="width: 450px" v-model.number="form.qipaijia" :disabled="form.shangpinzhuangtai === '竞拍中'" />
                    </el-form-item>

                    <el-form-item
                        label="加价幅度 "
                        prop="jiajiafudu"
                        required
                        :rules="[{required:true, message:'请填写加价幅度'}, {validator:rule.checkNumber, message:'输入一个有效数字'}, {validator:rule.checkMin, value:1}]"
                    >
                        <el-input type="number" placeholder="输入加价幅度" style="width: 450px" v-model.number="form.jiajiafudu" :disabled="form.shangpinzhuangtai === '竞拍中'" />
                    </el-form-item>

                    <el-form-item label="起拍时间 " prop="qipaishijian" required :rules="[{required:true, message:'请填写起拍时间'}]">
                        <el-date-picker v-model="form.qipaishijian" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择日期" :disabled="form.shangpinzhuangtai === '竞拍中'"> </el-date-picker>
                    </el-form-item>

                    <el-form-item label="结束时间 " prop="jieshushijian" required :rules="[{required:true, message:'请填写结束时间'}]">
                        <el-date-picker v-model="form.jieshushijian" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择日期" :disabled="form.shangpinzhuangtai === '竞拍中'"> </el-date-picker>
                    </el-form-item>

                    <el-form-item label="竞拍次数 " prop="jingpairenshu" :rules="[{validator:rule.checkNumber, message:'输入一个有效数字'}]">
                        <el-input type="number" placeholder="输入竞拍次数" style="width: 450px" v-model.number="form.jingpairenshu" />
                    </el-form-item>

                    <el-form-item label="商品详情 " prop="shangpinxiangqing">
                        <e-editor v-model="form.shangpinxiangqing" @getContent="getshangpinxiangqingContent"></e-editor>
                    </el-form-item>

                    <el-form-item label="商品状态 " prop="shangpinzhuangtai">
                        <el-select v-model="form.shangpinzhuangtai"
                            ><el-option label="竞拍中" value="竞拍中"></el-option>
                            <el-option label="已结束" value="已结束"></el-option>
                            <el-option label="已成交" value="已成交"></el-option>
                            <el-option label="已流拍" value="已流拍"></el-option>
                        </el-select>
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
    import { useShangpinxinxiFindById, canShangpinxinxiFindById, canShangpinxinxiUpdate } from "@/module";

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
    const form = useShangpinxinxiFindById(props.id);
    const emit = defineEmits(["success"]);
    const formModel = ref();
    const loading = ref(false);

    const submit = () => {
        formModel.value.validate().then((res) => {
            if (loading.value) return;
            loading.value = true;
            canShangpinxinxiUpdate(form).then(
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

    const getshangpinxiangqingContent = (v) => {
        form.shangpinxiangqing = v;
    };
</script>

<style scoped lang="scss">
    .views-shangpinxinxi-updt {
    }
</style>
