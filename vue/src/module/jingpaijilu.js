import http from "@/utils/ajax/http";
import { useRoute } from "vue-router";
import { useUserStore } from "@/stores";
import { reactive, ref, unref } from "vue";
import rule from "@/utils/rule";
import { extend, isArray } from "@/utils/extend";
import { ElMessageBox } from "element-plus";
import router from "@/router";
import event from "@/utils/event";

import { canShangpinxinxiFindById } from "./shangpinxinxi";

/**
 * 响应式的对象数据
 * @return {EJingpaijilu}
 */

export const JingpaijiluCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        shangpinbianhao: "",
        fenlei: "",
        shangpinmingcheng: "",
        shangpintupian: "",
        qipaijia: "",
        faburen: $session.username,
        jiajiafudu: "",
        jingpaijia: "",
        jingpairen: $session.username,
    };

    return form;
};

function exportForm(form, readMap) {
    var autoText = ["shangpinxinxiid", "shangpinbianhao", "fenlei", "shangpinmingcheng", "shangpintupian", "qipaijia", "faburen", "jiajiafudu"];
    for (var txt of autoText) {
        form[txt] = readMap[txt];
    }
}

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EJingpaijiluForm>}
 */
export const canJingpaijiluCreateForm = (id, readMap) => {
    return new Promise(async (resolve, reject) => {
        var form = JingpaijiluCreateForm();
        if (!readMap || !readMap.id) {
            readMap = await canShangpinxinxiFindById(id).catch(reject);
        }
        exportForm(form, readMap);
        form.shangpinxinxiid = readMap.id;
        resolve({ form, readMap });
    });
};

/**
 * 响应式获取竞拍记录 模块的表单字段数据
 * @return {EJingpaijiluForm}
 */
export const useJingpaijiluCreateForm = (id) => {
    const form = JingpaijiluCreateForm();
    const formReactive = reactive(form);

    const readMap = reactive({});
    canShangpinxinxiFindById(id).then(
        (map) => {
            exportForm(formReactive, map);
            extend(readMap, map);
            formReactive.shangpinxinxiid = map.id;
        },
        (err) => {
            ElMessageBox.alert(err.message);
        }
    );
    return { form: formReactive, readMap };
};

export const canJingpaijiluSelect = (filter, result) => {
    http.post("/api/jingpaijilu/selectPages").then((res) => {
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
export const useJingpaijiluSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canJingpaijiluSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EJingpaijilu>}
 */
export const canJingpaijiluFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/jingpaijilu/findById", { id }).then((res) => {
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
 * @return {EJingpaijilu}
 */
export const useJingpaijiluFindById = (id) => {
    var form = reactive({});

    canJingpaijiluFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EJingpaijilu} data
 * @return {Promise<EResponseData<EJingpaijilu>>}
 */
export const canJingpaijiluInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/jingpaijilu/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("jingpaijilu_insert", res.data);
                        event.emit("jingpaijilu_change", res.data);
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
 * @param {EJingpaijilu} data
 * @return {Promise<EResponseData<EJingpaijilu>>}
 */
export const canJingpaijiluUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/jingpaijilu/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("jingpaijilu_update", res.data);
                        event.emit("jingpaijilu_change", res.data);
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
export const canJingpaijiluDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/jingpaijilu/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("jingpaijilu_delete", res.data);
                        event.emit("jingpaijilu_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};
