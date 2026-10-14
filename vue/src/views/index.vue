<template>
    <div class="views-zhuye">
        <!-- 顶部导航栏 -->


        <!-- 轮播图区域 -->
        <div class="carousel-section">
            <el-carousel indicator-position="outside" height="500px" :autoplay="true" :interval="4000">
                <el-carousel-item v-for="item in bhtList" :key="item.id">
                    <div class="carousel-item" @click="$goto(item.url)">
                        <div class="carousel-overlay"></div>
                        <div class="carousel-image" :style="{'background-image': 'url('+$formatImageSrc(item.image)+')'}"></div>
                        <div class="carousel-content">

                            <p class="carousel-desc">探索更多珍贵拍品，发现无限可能</p>

                        </div>
                    </div>
                </el-carousel-item>
            </el-carousel>
        </div>

        <!-- 特色服务 -->
        <div class="features-section">
            <e-container>
                <el-row :gutter="30">
                    <el-col :xs="24" :sm="12" :md="6" v-for="feature in features" :key="feature.title">
                        <div class="feature-card">
                            <div class="feature-icon">{{ feature.icon }}</div>
                            <h3 class="feature-title">{{ feature.title }}</h3>
                            <p class="feature-desc">{{ feature.desc }}</p>
                        </div>
                    </el-col>
                </el-row>
            </e-container>
        </div>

        <!-- 最新拍品 -->
        <div class="products-section">
            <e-container>
                <div class="section-header">
                    <div class="section-title">
                        <span class="title-icon">🎨</span>
                        <h2>最新拍品</h2>
                        <span class="title-line"></span>
                    </div>
                    <router-link to="/shangpinxinxi" class="more-link">
                        查看更多 <el-icon class="el-icon--right"><ArrowRight /></el-icon>
                    </router-link>
                </div>
                <div class="products-grid">
                    <div class="product-card" v-for="r in shangpinxinxilist" :key="r.id" @click="$router.push({path:'/shangpinxinxi/detail',query:{id:r.id}})">
                        <div class="product-image">
                            <e-img :src="r.shangpintupian" pb="100"></e-img>
                            <div class="product-overlay">
                                <el-button type="warning" size="small" round>查看详情</el-button>
                            </div>
                        </div>
                        <div class="product-info">
                            <h3 class="product-name">{{ $substr(r.shangpinmingcheng, 20) }}</h3>
                            <p class="product-desc">{{ $substr(r.shangpinxiangqing, 30) }}</p>
                            <div class="product-price">
                                <span class="price-label">起拍价</span>
                                <span class="price-value">¥{{ r.qipaijia }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </e-container>
        </div>

        <!-- 热门拍品 -->
        <div class="hot-products-section">
            <e-container>
                <div class="section-header">
                    <div class="section-title">
                        <span class="title-icon">🔥</span>
                        <h2>热门拍品</h2>
                        <span class="title-line"></span>
                    </div>
                    <router-link to="/shangpinxinxi" class="more-link">
                        查看更多 <el-icon class="el-icon--right"><ArrowRight /></el-icon>
                    </router-link>
                </div>
                <div class="products-grid">
                    <div class="product-card hot" v-for="r in shangpinxinxilist1" :key="r.id" @click="$router.push({path:'/shangpinxinxi/detail',query:{id:r.id}})">
                        <div class="product-image">
                            <e-img :src="r.shangpintupian" pb="100"></e-img>
                            <div class="hot-badge">
                                <el-icon><Star /></el-icon> 热门
                            </div>
                            <div class="product-overlay">
                                <el-button type="warning" size="small" round>查看详情</el-button>
                            </div>
                        </div>
                        <div class="product-info">
                            <h3 class="product-name">{{ $substr(r.shangpinmingcheng, 20) }}</h3>
                            <p class="product-desc">{{ $substr(r.shangpinxiangqing, 30) }}</p>
                            <div class="product-price">
                                <span class="price-label">当前价</span>
                                <span class="price-value">¥{{ r.qipaijia }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </e-container>
        </div>

        <!-- 拍卖公告 -->
        <div class="notice-section">
            <e-container>
                <div class="section-header">
                    <div class="section-title">
                        <span class="title-icon">📢</span>
                        <h2>拍卖公告</h2>
                        <span class="title-line"></span>
                    </div>
                    <router-link to="/gonggao" class="more-link">
                        查看更多 <el-icon class="el-icon--right"><ArrowRight /></el-icon>
                    </router-link>
                </div>
                <div class="notice-list">
                    <div class="notice-item" v-for="r in gonggaolist" :key="r.id" @click="$router.push('/gonggao/detail?id='+r.id)">
                        <div class="notice-date">
                            <span class="day">{{ resetDate('d', r.faburiqi) }}</span>
                            <span class="month-year">{{ resetDate('Y-m', r.faburiqi) }}</span>
                        </div>
                        <div class="notice-content">
                            <h3 class="notice-title">{{ r.biaoti }}</h3>
                            <p class="notice-desc">{{ $substr(r.neirong, 80) }}</p>
                            <div class="notice-meta">
                                <span class="notice-time">
                                    <el-icon><Clock /></el-icon> {{ r.faburiqi }}
                                </span>
                            </div>
                        </div>
                        <div class="notice-arrow">
                            <el-icon><ArrowRight /></el-icon>
                        </div>
                    </div>
                </div>
            </e-container>
        </div>

        <!-- 底部 -->

    </div>
</template>

<script setup>
    import http from "@/utils/ajax/http";
    import DB from "@/utils/db";
    import { ref, reactive } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { resetDate } from "@/utils/date";
    import { Search, ArrowRight, Star, Clock, ChatDotRound, Share } from '@element-plus/icons-vue'

    const route = useRoute();
    
    // 特色服务
    const features = ref([
        { icon: '🏆', title: '正品保障', desc: '所有拍品经过专业鉴定，确保真伪' },
        { icon: '🔒', title: '安全交易', desc: '资金第三方托管，交易更放心' },
        { icon: '⚡', title: '快速竞拍', desc: '实时竞价系统，流畅不卡顿' },
        { icon: '💎', title: '专业服务', desc: '专业客服团队，全程贴心服务' }
    ]);

    // 获取轮播图信息
    const bhtList = DB.name("lunbotu").order("id desc").limit(5).selectRef();

    /**
     * 定义响应式变量 shangpinxinxilist，并获取数据商品信息模块的数据
     * @type {UnwrapNestedRefs<EShangpinxinxi[]>}
     */
    const shangpinxinxilist = DB.name("shangpinxinxi").where("issh", "是").limit("4").order("id desc").selectRef();

    /**
     * 定义响应式变量 shangpinxinxilist1，并获取数据商品信息模块的数据
     * @type {UnwrapNestedRefs<EShangpinxinxi[]>}
     */
    const shangpinxinxilist1 = DB.name("shangpinxinxi").where("issh", "是").limit("4").order("jingpairenshu desc").selectRef();

    /**
     * 定义响应式变量 gonggaolist，并获取数据公告模块的数据
     * @type {UnwrapNestedRefs<EGonggao[]>}
     */
    const gonggaolist = DB.name("gonggao").where("issh", "是").limit("4").order("id desc").selectRef();
</script>

<style scoped lang="scss">
    // 主色调变量
    $primary-color: #d4a843; // 拍卖金
    $primary-hover: #c49a3a;
    $secondary-color: #1a1a2e; // 深蓝黑
    $accent-color: #16213e; // 深蓝
    $text-primary: #333;
    $text-secondary: #666;
    $text-light: #999;
    $bg-light: #f8f9fa;
    $bg-gradient-start: #0f0c29;
    $bg-gradient-mid: #302b63;
    $bg-gradient-end: #24243e;

    .views-zhuye {
        min-height: 100vh;
        background: #fff;

        // 顶部导航栏
        .home-header {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            z-index: 1000;
            background: rgba(26, 26, 46, 0.95);
            backdrop-filter: blur(10px);
            box-shadow: 0 2px 20px rgba(0, 0, 0, 0.3);

            .header-content {
                max-width: 1400px;
                margin: 0 auto;
                padding: 15px 30px;
                display: flex;
                align-items: center;
                justify-content: space-between;

                .logo-section {
                    display: flex;
                    align-items: center;
                    gap: 15px;

                    .logo-icon {
                        width: 50px;
                        height: 50px;
                        background: linear-gradient(135deg, $primary-color 0%, #f0c75e 100%);
                        border-radius: 12px;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        box-shadow: 0 4px 15px rgba($primary-color, 0.3);

                        svg {
                            width: 30px;
                            height: 30px;
                            color: #fff;
                        }
                    }

                    .logo-text {
                        h1 {
                            font-size: 20px;
                            font-weight: 700;
                            color: #fff;
                            margin: 0;
                            letter-spacing: 1px;
                        }

                        p {
                            font-size: 11px;
                            color: rgba(255, 255, 255, 0.6);
                            margin: 3px 0 0 0;
                            letter-spacing: 2px;
                        }
                    }
                }

                .header-nav {
                    display: flex;
                    gap: 40px;

                    .nav-item {
                        color: rgba(255, 255, 255, 0.8);
                        text-decoration: none;
                        font-size: 15px;
                        font-weight: 500;
                        padding: 8px 16px;
                        border-radius: 8px;
                        transition: all 0.3s ease;

                        &:hover,
                        &.active {
                            color: #fff;
                            background: rgba($primary-color, 0.2);
                        }
                    }
                }

                .header-actions {
                    .search-btn {
                        width: 42px;
                        height: 42px;
                        background: rgba(255, 255, 255, 0.1);
                        border: 1px solid rgba(255, 255, 255, 0.2);
                        color: #fff;

                        &:hover {
                            background: $primary-color;
                            border-color: $primary-color;
                        }
                    }
                }
            }
        }

        // 轮播图区域
        .carousel-section {
            margin-top: 80px;

            :deep(.el-carousel) {
                .el-carousel__container {
                    height: 500px;
                }

                .el-carousel__indicators {
                    bottom: 20px;

                    .el-carousel__button {
                        width: 12px;
                        height: 12px;
                        border-radius: 50%;
                        background: rgba(255, 255, 255, 0.5);
                    }

                    .is-active .el-carousel__button {
                        background: $primary-color;
                        width: 30px;
                        border-radius: 6px;
                    }
                }
            }

            .carousel-item {
                position: relative;
                height: 500px;
                overflow: hidden;
                cursor: pointer;

                .carousel-overlay {
                    position: absolute;
                    top: 0;
                    left: 0;
                    right: 0;
                    bottom: 0;
                    background: linear-gradient(135deg, rgba(15, 12, 41, 0.7) 0%, rgba(48, 43, 99, 0.5) 100%);
                    z-index: 1;
                }

                .carousel-image {
                    position: absolute;
                    top: 0;
                    left: 0;
                    right: 0;
                    bottom: 0;
                    background-size: cover;
                    background-position: center;
                    transition: transform 0.5s ease;
                }

                &:hover .carousel-image {
                    transform: scale(1.05);
                }

                .carousel-content {
                    position: absolute;
                    top: 50%;
                    left: 50%;
                    transform: translate(-50%, -50%);
                    z-index: 2;
                    text-align: center;
                    color: #fff;

                    .carousel-title {
                        font-size: 42px;
                        font-weight: 700;
                        margin-bottom: 15px;
                        text-shadow: 0 4px 20px rgba(0, 0, 0, 0.5);
                    }

                    .carousel-desc {
                        font-size: 18px;
                        margin-bottom: 30px;
                        opacity: 0.9;
                    }

                    .carousel-btn {
                        background: linear-gradient(135deg, $primary-color 0%, #f0c75e 100%);
                        border: none;
                        padding: 15px 40px;
                        font-size: 16px;
                        font-weight: 600;

                        &:hover {
                            transform: translateY(-3px);
                            box-shadow: 0 10px 30px rgba($primary-color, 0.4);
                        }
                    }
                }
            }
        }

        // 特色服务
        .features-section {
            padding: 60px 0;
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);

            .feature-card {
                text-align: center;
                padding: 40px 20px;
                background: #fff;
                border-radius: 16px;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
                transition: all 0.3s ease;

                &:hover {
                    transform: translateY(-10px);
                    box-shadow: 0 20px 40px rgba($primary-color, 0.2);

                    .feature-icon {
                        transform: scale(1.2) rotate(10deg);
                    }
                }

                .feature-icon {
                    font-size: 50px;
                    margin-bottom: 20px;
                    transition: all 0.3s ease;
                }

                .feature-title {
                    font-size: 18px;
                    font-weight: 600;
                    color: $text-primary;
                    margin-bottom: 10px;
                }

                .feature-desc {
                    font-size: 14px;
                    color: $text-secondary;
                    line-height: 1.6;
                }
            }
        }

        // 拍品区域通用样式
        .products-section,
        .hot-products-section {
            padding: 80px 0;

            .section-header {
                display: flex;
                align-items: center;
                justify-content: space-between;
                margin-bottom: 50px;

                .section-title {
                    display: flex;
                    align-items: center;
                    gap: 15px;

                    .title-icon {
                        font-size: 32px;
                    }

                    h2 {
                        font-size: 28px;
                        font-weight: 700;
                        color: $text-primary;
                        margin: 0;
                    }

                    .title-line {
                        width: 60px;
                        height: 4px;
                        background: linear-gradient(90deg, $primary-color 0%, transparent 100%);
                        border-radius: 2px;
                    }
                }

                .more-link {
                    color: $primary-color;
                    text-decoration: none;
                    font-size: 15px;
                    font-weight: 500;
                    display: flex;
                    align-items: center;
                    gap: 5px;
                    padding: 10px 20px;
                    border: 2px solid $primary-color;
                    border-radius: 25px;
                    transition: all 0.3s ease;

                    &:hover {
                        background: $primary-color;
                        color: #fff;
                    }
                }
            }

            .products-grid {
                display: grid;
                grid-template-columns: repeat(4, 1fr);
                gap: 25px;

                @media (max-width: 1200px) {
                    grid-template-columns: repeat(3, 1fr);
                }

                @media (max-width: 900px) {
                    grid-template-columns: repeat(2, 1fr);
                }

                @media (max-width: 600px) {
                    grid-template-columns: 1fr;
                }

                .product-card {
                    background: #fff;
                    border-radius: 16px;
                    overflow: hidden;
                    box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
                    transition: all 0.3s ease;
                    cursor: pointer;

                    &:hover {
                        transform: translateY(-8px);
                        box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15);

                        .product-image {
                            .product-overlay {
                                opacity: 1;
                            }
                        }
                    }

                    &.hot {
                        .product-image::before {
                            content: '';
                            position: absolute;
                            top: 0;
                            left: 0;
                            right: 0;
                            bottom: 0;
                            background: linear-gradient(135deg, rgba($primary-color, 0.1) 0%, transparent 100%);
                            z-index: 1;
                        }
                    }

                    .product-image {
                        position: relative;
                        overflow: hidden;
                        aspect-ratio: 1;

                        :deep(img) {
                            width: 100%;
                            height: 100%;
                            object-fit: cover;
                            transition: transform 0.5s ease;
                        }

                        &:hover :deep(img) {
                            transform: scale(1.1);
                        }

                        .hot-badge {
                            position: absolute;
                            top: 15px;
                            right: 15px;
                            z-index: 10;
                            background: linear-gradient(135deg, $primary-color 0%, #f0c75e 100%);
                            color: #fff;
                            padding: 6px 15px;
                            border-radius: 20px;
                            font-size: 13px;
                            font-weight: 600;
                            display: flex;
                            align-items: center;
                            gap: 5px;
                            box-shadow: 0 4px 15px rgba($primary-color, 0.4);
                        }

                        .product-overlay {
                            position: absolute;
                            top: 0;
                            left: 0;
                            right: 0;
                            bottom: 0;
                            background: rgba(0, 0, 0, 0.5);
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            opacity: 0;
                            transition: opacity 0.3s ease;
                        }
                    }

                    .product-info {
                        padding: 20px;

                        .product-name {
                            font-size: 16px;
                            font-weight: 600;
                            color: $text-primary;
                            margin: 0 0 10px 0;
                            overflow: hidden;
                            text-overflow: ellipsis;
                            display: -webkit-box;
                            -webkit-line-clamp: 1;
                            -webkit-box-orient: vertical;
                        }

                        .product-desc {
                            font-size: 13px;
                            color: $text-secondary;
                            margin: 0 0 15px 0;
                            overflow: hidden;
                            text-overflow: ellipsis;
                            display: -webkit-box;
                            -webkit-line-clamp: 2;
                            -webkit-box-orient: vertical;
                            line-height: 1.6;
                        }

                        .product-price {
                            display: flex;
                            align-items: center;
                            justify-content: space-between;
                            padding-top: 15px;
                            border-top: 1px solid #eee;

                            .price-label {
                                font-size: 13px;
                                color: $text-light;
                            }

                            .price-value {
                                font-size: 20px;
                                font-weight: 700;
                                color: $primary-color;
                            }
                        }
                    }
                }
            }
        }

        // 热门拍品特殊背景
        .hot-products-section {
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
        }

        // 公告区域
        .notice-section {
            padding: 80px 0;

            .notice-list {
                display: flex;
                flex-direction: column;
                gap: 20px;

                .notice-item {
                    display: flex;
                    align-items: center;
                    gap: 25px;
                    padding: 25px;
                    background: #fff;
                    border-radius: 12px;
                    box-shadow: 0 5px 20px rgba(0, 0, 0, 0.06);
                    transition: all 0.3s ease;
                    cursor: pointer;

                    &:hover {
                        transform: translateX(10px);
                        box-shadow: 0 10px 30px rgba($primary-color, 0.15);

                        .notice-arrow {
                            color: $primary-color;
                            transform: translateX(5px);
                        }
                    }

                    .notice-date {
                        text-align: center;
                        padding: 15px 20px;
                        background: linear-gradient(135deg, $primary-color 0%, #f0c75e 100%);
                        border-radius: 12px;
                        color: #fff;
                        min-width: 80px;

                        .day {
                            display: block;
                            font-size: 28px;
                            font-weight: 700;
                            line-height: 1;
                        }

                        .month-year {
                            display: block;
                            font-size: 12px;
                            opacity: 0.9;
                            margin-top: 5px;
                        }
                    }

                    .notice-content {
                        flex: 1;

                        .notice-title {
                            font-size: 18px;
                            font-weight: 600;
                            color: $text-primary;
                            margin: 0 0 10px 0;
                        }

                        .notice-desc {
                            font-size: 14px;
                            color: $text-secondary;
                            margin: 0 0 10px 0;
                            line-height: 1.6;
                        }

                        .notice-meta {
                            .notice-time {
                                font-size: 13px;
                                color: $text-light;
                                display: flex;
                                align-items: center;
                                gap: 5px;
                            }
                        }
                    }

                    .notice-arrow {
                        color: $text-light;
                        font-size: 20px;
                        transition: all 0.3s ease;
                    }
                }
            }
        }

        // 底部
        .home-footer {
            background: linear-gradient(135deg, $bg-gradient-start 0%, $bg-gradient-mid 100%);
            padding: 60px 0 30px;
            color: #fff;

            .footer-content {
                max-width: 1400px;
                margin: 0 auto;
                padding: 0 30px;
                display: grid;
                grid-template-columns: repeat(3, 1fr);
                gap: 50px;
                margin-bottom: 40px;

                .footer-section {
                    h4 {
                        font-size: 18px;
                        font-weight: 600;
                        margin: 0 0 20px 0;
                        color: $primary-color;
                    }

                    p {
                        font-size: 14px;
                        line-height: 2;
                        color: rgba(255, 255, 255, 0.7);
                        margin: 0;
                    }

                    .social-links {
                        display: flex;
                        gap: 15px;
                        font-size: 24px;
                        color: rgba(255, 255, 255, 0.7);

                        :deep(.el-icon) {
                            cursor: pointer;
                            transition: all 0.3s ease;

                            &:hover {
                                color: $primary-color;
                                transform: scale(1.2);
                            }
                        }
                    }
                }
            }

            .footer-bottom {
                text-align: center;
                padding-top: 30px;
                border-top: 1px solid rgba(255, 255, 255, 0.1);

                p {
                    font-size: 13px;
                    color: rgba(255, 255, 255, 0.5);
                    margin: 0;
                }
            }
        }
    }
</style>
