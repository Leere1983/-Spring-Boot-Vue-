<template>
    <div class="main-header" :class="{ 'main-header-fixed': isFixMenuDesktop, hover: isHover }" style="">
        <div class="header-top">
            <div class="auto-1400 clearfix">
                <div class="top-left fl">{{ config.title }}</div>
                <div class="top-right fr">
                    <ul>
                        <li>欢迎访问{{ config.title }}</li>
                        <template v-if="$session.username">
                            <li class="sli">
                                <span class="aicf" v-if="$session.touxiang"><img :src="$formatImageSrc($session.touxiang)" /></span>
                                <a href="javascript:;">{{ $session.username }}</a>
                            </li>
                            <li class="sli user-icon-but">
                                <router-link to="/admin/sy">个人中心</router-link>
                                <span>/</span>
                                <a href="javascript:;" @click="logout">退出登录</a>
                            </li>
                        </template>
                        <template v-else>
                            <li >
                                <el-button type="primary" @click="$router.push('/login')" size="small"> 登录 </el-button>
                            </li>
                        </template>
                    </ul>
                    <div class="login-model" v-if="!$session.username" @click.stop.prevent :class="{ 'show-model': isShowLogin }" :style="{ height: loginModelHeight }">
                        <div class="login-box" ref="loginBoxRef">
                            <form action="javascript:;">
                                <div class="login-input input-username">
                                    <span class="input-title">用户名：</span>
                                    <input type="text" class="input" v-model="loginForm.username" name="username" placeholder="输入用户名" />
                                    <span class="line"></span>
                                </div>
                                <div class="login-input input-password">
                                    <span class="input-title">密码：</span>
                                    <input type="password" class="input" v-model="loginForm.pwd" name="pwd" placeholder="输入密码" />
                                    <span class="line"></span>
                                </div>
                                <div class="login-input input-captch">
                                    <span class="input-title">验证码：</span>
                                    <input type="text" class="input" name="pagerandom" v-model="loginForm.pagerandom" placeholder="输入验证码" />
                                    <span class="line"></span>
                                    <img :src="captchaUrl" @click="loadCaptcha" class="captch" />
                                </div>
                                <div class="login-input input-cx" v-if="rules.length > 1">
                                    <select class="input" name="cx" v-model="loginForm.cx" @change="loginForm.cx = $event.target.value">
                                        <option :value="o" v-for="o in rules" :key="o">{{ o }}</option>
                                    </select>
                                    <span class="line"></span>
                                </div>
                                <div class="login-btn">
                                    <button class="input-btn-bottom" type="button" @click="onLogin">登录</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="" ref="desktop">
            <div class="main-box ms300">
                <div class="auto-1400">
                    <div class="logo-box fl">
                        <div class="logo">
                            <a href="javascript:;" title="">
                                {{ config.title }}
                                <!--<img height="65" src="@assets/images/logo.png">-->
                            </a>
                        </div>
                    </div>
                    <div class="fl adTxt"></div>
                    <nav class="main-menu ms300 fl">
                        <div class="mobile mobile-close" @click="isHover = false">
                            <i class="fa fa-close"></i>
                        </div>
                        <ul class="navigation">
                            <li :class="{ 'active-menu': isFullPathActive('/index') }">
                                <router-link :to="'/index'" title="首页"> 首页 </router-link>
                            </li>
                            <li v-for="(m, k) in listMenushangpinfenlei" :key="m.id" :class="{ active: isFullPathActive({ path: '/shangpinxinxi', query: { fenlei: m.id } }) }">
                                <router-link :to="{ path: '/shangpinxinxi', query: { fenlei: m.id } }" :title="m.fenleimingcheng">
                                    <span class="ms200"
                                    >{{ m.fenleimingcheng }}<br /><em>{{ m.fenleimingcheng }}</em></span
                                    >
                                </router-link>
                            </li>
                            <li :class="{ 'active-menu': isFullPathActive({ path: '/gonggao' }) }">
                                <router-link :to="{ path: '/gonggao' }" title="公告平台"> 公告平台 </router-link>
                            </li>
                            <template v-if="$session.cx == '用户'|| $session.cx == '商家'">
                                <li :class="{ 'active-menu': isFullPathActive({ path: '/liuyanban/add' }) }">
                                    <router-link :to="{ path: '/liuyanban/add' }" title="留言反馈"> 留言反馈 </router-link>
                                </li>
                            </template>

                        </ul>
                    </nav>
                    <div class="nav-toggler">
                        <button class="hidden-bar-opener">
                            <span class="aicf"><img src="@/assets/images/nav-cd.png" /></span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="mobile mobile-mask" @click="isHover = false"></div>
    </div>
