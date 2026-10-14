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
 * @return {ELiuyanban}
 */

export const LiuyanbanCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        xingming: $session.xingming,
        lianxidianhua: $session.shouji,
        fankuineirong: "",
        fankuiyonghu: $session.username,
        fankuizhuangtai: "待回复",
    };

    return form;
};

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<ELiuyanbanForm>}
 */
export const canLiuyanbanCreateForm = () => {
    return new Promise(async (resolve, reject) => {
        var form = LiuyanbanCreateForm();
        resolve({ form });
    });
};

/**
 * 响应式获取留言反馈 模块的表单字段数据
 * @return {ELiuyanbanForm}
 */
export const useLiuyanbanCreateForm = () => {
    const form = LiuyanbanCreateForm();
    const formReactive = reactive(form);

    return { form: formReactive };
};

export const canLiuyanbanSelect = (filter, result) => {
    http.post("/api/liuyanban/selectPages").then((res) => {
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
export const useLiuyanbanSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canLiuyanbanSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<ELiuyanban>}
 */
export const canLiuyanbanFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/liuyanban/findById", { id }).then((res) => {
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
 * @return {ELiuyanban}
 */
export const useLiuyanbanFindById = (id) => {
    var form = reactive({});

    canLiuyanbanFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {ELiuyanban} data
 * @return {Promise<EResponseData<ELiuyanban>>}
 */
export const canLiuyanbanInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/liuyanban/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("liuyanban_insert", res.data);
                        event.emit("liuyanban_change", res.data);
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
 * @param {ELiuyanban} data
 * @return {Promise<EResponseData<ELiuyanban>>}
 */
export const canLiuyanbanUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/liuyanban/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("liuyanban_update", res.data);
                        event.emit("liuyanban_change", res.data);
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
export const canLiuyanbanDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/liuyanban/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("liuyanban_delete", res.data);
                        event.emit("liuyanban_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};
