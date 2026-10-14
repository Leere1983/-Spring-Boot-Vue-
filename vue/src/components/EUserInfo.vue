<template>
    <div class="e-user" @click.stop.prevent="clickUser" v-if="user">
        <div class="e-user-touxiang" v-if="touxiang" :style="{'--touxiang-width':thumbWidth}">
            <e-img :src="user.touxiang" v-if="user.touxiang" pb="100" is-round />
        </div>
        <div class="e-user-info">
            <div class="nickname">
                <slot name="nickname" :user="user"> {{ user.name }} </slot>
            </div>
            <slot :user="user"></slot>
        </div>
    </div>
</template>
<script setup>
    import DB from "@/utils/db";
    import { watch, ref, unref } from "vue";
    const props = defineProps({
        module: String,
        name: String,
        touxiang: String,
        username: String,
        value: String,
        thumbWidth: {
            type: String,
            default: "64px",
        },
    });

    const emits = defineEmits(["click"]);
    const user = ref({});
    const clickUser = () => {
        emits("click", unref(user));
    };
    watch(
        () => props,
        async () => {
            if (props.value) {
                var row = await DB.name(props.module).where(props.username, props.value).find();
                row.name = row[props.name];
                if (props.touxiang) {
                    row.touxiang = row[props.touxiang];
                }
                user.value = row;
                console.log(unref(user));
            }
        },
        { immediate: true, deep: true }
    );
</script>

<style scoped lang="scss">
    .e-user {
        width: 100%;
        display: flex;
        .e-user-touxiang {
            width: var(--touxiang-width, 64px);
            height: var(--touxiang-width, 64px);
            margin-right: 10px;
            flex-shrink: 0;
            padding: 5px;
        }
        .e-user-info {
            flex-grow: 1;
            flex-shrink: 1;
            width: 0;
            /*padding: 10px 0;*/
            .nickname {
                font-size: 16px;
                /*font-weight: 600;*/
            }
        }
    }
</style>
