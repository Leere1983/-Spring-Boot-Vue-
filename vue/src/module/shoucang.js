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
 * @return {EShoucang}
 */

export const ShoucangCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        xwid: route.query?.xwid,
        biao: route.query?.biao,
        biaoti: route.query?.biaoti,
    };

    return form;
};

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EShoucangForm>}
 */
export const canShoucangCreateForm = () => {
    return new Promise(async (resolve, reject) => {
        var form = ShoucangCreateForm();
        resolve({ form });
    });
};

/**
 * 响应式获取收藏 模块的表单字段数据
 * @return {EShoucangForm}
 */
export const useShoucangCreateForm = () => {
    const form = ShoucangCreateForm();
    const formReactive = reactive(form);

    return { form: formReactive };
};

export const canShoucangSelect = (filter, result) => {
    http.post("/api/shoucang/selectPages").then((res) => {
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
export const useShoucangSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canShoucangSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EShoucang>}
 */
export const canShoucangFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/shoucang/findById", { id }).then((res) => {
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
 * @return {EShoucang}
 */
export const useShoucangFindById = (id) => {
    var form = reactive({});

    canShoucangFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EShoucang} data
 * @return {Promise<EResponseData<EShoucang>>}
 */
export const canShoucangInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/shoucang/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("shoucang_insert", res.data);
                        event.emit("shoucang_change", res.data);
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
 * @param {EShoucang} data
 * @return {Promise<EResponseData<EShoucang>>}
 */
export const canShoucangUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/shoucang/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("shoucang_update", res.data);
                        event.emit("shoucang_change", res.data);
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
export const canShoucangDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/shoucang/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("shoucang_delete", res.data);
                        event.emit("shoucang_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};
