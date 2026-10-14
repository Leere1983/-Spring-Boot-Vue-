import http from "@/utils/ajax/http";
import { useRoute } from "vue-router";
import { useUserStore } from "@/stores";
import { reactive, ref, unref } from "vue";
import rule from "@/utils/rule";
import { extend, isArray } from "@/utils/extend";
import { ElMessageBox } from "element-plus";
import router from "@/router";
import event from "@/utils/event";

/**
 * 响应式的对象数据
 * @return {EZaixianchongzhi}
 */

export const ZaixianchongzhiCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        chongzhibianhao: rule.getID(),
        chongzhijine: "",
        chongzhiren: $session.username,
        iszf: "否",
    };

    return form;
};

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EZaixianchongzhiForm>}
 */
export const canZaixianchongzhiCreateForm = () => {
    return new Promise(async (resolve, reject) => {
        var form = ZaixianchongzhiCreateForm();
        resolve({ form });
    });
};

/**
 * 响应式获取在线充值 模块的表单字段数据
 * @return {EZaixianchongzhiForm}
 */
export const useZaixianchongzhiCreateForm = () => {
    const form = ZaixianchongzhiCreateForm();
    const formReactive = reactive(form);

    return { form: formReactive };
};

export const canZaixianchongzhiSelect = (filter, result) => {
    http.post("/api/zaixianchongzhi/selectPages").then((res) => {
        if (res.code == 0) {
            extend(result, res.data);
        } else {
            ElMessageBox.alert(res.msg);
        }
    });
};

/**
 * 获取分页数据
 * @param filter
 */
export const useZaixianchongzhiSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canZaixianchongzhiSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EZaixianchongzhi>}
 */
export const canZaixianchongzhiFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/zaixianchongzhi/findById", { id }).then((res) => {
            if (res.code == 0) {
                resolve(res.data);
            } else {
                reject(new Error(res.msg));
            }
        }, reject);
    });
};

/**
 * 根据id 获取一行数据
 * @param id
 * @return {EZaixianchongzhi}
 */
export const useZaixianchongzhiFindById = (id) => {
    var form = reactive({});

    canZaixianchongzhiFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EZaixianchongzhi} data
 * @return {Promise<EResponseData<EZaixianchongzhi>>}
 */
export const canZaixianchongzhiInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/zaixianchongzhi/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("zaixianchongzhi_insert", res.data);
                        event.emit("zaixianchongzhi_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};

/**
 * 根据数据更新数据库
 * @param {EZaixianchongzhi} data
 * @return {Promise<EResponseData<EZaixianchongzhi>>}
 */
export const canZaixianchongzhiUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/zaixianchongzhi/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("zaixianchongzhi_update", res.data);
                        event.emit("zaixianchongzhi_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};

/**
 * 根据id 或者列表id
 * @param {number|number[]} id
 * @return {Promise<EResponseData<string>>}
 */
export const canZaixianchongzhiDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/zaixianchongzhi/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("zaixianchongzhi_delete", res.data);
                        event.emit("zaixianchongzhi_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};
