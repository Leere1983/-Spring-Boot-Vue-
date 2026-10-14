<template>
    <div class="e-select-list">
        <div class="select-update">
            <div>
                <el-select @change="$emit('update:modelValue' , $event)" :model-value="modelValue" filterable>
                    <el-option :value="0" label="请选择"></el-option>
                    <el-option :value="v.id" :label="getLabel(v)" v-for="v in list" :key="v.id"></el-option>
                </el-select>
            </div>
            <div>
                关键词：
                <el-input style="width: 150px" v-model="searchMsg.keyword" />
            </div>
            <div class="select-search-box">
                <slot name="search"></slot>
            </div>
            <div class="search-btn">
                <el-button icon="el-icon-search" @click="searchTable(true)"> {{searchText}} </el-button>
            </div>
        </div>
        <div class="" v-if="modelValue!=0">
            <slot> </slot>
        </div>
    </div>
</template>
<style type="text/scss" lang="scss">
    .e-select-list {
        .select-search-box {
            display: flex;
            > div {
                margin-right: 10px;
            }
        }
        .select-update {
            display: flex;
            flex-wrap: wrap;
            > div {
                margin-right: 10px;
            }
            > div:last-child {
                margin-right: 0px;
            }
        }
        .search-list {
        }
    }
</style>
<script>
    import { empty, isArray, isObject, isFunction } from "@/utils/extend";
    import { findObject } from "@/utils/utils";
    import DB from "@/utils/db";

    export default {
        name: "e-select-list",
        data() {
            return {
                list: [],
                searchMsg: {
                    keyword: "",
                },
            };
        },
        emits: ["update-where", "update-query", "update:modelValue", "select-list"],
        props: {
            modelValue: [String, Number],
            model: {
                type: Object,
                required: true,
            },
            module: {
                type: String,
                required: true,
            },
            selectUpdate: [String, Array],
            searchUpdate: {
                type: [String, Array],
                default: "",
            },
            showField: {
                type: [String, Array],
            },
            filters: {
                type: Function,
            },
            isInit: {
                type: Boolean,
                default: true,
            },
            searchText: {
                type: String,
                default: "搜索",
            },
        },
        watch: {
            modelValue(val) {
                this.selectOption(val);
            },
        },
        computed: {},
        methods: {
            selectOption(value) {
                var v = findObject(this.list, (r) => r.id == value);
                if (v !== false) {
                    this.updateModel(v);
                } else if (value == 0) {
                    this.updateModel({});
                }
            },
            updateModel(v) {
                var field = isArray(this.selectUpdate) ? this.selectUpdate : this.selectUpdate.split(",");
                for (var i = 0; i < field.length; i++) {
                    var f = field[i];
                    if (f) {
                        var def = /(^[0-9]+$)/.test(this.model[f]) && this.model[f].length < 11 ? 0 : "";
                        this.model[f] = v[f] !== undefined ? v[f] : def;
                    }
                }
            },
            getLabel(v) {
                var result = "";
                var field = [];
                if (this.showField) {
                    field = isArray(this.showField) ? this.showField : this.showField.split(",").filter((v) => !empty(v));
                } else {
                    field = isArray(this.selectUpdate) ? this.selectUpdate : this.selectUpdate.split(",");
                }

                for (var i = 0; i < field.length; i++) {
                    var f = field[i];
                    if (f && v[f]) {
                        result += v[f] + " - ";
                    }
                }
                return result;
            },
            searchTable(isEmit) {
                var where = [];
                var field = isArray(this.selectUpdate) ? this.selectUpdate : this.selectUpdate.split(",");
                var ext = isArray(this.searchUpdate) ? this.searchUpdate : this.searchUpdate.split(",");
                var fs = field.concat(ext).filter((f) => f != "");
                if (this.searchMsg.keyword) {
                    where.push([fs.join("|"), "like", "%" + this.searchMsg.keyword + "%"]);
                }

                var query = DB.name(this.module);

                this.$emit("update-where", where);
                this.$emit("update-query", query);

                if (where.length > 0) {
                    where.forEach((s) => {
                        query.where.apply(query, s);
                    });
                }
                if (!query.hasOption("order")) {
                    query.order("id desc");
                }
                return new Promise(async (resolve, reject) => {
                    var res = await query.select().catch(reject);
                    if (isEmit) {
                        this.$emit("update:modelValue", 0);
                    }
                    if (isFunction(this.filters)) {
                        this.list = this.filters(res);
                    } else {
                        this.list = res;
                    }
                    this.$emit("select-list", this.list);
                    resolve(res);
                });
            },
        },
        created() {
            if (this.isInit) {
                this.searchTable().then((res) => {
                    if (this.modelValue) {
                        this.selectOption(this.modelValue);
                    }
                });
            }
        },
        mounted() {},
        destroyed() {},
    };
</script>
