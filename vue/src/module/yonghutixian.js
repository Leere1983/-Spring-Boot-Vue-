import http from "@/utils/ajax/http";
import { useRoute } from "vue-router";
import { useUserStore } from "@/stores";
import { reactive, ref, unref } from "vue";
import rule from "@/utils/rule";
import { extend, isArray } from "@/utils/extend";
import { ElMessageBox } from "element-plus";
import router from "@/router";
import event from "@/utils/event";

import { canYonghuFindById } from "./yonghu";

/**
 * 响应式的对象数据
 * @return {EYonghutixian}
 */

export const YonghutixianCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        yonghuming: $session.username,
        xingming: "",
        tixianjine: "",
        tixianren: $session.username,
    };

    return form;
};

function exportForm(form, readMap) {
    var autoText = ["yonghuid", "yonghuming", "xingming"];
    for (var txt of autoText) {
        form[txt] = readMap[txt];
    }
}

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EYonghutixianForm>}
 */
export const canYonghutixianCreateForm = (id, readMap) => {
    return new Promise(async (resolve, reject) => {
        var form = YonghutixianCreateForm();
        if (!readMap || !readMap.id) {
            readMap = await canYonghuFindById(id).catch(reject);
        }
        exportForm(form, readMap);
        form.yonghuid = readMap.id;
        resolve({ form, readMap });
    });
};

/**
 * 响应式获取用户提现 模块的表单字段数据
 * @return {EYonghutixianForm}
 */
export const useYonghutixianCreateForm = (id) => {
    const form = YonghutixianCreateForm();
    const formReactive = reactive(form);

    const readMap = reactive({});
    canYonghuFindById(id).then(
        (map) => {
            exportForm(formReactive, map);
            extend(readMap, map);
            formReactive.yonghuid = map.id;
        },
        (err) => {
            ElMessageBox.alert(err.message);
        }
    );
    return { form: formReactive, readMap };
};

export const canYonghutixianSelect = (filter, result) => {
    http.post("/api/yonghutixian/selectPages").then((res) => {
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
export const useYonghutixianSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canYonghutixianSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EYonghutixian>}
 */
export const canYonghutixianFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/yonghutixian/findById", { id }).then((res) => {
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
 * @return {EYonghutixian}
 */
export const useYonghutixianFindById = (id) => {
    var form = reactive({});

    canYonghutixianFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EYonghutixian} data
 * @return {Promise<EResponseData<EYonghutixian>>}
 */
export const canYonghutixianInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/yonghutixian/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("yonghutixian_insert", res.data);
                        event.emit("yonghutixian_change", res.data);
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
 * @param {EYonghutixian} data
 * @return {Promise<EResponseData<EYonghutixian>>}
 */
export const canYonghutixianUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/yonghutixian/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("yonghutixian_update", res.data);
                        event.emit("yonghutixian_change", res.data);
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
export const canYonghutixianDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/yonghutixian/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("yonghutixian_delete", res.data);
                        event.emit("yonghutixian_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};
