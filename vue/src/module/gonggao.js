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
 * @return {EGonggao}
 */

export const GonggaoCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        biaoti: "",
        neirong: "",
        faburen: $session.username,
        issh: "否",
    };

    return form;
};

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EGonggaoForm>}
 */
export const canGonggaoCreateForm = () => {
    return new Promise(async (resolve, reject) => {
        var form = GonggaoCreateForm();
        resolve({ form });
    });
};

/**
 * 响应式获取公告 模块的表单字段数据
 * @return {EGonggaoForm}
 */
export const useGonggaoCreateForm = () => {
    const form = GonggaoCreateForm();
    const formReactive = reactive(form);

    return { form: formReactive };
};

export const canGonggaoSelect = (filter, result) => {
    http.post("/api/gonggao/selectPages").then((res) => {
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
export const useGonggaoSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canGonggaoSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EGonggao>}
 */
export const canGonggaoFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/gonggao/findById", { id }).then((res) => {
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
 * @return {EGonggao}
 */
export const useGonggaoFindById = (id) => {
    var form = reactive({});

    canGonggaoFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EGonggao} data
 * @return {Promise<EResponseData<EGonggao>>}
 */
export const canGonggaoInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/gonggao/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("gonggao_insert", res.data);
                        event.emit("gonggao_change", res.data);
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
 * @param {EGonggao} data
 * @return {Promise<EResponseData<EGonggao>>}
 */
export const canGonggaoUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/gonggao/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("gonggao_update", res.data);
                        event.emit("gonggao_change", res.data);
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
 * @param {EGonggao} data
 * @return {Promise<EResponseData<EGonggao>>}
 */
export const canGonggaoCheckIssh = (row) => {
    return new Promise((resolve, reject) => {
        var id = row.id;
        var value = row.issh === "是" ? "否" : "是";
        http.get("/api/gonggao/checkIssh", { id, value })
            .json()
            .then(
                (res) => {
                    if (res.code == 0) {
                        row.issh = value;
                    }
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("gonggao_update", row);
                        event.emit("gonggao_change", row);
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
export const canGonggaoDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/gonggao/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("gonggao_delete", res.data);
                        event.emit("gonggao_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};

export const useGonggaoshoucang = (id, iss, count) => {
    const is_shoucang = iss ? iss : ref(false);
    const shoucangCount = count ? count : ref(0);

    http.get("/api/gonggao/getshoucang", { id }).then((res) => {
        if (res.code == 0) {
            is_shoucang.value = res.data.is_shoucang;
            shoucangCount.value = res.data.shoucangCount;
        }
    });

    return { is_shoucang, shoucangCount };
};
