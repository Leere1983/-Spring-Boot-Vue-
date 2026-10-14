<template>
    <div class="sidebar">
        <el-menu
            class="sidebar-el-menu"
            :default-active="onRoutes"
            :collapse="collapse"
            background-color="#324157"
            text-color="#bfcbd9"
            active-text-color="#20a0ff"
            unique-opened
            router
        >
            <template v-for="(item,k) in menus">
                <template v-if="item.children">
                    <el-sub-menu :index="k+''" :key="k">
                        <template #title>
                            <i :class="item.icon ? item.icon : 'el-icon-arrow-right'"></i>
                            <span>{{ item.label }}</span>
                        </template>
                        <template v-for="(subItem,s) in item.children">
                            <el-sub-menu v-if="subItem.children" :index="k+'-'+s" :key="k+'-'+s">
                                <template #title>{{ subItem.label }}</template>
                                <el-menu-item v-for="(threeItem,i) in subItem.children" :key="k+'-'+s+'-'+i" :index="k+'-'+s+'-'+i">{{ threeItem.label }}</el-menu-item>
                            </el-sub-menu>
                            <el-menu-item v-else :route="subItem.to" :index="k+'-'+s" :key="k+'-'+s">{{ subItem.label }}</el-menu-item>
                        </template>
                    </el-sub-menu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index" :route="item.to" :key="item.index">
                        <i :class="item.icon ? item.icon : 'el-icon-arrow-right'"></i>
                        <template #title>
                            <span> {{ item.label }} </span>
                        </template>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
    import bus from "./bus";

    import menu from "@/views/admin/sidebar/index";

    export default {
        data() {
            return {
                collapse: false,
                menus: [],
            };
        },
        computed: {
            onRoutes() {
                return this.$route.path.replace("/", "");
            },
        },
        created() {
            // 通过 Event Bus 进行组件间通信，来折叠侧边栏
            bus.on("collapse", (msg) => {
                this.collapse = msg;
                bus.emit("collapse-content", msg);
            });
            menu.getMenus().then((res) => {
                this.menus = res;
            });
        },
    };
</script>

<style scoped>
    .sidebar {
        display: block;
        position: absolute;
        left: 0;
        top: 70px;
        bottom: 0;
        overflow-y: scroll;
    }
    .sidebar::-webkit-scrollbar {
        width: 0;
    }
    .sidebar-el-menu:not(.el-menu--collapse) {
        width: 250px;
    }
    .sidebar > ul {
        height: 100%;
        overflow-y: auto;
    }
</style>
