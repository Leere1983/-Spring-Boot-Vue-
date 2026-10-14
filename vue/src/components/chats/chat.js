import { defineStore } from "pinia";
import bus from './bus'
import {sendMessage} from './websocket'
import {extend, findIndex, findObject} from "@/utils";
import {handlerMsg, updateChat} from "@/components/chats/utils";
import http from "@/utils/ajax/http";
import {useUserStore} from "@/stores";
import rule from "@/utils/rule";
import playMusicSrc from './play.mp3';

/**
 * 消息结构体
 * @typedef {Object} Msg
 * @property {number} id 对象id
 * @property {number} siliaoid 会话id
 * @property {string} bianhao 会话编号
 * @property {string} neirong 会话内容
 * @property {string} fasongren 发送人
 * @property {string} fasongshijian 发送时间
 * @property {("是"|"否")} shifouchakan 是否查看
 */

/**
 * @typedef {Object} SendMsg
 * @property {number} id 对象id
 * @property {number} siliaoid 会话id
 * @property {string} bianhao 会话编号
 * @property {string} neirong 会话内容
 * @property {string} fasongren 发送人
 * @property {string} fasongshijian 发送时间
 * @property {("是"|"否")} shifouchakan 是否查看
 * @property {string} job 任务对象 发送时会有，
 * @property {"loading"|"done"|"error"} status 发送状态
 * @property {number} timer 发送是否超时
 */


/**
 * @typedef {Object} SessionChat
 * @property {number} id 会话id
 * @property {string} bianhao 会话编号
 * @property {string} shouxinren 收信人
 * @property {string} faxinren 发信人
 * @property {string} addtime 最后时间
 * @property {'loading'|'one'|'loop'|'done'} loading 加载状态
 * @property {Array<Msg>} data 消息列表
 * @property {number} timer    消息时钟
 * @property {Set<number>} distincData 当前的会话id
 * @property {number} offsetMax 偏移最大id
 * @property {number} offsetMin 偏移最小id
 * @property {Object} searchForm 当前分页信息
 */

/**
 *
 * @param {Object} sess
 * @return {SessionChat}
 */
function createChat(sess){
    var chat = extend(true,{},sess);
    chat.loading = 'loading';
    chat.data = [];
    chat.timer = null;
    chat.distincData = new Set();
    chat.offsetMax = 0;
    chat.offsetMin = 0;
    chat.searchForm = {
        pageNumber: 1,
        pageSize: 20
    };
    return chat;
}