</template>
<script setup>
import { useRouter, useRoute } from "vue-router";
import { computed, ref, reactive, watch, onMounted, onBeforeUnmount } from "vue";
import { logout, session, useEvent } from "@/utils";
import config from "@/config";
import DB from "@/utils/db";
import { Search } from "@element-plus/icons-vue";
import { isObject } from "@/utils/extend";
import { isFullPathActive, isPathActive } from "@/router/router-utils";
import { useUserStore } from "@/stores";
import domEvent from "@/utils/dom-event";

import { captch } from "@/utils/utils";
import { canLogin } from "@/stores";
import { ElMessage } from "element-plus";

const isShowLogin = ref(false);

const loginBoxRef = ref(null);
const captchaUrl = ref("");
const rules = ["用户", "商家"];
const loginForm = reactive({
    username: "",
    pwd: "",
    cx: rules[0],
    /* 验证码段 */
    pagerandom: "",
    a: "a",
    /* 验证码段 */
});
const loadCaptcha = () => {
    captch().then((res) => {
        loginForm.captchToken = res.token;
        captchaUrl.value = res.url;
    });
};

const onLogin = async () => {
    const res = await canLogin(loginForm);
    if (res.code === 0) {
        ElMessage.success("登录成功");
    } else {
        if (res.code === 20) {
            loadCaptcha();
        }
        ElMessage.error(res.msg);
    }
};
const loginModelHeight = computed(() => {
    if (isShowLogin.value) {
        return loginBoxRef.value.getBoundingClientRect().height + "px";
    } else {
        return 0;
    }
});
const showLogin = (e) => {
    e.stopPropagation();
    e.preventDefault();
    if (!isShowLogin.value) {
        loginForm.username = "";
        loginForm.pwd = "";
        loginForm.pagerandom = "";
        loginForm.cx = rules[0];
        loadCaptcha();
        domEvent.once(document, "click", () => {
            isShowLogin.value = false;
        });
    }
    isShowLogin.value = !isShowLogin.value;
};

const router = useRouter();
const userState = useUserStore();

const isHover = ref(false);

const loadListMenu = async (module, target) => {
    target.value = await DB.name(module).order("id desc").select();
};

const route = useRoute();
const listMenushangpinfenlei = ref([]);
loadListMenu("shangpinfenlei", listMenushangpinfenlei);

const isFixMenuDesktop = ref(false);
const desktop = ref(null);

const onScroll = () => {
    if (desktop.value) {
        let scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
        var rect = desktop.value.getBoundingClientRect();
        var top = rect.top + scrollTop;
        if (scrollTop > top) {
            isFixMenuDesktop.value = true;
        } else {
            isFixMenuDesktop.value = false;
        }
    }
};

onMounted(() => {
    onScroll();
    domEvent.on(window, "scroll", onScroll);
});
onBeforeUnmount(() => {
    domEvent.off(window, "scroll", onScroll);
});

const status = ref(false);
watch(
    () => route,
    () => {
        status.value = false;
    },
    { deep: true }
);
</script>

