<template>
    <div class="login-wrap">
        <!-- 背景装饰 -->
        <div class="background-decoration">
            <div class="circle circle-1"></div>
            <div class="circle circle-2"></div>
            <div class="circle circle-3"></div>
        </div>
        
        <div class="login-container">
            <!-- 左侧宣传区域 -->
            <div class="login-banner">
                <div class="banner-content">
                    <div class="logo-area">
                        <div class="logo-icon">
                            <svg viewBox="0 0 1024 1024" width="60" height="60">
                                <path fill="currentColor" d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z"/>
                                <path fill="currentColor" d="M686.7 638.6L544.1 535.5V288c0-4.4-3.6-8-8-8H488c-4.4 0-8 3.6-8 8v275.4c0 2.1 1.7 3.9 3.8 3.9h16.4c1 0 1.9 0.4 2.6 1l142.7 103c3.2 2.3 7.7 1.6 10-1.6l33.3-46.8c2.3-3.2 1.6-7.7-1.6-10z"/>
                            </svg>
                        </div>
                        <h1 class="platform-name">拍卖交易平台</h1>
                        <p class="platform-slogan">公平公正 · 诚信交易 · 优质服务</p>
                    </div>
                    
                    <div class="feature-list">
                        <div class="feature-item">
                            <div class="feature-icon">🏆</div>
                            <div class="feature-text">
                                <h3>正品保障</h3>
                                <p>所有拍品严格审核</p>
                            </div>
                        </div>
                        <div class="feature-item">
                            <div class="feature-icon">🔒</div>
                            <div class="feature-text">
                                <h3>安全交易</h3>
                                <p>资金托管放心购</p>
                            </div>
                        </div>
                        <div class="feature-item">
                            <div class="feature-icon">⚡</div>
                            <div class="feature-text">
                                <h3>快速竞拍</h3>
                                <p>实时竞价不等待</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- 右侧登录表单 -->
            <div class="login-form-wrapper">
                <div class="form-header">
                    <h2>欢迎登录</h2>
                    <p>请输入您的账号信息</p>
                </div>
                
                <el-form :model="param" :rules="rules" ref="login" label-width="0px" class="login-form">
                    <el-form-item prop="username">
                        <div class="input-wrapper">
                            <el-icon class="input-icon"><User /></el-icon>
                            <el-input v-model="param.username" placeholder="请输入用户名" class="custom-input">
                                <template #append>
                                    <el-dropdown trigger="click">
                                        <span class="role-selector">{{ param.cx }} <el-icon class="el-icon--right"><ArrowDown /></el-icon> </span>
                                        <template #dropdown>
                                            <el-dropdown-menu>
                                                <el-dropdown-item v-for="r in roles" @click="param.cx = r" :key="r">{{ r }} </el-dropdown-item>
                                            </el-dropdown-menu>
                                        </template>
                                    </el-dropdown>
                                </template>
                            </el-input>
                        </div>
                    </el-form-item>
                    
                    <el-form-item prop="pwd">
                        <div class="input-wrapper">
                            <el-icon class="input-icon"><Lock /></el-icon>
                            <el-input type="password" placeholder="请输入密码" v-model="param.pwd" @keyup.enter="submitForm()" class="custom-input" show-password>
                            </el-input>
                        </div>
                    </el-form-item>
                    
                    <el-form-item prop="pagerandom" class="captcha-item">
                        <div class="input-wrapper captcha-wrapper">
                            <el-icon class="input-icon"><Key /></el-icon>
                            <el-input ref="random" placeholder="请输入验证码" v-model="param.pagerandom" @keyup.enter="submitForm()" class="custom-input captcha-input">
                            </el-input>
                            <div class="captcha-image" @click="loadCaptchToInput" title="点击刷新验证码">
                                <img :src="captchUrl" alt="验证码" unselectable="on" />
                            </div>
                        </div>
                    </el-form-item>
                    

                    
                    <el-form-item>
                        <el-button type="primary" class="login-button" @click="submitForm()" :loading="loading">
                            <span v-if="!loading">立即登录</span>
                            <span v-else>登录中...</span>
                        </el-button>
                    </el-form-item>
                    
                    <div class="register-links">
                        <span>还没有账号？</span>
                        <el-button type="primary" link class="register-btn" @click="openShangjiaRegister">
                            <el-icon><Shop /></el-icon> 商家注册
                        </el-button>
                        <el-button type="primary" link class="register-btn" @click="openYonghuRegister">
                            <el-icon><User /></el-icon> 用户注册
                        </el-button>
                    </div>
                    
                    <p class="login-tips">
                        <el-icon><InfoFilled /></el-icon>
                        温馨提示：请妥善保管账号信息，勿与他人共享
                    </p>
                </el-form>
            </div>
        </div>
        
        <!-- 底部版权信息 -->
        <div class="login-footer">
            <p>© 2026 拍卖交易平台 版权所有</p>
        </div>
        
        <!-- 注册对话框 -->
        <el-dialog
            title="商家注册"
            v-model="dialogVisible"
            width="600px"
            :close-on-click-modal="false"
            class="custom-dialog"
        >
            <shangjia-add :is-houxu="false" @success="handleShangjiaSuccess"></shangjia-add>
        </el-dialog>
        
        <el-dialog
            title="用户注册"
            v-model="dialogVisible1"
            width="600px"
            :close-on-click-modal="false"
            class="custom-dialog"
        >
            <yonghu-add :is-houxu="false" @success="handleYonghuSuccess"></yonghu-add>
        </el-dialog>
    </div>
