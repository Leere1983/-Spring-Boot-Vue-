import { defineStore } from "pinia";
import { delCache, getCache, setCache } from "@/utils/caches";
import http from "@/utils/ajax/http";
import config from "@/config";
import { isObject, extend } from "@/utils/extend";
import event from "@/utils/event";

const authTokenName = "authToken";
const sessionInfoName = "sessionInfo";

/**
 * 用户信息
 * @type {StoreDefinition<"user", {session: {id: "", username: ""}, token: null|any}, {}, {}>}
 * @return {token:'',session:{id:'',username:''}}
 */
export const useUserStore = defineStore("user", {
    state() {
        return {
            // 验证登录的token
            token: getCache(authTokenName),
            // session信息
            /**
             * @type {{id:'',username:''}}
             */
            session: getCache(sessionInfoName),

            roles: getCache("user_roles"),
        };
    },
    actions: {
        /**
         * 判断是否登录
         * @returns {boolean}
         */
        isLogin() {
            return !!this.token;
        },
        /**
         * 判断是否有用户信息
         * @returns {boolean}
         */
        isUserInfo() {
            return this.session && this.session.id;
        },
        setSession(info) {
            if (info && isObject(info)) {
                if (info.object) {
                    info = extend(true, {}, info, info.object);
                    delete info.object;
                }
                this.session = info;
            } else {
                this.session = null;
            }
            if (this.session) {
                setCache(sessionInfoName, this.session, 7 * 86400);
            } else {
                delCache(sessionInfoName);
            }
        },

        getSession(name = "") {
            if (this.isUserInfo() && this.session) {
                return this.session[name] || null;
            }
            return null;
        },
        setToken(token) {
            this.token = token;
            // 设置token的有效期为7天
            if (token) {
                setCache(authTokenName, token, 7 * 86400);
            } else {
                delCache(authTokenName);
            }
        },
        setRoles(roles) {
            this.roles = roles;
            // 设置token的有效期为7天
            if (roles) {
                setCache("user_roles", roles, 7 * 86400);
            } else {
                delCache("user_roles");
                this.roles = [];
            }
        },
        /**
         * 登录信息
         * @param form {{username:'',password:'',pagerandom:''}}
         */
        login(form) {
            // 登录信息
            return new Promise((resolve, reject) => {
                http.post(config.login_url, form).then((res) => {
                    if (res.code == 0) {
                        this.setSession(res.data.session);
                        this.setToken(res.data.token);
                        if (res.data.roles) {
                            this.setRoles(res.data.roles);
                        }
                        event.emit("login");
                    } else {
                        this.setSession(null);
                        this.setToken(null);
                        event.emit("logout");
                    }
                    resolve(res);
                }, reject);
            });
        },
        /**
         * 用tonken进行登录
         * @returns {Promise<unknown>}
         */
        tokenLogin() {
            /**
             * 返回一个异步信息
             */
            return new Promise((resolve, reject) => {
                http.get(config.token_login, {
                    token: this.token,
                }).then((res) => {
                    if (res.code == 0) {
                        this.setSession(res.data.session);
                        this.setToken(res.data.token);
                        //this.roles = res.data.roles;
                        event.emit("login");
                    } else {
                        this.setToken(null);
                        this.setSession(null);
                        this.setRoles(null);
                        event.emit("logout");
                    }
                    resolve(res);
                }, reject);
            });
        },
        logout() {
            return new Promise((resolve, reject) => {
                http.get(config.logout_login).then((res) => {
                    if (res.code == 0) {
                        this.setToken(null);
                        this.setSession(null);
                        this.setRoles(null);
                        event.emit("logout");
                        resolve(res.data);
                    } else {
                        reject(new Error(res.msg));
                    }
                }, reject);
            });
        },
    },
});

export const canLogin = (form) => {
    const userStore = useUserStore();
    return userStore.login(form);
};
