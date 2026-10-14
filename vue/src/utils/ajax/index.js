/**
 * Created by JnToo on 2017/8/1.
 */

import http from "./http";

function plugin(app) {
    if (plugin.installed) {
        return;
    }
    plugin.installed = true;
    app.config.globalProperties.https = http;
    app.config.globalProperties.$get = http.get;
    app.config.globalProperties.$post = http.post;
    app.config.globalProperties.$ajax = http.http;
}

if (typeof window !== "undefined" && window.Vue) {
    window.Vue.use(plugin);
}

export const httpPost = http.post;
export const httpGet = http.get;

export default plugin;
