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
 * @return {EShangpinfenlei}
 */

export const ShangpinfenleiCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        fenleimingcheng: "",
    };

    return form;
};

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EShangpinfenleiForm>}
 */
export const canShangpinfenleiCreateForm = () => {
    return new Promise(async (resolve, reject) => {
        var form = ShangpinfenleiCreateForm();
        resolve({ form });
    });
};

/**
 * 响应式获取商品分类 模块的表单字段数据
 * @return {EShangpinfenleiForm}
 */
export const useShangpinfenleiCreateForm = () => {
    const form = ShangpinfenleiCreateForm();
    const formReactive = reactive(form);

    return { form: formReactive };
};

export const canShangpinfenleiSelect = (filter, result) => {
    http.post("/api/shangpinfenlei/selectPages").then((res) => {
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
export const useShangpinfenleiSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canShangpinfenleiSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EShangpinfenlei>}
 */
export const canShangpinfenleiFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/shangpinfenlei/findById", { id }).then((res) => {
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
 * @return {EShangpinfenlei}
 */
export const useShangpinfenleiFindById = (id) => {
    var form = reactive({});

    canShangpinfenleiFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EShangpinfenlei} data
 * @return {Promise<EResponseData<EShangpinfenlei>>}
 */
export const canShangpinfenleiInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/shangpinfenlei/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("shangpinfenlei_insert", res.data);
                        event.emit("shangpinfenlei_change", res.data);
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
 * @param {EShangpinfenlei} data
 * @return {Promise<EResponseData<EShangpinfenlei>>}
 */
export const canShangpinfenleiUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/shangpinfenlei/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("shangpinfenlei_update", res.data);
                        event.emit("shangpinfenlei_change", res.data);
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
export const canShangpinfenleiDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/shangpinfenlei/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("shangpinfenlei_delete", res.data);
                        event.emit("shangpinfenlei_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};
