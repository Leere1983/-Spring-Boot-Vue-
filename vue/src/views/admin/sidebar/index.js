import { useUserStore } from "@/stores/user";

import guanliyuan from "./guanliyuan";
import yonghu from "./yonghu";
import shangjia from "./shangjia";

export function getMenus() {
    return new Promise((resolve, reject) => {
        var userStore = useUserStore();
        var cx = userStore.getSession("cx");
        if (cx == "管理员") {
            resolve(guanliyuan);
        }
        if (cx == "用户") {
            resolve(yonghu);
        }
        if (cx == "商家") {
            resolve(shangjia);
        }
    });
}

export default {
    getMenus,
};
