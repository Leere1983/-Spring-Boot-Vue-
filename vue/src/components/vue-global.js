/**
 * vue 全局参数
 */
import { useUserStore } from "@/stores";
import date, { getTime } from "@/utils/date";
import { formatImageSrc, upload } from "@/utils/utils";
import { isString } from "@/utils/extend";
import Print from "@/utils/print";
export function vueGlobalInstall(app) {
    Object.defineProperty(app.config.globalProperties, "$session", {
        enumerable: true,
        get: () => useUserStore().session || {},
    });

    app.config.globalProperties.$date = date;
    app.config.globalProperties.$print = Print;
    app.config.globalProperties.$substr = (str, len, append = "...") => {
        if (!str) return "";
        var content = String(str)
            .replace(/<[^>]+>/g, "")
            .replace(/\&nbsp\;/gi, "")
            .replace(/\s+/gi, "")
            .replace(/(\n|\r)/gi, "");
        if (content.length < len) {
            return content;
        }
        return content.substring(0, len) + append;
    };

    app.config.globalProperties.$useDate = (format, time = 0) => {
        return date(format, getTime() + time);
    };

    app.config.globalProperties.$formatImageSrc = formatImageSrc;
    app.config.globalProperties.$upload = upload;
    app.config.globalProperties.$goto = (url, replace = false) => {
        let router = app.config.globalProperties.$router;

        if (isString(url) && (url == "#" || url.indexOf("javascript:") != -1)) return;

        if (isString(url) && /^https?\:\/\//gi.test(url)) {
            if (replace) {
                location.replace(url);
            } else {
                location.href = url;
            }
            return;
        }
        if (replace) {
            router.replace(url);
        } else {
            router.push(url);
        }
    };
}