</template>

<script>
    import config from "@/config";
    import { canLogin } from "@/stores";
    import { captch } from "@/utils/utils";
    import shangjiaAdd from '@/views/shangjia/add'
    import yonghuAdd from '@/views/yonghu/add'
    import { User, Lock, Key, ArrowDown, Shop, InfoFilled } from '@element-plus/icons-vue'

    export default {
        components: { shangjiaAdd, yonghuAdd, User, Lock, Key, ArrowDown, Shop, InfoFilled },
        data: function () {
            return {
                config,
                captchUrl: "",
                loading: false,
                rememberMe: false,
                param: {
                    username: "",
                    pwd: "",
                    cx: "",
                    pagerandom: "",
                    captchToken: ""
                },
                rules: {
                    username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
                    pwd: [{ required: true, message: "请输入密码", trigger: "blur" }],
                    pagerandom: [{ required: true, message: "请输入验证码", trigger: "blur" }],
                },
                roles: ["管理员", "用户", "商家"],
                dialogVisible: false,
                dialogVisible1: false,
            };
        },
        methods: {
            openShangjiaRegister() {
                this.dialogVisible = true;
            },
            openYonghuRegister() {
                this.dialogVisible1 = true;
            },
            handleShangjiaSuccess() {
                this.$message.success("注册成功");
                this.dialogVisible = false;
            },
            handleYonghuSuccess() {
                this.$message.success("注册成功");
                this.dialogVisible1 = false;
            },
            submitForm() {
                this.$refs.login.validate((valid) => {
                    if (valid) {
                        this.loading = true;
                        canLogin(this.param)
                            .then((res) => {
                                this.loading = false;
                                if (res.code == 0) {
                                    this.$message.success("登录成功");
                                    var redirect = this.$route.query.redirect;
                                    if (redirect) {
                                        this.$router.replace(redirect);
                                    } else {
                                        this.$router.replace("/admin/sy");
                                    }
                                } else {
                                    this.$message.error(res.msg);
                                }
                                if (res.code == 20) {
                                    this.loadCaptch();
                                }
                            })
                            .catch((err) => {
                                this.loading = false;
                                this.$message.error(err.message);
                            });
                    } else {
                        this.$message.error("请输入账号和密码");
                        return false;
                    }
                });
            },
            loadCaptchToInput() {
                this.loadCaptch();
                this.param.pagerandom = "";
                this.$refs.random.focus();
            },
            loadCaptch() {
                captch().then((res) => {
                    this.param.captchToken = res.token;
                    this.captchUrl = res.url;
                });
            },
        },
        created() {
            this.loadCaptch();
            this.param.cx = this.roles[0];
        },
    };
</script>

