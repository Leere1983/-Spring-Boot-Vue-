import http from "@/utils/ajax/http";
import { useRoute } from "vue-router";
import { useUserStore } from "@/stores";
import { reactive, ref, unref } from "vue";
import rule from "@/utils/rule";
import { extend, isArray } from "@/utils/extend";
import { ElMessageBox } from "element-plus";
import router from "@/router";
import event from "@/utils/event";

import { canSiliaoFindById } from "./siliao";

/**
 * 响应式的对象数据
 * @return {EXiaoxi}
 */

export const XiaoxiCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        bianhao: "",
        neirong: "",
        fasongren: $session.username,
        fasongshijian: rule.date("Y-m-d H:i:s"),
        shifouzhakan: "",
    };

    return form;
};

function exportForm(form, readMap) {
    var autoText = ["siliaoid", "bianhao"];
    for (var txt of autoText) {
        form[txt] = readMap[txt];
    }
}

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EXiaoxiForm>}
 */
export const canXiaoxiCreateForm = (id, readMap) => {
    return new Promise(async (resolve, reject) => {
        var form = XiaoxiCreateForm();
        if (!readMap || !readMap.id) {
            readMap = await canSiliaoFindById(id).catch(reject);
        }
        exportForm(form, readMap);
        form.siliaoid = readMap.id;
        resolve({ form, readMap });
    });
};

/**
 * 响应式获取消息 模块的表单字段数据
 * @return {EXiaoxiForm}
 */
export const useXiaoxiCreateForm = (id) => {
    const form = XiaoxiCreateForm();
    const formReactive = reactive(form);

    const readMap = reactive({});
    canSiliaoFindById(id).then(
        (map) => {
            exportForm(formReactive, map);
            extend(readMap, map);
            formReactive.siliaoid = map.id;
        },
        (err) => {
            ElMessageBox.alert(err.message);
        }
    );
    return { form: formReactive, readMap };
};

export const canXiaoxiSelect = (filter, result) => {
    http.post("/api/xiaoxi/selectPages").then((res) => {
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
export const useXiaoxiSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canXiaoxiSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EXiaoxi>}
 */
export const canXiaoxiFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/xiaoxi/findById", { id }).then((res) => {
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
 * @return {EXiaoxi}
 */
export const useXiaoxiFindById = (id) => {
    var form = reactive({});

    canXiaoxiFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EXiaoxi} data
 * @return {Promise<EResponseData<EXiaoxi>>}
 */
export const canXiaoxiInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/xiaoxi/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("xiaoxi_insert", res.data);
                        event.emit("xiaoxi_change", res.data);
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
 * @param {EXiaoxi} data
 * @return {Promise<EResponseData<EXiaoxi>>}
 */
export const canXiaoxiUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/xiaoxi/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("xiaoxi_update", res.data);
                        event.emit("xiaoxi_change", res.data);
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
export const canXiaoxiDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/xiaoxi/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("xiaoxi_delete", res.data);
                        event.emit("xiaoxi_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};
