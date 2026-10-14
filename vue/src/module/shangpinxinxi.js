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
 * @return {EShangpinxinxi}
 */

export const ShangpinxinxiCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        shangpinbianhao: rule.getID(),
        fenlei: "",
        shangpinmingcheng: "",
        shangpintupian: "",
        qipaijia: "",
        jiajiafudu: "",
        qipaishijian: "",
        jieshushijian: "",
        jingpairenshu: "0",
        shangpinxiangqing: "",
        shangpinzhuangtai: "未开始",
        jingpairen: $session.username,
        faburen: $session.username,
        issh: "否",
    };

    return form;
};

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EShangpinxinxiForm>}
 */
export const canShangpinxinxiCreateForm = () => {
    return new Promise(async (resolve, reject) => {
        var form = ShangpinxinxiCreateForm();
        resolve({ form });
    });
};

/**
 * 响应式获取商品信息 模块的表单字段数据
 * @return {EShangpinxinxiForm}
 */
export const useShangpinxinxiCreateForm = () => {
    const form = ShangpinxinxiCreateForm();
    const formReactive = reactive(form);

    return { form: formReactive };
};

export const canShangpinxinxiSelect = (filter, result) => {
    http.post("/api/shangpinxinxi/selectPages").then((res) => {
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
export const useShangpinxinxiSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canShangpinxinxiSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EShangpinxinxi>}
 */
export const canShangpinxinxiFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/shangpinxinxi/findById", { id }).then((res) => {
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
 * @return {EShangpinxinxi}
 */
export const useShangpinxinxiFindById = (id) => {
    var form = reactive({});

    canShangpinxinxiFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EShangpinxinxi} data
 * @return {Promise<EResponseData<EShangpinxinxi>>}
 */
export const canShangpinxinxiInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/shangpinxinxi/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("shangpinxinxi_insert", res.data);
                        event.emit("shangpinxinxi_change", res.data);
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
 * @param {EShangpinxinxi} data
 * @return {Promise<EResponseData<EShangpinxinxi>>}
 */
export const canShangpinxinxiUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/shangpinxinxi/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("shangpinxinxi_update", res.data);
                        event.emit("shangpinxinxi_change", res.data);
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
 * @param {EShangpinxinxi} data
 * @return {Promise<EResponseData<EShangpinxinxi>>}
 */
export const canShangpinxinxiCheckIssh = (row) => {
    return new Promise((resolve, reject) => {
        var id = row.id;
        var value = row.issh === "是" ? "否" : "是";
        http.get("/api/shangpinxinxi/checkIssh", { id, value })
            .json()
            .then(
                (res) => {
                    if (res.code == 0) {
                        row.issh = value;
                    }
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("shangpinxinxi_update", row);
                        event.emit("shangpinxinxi_change", row);
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
export const canShangpinxinxiDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/shangpinxinxi/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("shangpinxinxi_delete", res.data);
                        event.emit("shangpinxinxi_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};

export const useShangpinxinxishoucang = (id, iss, count) => {
    const is_shoucang = iss ? iss : ref(false);
    const shoucangCount = count ? count : ref(0);

    http.get("/api/shangpinxinxi/getshoucang", { id }).then((res) => {
        if (res.code == 0) {
            is_shoucang.value = res.data.is_shoucang;
            shoucangCount.value = res.data.shoucangCount;
        }
    });

    return { is_shoucang, shoucangCount };
};