<style lang="scss" scoped>
    // 主色调变量
    $primary-color: #d4a843; // 拍卖金
    $primary-hover: #c49a3a;
    $secondary-color: #1a1a2e; // 深蓝黑
    $accent-color: #16213e; // 深蓝
    $text-primary: #333;
    $text-secondary: #666;
    $text-light: #999;
    $bg-gradient-start: #0f0c29;
    $bg-gradient-mid: #302b63;
    $bg-gradient-end: #24243e;
    
    .login-wrap {
        position: relative;
        width: 100%;
        height: 100vh;
        min-height: 700px;
        background: linear-gradient(135deg, $bg-gradient-start 0%, $bg-gradient-mid 50%, $bg-gradient-end 100%);
        overflow: hidden;
        display: flex;
        flex-direction: column;
        
        // 背景装饰圆圈
        .background-decoration {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            overflow: hidden;
            pointer-events: none;
            
            .circle {
                position: absolute;
                border-radius: 50%;
                background: linear-gradient(135deg, rgba($primary-color, 0.1) 0%, rgba($primary-color, 0.05) 100%);
                animation: float 20s ease-in-out infinite;
                
                &.circle-1 {
                    width: 400px;
                    height: 400px;
                    top: -150px;
                    right: -100px;
                    animation-delay: 0s;
                }
                
                &.circle-2 {
                    width: 300px;
                    height: 300px;
                    bottom: -100px;
                    left: -50px;
                    animation-delay: -7s;
                }
                
                &.circle-3 {
                    width: 200px;
                    height: 200px;
                    top: 50%;
                    left: 50%;
                    animation-delay: -14s;
                }
            }
        }
        
        .login-container {
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 40px;
            gap: 0;
            
            // 左侧宣传区域
            .login-banner {
                flex: 1;
                max-width: 500px;
                padding: 40px;
                color: #fff;
                display: flex;
                align-items: center;
                
                .banner-content {
                    .logo-area {
                        margin-bottom: 50px;
                        
                        .logo-icon {
                            width: 80px;
                            height: 80px;
                            margin: 0 auto 20px;
                            background: linear-gradient(135deg, $primary-color 0%, #f0c75e 100%);
                            border-radius: 20px;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            box-shadow: 0 10px 30px rgba($primary-color, 0.3);
                            
                            svg {
                                width: 50px;
                                height: 50px;
                                color: #fff;
                            }
                        }
                        
                        .platform-name {
                            font-size: 32px;
                            font-weight: 700;
                            text-align: center;
                            margin-bottom: 10px;
                            background: linear-gradient(135deg, #fff 0%, $primary-color 100%);
                            -webkit-background-clip: text;
                            -webkit-text-fill-color: transparent;
                            background-clip: text;
                        }
                        
                        .platform-slogan {
                            font-size: 14px;
                            text-align: center;
                            color: rgba(255, 255, 255, 0.7);
                            letter-spacing: 2px;
                        }
                    }
                    
                    .feature-list {
                        .feature-item {
                            display: flex;
                            align-items: center;
                            padding: 20px;
                            margin-bottom: 15px;
                            background: rgba(255, 255, 255, 0.05);
                            border-radius: 12px;
                            border: 1px solid rgba(255, 255, 255, 0.1);
                            backdrop-filter: blur(10px);
                            transition: all 0.3s ease;
                            
                            &:hover {
                                background: rgba(255, 255, 255, 0.1);
                                transform: translateX(10px);
                            }
                            
                            .feature-icon {
                                font-size: 36px;
                                margin-right: 15px;
                            }
                            
                            .feature-text {
                                h3 {
                                    font-size: 16px;
                                    margin-bottom: 5px;
                                    color: #fff;
                                }
                                
                                p {
                                    font-size: 12px;
                                    color: rgba(255, 255, 255, 0.6);
                                }
                            }
                        }
                    }
                }
            }
            
            // 右侧登录表单
            .login-form-wrapper {
                width: 420px;
                padding: 50px 40px;
                background: rgba(255, 255, 255, 0.98);
                border-radius: 20px;
                box-shadow: 0 25px 50px rgba(0, 0, 0, 0.3);
                
                .form-header {
                    text-align: center;
                    margin-bottom: 35px;
                    
                    h2 {
                        font-size: 28px;
                        font-weight: 700;
                        color: $text-primary;
                        margin-bottom: 8px;
                    }
                    
                    p {
                        font-size: 14px;
                        color: $text-secondary;
                    }
                }
                
                .login-form {
                    .input-wrapper {
                        display: flex;
                        align-items: center;
                        position: relative;
                        
                        .input-icon {
                            position: absolute;
                            left: 15px;
                            top: 50%;
                            transform: translateY(-50%);
                            color: $text-light;
                            font-size: 18px;
                            z-index: 10;
                        }
                        
                        .custom-input {
                            flex: 1;
                            
                            :deep(.el-input__wrapper) {
                                padding-left: 45px;
                                border-radius: 10px;
                                box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
                                border: 1px solid #e8e8e8;
                                transition: all 0.3s ease;
                                
                                &:hover {
                                    border-color: $primary-color;
                                }
                                
                                &.is-focus {
                                    border-color: $primary-color;
                                    box-shadow: 0 0 0 3px rgba($primary-color, 0.1);
                                }
                            }
                            
                            .el-input-group__append {
                                background: #f5f5f5;
                                border: none;
                                border-radius: 0 10px 10px 0;
                                padding: 0 15px;
                                
                                .role-selector {
                                    color: $primary-color;
                                    font-weight: 500;
                                    font-size: 13px;
                                    
                                    &:hover {
                                        color: $primary-hover;
                                    }
                                }
                            }
                        }
                        
                        &.captcha-wrapper {
                            .captcha-input {
                                :deep(.el-input__wrapper) {
                                    border-radius: 10px 0 0 10px;
                                }
                            }
                            
                            .captcha-image {
                                width: 110px;
                                height: 42px;
                                background: #f5f5f5;
                                border-radius: 0 10px 10px 0;
                                overflow: hidden;
                                cursor: pointer;
                                border: 1px solid #e8e8e8;
                                border-left: none;
                                transition: all 0.3s ease;
                                
                                &:hover {
                                    opacity: 0.8;
                                }
                                
                                img {
                                    width: 100%;
                                    height: 100%;
                                    object-fit: cover;
                                }
                            }
                        }
                    }
                    
                    .captcha-item {
                        margin-bottom: 15px;
                    }
                    
                    .login-options {
                        display: flex;
                        justify-content: flex-start;
                        margin-bottom: 20px;
                        
                        .remember-me {
                            :deep(.el-checkbox__label) {
                                color: $text-secondary;
                                font-size: 13px;
                            }
                        }
                    }
                    
                    .login-button {
                        width: 100%;
                        height: 48px;
                        font-size: 16px;
                        font-weight: 600;
                        border-radius: 10px;
                        background: linear-gradient(135deg, $primary-color 0%, #f0c75e 100%);
                        border: none;
                        transition: all 0.3s ease;
                        
                        &:hover {
                            transform: translateY(-2px);
                            box-shadow: 0 10px 20px rgba($primary-color, 0.3);
                        }
                        
                        &:active {
                            transform: translateY(0);
                        }
                    }
                    
                    .register-links {
                        text-align: center;
                        margin-top: 25px;
                        padding-top: 25px;
                        border-top: 1px solid #eee;
                        
                        span {
                            color: $text-secondary;
                            font-size: 14px;
                            margin-right: 10px;
                        }
                        
                        .register-btn {
                            color: $primary-color;
                            font-size: 14px;
                            padding: 5px 10px;
                            
                            &:hover {
                                color: $primary-hover;
                            }
                        }
                    }
                    
                    .login-tips {
                        margin-top: 20px;
                        padding: 12px 15px;
                        background: linear-gradient(135deg, rgba($primary-color, 0.08) 0%, rgba($primary-color, 0.02) 100%);
                        border-radius: 8px;
                        font-size: 12px;
                        color: $text-secondary;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        gap: 6px;
                        
                        :deep(.el-icon) {
                            color: $primary-color;
                        }
                    }
                }
            }
        }
        
        .login-footer {
            padding: 20px;
            text-align: center;
            
            p {
                color: rgba(255, 255, 255, 0.5);
                font-size: 12px;
            }
        }
    }
    
    // 对话框样式
    .custom-dialog {
        :deep(.el-dialog__header) {
            padding: 20px 25px;
            border-bottom: 1px solid #eee;
            
            .el-dialog__title {
                font-size: 18px;
                font-weight: 600;
                color: $text-primary;
            }
        }
        
        :deep(.el-dialog__body) {
            padding: 25px;
        }
    }
    
    // 浮动动画
    @keyframes float {
        0%, 100% {
            transform: translate(0, 0) scale(1);
        }
        33% {
            transform: translate(30px, -30px) scale(1.05);
        }
        66% {
            transform: translate(-20px, 20px) scale(0.95);
        }
    }
    
    // 响应式设计
    @media (max-width: 900px) {
        .login-container {
            flex-direction: column;
            
            .login-banner {
                display: none;
            }
            
            .login-form-wrapper {
                width: 100%;
                max-width: 420px;
            }
        }
    }
</style>
