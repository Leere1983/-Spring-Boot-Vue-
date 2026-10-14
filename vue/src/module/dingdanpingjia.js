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
 * @return {EDingdanpingjia}
 */

export const DingdanpingjiaCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        shangpinbianhao: "",
        shangpinmingcheng: "",
        fenlei: "",
        faburen: $session.username,
        dingdanbianhao: "",
        jingpairen: $session.username,
        pingfen: "",
        pingjia: "",
    };

    return form;
};

function exportForm(form, readMap) {
    var autoText = ["jingpaidingdanid", "shangpinbianhao", "shangpinmingcheng", "fenlei", "faburen", "dingdanbianhao", "jingpairen"];
    for (var txt of autoText) {
        form[txt] = readMap[txt];
    }
}

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EDingdanpingjiaForm>}
 */
export const canDingdanpingjiaCreateForm = (id, readMap) => {
    return new Promise(async (resolve, reject) => {
        var form = DingdanpingjiaCreateForm();
        if (!readMap || !readMap.id) {
            readMap = await canJingpaidingdanFindById(id).catch(reject);
        }
        exportForm(form, readMap);
        form.jingpaidingdanid = readMap.id;
        resolve({ form, readMap });
    });
};

/**
 * 响应式获取订单评价 模块的表单字段数据
 * @return {EDingdanpingjiaForm}
 */
export const useDingdanpingjiaCreateForm = (id) => {
    const form = DingdanpingjiaCreateForm();
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

export const canDingdanpingjiaSelect = (filter, result) => {
    http.post("/api/dingdanpingjia/selectPages").then((res) => {
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
export const useDingdanpingjiaSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canDingdanpingjiaSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EDingdanpingjia>}
 */
export const canDingdanpingjiaFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/dingdanpingjia/findById", { id }).then((res) => {
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
 * @return {EDingdanpingjia}
 */
export const useDingdanpingjiaFindById = (id) => {
    var form = reactive({});

    canDingdanpingjiaFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EDingdanpingjia} data
 * @return {Promise<EResponseData<EDingdanpingjia>>}
 */
export const canDingdanpingjiaInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/dingdanpingjia/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("dingdanpingjia_insert", res.data);
                        event.emit("dingdanpingjia_change", res.data);
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
 * @param {EDingdanpingjia} data
 * @return {Promise<EResponseData<EDingdanpingjia>>}
 */
export const canDingdanpingjiaUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/dingdanpingjia/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("dingdanpingjia_update", res.data);
                        event.emit("dingdanpingjia_change", res.data);
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
export const canDingdanpingjiaDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/dingdanpingjia/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("dingdanpingjia_delete", res.data);
                        event.emit("dingdanpingjia_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};
