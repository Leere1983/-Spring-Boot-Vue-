import axios from "axios";
import config from "@/config";
import Qs from "qs";
import jdk, { isArray } from "@/utils/extend";
import router from "@/router";

import { useUserStore } from "@/stores";
import { ElNotification } from "element-plus";

/**
 * 封装axios数据提交，创建AxiosInstance实例，返回给到https
 * @type {axios.AxiosInstance}
 */
var https = axios.create({
    baseURL: config.service_url,
    withCredentials: true,
    /**
     * 将数据转化成json方式提交
     */
    transformRequest: [
        function (data, headers) {
            if (!(data instanceof FormData) && typeof data != "string") {
                if (headers["Content-Type"] && headers["Content-Type"] == "application/json;charset=UTF-8") {
                    data = JSON.stringify(data);
                } else {
                    data = Qs.stringify(data, { arrayFormat: "repeat" });
                }
            }
            return data;
        },
    ],
});

/**
 * 创建一个链式操作方法
 * @param url  后端路径，不设置则使用默认的 config.service_url 地址
 * @param content  提交的内容
 * @returns {fetchModel}
 */
function fetchModel(url, content) {
    /**
     * 非new 方式时自动设置为fetchModel 方法
     */
    if (!(this instanceof fetchModel)) {
        return new fetchModel(url, content);
    }
    /**
     * 获取用户信息
     * @type {Store<"user", {sessionInfo: null, id: number, token: null|any, username: string}, {}, {}>}
     */
    var userStore = useUserStore();

    // 设置内容
    this._content = content;
    // 创建初始化信息
    this._init = jdk.extend(
        true,
        {},
        {
            method: "GET",
            cache: "no-cache",
            headers: {
                //'Content-Type': 'application/json',
                // 设置X-Requested-With 头参数，使用第三方的时候需要设置 这个头
                "X-Requested-With": "XMLHttpRequest",
                // 给后端进行验证登录的
                token: userStore.token,
            },
            cors: "cors",
            credentials: true,
            data: {},
        }
    );
    this.url(url);
    // 设置为json 方式提交
    this.json();
}

/**
 * 设置基本信息
 * @type {string}
 */
fetchModel.baseUrl = config.service_url;
// fetchModel 的子方法
fetchModel.prototype = {
    // 模型参数
    _url: "",
    _content: null,
    _init: null,
    _leval: false,
    // 使用什么方式提交
    method: function (method) {
        this._init.method = method;
        return this;
    },
    // get 方法
    get: function () {
        return this.method("GET");
    },
    // 层级
    leval: function (setLeval) {
        this._leval = setLeval;
        return this;
    },
    // json方式提交
    json() {
        return this.header("Content-Type", "application/json;charset=UTF-8");
    },
    // post 方式提交
    post: function () {
        return this.method("POST");
    },
    // put 方式提交
    put: function () {
        return this.method("PUT");
    },
    // 删除方式提交
    delete: function () {
        return this.method("DELETE");
    },
    // head 方式提交
    head: function () {
        return this.method("HEAD");
    },
    // url 地址
    url: function (url) {
        this._url = url;
        return this;
    },
    /**
     * 设置进度条的回调方法
     * @param target
     * @returns {fetchModel}
     */
    progress: function (target) {
        this._init.onUploadProgress = target;
        return this;
    },
    header: function (name, value) {
        this._init.headers[name] = value;
        return this;
    },
    body: function (value) {
        this._init.data = value;
        return this;
    },
    add: function (name, value) {
        this._init[name] = value;
        return this;
    },
    then: function (fn, fn2) {
        // 开始执行
        var url = this.getUrl();

        return new Promise((resolve, reject) => {
            https(url, this._init)
                .then((result) => {
                    var data = result.data;
                    if ((data.code == 1001 || result.status == 401) && router.currentRoute.name != "Login") {
                        //store.commit("user/deleteToken");
                        useUserStore().setToken(null);
                        useUserStore().setSession(null);
                        router.replace({
                            path: "/login",
                            query: {
                                callback: router.currentRoute.fullPath,
                            },
                        });
                    }
                    resolve(data);
                })
                .catch((err) => {
                    console.log(err);
                    if (err.response) {
                        var result = err.response;
                        var data = result.data;

                        if ((data.code == 1001 || result.status == 401) && router.currentRoute.name != "Login") {
                            //store.commit("user/deleteToken");
                            useUserStore().setToken(null);
                            useUserStore().setSession(null);
                            router.replace({
                                path: "/login",
                                query: {
                                    callback: router.currentRoute.fullPath,
                                },
                            });
                        }
                        resolve(data);
                    } else {
                        ElNotification.error({
                            title: "访问服务器错误",
                            dangerouslyUseHTMLString: true,
                            message: err.message + ",服务器尚未启动",
                        });
                        reject(err);
                    }
                });
        }).then(fn, fn2);
    },
    getUrl() {
        var url = this._url;
        if (this._init.method == "GET" && this._init.data) {
            var body = this._init.data;
            var query = jdk.isObject(body) ? Qs.stringify(body) : body;
            url += (url.indexOf("?") == -1 ? "?" : "&") + query;
            this._init.data = null;
        }
        return url;
    },
    thenBlob(fn, fn2) {
        var url = this.getUrl();
        https(url, jdk.extend(true, {}, this._init, { responseType: "blob" }))
            .then((res) => {
                fn(res.data);
            })
            .catch((err) => {
                console.log(err);
                ElNotification.error({
                    title: "访问服务器错误",
                    dangerouslyUseHTMLString: true,
                    message: err.message + ",服务器尚未启动",
                });
                fn2 && fn2(err);
            });
    },
};

export function fetchGet(url, param) {
    var m = new fetchModel(url).get();
    if (param) m.body(param);

    return m;
}

export function fetchPost(url, param) {
    var m = new fetchModel(url).post();
    if (param) m.body(param);
    return m;
}

export function fetchPostJson(url, param) {
    var m = new fetchModel(url).post().json();
    if (param) m.body(param);
    return m;
}

export default {
    http: fetchModel,
    get: fetchGet,
    post: fetchPost,
    json: fetchPostJson,

    //leval:leval
};
