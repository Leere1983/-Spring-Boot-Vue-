import { createRouter, createWebHistory } from "vue-router";
import { useUserStore } from "@/stores";
import routes from "./routes";
import config from "@/config";
import { ElMessageBox } from "element-plus";
import { inArray } from "@/utils";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes,
});

router.beforeEach(async (to, from) => {
    //to.meta.login
    var user = useUserStore();
    if (to.meta.authLogin) {
        // 需要验证登录
        if (!user.isLogin()) {
            if (to.meta.msg) {
                await ElMessageBox.alert("尚未登录，请登录后操作");
            }
            return `/login?redirect=${to.fullPath}`;
        }
    }

    document.title = (to.meta.title ? to.meta.title + "-" : "") + config.title;
});

export default router;
