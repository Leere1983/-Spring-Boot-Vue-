import http from "./ajax/http";
import config from "@/config";
import { ref } from "vue";

function executeQuery(name, options, func, args) {
    return new Promise((resolve, reject) => {
        http.post(config.query_url, {
            name,
            options,
            func,
            args,
        })
            .json()
            .then((res) => {
                if (res.code == 0) {
                    resolve(res.data);
                } else {
                    reject(new Error(res.msg));
                }
            }, reject);
    });
}

export class Query {
    name = "";
    options = {};
    constructor(name) {
        this.name = name;
        this.options = {};
    }
    /**
     *
     * @param field
     * @param exp
     * @param condtion
     * @returns {Query}
     */
    where(field, exp, condtion) {
        if (!this.options.where) {
            this.options.where = [];
        }
        var len = arguments.length;
        if (arguments.length == 1) {
            this.options.where.push({ raw: true, name: field });
            return this;
        }
        if (len == 2) {
            condtion = exp;
            exp = "=";
        }
        this.options.where.push({
            name: field,
            exp: exp,
            value: condtion,
        });
        return this;
    }
    /**
     *
     * @param func
     * @param args
     * @returns {*|Promise<unknown>|Promise|Promise|Promise|Promise}
     */
    executeQuery(func, args) {
        return executeQuery(this.name, this.options, func, args);
    }
    /**
     *
     * @returns {*}
     */
    select() {
        return this.executeQuery("select", null);
    }
    /**
     *
     * @param id
     * @returns {*}
     */
    find(id) {
        var args = null;
        if (id) {
            args = [id];
        }
        return this.executeQuery("find", args);
    }
    count(field) {
        var args = null;
        if (field) {
            args = [field];
        }
        return this.executeQuery("count", args);
    }
    avg(field) {
        var args = null;
        if (field) {
            args = [field];
        }
        return this.executeQuery("avg", args);
    }
    max(field) {
        var args = null;
        if (field) {
            args = [field];
        }
        return this.executeQuery("max", args);
    }
    min(field) {
        var args = null;
        if (field) {
            args = [field];
        }
        return this.executeQuery("min", args);
    }
    sum(field) {
        var args = null;
        if (field) {
            args = [field];
        }
        return this.executeQuery("sum", args);
    }
    column(field, key) {
        return new Promise((resolve, reject) => {
            this.field(field);
            if (key) {
                this.field(key);
            }
            var isAllfield = field.indexOf(",") !== -1 || field.indexOf("*") !== -1;
            this.select().then((res) => {
                var lists = res;
                var result;
                if (key) {
                    result = {};
                    for (var ci of lists) {
                        if (isAllfield) {
                            result[ci[key]] = ci;
                        } else {
                            result[ci[key]] = ci[field];
                        }
                    }
                } else {
                    result = [];
                    for (var ci of lists) {
                        if (isAllfield) {
                            result.push(ci);
                        } else {
                            result.push(ci[field]);
                        }
                    }
                }
                resolve(result);
            }, reject);
        });
    }
    /**
     *
     * @param field
     * @param sort
     * @returns {Query}
     */
    order(field, sort) {
        var len = arguments.length;
        var order = field;
        if (len == 2) {
            order += " " + sort;
        }
        this.getOptionList("order").push(order);
        return this;
    }
    alias(name) {
        this.options.alias = name;
        return this;
    }
    limit(offset, nLimit) {
        var len = arguments.length;
        var map = this.getOptionMap("limit");
        if (len == 1) {
            nLimit = Math.floor(offset);
            offset = null;
        }
        map.size = Math.floor(nLimit);
        map.offset = Math.floor(offset);
        return this;
    }
    joinInner(table, cond) {
        return this.join(table, cond, "INNER");
    }
    joinRight(table, cond) {
        return this.join(table, cond, "RIGHT");
    }
    joinLeft(table, cond) {
        return this.join(table, cond, "LEFT");
    }
    join(table, cond, type) {
        var str = ` ${type} JOIN ${table} ON ${cond} `;
        this.getOptionList("join").push(str);
        return this;
    }
    field(fi) {
        this.getOptionList("field").push(fi);
        return this;
    }
    group(gr) {
        this.getOptionList("group").push(gr);
        return this;
    }
    getOptionMap(name) {
        if (!this.options[name]) {
            this.options[name] = {};
        }
        return this.options[name];
    }
    getOptionList(name) {
        if (!this.options[name]) {
            this.options[name] = [];
        }
        return this.options[name];
    }
    hasOption(name) {
        return !!this.options[name];
    }
}

export class QueryRef extends Query {
    constructor(name) {
        super(name);
    }

    /**
     *
     * @param func
     * @param value
     * @param arg
     * @return Ref
     */
    execute(func, value, ...arg) {
        const v = ref(value);
        super[func].apply(this, arg).then(
            (res) => {
                v.value = res;
            },
            (err) => {
                console.log("获取数据出错了", err.message);
            }
        );
        return v;
    }
    selectRef() {
        return this.execute("select", []);
    }
    findRef(id) {
        return this.execute("find", {}, id);
    }
    sumRef(field) {
        return this.execute("sum", 0, field);
    }
    countRef(field) {
        return this.execute("count", 0, field);
    }
    minRef(field) {
        return this.execute("min", 0, field);
    }
    maxRef(field) {
        return this.execute("max", 0, field);
    }
    avgRef(field) {
        return this.execute("avg", 0, field);
    }
    columnRef(field, key) {
        return this.execute("avg", 0, field, key);
    }
}

export const DB = {
    /**
     * @returns QueryRef
     */
    name(name) {
        return new QueryRef(name);
    },
};

export default DB;
