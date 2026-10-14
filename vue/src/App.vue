<script setup>
import { RouterLink, RouterView, useRouter } from "vue-router";
import { useUserStore } from "@/stores";
import AiQaDialog from "@/components/ai-qa-dialog.vue";
let userStore = useUserStore();
userStore.tokenLogin();
</script>

<template>
    <RouterView />
    <e-chat-dialog/>
    <AiQaDialog />
</template>

<style>
html,
body,
#app {
    height: 100%;
}
</style>
