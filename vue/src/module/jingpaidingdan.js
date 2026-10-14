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
 * @return {EJingpaidingdan}
 */

export const JingpaidingdanCreateForm = () => {
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
        dangqianjingpaijia: "",
        jingpairen: $session.username,
        faburen: $session.username,
        fenlei: "",
        dingdanbianhao: rule.getID(),
        dingdanzhuangtai: "待支付",
        lianxiren: "",
        lianxidianhua: "",
        lianxidizhi: "",

        issh: "否",
        iszf: "否",
    };

    return form;
};

function exportForm(form, readMap) {
    var autoText = ["shangpinxinxiid", "shangpinbianhao", "shangpinmingcheng", "shangpintupian", "dangqianjingpaijia", "jingpairen", "faburen", "fenlei"];
    for (var txt of autoText) {
        form[txt] = readMap[txt];
    }
}

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EJingpaidingdanForm>}
 */
export const canJingpaidingdanCreateForm = (id, readMap) => {
    return new Promise(async (resolve, reject) => {
        var form = JingpaidingdanCreateForm();
        if (!readMap || !readMap.id) {
            readMap = await canShangpinxinxiFindById(id).catch(reject);
        }
        exportForm(form, readMap);
        form.shangpinxinxiid = readMap.id;
        resolve({ form, readMap });
    });
};

/**
 * 响应式获取竞拍订单 模块的表单字段数据
 * @return {EJingpaidingdanForm}
 */
export const useJingpaidingdanCreateForm = (id) => {
    const form = JingpaidingdanCreateForm();
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

export const canJingpaidingdanSelect = (filter, result) => {
    http.post("/api/jingpaidingdan/selectPages").then((res) => {
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
export const useJingpaidingdanSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canJingpaidingdanSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EJingpaidingdan>}
 */
export const canJingpaidingdanFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/jingpaidingdan/findById", { id }).then((res) => {
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
 * @return {EJingpaidingdan}
 */
export const useJingpaidingdanFindById = (id) => {
    var form = reactive({});

    canJingpaidingdanFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EJingpaidingdan} data
 * @return {Promise<EResponseData<EJingpaidingdan>>}
 */
export const canJingpaidingdanInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/jingpaidingdan/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("jingpaidingdan_insert", res.data);
                        event.emit("jingpaidingdan_change", res.data);
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
 * @param {EJingpaidingdan} data
 * @return {Promise<EResponseData<EJingpaidingdan>>}
 */
export const canJingpaidingdanUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/jingpaidingdan/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("jingpaidingdan_update", res.data);
                        event.emit("jingpaidingdan_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};

/**
 * 审核是否上架
 * @param {EJingpaidingdan} data
 * @return {Promise<EResponseData<EJingpaidingdan>>}
 */
export const canJingpaidingdanCheckIssh = (row) => {
    return new Promise((resolve, reject) => {
        var id = row.id;
        var value = row.issh === "是" ? "否" : "是";
        http.get("/api/jingpaidingdan/checkIssh", { id, value })
            .json()
            .then(
                (res) => {
                    if (res.code == 0) {
                        row.issh = value;
                    }
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("jingpaidingdan_update", row);
                        event.emit("jingpaidingdan_change", row);
                    }
                },
                (err) => {
                    ElMessageBox.alert(err.message);
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
export const canJingpaidingdanDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/jingpaidingdan/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("jingpaidingdan_delete", res.data);
                        event.emit("jingpaidingdan_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};
