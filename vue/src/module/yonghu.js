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
 * @return {EYonghu}
 */

export const YonghuCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        yonghuming: "",
        mima: "",
        xingming: "",
        xingbie: "",
        shouji: "",
        youxiang: "",
        shenfenzheng: "",
        touxiang: "",
    };

    return form;
};

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EYonghuForm>}
 */
export const canYonghuCreateForm = () => {
    return new Promise(async (resolve, reject) => {
        var form = YonghuCreateForm();
        resolve({ form });
    });
};

/**
 * 响应式获取用户 模块的表单字段数据
 * @return {EYonghuForm}
 */
export const useYonghuCreateForm = () => {
    const form = YonghuCreateForm();
    const formReactive = reactive(form);

    return { form: formReactive };
};

export const canYonghuSelect = (filter, result) => {
    http.post("/api/yonghu/selectPages").then((res) => {
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
export const useYonghuSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canYonghuSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EYonghu>}
 */
export const canYonghuFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/yonghu/findById", { id }).then((res) => {
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
 * @return {EYonghu}
 */
export const useYonghuFindById = (id) => {
    var form = reactive({});

    canYonghuFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EYonghu} data
 * @return {Promise<EResponseData<EYonghu>>}
 */
export const canYonghuInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/yonghu/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("yonghu_insert", res.data);
                        event.emit("yonghu_change", res.data);
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
 * @param {EYonghu} data
 * @return {Promise<EResponseData<EYonghu>>}
 */
export const canYonghuUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/yonghu/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("yonghu_update", res.data);
                        event.emit("yonghu_change", res.data);
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
export const canYonghuDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/yonghu/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("yonghu_delete", res.data);
                        event.emit("yonghu_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};
