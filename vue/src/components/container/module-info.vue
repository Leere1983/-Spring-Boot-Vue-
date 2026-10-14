<template>
    <div class="e-module-info">
        <slot :data="data"></slot>
    </div>
</template>
<style type="type/scss" lang="scss"></style>
<script>
    import DB from "@/utils/db";

    export default {
        name: "e-module-info",
        data() {
            return {
                data: {},
            };
        },
        props: {
            module: String,
            order: String,
            where: Array,
            field: String,
        },
        watch: {
            module() {
                this.loadData();
            },
            order() {
                this.loadData();
            },
            where() {
                this.loadData();
            },
            field() {
                this.loadData();
            },
        },
        computed: {},
        methods: {
            loadData() {
                var query = DB.name(this.module);
                if (this.where) {
                    this.where.forEach((r) => {
                        query.where.apply(query, r);
                    });
                }
                if (this.order) {
                    query.order(this.order);
                }
                if (this.field) {
                    query.field(this.field);
                }
                query.limit(1);
                query.find().then((res) => {
                    this.data = res;
                });
            },
        },
        created() {
            if (this.module) {
                this.loadData();
            }
        },
        mounted() {},
        destroyed() {},
    };
</script>
