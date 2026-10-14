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
 * @return {EShangjia}
 */

export const ShangjiaCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        zhanghao: "",
        mima: "",
        dianpumingcheng: "",
        fuzeren: "",
        shenfenzheng: "",
        dianhua: "",
        youxiang: "",
        dizhi: "",
        jianjie: "",
        zizhizhengshu: "",

        issh: "否",
    };

    return form;
};

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EShangjiaForm>}
 */
export const canShangjiaCreateForm = () => {
    return new Promise(async (resolve, reject) => {
        var form = ShangjiaCreateForm();
        resolve({ form });
    });
};

/**
 * 响应式获取商家 模块的表单字段数据
 * @return {EShangjiaForm}
 */
export const useShangjiaCreateForm = () => {
    const form = ShangjiaCreateForm();
    const formReactive = reactive(form);

    return { form: formReactive };
};

export const canShangjiaSelect = (filter, result) => {
    http.post("/api/shangjia/selectPages").then((res) => {
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
export const useShangjiaSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canShangjiaSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EShangjia>}
 */
export const canShangjiaFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/shangjia/findById", { id }).then((res) => {
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
 * @return {EShangjia}
 */
export const useShangjiaFindById = (id) => {
    var form = reactive({});

    canShangjiaFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EShangjia} data
 * @return {Promise<EResponseData<EShangjia>>}
 */
export const canShangjiaInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/shangjia/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("shangjia_insert", res.data);
                        event.emit("shangjia_change", res.data);
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
 * @param {EShangjia} data
 * @return {Promise<EResponseData<EShangjia>>}
 */
export const canShangjiaUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/shangjia/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("shangjia_update", res.data);
                        event.emit("shangjia_change", res.data);
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
 * @param {EShangjia} data
 * @return {Promise<EResponseData<EShangjia>>}
 */
export const canShangjiaCheckIssh = (row) => {
    return new Promise((resolve, reject) => {
        var id = row.id;
        var value = row.issh === "是" ? "否" : "是";
        http.get("/api/shangjia/checkIssh", { id, value })
            .json()
            .then(
                (res) => {
                    if (res.code == 0) {
                        row.issh = value;
                    }
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("shangjia_update", row);
                        event.emit("shangjia_change", row);
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
export const canShangjiaDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/shangjia/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("shangjia_delete", res.data);
                        event.emit("shangjia_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};
