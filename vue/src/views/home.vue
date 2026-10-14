<script setup>
    import { RouterView } from "vue-router";
    import CommonHeader from "@/views/header.vue";
    import CommonFooter from "@/views/footer.vue";
</script>

<template>
    <div class="index">
        <common-header></common-header>
        <router-view />
        <common-footer></common-footer>
    </div>
</template>

<style scoped lang="scss">
    .index {
    }
</style>
