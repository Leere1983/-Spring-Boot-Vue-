import {isObject} from "@/utils/extend";
import {formatImageSrc} from "@/utils/utils";
import {useUserStore} from "@/stores";

export function updateChat( chat ) {
    chat.loading = 'loading';
    chat.data = [];
    chat.timer = null;
    chat.distincData = [];
    chat.offsetMax = 0;
    chat.offsetMin = 0;
    chat.searchForm = {
        pageNumber: 1,
        pageSize: 20
    };
}

export function handlerMsg(e) {
    var session = useUserStore().session;
    return {
        id:e.id,
        chatid:e.siliaoid,
        content:JSON.parse(e.neirong),
        avatar:formatImageSrc(e.fasongrenObj.touxiang),
        isOneself:e.fasongren === session.username,
        addtime:e.fasongshijian,
        nickname:e.fasongrenObj.xingming
    };

}
export default {
    handlerMsg,
    updateChat
}
