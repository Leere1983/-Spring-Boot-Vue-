import http from "@/utils/ajax/http";
import { useRoute } from "vue-router";
import { useUserStore } from "@/stores";
import { reactive, ref, unref } from "vue";
import rule from "@/utils/rule";
import { extend, isArray } from "@/utils/extend";
import { ElMessageBox } from "element-plus";
import router from "@/router";
import event from "@/utils/event";

import { canJingpaidingdanFindById } from "./jingpaidingdan";

/**
 * 响应式的对象数据
 * @return {EQuerenshouhuo}
 */

export const QuerenshouhuoCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        shangpinbianhao: "",
        shangpinmingcheng: "",
        shangpintupian: "",
        jingpairen: $session.username,
        faburen: $session.username,
        fenlei: "",
        dingdanbianhao: "",
        shouhuobeizhu: "",
    };

    return form;
};

function exportForm(form, readMap) {
    var autoText = ["jingpaidingdanid", "shangpinbianhao", "shangpinmingcheng", "shangpintupian", "jingpairen", "faburen", "fenlei", "dingdanbianhao"];
    for (var txt of autoText) {
        form[txt] = readMap[txt];
    }
}

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EQuerenshouhuoForm>}
 */
export const canQuerenshouhuoCreateForm = (id, readMap) => {
    return new Promise(async (resolve, reject) => {
        var form = QuerenshouhuoCreateForm();
        if (!readMap || !readMap.id) {
            readMap = await canJingpaidingdanFindById(id).catch(reject);
        }
        exportForm(form, readMap);
        form.jingpaidingdanid = readMap.id;
        resolve({ form, readMap });
    });
};

/**
 * 响应式获取确认收货 模块的表单字段数据
 * @return {EQuerenshouhuoForm}
 */
export const useQuerenshouhuoCreateForm = (id) => {
    const form = QuerenshouhuoCreateForm();
    const formReactive = reactive(form);

    const readMap = reactive({});
    canJingpaidingdanFindById(id).then(
        (map) => {
            exportForm(formReactive, map);
            extend(readMap, map);
            formReactive.jingpaidingdanid = map.id;
        },
        (err) => {
            ElMessageBox.alert(err.message);
        }
    );
    return { form: formReactive, readMap };
};

export const canQuerenshouhuoSelect = (filter, result) => {
    http.post("/api/querenshouhuo/selectPages").then((res) => {
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
export const useQuerenshouhuoSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canQuerenshouhuoSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EQuerenshouhuo>}
 */
export const canQuerenshouhuoFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/querenshouhuo/findById", { id }).then((res) => {
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
 * @return {EQuerenshouhuo}
 */
export const useQuerenshouhuoFindById = (id) => {
    var form = reactive({});

    canQuerenshouhuoFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EQuerenshouhuo} data
 * @return {Promise<EResponseData<EQuerenshouhuo>>}
 */
export const canQuerenshouhuoInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/querenshouhuo/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("querenshouhuo_insert", res.data);
                        event.emit("querenshouhuo_change", res.data);
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
 * @param {EQuerenshouhuo} data
 * @return {Promise<EResponseData<EQuerenshouhuo>>}
 */
export const canQuerenshouhuoUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/querenshouhuo/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("querenshouhuo_update", res.data);
                        event.emit("querenshouhuo_change", res.data);
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
export const canQuerenshouhuoDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/querenshouhuo/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("querenshouhuo_delete", res.data);
                        event.emit("querenshouhuo_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};
