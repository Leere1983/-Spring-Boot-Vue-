<script>
    import DB from "@/utils/db";

    export default {
        name: "e-dataset-table",
        data() {
            return {
                lists: [],
            };
        },
        props: {
            table: {
                type: String,
                required: true,
            },
            where: {
                type: Object,
            },
            limit: {
                type: [String, Number],
            },
            order: {
                type: [String],
            },
            field: {
                type: [String],
            },
            var: {
                type: String,
                required: true,
            },
        },
        render(createElement) {
            var result = {};
            result[this.var] = this.lists;
            return createElement("div", [this.$scopedSlots.default(result)]);
        },
        watch: {
            table() {
                this.loadList();
            },
            where() {
                this.loadList();
            },
            limit() {
                this.loadList();
            },
            order() {
                this.loadList();
            },
            field() {
                this.loadList();
            },
            var() {
                this.loadList();
            },
        },
        methods: {
            async loadList() {
                if (!this.table) return;
                var query = DB.name(this.table);
                if (this.where) {
                    this.where.forEach((where) => {
                        query.where.apply(query, where);
                    });
                }
                if (this.limit) {
                    query.limit(this.limit);
                }
                if (this.order) {
                    query.order(this.order);
                }
                if (this.field) {
                    query.field(this.field);
                }
                this.lists = await query.select();
            },
        },
        created() {
            if (this.table) {
                this.loadList();
            }
        },
        mounted() {},
        destroyed() {},
    };
</script>
