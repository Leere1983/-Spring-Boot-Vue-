<script>
    import { isFunction } from "@/utils/extend";
    import DB from "@/utils/db";
    import { h as createElement, resolveComponent } from "vue";

    export default {
        name: "e-select-option",
        data() {
            return {
                options: [],
            };
        },
        props: {
            type: {
                type: String,
                default: "radio",
            },
            module: {
                type: String,
                required: true,
            },
            value: {
                type: String,
                required: true,
            },
            label: {
                type: String,
                required: true,
            },
            order: {
                type: String,
                default: "id desc",
            },
            where: {
                type: Array,
            },
            filter: {
                type: Function,
                default: null,
            },
            lists: {
                type: Array,
            },
        },
        watch: {
            where: {
                handler() {
                    this.loadModule();
                },
                deep: true,
            },
            order() {
                this.loadModule();
            },
            value() {
                this.loadModule();
            },
            label() {
                this.loadModule();
            },
        },
        methods: {
            async loadModule() {
                var query = DB.name(this.module);
                if (this.filter && isFunction(this.filter)) {
                    this.filter(query);
                }
                if (this.where) {
                    this.where.forEach((s) => {
                        query.where.apply(query, s);
                    });
                }
                query.order(this.order);
                var lists = await query.select();
                if (this.value == "id") {
                    for (var ci of lists) {
                        ci.id = Math.floor(ci.id);
                    }
                }
                this.options = lists;
                this.$emit("update:lists", lists);
            },
        },
        created() {
            this.loadModule();
        },
        render() {
            var { type, value, label, options } = this;
            var result = [];
            var ci;
            switch (type) {
                case "radio":
                case "checkbox":
                    for (ci of options) {
                        result.push(
                            createElement(
                                resolveComponent("el-" + type),
                                {
                                    label: ci[value],
                                },
                                [ci[label]]
                            )
                        );
                    }
                    break;
                default:
                    for (ci of options) {
                        result.push(
                            createElement(resolveComponent("el-option"), {
                                value: ci[value],
                                label: ci[label],
                            })
                        );
                    }
                    break;
            }
            return result;
        },
        computed: {},
    };
</script>
