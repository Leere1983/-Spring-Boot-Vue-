import { ref, onUnmounted, watch, computed } from "vue";
import date, { getTime } from "@/utils/date";
import eventMitt from "@/utils/event";
import { useUserStore } from "@/stores";

export function useDate(format, addtime = 0) {
    const time = ref("");
    const updateTimer = () => {
        time.value = date(format, getTime() + addtime);
    };
    updateTimer();
    const timer = setInterval(updateTimer, 1000);
    onUnmounted(() => {
        clearInterval(timer);
    });
    return time;
}

export function useEvent(event, callback) {
    const func = function () {
        return callback.apply(null, arguments);
    };
    eventMitt.on(event, func);
    onUnmounted(() => {
        eventMitt.off(event, func);
    });
}

export function useSession(callback = null, immediate = false) {
    var store = useUserStore();
    var session = computed(() => {
        return store.session;
    });
    if (callback) {
        var cancel = watch(session, callback, {
            deep: true,
            immediate,
        });
        // 取消监听
        onUnmounted(() => {
            cancel();
        });
    }
    return session;
}
