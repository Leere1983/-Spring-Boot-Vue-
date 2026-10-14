<template>
    <el-button @click="check" v-if="$session.username" v-bind="$attrs">
        <slot>聊天</slot>
    </el-button>
</template>
<style type="type/scss" lang="scss">

</style>
<script>
import {useChatStore} from "@/components/chats/chat";

export default {
    name: "e-chat-check",
    data() {
        return {}
    },
    props:{
        rid:{
            type:[String,Number]
        },
    },
    methods: {
        check(){
            var obj = {
                rid:this.rid,
                sid:this.$session.username
            };
            useChatStore().check(obj);
        }
    },
    created() {
    },
    mounted() {
    },
    destroyed() {
    }
}
</script>
