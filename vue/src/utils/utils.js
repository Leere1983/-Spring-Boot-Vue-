import config from "@/config";
import { isFunction, isObject, isArray } from "@/utils/extend";
import http from "@/utils/ajax/http";
import { ElMessage as message, ElMessageBox } from "element-plus";
import { useUserStore } from "@/stores";
import router from "@/router";
import { unref } from "vue";

export function formatImageSrc(value) {
    if (!value) return "";
    if (value instanceof Blob) {
        return value;
    }
    if (value.indexOf("data:image") === 0) {
        return value;
    }
    if (value.indexOf("/") === 0) {
        return config.service_url + value;
    }
    var url = value;
    if (!url.match(/^https?:\/\//gi)) {
        return config.service_url + "/" + value;
    }
    return url;
}

export function session(name) {
    var user = useUserStore();
    return user.getSession(name);
}

export function logout() {
    ElMessageBox.confirm("确定退出登录？", "温馨提示")
        .then(async (res) => {
            var user = useUserStore();
            try {
                await user.logout();
                message.success("退出登录成功");
            } catch (e) {
                message.error("退出失败");
            } finally {
                if (router.hasRoute("Index")) {
                    router.replace("/index");
                } else {
                    router.replace("/login");
                }
            }
        })
        .catch((err) => {
            console.error(err);
            console.log("取消登录");
        });
}

export function remove(arr, key) {
    if (isObject(key) || isArray(key)) {
        var index = findIndex(arr, (r) => r === key);
        if (index !== false) {
            remove(arr, index);
        }
    } else {
        if (isObject(arr)) {
            delete arr[key];
        } else {
            arr.splice(key, 1);
        }
    }
}

export async function confirmMessage(row, createForm, insertForm, message, successMsg = "处理成功") {
    try {
        await ElMessageBox.confirm(message);
    } catch (e) {
        console.error(e);
        return;
    }
    try {
        var { form } = await createForm(row.id);
        var res = await insertForm(form);
        if (res.code == 0) {
            ElMessage.success(successMsg);
            loadList(search.page);
        } else {
            ElMessage.error(res.msg);
        }
    } catch (e) {
        console.error(e);
        return;
    }
}

export function checkIssh(row, module) {
    http.post(config.checkUpdate, {
        tablename: module,
        id: row.id,
        yuan: row.issh,
    }).then(
        (res) => {
            if (res.code == 0) {
                row.issh = row.issh == "是" ? "否" : "是";
            } else {
                message.error(res.msg);
            }
        },
        (err) => {
            message.error(err.message);
        }
    );
}

export function findIndex(arr, callback) {
    if (!isFunction(callback)) {
        throw new Error("findObject第二个参数是回调函数");
    }
    for (var i in arr) {
        var ci = arr[i];
        if (callback(ci, i)) {
            return i;
        }
    }
    return false;
}

export function findObject(arr, callback) {
    if (!isFunction(callback)) {
        throw new Error("findObject第二个参数是回调函数");
    }
    for (var i in arr) {
        var ci = arr[i];
        if (callback(ci, i)) {
            return ci;
        }
    }
    return false;
}

export function upload(file) {
    return new Promise((resolve, reject) => {
        var formdata = new FormData();
        formdata.append("fujian", file, file.name || "tmp.png");
        http.post(config.uploadUrl, formdata)
            .then((res) => {
                if (res.code == 0) {
                    resolve(res.data);
                } else {
                    reject(res.msg);
                }
            })
            .catch((err) => {
                reject(err.message);
            });
    });
}

export function formatHtml(html) {
    var regex = /(<([^>]+)>)/gi;
    return html.replace(regex, "");
}

export function substr(str, length, append = "...") {
    if (!str) return "";
    var s = formatHtml(unref(str));
    if (s.length > length) {
        return s.substr(0, length) + append;
    }
    return s;
}

export function captch() {
    return new Promise((resolve, reject) => {
        http.get(config.captch_url, { rand: Math.floor(Math.random() * 10000) }).then(
            (res) => {
                if (res.code == 0) {
                    resolve(res.data);
                } else {
                    message.error(res.msg);
                    reject(res.msg);
                }
            },
            (err) => {
                reject(err.message);
            }
        );
    });
}

export function downloadFile(fileRealPath, fileName) {
    let link = document.createElement("a");
    let url = fileRealPath; //codeIMG  要下载的路径
    // 这里是将url转成blob地址，
    http.get(url).thenBlob((blob) => {
        // 将链接地址字符内容转变成blob地址
        link.href = window.URL.createObjectURL(blob);
        link.download = fileName;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(link.href);
    });
}

export default {
    findIndex,
    findObject,
    formatImageSrc,
    checkIssh,
    remove,
    logout,
    session,
    downloadFile,
    captch,
};