<style lang="scss" type="text/scss" scoped>
.main-header {
    width: 100%;
    height: 128px;
    background: #ffffff;
    --main-header-bgckground: var(--theme-primary-color, #5abdcb);

    .mobile {
        display: none;
    }
    .auto-1400 {
        width: 1400px;
        padding: 0 10px;
        display: table;
        margin: 0 auto;
    }

    .login-model {
        position: absolute;
        right: 0;
        top: 40px;
        width: 300px;
        z-index: 100;
        background: var(--main-header-bgckground);
        height: 0;
        overflow: hidden;
        transition: all 0.3s ease-in-out;
        input,
        select,
        input:focus,
        input:active,
        select:focus,
        select:active {
            outline: none;
        }
        .login-box {
            padding: 15px;
            .login-input {
                margin-bottom: 10px;
                position: relative;
                .captch {
                    position: absolute;
                    right: 0;
                    top: 0;
                    width: 100px;
                    height: 30px;
                }
            }
            .input {
                font-size: 14px;
                width: 100%;
                border: none;
                border-radius: 0;
                padding: 8px 10px;
                color: #ffffff;
                background: transparent;
                border-bottom: 1px solid #a6a6a6;
                position: relative;
            }
            select.input option {
                color: var(--main-header-bgckground);
            }
            .input + .line {
                position: absolute;
                left: 0;
                bottom: 0;
                height: 1px;
                width: 0;
                transition: width 0.3s ease-in-out;
                background: #ffffff;
            }
            .input-title {
                position: absolute;
                display: none;
                left: 0;
                top: 0;
                padding: 8px 0;
                width: 60px;
                text-align: right;
            }
            .input:focus + .line {
                width: 100%;
            }
            .input::placeholder {
                color: #ffffff;
            }
            .input-btn-bottom {
                width: 100%;
                border-radius: 24px;
                padding: 10px 0;
                background: #ffffff;
                font-size: 18px;
                border: none;
                color: var(--main-header-bgckground);
            }
        }
    }
    .login-model.show-model {
        border-radius: 0 0 5px 5px;
    }

    .fl {
        float: left;
    }

    .fr {
        float: right;
    }

    .header-top {
        background: var(--main-header-bgckground);
        color: #ffffff;
        font-size: 13px;
        height: 40px;
        /*display: flex;
        align-items: center;
        justify-content: space-between;*/

        .top-left {
            line-height: 40px;
            font-size: 14px;
        }

        .top-right {
            position: relative;
            ul {
                display: flex;
                align-items: center;
                height: 40px;

                li {
                    position: relative;
                    line-height: 24px;
                    margin-right: 10px;
                    display: flex;
                    align-items: center;

                    .aicf {
                        border-radius: 50%;
                        overflow: hidden;
                        font-size: 14px;
                        margin-right: 6px;
                        width: 16px;
                        height: 16px;
                    }

                    a {
                        position: relative;
                        color: #ffffff;
                    }
                }

                li.sli {
                    padding: 0 10px;
                    border-radius: 24px;
                    background: #fff;
                    color: var(--main-header-bgckground);
                    transition: all 300ms;

                    a {
                        color: var(--main-header-bgckground);
                    }
                }

                li.cart {
                    margin-right: 15px;
                    display: flex;
                    align-items: center;
                    cursor: pointer;
                    .ico {
                        color: #f5b69a;
                        margin-right: 5px;
                    }
                    .cart-num {
                        margin-left: 5px;
                    }
                }
                li.cart:hover {
                    color: #f5b69a;
                }

                .user-icon-but {
                    span {
                        margin-left: 8px;
                        margin-right: 8px;
                        color: #dddddd;
                    }
                }

                .search-form {
                    display: flex;
                    align-items: center;

                    .search-input {
                        color: #0b0b0b;
                    }

                    .search-input,
                    .search-btn {
                        border: none;
                        outline: none;
                        background: transparent;
                        height: 24px;
                        line-height: 24px;
                    }
                    .search-btn {
                        color: var(--main-header-bgckground);
                    }

                    .search-input:focus {
                        outline: none;
                    }
                }

                li:last-child {
                    margin-right: 0;
                }
            }
        }
    }

    .ms300 {
        transition: all 300ms ease;
    }

    .ms200 {
        transition: all 200ms ease;
    }

    .adTxt {
        margin: 0px 30px;
        min-width: 150px;
        max-width: 300px;
        overflow: hidden;
    }

    .main-box {
        width: 100%;
        height: 88px;
        background: #ffffff;
        box-shadow: 0 0 4px rgba(0, 0, 0, 0.1);

        .logo-box {
            float: left;
            padding-top: 16px;
            padding-bottom: 5px;

            .logo {
                line-height: 60px;
                font-size: 28px;
                a {
                    color: var(--main-header-bgckground);
                }
            }

            .logo img {
                display: inline-block;
                max-width: 100%;
            }
        }

        .main-menu {
            float: right;

            .navigation {
                > li {
                    float: left;
                    display: inline;
                    padding: 34px 25px 22px 33px;
                    margin-left: -4px;
                    background: url(@/assets/images/top-nav-bg.png) left 40px no-repeat;

                    > a {
                        position: relative;
                        display: block;
                        overflow: hidden;
                        font-size: 16px;
                        height: 24px;
                        color: #111111;
                        line-height: 24px;
                        text-transform: uppercase;

                        > span {
                            display: block;
                            margin-top: 0;

                            em {
                                color: var(--main-header-bgckground, #5abdcb);
                                font-style: normal;
                            }
                        }
                    }
                }

                > li:first-child {
                    background: none;
                }

                > li:last-child {
                    padding-right: 10px;
                }

                > li:hover > a > span {
                    margin-top: -24px;
                }
            }

            /* 子菜单 */
            div.sub-menu {
                position: absolute;
                left: 0;
                right: 0;
                margin-top: 15px;
                overflow: hidden;
                z-index: 100000;
                opacity: 0;
                visibility: hidden;
                background: #f5f5f5;
                transition: all 600ms ease;

                .items {
                    overflow: hidden;
                    width: 1400px;
                    padding: 30px 10px;
                    margin: 0 auto;
                    display: flex;
                    flex-wrap: wrap;

                    .item {
                        width: 33.333%;
                        border-left: 1px solid #ddd;
                        border-right: 1px solid #ddd;
                        /*&.item:nth-child(3n), &.item:last-child{
                            border-right: 1px solid #dddddd;
                        }*/
                        a {
                            padding: 13px 0;
                            display: block;
                            text-align: center;
                            font-size: 18px;
                            color: #444;
                        }

                        a:hover {
                            transform: translateY(-5px);
                            color: var(--main-header-bgckground);
                        }
                    }
                }
            }

            .navigation > li:hover > div.sub-menu {
                opacity: 1;
                visibility: visible;
            }
        }

        .nav-toggler {
            float: right;
            margin-top: 16px;
            display: none;
        }
    }
}

.main-header.main-header-fixed {
    .main-box {
        position: fixed;
        left: 0;
        top: 0;
        width: 100%;
        background: #ffffff;
        z-index: 10000;
    }
}

@media screen and (max-width: 900px) {
    .main-header {
        height: auto;
        .auto-1400,
        .auto-1200 {
            width: 96%;
            padding: 0 2%;
        }

        .main-box .logo-box {
            padding-top: 10px;
        }

        .mobile {
            display: block;
        }
        .mobile.mobile-mask {
            visibility: hidden;
            position: fixed;
            left: 0;
            top: 0;
            width: 100vw;
            height: 100vh;
            opacity: 0;
            background: rgba(0, 0, 0, 0.5);
            z-index: 100;
            transition: opacity 0.3s ease;
        }

        .talkerMenu {
            margin-top: 0px;
            top: unset;
            bottom: 5%;
            -webkit-transform: scale(0.5);
            -moz-transform: scale(0.5);
            transform: scale(0.5);
        }

        .talkerMenu ul li.qqicon,
        .talkerMenu ul li.wxicon {
            display: none;
        }

        /*.main-header .header-top {padding-bottom:10px;}
        .main-header .header-top .top-left {clear:both;width:100%;text-align:center;}
        .main-header .header-top .top-right ul li {width:32%;text-align:center;margin-left:0;margin-right:0;font-size:12px;}
        .main-header .header-top .top-right ul li .aicf {display:none;}
        .main-header .header-top .top-right ul li.sli {width:45%;padding:0;margin-left:3%;}*/
        /*.main-header .header-top { margin-top: -110px;}*/

        .adTxt {
            display: none;
        }

        .nav-toggler {
            display: block !important;

            .hidden-bar-opener {
                position: relative;
                display: block;
                height: 36px;
                width: 40px;
                background: #5abdcb;
                color: #ffffff;
                text-align: center;
                font-size: 18px;
                line-height: 34px;
                border: 1px solid #5abdcb;
                border-radius: 3px;
                font-weight: normal;
            }
        }

        .header-top {
            display: none;
        }

        .main-box {
            .main-menu {
                position: fixed;
                top: 0;
                right: -305px;
                width: 305px;
                bottom: 0;
                background: #272727;
                z-index: 9999;
                display: block;
                padding-top: 30px;
                .hidden-bar-closer {
                    position: absolute;
                    z-index: 1;
                    top: 0;
                    width: 100%;
                    height: 20px;
                    background: rgba(255, 255, 255, 0.2);
                    border-radius: 2px;
                    line-height: 20px;
                    color: #fff;
                    text-align: center;
                    font-size: 12px;
                }
                .navigation {
                    display: flex;
                    flex-direction: column;
                    > li {
                        padding: 0;
                        line-height: 40px;
                        border-bottom: 1px solid #666;
                        margin-left: 0;
                        background: transparent;
                        > a {
                            position: relative;
                            display: block;
                            overflow: hidden;
                            font-size: 16px;
                            height: 48px;
                            color: #fff;
                            line-height: 48px;
                            text-transform: uppercase;
                            padding-left: 25px;
                            > .aicf {
                                position: absolute;
                                right: 0;
                                top: 0;
                                width: 48px;
                                height: 48px;
                                text-align: center;
                                line-height: 48px;
                            }
                            span {
                                display: block;
                                margin-top: 0;
                                padding-left: 2em;
                                em {
                                    color: #fc721e;
                                    font-style: normal;
                                }
                            }
                        }
                        .sub-menu {
                            display: none;
                            border-top: 1px solid #444;
                        }
                        .items {
                            clear: both;
                            width: 100%;
                            overflow: hidden;
                            > div.img {
                                display: none;
                            }
                            a {
                                color: #fff;
                                line-height: 48px;
                                display: block;
                                text-indent: 4em;
                                font-size: 14px;
                                .aicf {
                                    margin-right: 5px;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    .main-header.hover {
        .main-box {
            .main-menu {
                right: 0px;
                animation: fadeInRight 300ms;
                .mobile-close {
                    position: absolute;
                    right: 5px;
                    top: 5px;
                    width: 35px;
                    height: 35px;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    background: #ffffff;
                    border-radius: 50%;
                }
            }
        }
        .mobile.mobile-mask {
            opacity: 1;
            visibility: visible;
        }
    }
}
</style>
