<template>
    <span class="e-select-view"> {{content}} </span>
</template>
<style type="text/scss" lang="scss"></style>
<script>
    import DB from "@/utils/db";

    export default {
        name: "e-select-view",
        data() {
            return {
                content: "",
            };
        },
        props: {
            value: [String, Number],
            module: {
                type: String,
                required: true,
            },
            select: {
                type: [String, Number],
                required: true,
            },
            show: {
                type: [String, Number],
                required: true,
            },
        },
        watch: {
            value() {
                this.getValue();
            },
        },
        computed: {},
        methods: {
            getValue() {
                if (this.value) {
                    DB.name(this.module)
                        .where(this.select, "in", this.value)
                        .select()
                        .then((res) => {
                            var list = res.map((r) => r[this.show]);
                            this.content = list.join(" ");
                        });
                } else {
                    this.content = "";
                }
            },
        },
        created() {
            this.getValue();
        },
        mounted() {},
        destroyed() {},
    };
</script>
