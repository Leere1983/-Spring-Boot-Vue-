<template>
    <div>
        <div class="chat">
            <div class="chat-message-body" ref="chatform" @scroll="scroll" v-loading="loading">
                <div dis-hover v-for="(item,index) in currentChat.data" :key="index" class="message-card">
                    <div :class="item.isOneself?'message-row-right': 'message-row-left'">
                        <el-image :src="item.avatar?item.avatar:defualtAvatar"
                                  height="35" width="35" class="avatar" fit="cover" round></el-image>
                        
                        <div class="message-content">
                            <div :style="item.isOneself?'text-align:right;display: flex;flex-direction:row-reverse':''">
                                {{ item.nickname }}
                                <span class="message-time">
                                    {{ item.addtime }}
                                </span>
                            </div>
                            <div class="message-body">
                                <msg-index :msg="item.content"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <el-input
                v-model="form.msg"
                type="textarea"
                style="margin:10px 0;"
                placeholder="主动一点，世界会更大！"
                @keyup.shift.enter="sendMsg"
                :rows="4">
            
            </el-input>
        </div>
        <div class="footer-btn">
            <el-button type="primary" @click="sendMsg">发送</el-button>
        </div>
    </div>
</template>


<script>
import {extend, isObject} from "@/utils/extend";
import bus from '@/components/chats/bus';
import avatar from './avatar.jpg';
import {mapState, mapActions} from 'pinia'
import {useChatStore} from "@/components/chats/chat";
import MsgIndex from './msg/index.vue'
import TWEEN from './Tween'

export default {
    name: "e-chat",
    components: {MsgIndex},
    props: {},
    data() {
        return {
            loading: false,
            defualtAvatar: avatar, // 后端没有返回头像默认头像，注意：需要用require请求方式才能动态访问本地文件
            form: { // 发送数据提交数据表单
                content: '',
                msg: ""
            },
        };
    },
    computed: {
        ...mapState(useChatStore, {
            currentChat: (state) => state.currentChat,
        }),
    },
    methods: {
        scrollToBottom() { // 滚动到窗体底部
            this.$nextTick(() => {
                let chatform = this.$refs.chatform;
                if (chatform) {
                    
                    var target = chatform.scrollHeight + chatform.offsetTop;
                    console.log("scrollToBottom",target,chatform.scrollHeight);
                    
                    new TWEEN.Tween({tweeningNumber: chatform.scrollTop})
                        .easing(TWEEN.Easing.Quadratic.Out)
                        .to({ tweeningNumber: target }, 100)
                        .onUpdate(function (data) {
                            chatform.scrollTop = data.tweeningNumber;
                        })
                        .run()
             
                    //chatform.scrollTop = chatform.scrollHeight;
                }
            });
        },
        // 滚动到最上方，取历史数据，根据分页参数取。不用修改偏移标记位，但是需要判重
        scroll() {
            if (this.loading) return;
            let chatform = this.$refs.chatform;
            if (chatform) {
                let scrollTop = chatform.scrollTop;
                
                if (scrollTop == 0) {
                    this.loading = true;
                    useChatStore().loadChatMessageNext(this.currentChat).then(res => {
                        this.loading = false;
                    }).catch(err=>{
                      this.loading = false;
                    });
                    this.$emit('scroll-top');
                }
            }
        },
        sendMsg() { // 发送消息
            if (!this.form.msg) {
                this.$message.warning("不能发送空白信息");
                return;
            }
            let content = { // 封装消息体
                type: "txt",
                msg: this.form.msg
            };
            //content = this.form.msg;
            this.sendContent({content, chat: this.currentChat}).then(res => {
                this.form.msg = '';
                this.scrollToBottom();
            }).catch(err=>{
              this.loading = false;
            });
        },
        loopChatMessage() {
            this.scrollToBottom();
        },
        ...mapActions(useChatStore, ['sendContent'])
    },
    mounted() {
        bus.on('loopChatMessage', this.loopChatMessage);
        this.scrollToBottom();
    },
    beforeDestroy() {
        bus.off('loopChatMessage', this.loopChatMessage);
    }
};
</script>

<style lang="scss">
.message {
    height: 350px;
}

.ivu-card-body {
    padding: 5px;
}

.ivu-modal-body {
    padding: 0px 16px 16px 16px;
}

.chat-message-body {
    background-color: #F8F8F6;
    height: 350px;
    overflow: auto;
}

.message-card {
    margin: 5px;
    
    .avatar {
        width: 45px;
        background-color: #ffffff;
        height: 45px;
        border-radius: 50%;
        overflow: hidden;
    }
}

.message-row-left {
    display: flex;
    flex-direction: row;
}

.message-row-right {
    display: flex;
    flex-direction: row-reverse;
}

.message-content {
    margin: -5px 5px 5px 5px;
    display: flex;
    flex-direction: column;
}

.message-body {
    border: 1px solid #D9DAD9;
    padding: 5px;
    border-radius: 3px;
    background-color: #FFF;
}

.message-time {
    margin: 0 5px;
    font-size: 5px;
    color: #D9DAD9;
}

.footer-btn {
    position: absolute;
    
    margin-bottom: 5px;
}

.spin-icon-load {
    animation: ani-spin 1s linear infinite;
}

@keyframes ani-spin {
    from {
        transform: rotate(0deg);
    }
    50% {
        transform: rotate(180deg);
    }
    to {
        transform: rotate(360deg);
    }
}
</style>
