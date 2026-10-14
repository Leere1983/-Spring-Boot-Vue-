import chats from "./chats";
import chatSession from './chat-session';
import button from "./button";
import dialog from "./chat-dialog";
import check from "./chat-check";
import {init} from './websocket';
import {createApp} from "vue";
import ElementPlus from 'element-plus'
/**
 *
 * @param {App<Element>} Vue
 */
function install(Vue) {
    Vue.component(chats.name , chats)
    Vue.component(chatSession.name , chatSession)
    Vue.component(button.name , button)
    Vue.component(dialog.name , dialog)
    Vue.component(check.name  , check)
    init();
}

export default {
    install
}
