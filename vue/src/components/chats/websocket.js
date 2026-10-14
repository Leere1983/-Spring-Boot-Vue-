import bus from './bus';
import event from '@/utils/event';
import {formatImageSrc, isString} from "@/utils";
import {useUserStore} from "@/stores";

/**
 *
 * @type {WebSocket}
 */
var websocket = null;
var timer = null;

function updateHeartbeat()
{
    clearInterval(timer);
    timer = setInterval(()=>{
        sendMessage({cmd:'heartbeat'});
    },30*1000);
}

function connect(){
    var userStore = useUserStore();
    var session = userStore.session;

    var url = formatImageSrc('/websocket/'+session.username);
    url = url.replace(/^http(s)?/i , ($0,$1)=>{
        return 'ws'+($1||'');
    });
    websocket = new WebSocket(url);
    websocket.onopen = (e)=>{
        bus.emit('connect');
        console.log(`connect`);
        updateHeartbeat();
    };
    websocket.onmessage = (e)=>{
        bus.emit('message' , JSON.parse(e.data));
    };
    websocket.onclose = (e)=>{
        bus.emit('close',e);
        console.log('websocket close');
    };
    websocket.onerror = (e)=>{
        bus.emit('error' , e);
        console.log(`链接错误`);
    }
}

export function sendMessage(data){
    updateHeartbeat();
    if(!isString(data)){
        data = JSON.stringify(data);
    }
    if(isOpen()){
        websocket.send(data);
    }
}

export function isOpen(){
    return websocket && websocket.readyState === websocket.OPEN;
}

export function init(){
    event.on('login' , connect);
    event.on('logout' , ()=>{
        if(isOpen()){
            websocket.close();
            websocket = null;
        }
    });

}



