<template>
    <div class="session-box">
        <div class="session-list">
            <div class="session-chat" v-for="v in sessionList" :class="{active:chat && v.id===chat.id}" :key="v.id" @click="selectChat(v)">
                <div class="session">
                    <div class="headimgurl">
                        <e-img-box pb="100" :src="v.user?.touxiang"></e-img-box>
                    </div>
                    <div class="username">
                        <div class="name">
                            <div class="">{{ v.user?.xingming }}</div>
                            <div class="">
                                <el-badge v-show="v.readCount>0" :value="v.readCount" :max="99" class="item"></el-badge>
                            </div>
                        </div>
                        <div class="time">{{ v.addtime }}</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="session-content">
            <template v-if="chat">
                <div class="session-siliao-item">
                    <e-chat></e-chat>
                </div>
            </template>
        </div>
    </div>
</template>

<script>

    import {extend, isObject} from "@/utils/extend";
    import rule from "@/utils/rule";
    import EChat from './chats.vue'
    
    import {mapState,mapActions} from 'pinia'
    import {useChatStore} from "@/components/chats/chat";
    
    export default {
        name: "e-chat-session",
        data() {
            return {

            };
        },
        components:{
            EChat
        },
        computed:{
            ...mapState(useChatStore,{
                sessionList:(state)=>state.sessionList,
                chat:(state)=>state.currentChat,
            }),
        },
        methods: {
            ...mapActions(useChatStore,['selectChat']),
        },
        mounted() {
        }
    };
</script>

<style lang="scss">
    .session-box{
      display: flex;
      font-size: 14px;
      height: 550px;
      .session-list{
        flex-grow: 0;
        flex-shrink: 0;
        width: 200px;
        padding: 10px;
        border-right: 1px solid #DEDEDE;
        overflow: hidden;
        overflow-y: auto;
        .session-chat{
          padding: 5px;
          transition: background-color 100ms;
        }
        .session{
          display: flex;
          height: 40px;
          .headimgurl{
            width: 40px;
            margin-right: 10px;
            flex-grow: 0;
            flex-shrink: 0;
          }
          .username{
            flex-grow: 1;
            flex-shrink: 1;
            display: flex;
            flex-flow: column;
            justify-content: space-between;
            .name{
              display: flex;
              justify-content: space-between;
            }
            .time{
              font-size: 11px;
            }
          }
        }
        .session-chat.active{
          background: #f2f2f2;
        }
      }
      .session-content{
        flex-grow: 1;
        flex-shrink: 1;
        padding: 10px;
      }
    }
</style>
