import { createApp } from "vue";
import { createPinia } from "pinia";

import App from "./App.vue";
import router from "./router";
import ElementPlus from "element-plus";
import Components from "./components/index";
import zhCN from "element-plus/es/locale/lang/zh-cn";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";

import "vue3-video-play/dist/style.css";
import "element-plus/dist/index.css";
import "vue-design-plugin/dist/style.css";
import vueDesignPlugin from "vue-design-plugin";
import { formatImageSrc, upload } from "@/utils/utils";
import "@/assets/font-awesome/css/font-awesome.css";
import "./assets/styles.scss";
import httpPlugin from "@/utils/ajax";

const app = createApp(App);

app.use(ElementPlus, {
    locale: zhCN,
});
app.use(createPinia());
app.use(router);
app.use(vueDesignPlugin, {
    formatImageSrc,
    uploadFile: upload,
});
app.use(httpPlugin);
app.use(Components);
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    var k = key
        .replace(/[A-Z]/g, ($0) => {
            return "-" + $0;
        })
        .toLocaleLowerCase();
    if (k.indexOf("-") === 0) {
        k = k.substring(1);
    }
    app.component("el-icon-" + k, component);
}
app.mount("#app");
