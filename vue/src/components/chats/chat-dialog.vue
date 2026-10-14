<template>
    <el-dialog class="chat-dialog" :title="chat ? chat.user?.xingming : '聊天内容'" v-model="visible" width="60%" center :before-close="beforeClose">
        <e-chat-session></e-chat-session>
    </el-dialog>
</template>
<style type="type/scss" lang="scss">
.chat-dialog{
  .el-dialog__body{
    padding: 0!important;
  }
}
</style>
<script>
    import {mapState,mapActions} from 'pinia'
    import {useChatStore} from "@/components/chats/chat";
    import EChatSession from './chat-session.vue'

    
export default {
    name: "e-chat-dialog",
    data() {
        return {}
    },
    components:{
        EChatSession
    },
    watch: {},
    computed: {
        ...mapState(useChatStore,{
            chat:(state)=>state.currentChat,
        }),
        visible:{
            get(){
                return useChatStore().visibleModel
            },
            set(val){
                useChatStore().visibleModel = val;
            }
        }
    },
    methods: {
        beforeClose(next){
            useChatStore().selectChat(null);
            //useChatStore().hideModel();
            next();
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
