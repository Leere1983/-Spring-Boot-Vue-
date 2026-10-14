<template>
    <el-badge :value="collectCount" style="margin-right: 5px">
        <el-button :type="isCollect ? 'danger' : 'primary'" @click="onChangeCollect">
            <template #icon>
                <slot name="icon" :isCollect="isCollect">
                    <i :class="isCollect ? 'fa fa-thumbs-up' : 'fa fa-thumbs-o-up'"></i>
                </slot>
            </template>
            <slot :isCollect="isCollect"> {{ isCollect ? successText : notText }} </slot>
        </el-button>
    </el-badge>
</template>

<script setup>
    import http from "@/utils/ajax/http";
    import DB from "@/utils/db";
    import { session } from "@/utils/utils";
    import { ref, reactive, watch, unref } from "vue";
    import * as modules from "@/module";
    import { ElMessage } from "element-plus";

    const props = defineProps({
        module: {
            type: String,
        },
        form: Object,
        biao: {
            type: String,
        },
        biaoid: {
            type: String,
        },
        biaoti: {
            type: String,
        },
        successText: {
            type: String,
            default: "",
        },
        notText: {
            type: String,
            default: "",
        },
    });
    const isCollect = ref(false);
    const collectCount = ref(0);

    const loadCollect = () => {
        var moduleClass = parseName(props.module, 1);
        var biaoClass = parseName(props.biao, 1);
        var createForm = `can${moduleClass}CreateForm`;
        var useGet = `use${biaoClass}${props.module}`;
        console.log(useGet);
        modules[useGet](props.biaoid, isCollect, collectCount);
        //var get = module[]
    };

    const onChangeCollect = async () => {
        if (!session("username")) {
            ElMessage.error("请登录后操作");
            return;
        }
        var moduleClass = parseName(props.module, 1);
        var biaoClass = parseName(props.biao, 1);
        var createForm = `can${moduleClass}CreateForm`;
        var insert = `can${moduleClass}Insert`;
        var useGet = `use${biaoClass}${props.module}`;
        var o = props.form;
        var { form } = await modules[createForm]();
        form[o.biaoid] = props.biaoid;
        form[o.biao] = props.biao;
        form[o.biaoti] = props.biaoti;
        var s = await modules[insert](form);
        modules[useGet](props.biaoid, isCollect, collectCount);
    };

    /**
     *
     * @param {string} name
     * @param {number} type
     */
    function parseName(name, type = 0) {
        var str;
        if (type) {
            str = name.replace(/_(a-zA-Z)/i, ($0, $1) => {
                return $1.toLocaleUpperCase();
            });
            return str.substring(0, 1).toLocaleUpperCase() + str.substring(1);
        } else {
            str = name.replace(/[A-Z]/i, ($0) => "_" + $0.toLocaleLowerCase());
            if (str.indexOf("_") === 0) {
                str = str.substring(1);
            }
            return str;
        }
    }

    watch(() => props, loadCollect, { immediate: true, deep: true });
</script>

<style scoped></style>
