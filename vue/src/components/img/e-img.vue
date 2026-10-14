<template>
    <div style="width: 100%">
        <div class="img-box" :class="classData">
            <div class="img" :style="{'background-image':'url('+image+')'}"></div>
        </div>
    </div>
</template>

<script setup>
    import { formatImageSrc } from "@/utils/utils";
    import { isObject, isArray } from "@/utils/extend";
    import { computed } from "vue";
    import utils from "@/utils/utils";

    var props = defineProps({
        pb: {
            type: [String, Number],
            default: 100,
        },
        src: [String, Array, Object],
        isRound: {
            type: Boolean,
            default: false,
        },
        isScale: {
            type: Boolean,
            default: false,
        },
    });

    const image = computed(() => {
        var src = props.src;
        if (!src) {
            return "";
        }
        if (src instanceof Blob) {
            return src;
        }
        if (src.indexOf("data:image") === 0) {
            return src;
        }
        if (isArray(src)) {
            src = src[0];
        }
        if (isObject(src)) {
            src = src.filepath;
        }

        var a = src.split(",");
        return formatImageSrc(a[0]);
    });

    const classData = computed(() => {
        var cls = "pb" + props.pb;
        if (props.isScale) {
            cls += " img-scale";
        }
        if (props.isRound) {
            cls += " img-round";
        }
        return cls;
    });
</script>

<style lang="scss">
    .img-box.img-round {
        border-radius: 50%;
    }
</style>
