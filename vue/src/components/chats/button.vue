<template>

        <el-button size="small" @click="showModel">
            消息 {{ readCount }}
        </el-button>


</template>

<script>
import {mapState,mapActions} from 'pinia'
import {useChatStore} from "@/components/chats/chat";

export default {
    name: "e-chat-button",
    data(){
        return {
            visible:false
        }
    },
    computed:{
        ...mapState(useChatStore , {
            readCount:'readCount',
            visibleModel:'visibleModel'
        })
    },
    methods:{
        beforeClose(){
            return true;
        },
        showModel(){
            this.showModel();
        },
        ...mapActions(useChatStore,['showModel']),
    }
}
</script>

<style scoped>

</style>
