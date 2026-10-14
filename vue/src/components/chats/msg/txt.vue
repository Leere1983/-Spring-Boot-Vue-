<template>
  <div v-html="getContent(msg.msg)"></div>
</template>
<script setup>
const props = defineProps({
  msg:Object
});
const getContent = (html)=>{
  return html ? html.replace(/[\n]/ig , "<br>") : '';
}

</script>

<style scoped lang="scss">

</style>