export const useChatStore = defineStore('chat' , {
    state(){
        return {
            currentSessionTime:'',
            /**
             * @type {Array<SessionChat>}
             */
            sessionList:[],
            currentChat:null,
            status:false,
            visibleModel:false,
            isMusic:false,
            mine:{},
            /**
             * @type {Array<SendMsg>}
             */
            jobMsg:[]
        }
    },
    getters:{
        readCount:state=>{
            var readCount = 0;
            for (var co of state.sessionList){
                readCount += co.readCount;
            }
            return readCount;
        }
    },
    actions:{
        showModel(){
            this.visibleModel = true;
        },
        hideModel(){
            this.visibleModel = false;
        },
        onLogin(){
            this.status = true;
            const userStore = useUserStore();

            // 获取发送方信息
            http.get('/api/chat/checkmine',{sid:userStore.getSession('username')}).then(res=>{
                this.mine = res.data;
            });

            // 获取会话列表
            sendMessage({
                cmd:'sessionList',
                type:'one',
            });

        },
        onLogout(){
            this.visibleModel = false;
            this.status = false;
            this.currentChat = null;
            this.sessionList = [];
            this.jobMsg = [];
        },
        onPackage(msg){
            if(!this.status)return;
            console.log('onPackage',msg.cmd ,msg.data);
            if(msg.cmd === 'message'){
                this.onMessage(msg.data);
            }else if(msg.cmd === 'message_job'){
                // 消息发送完成,处理任务
                this.onMessageJob(msg.data);
            }else if(msg.cmd === 'sessionList'){
                // 更新sessionList
                this.onSessionList(msg.data);
            }
        },
        onMessage({msg,chat}){
            /**
             * @type {SessionChat|boolean}
             */
            var sessionChat = findObject(this.sessionList,c=>c.id == chat.id);
            if(!sessionChat){
                // 这个会话列表为空，插入会话
                sessionChat = createChat(chat);
                // 将数据插入到新的会话列表
                this.sessionList.push(sessionChat);
            } else {
                // 数量累加
                sessionChat.readCount++;
                if(this.currentChat?.id == sessionChat.id){
                    this.updateRead({chat:sessionChat})
                }
            }
            var e = handlerMsg(msg);

            var msgid = Math.floor(e.id);
            sessionChat.offsetMax = msgid;
            sessionChat.data.push(e);
            sessionChat.distincData.add(msgid);
            this.playMusic();
            bus.emit("loopChatMessage");
        },
        /**
         *
         * @param {string} job
         * @param {Msg} msg
         */
        onMessageJob({job,msg}){
            // 消息发送完成，更新msg 包的状态
            var index = findIndex(this.jobMsg,s=>s.job === job);
            if(index !== false){
                var [obj] = this.jobMsg.splice(index,1);
                obj.id = msg.id;
                obj.status = 'done';

                var chat = findObject(this.sessionList,s=>s.id == obj.chatid);
                var msgid = Math.floor(obj.id);
                chat.offsetMax = msgid;
                chat.distincData.add(msgid);

                clearTimeout(obj.timer);
            }
        },
        playMusic(){
            if(this.isMusic)return;
            var audio = document.querySelector('#audio_chat_fuck');
            if(!audio){
                audio = new Audio(playMusicSrc);
                document.body.append(audio);
                audio.addEventListener("ended" , ()=>{
                    this.isMusic = false;
                    audio.pause();
                });
            }
            audio.currentTime = 0;
            audio.play();
        },
        onSessionList(sessionList){
            /**
             * @type {Array<SessionChat>}
             */
            var list = [];
            for(var chat of sessionList){
                var c = createChat(chat);
                list.push(c);
            }

            this.sessionList = list.sort((a,b)=>{
                if(a.addtime === b.addtime){
                    return 0;
                }
                return b.addtime > a.addtime ? 1 : -1;
            });
        },
        check( {sid,rid} ){
            return new Promise((resolve, reject) => {
                var index = this.sessionList.findIndex(chat=>(chat.shouxinren == sid && chat.faxinren == rid) || (chat.shouxinren == rid && chat.faxinren == sid) );
                if(index !== -1){
                    var chat = this.sessionList[index];
                    this.selectChat(chat);
                    this.visibleModel = true;
                    resolve(chat);
                    return;
                }
                http.post("/api/chat/check" , {sid,rid}).json().then(res=>{
                    var chat = createChat(res.data);
                    this.visibleModel = true;
                    var index = this.sessionList.findIndex(s=>s.id == chat.id);
                    if(index === -1){
                        // 检测chat 是否存在
                        this.sessionList.unshift(chat);
                        this.selectChat(chat);
                    }
                    resolve(chat);
                },reject);
            });
        },
        /**
         *
         * @param {SessionChat} chat
         */
        selectChat(chat){
            this.currentChat = chat;
            if(chat){
                this.updateRead({chat});
                if(chat.loading === 'loading'){
                    // 第一次加载消息
                    chat.loading = 'one';
                    this.loadChatMessageList(chat,'one').then(res=>{
                        bus.emit("loopChatMessage");
                    });
                }
            }
        },
        /**
         * 加载下一页的聊天记录
         * @param {SessionChat} chat
         */
        loadChatMessageNext(chat){
            return new Promise((resolve, reject) => {
                // 加载下一页内容
                if(chat.loading == 'loop'){
                    // 等于继续loop的时候在loop
                    chat.loading = 'loadnext';
                    this.loadChatMessageList(chat,'top').then(resolve,reject);
                }else{
                    reject('not');
                }
            })
        },
        /**
         *
         * @param {SessionChat} chat
         * @param loadType
         */
        loadChatMessageList(chat , loadType){
            return new Promise((resolve, reject) => {
                var form = {
                    pageSize:20,
                    pageNumber:1,
                    type:loadType,
                    chatid:chat.id
                };
                form.offsetMax = chat.offsetMax;
                form.offsetMin = chat.offsetMin;

                http.post('/api/chat/chatMessage',form).json().then(res=>{
                    var list = res.data;
                    list.forEach(e=>{
                        var id = Math.floor(e.id);
                        if(chat.offsetMax < id){
                            chat.offsetMax = id;
                        }else if(chat.offsetMin === 0 || chat.offsetMin > id) {
                            chat.offsetMin = id;
                        }
                        if(!chat.distincData.has(id)){
                            var data = handlerMsg( e );
                            chat.distincData.add(id);
                            if (loadType == 'one' || loadType == 'top'){
                                chat.data.unshift(data);
                            }else if(loadType == 'loop'){
                                chat.data.push(data);
                            }
                        }
                    });
                    if(list.length == 0){
                        chat.loading = 'done';
                    }else{
                        chat.loading = 'loop';
                    }
                    resolve();
                },reject);
            });
        },
        updateRead({chat}){
            var sid = useUserStore().getSession('username');
            http.post("/api/chat/updateRead",{chatid:chat.id,sid}).json().then(res=>{
                chat.readCount = 0;
            });
        },
        sendContent({content,chat}){
            return new Promise((resolve, reject) => {
                var userStore = useUserStore();
                var session = userStore.session;

                var job = rule.getID();
                /**
                 *
                 * @type {SendMsg}
                 */
                var form = {
                    neirong: JSON.stringify(content),
                    fasongren: session.username,
                    fasongshijian: rule.date("Y-m-d H:i:s"),
                    shifouzhakan: "否",
                    siliaoid: chat.id,
                };
                sendMessage({
                    cmd:'send',
                    data:form,
                    job:job
                });

                form.fasongrenObj = this.mine;
                var msg = handlerMsg(form);
                msg.job = job;
                msg.status = 'loading';
                msg.timer = setTimeout(()=>{
                    msg.status = 'error'; // 发送超时
                },5000);

                chat.data.push(msg);
                this.jobMsg.push(msg);
                resolve(msg);
            });
        }

    }
});


bus.on('connect' , function (){
    useChatStore().onLogin();
});
bus.on("close" , function (){
    useChatStore().onLogout();
});
bus.on('message' ,function (data){
    useChatStore().onPackage(data);
});
