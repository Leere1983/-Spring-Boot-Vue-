import http from "@/utils/ajax/http";
import { useRoute } from "vue-router";
import { useUserStore } from "@/stores";
import { reactive, ref, unref } from "vue";
import rule from "@/utils/rule";
import { extend, isArray } from "@/utils/extend";
import { ElMessageBox } from "element-plus";
import router from "@/router";
import event from "@/utils/event";

import { canShangjiaFindById } from "./shangjia";

/**
 * 响应式的对象数据
 * @return {EShangjiatixian}
 */

export const ShangjiatixianCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        zhanghao: $session.username,
        dianpumingcheng: "",
        fuzeren: "",
        tixianjine: "",
        tixianshangjia: $session.username,
    };

    return form;
};

function exportForm(form, readMap) {
    var autoText = ["shangjiaid", "zhanghao", "dianpumingcheng", "fuzeren"];
    for (var txt of autoText) {
        form[txt] = readMap[txt];
    }
}

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EShangjiatixianForm>}
 */
export const canShangjiatixianCreateForm = (id, readMap) => {
    return new Promise(async (resolve, reject) => {
        var form = ShangjiatixianCreateForm();
        if (!readMap || !readMap.id) {
            readMap = await canShangjiaFindById(id).catch(reject);
        }
        exportForm(form, readMap);
        form.shangjiaid = readMap.id;
        resolve({ form, readMap });
    });
};

/**
 * 响应式获取商家提现 模块的表单字段数据
 * @return {EShangjiatixianForm}
 */
export const useShangjiatixianCreateForm = (id) => {
    const form = ShangjiatixianCreateForm();
    const formReactive = reactive(form);

    const readMap = reactive({});
    canShangjiaFindById(id).then(
        (map) => {
            exportForm(formReactive, map);
            extend(readMap, map);
            formReactive.shangjiaid = map.id;
        },
        (err) => {
            ElMessageBox.alert(err.message);
        }
    );
    return { form: formReactive, readMap };
};

export const canShangjiatixianSelect = (filter, result) => {
    http.post("/api/shangjiatixian/selectPages").then((res) => {
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
export const useShangjiatixianSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canShangjiatixianSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EShangjiatixian>}
 */
export const canShangjiatixianFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/shangjiatixian/findById", { id }).then((res) => {
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
 * @return {EShangjiatixian}
 */
export const useShangjiatixianFindById = (id) => {
    var form = reactive({});

    canShangjiatixianFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EShangjiatixian} data
 * @return {Promise<EResponseData<EShangjiatixian>>}
 */
export const canShangjiatixianInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/shangjiatixian/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("shangjiatixian_insert", res.data);
                        event.emit("shangjiatixian_change", res.data);
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
 * @param {EShangjiatixian} data
 * @return {Promise<EResponseData<EShangjiatixian>>}
 */
export const canShangjiatixianUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/shangjiatixian/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("shangjiatixian_update", res.data);
                        event.emit("shangjiatixian_change", res.data);
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
export const canShangjiatixianDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/shangjiatixian/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("shangjiatixian_delete", res.data);
                        event.emit("shangjiatixian_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};
