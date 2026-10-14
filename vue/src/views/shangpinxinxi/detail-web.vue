<template>
    <div class="views-shangpinxinxi-web-detail">
        <e-container>
            <!-- 头部标题 -->
            <div class="page-header">
                <h1 class="page-title">
                    <i class="fa fa-file-text"></i>
                    <span>商品详情</span>
                </h1>
            </div>

            <!-- 商品主要内容 -->
            <div class="product-main">
                <div class="product-card">
                    <!-- 左侧图片区域 -->
                    <div class="product-gallery">
                        <div class="gallery-wrapper">
                            <e-shangpinban :images="map.shangpintupian"></e-shangpinban>
                        </div>
                    </div>

                    <!-- 右侧信息区域 -->
                    <div class="product-info">
                        <h2 class="product-title" v-text="map.shangpinmingcheng"></h2>
                        
                        <!-- 价格突出显示 -->
                        <div class="price-section">
                            <div class="price-item current-price">
                                <span class="price-label">当前竞拍价</span>
                                <span class="price-value">¥{{ map.dangqianjingpaijia }}</span>
                            </div>
                            <div class="price-item">
                                <span class="price-label">起拍价</span>
                                <span class="price-value">¥{{ map.qipaijia }}</span>
                            </div>
                        </div>

                        <!-- 商品信息网格 -->
                        <div class="info-grid">
                            <div class="info-item">
                                <span class="info-label"><i class="fa fa-th-large"></i> 分类</span>
                                <span class="info-value">
                                    <e-select-view module="shangpinfenlei" :value="map.fenlei" select="id" show="fenleimingcheng"></e-select-view>
                                </span>
                            </div>
                            <div class="info-item">
                                <span class="info-label"><i class="fa fa-arrow-up"></i> 加价幅度</span>
                                <span class="info-value">¥{{ map.jiajiafudu }}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label"><i class="fa fa-clock-o"></i> 起拍时间</span>
                                <span class="info-value">{{ map.qipaishijian }}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label"><i class="fa fa-flag-checkered"></i> 结束时间</span>
                                <span class="info-value">{{ map.jieshushijian }}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label"><i class="fa fa-info-circle"></i> 商品状态</span>
                                <span class="info-value">
                                    <el-tag :type="getStatusType(map.shangpinzhuangtai)">{{ map.shangpinzhuangtai }}</el-tag>
                                </span>
                            </div>
                            <div class="info-item">
                                <span class="info-label"><i class="fa fa-user"></i> 发布人</span>
                                <span class="info-value">{{ map.faburen }}</span>
                            </div>
                            <div class="info-item" v-if="map.jingpairen">
                                <span class="info-label"><i class="fa fa-trophy"></i> 当前竞拍人</span>
                                <span class="info-value">{{ map.jingpairen }}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label"><i class="fa fa-fire"></i> 竞拍次数</span>
                                <span class="info-value">{{ map.jingpairenshu }} 次</span>
                            </div>
                        </div>

                        <!-- 操作按钮区域 -->
                        <template v-if="$session.cx == '用户'">
                            <div class="action-buttons">
                                <e-chat-check 
                                    v-if="map.faburen != $session.faburen" 
                                    type="success" 
                                    :rid="map.faburen"
                                    class="btn-consult"
                                >
                                    <i class="fa fa-comments"></i> 咨询
                                </e-chat-check>

                                <template v-if="map.qipaishijian">
                                    <template v-if="isTimeType(map.qipaishijian, map.jieshushijian) == 2">
                                        <el-button class="btn-wait" disabled>
                                            <i class="fa fa-hourglass-o"></i> 竞拍尚未开始，请耐心等待
                                        </el-button>
                                    </template>
                                    <template v-else-if="isTimeType(map.qipaishijian, map.jieshushijian) == 3">
                                        <el-tag v-if="map.jingpairen == $session.username" type="success" size="large">
                                            <i class="fa fa-check-circle"></i> 竞拍成功
                                        </el-tag>
                                        <el-button 
                                            v-if="map.jingpairen == $session.username" 
                                            type="primary" 
                                            size="large"
                                            @click="$router.push('/jingpaidingdan/add?id='+map.id)"
                                            class="btn-order"
                                        >
                                            <i class="fa fa-shopping-cart"></i> 下单
                                        </el-button>
                                        <span class="status-text ended">已竞拍结束</span>
                                    </template>
                                    <template v-else>
                                        <el-button 
                                            type="primary" 
                                            size="large"
                                            @click="$router.push('/jingpaijilu/add?id='+map.id)"
                                            class="btn-bid"
                                        >
                                            <i class="fa fa-gavel"></i> 立即竞拍
                                        </el-button>
                                    </template>
                                </template>

                                <e-collect
                                    module="shoucang"
                                    :form="{biaoid:'xwid',biao:'biao',biaoti:'biaoti'}"
                                    :biaoid="$route.query.id"
                                    biao="shangpinxinxi"
                                    :biaoti="map.shangpinmingcheng"
                                    class="btn-collect"
                                >
                                    <template #icon="{isCollect}">
                                        <i class="fa" :class="isCollect ? 'fa-star' : 'fa-star-o'"></i>
                                    </template>
                                    <template #default="{isCollect}">
                                        {{ isCollect ? '取消收藏' : '收藏商品' }}
                                    </template>
                                </e-collect>
                            </div>
                        </template>
                    </div>
                </div>

                <!-- 商品详情描述 -->
                <div class="product-detail-section">
                    <div class="section-title">
                        <i class="fa fa-align-left"></i>
                        <span>商品详情</span>
                    </div>
                    <div class="product-detail-content" v-html="map.shangpinxiangqing"></div>
                </div>
            </div>
        </e-container>
    </div>
</template>

<script setup>
    import http from "@/utils/ajax/http";
    import DB from "@/utils/db";
    import EShangpinban from "@/components/shangpin/shangpinban.vue";

    import { ref, reactive, watch, computed } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { extend } from "@/utils/extend";
    import { useShangpinxinxiFindById, canShangpinxinxiFindById } from "@/module";

    const route = useRoute();
    const props = defineProps({
        id: {
            type: [Number, String],
        },
        isShowBtn: {
            type: Boolean,
            default: true,
        },
    });

    /**
     * 获取详情页面的一行数据,当url参数id变更时，当url参数id变更时，自动更新map中的数据
     * @type {EShangpinxinxi}
     */
    const map = useShangpinxinxiFindById(props.id);
    watch(
        () => props.id,
        (id) => {
            canShangpinxinxiFindById(id).then((res) => {
                extend(map, res);
            });
        }
    );
    // end 获取详情页面的一行数据

    const isTimeType = (start, end) => {
        var timestamp = Date.parse(new Date());
        var startTime = Date.parse(start.replace(/-/gi, "/")),
            endTime = Date.parse(end.replace(/-/gi, "/"));
        if (startTime > timestamp) {
            return 2;
        }
        if (timestamp > startTime && timestamp < endTime) {
            return 1;
        }
        return 3;
    };

    // 获取状态标签类型
    const getStatusType = (status) => {
        if (!status) return 'info';
        const statusMap = {
            '进行中': 'primary',
            '已结束': 'info',
            '未开始': 'warning',
            '已取消': 'danger',
            '成功': 'success'
        };
        return statusMap[status] || 'info';
    };
</script>

<style scoped lang="scss">
    .views-shangpinxinxi-web-detail {
        min-height: 100vh;
        background: linear-gradient(135deg, #f5f7fa 0%, #e4e9f2 100%);
        padding: 20px;

        .page-header {
            margin-bottom: 24px;
            padding: 20px;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);

            .page-title {
                margin: 0;
                font-size: 24px;
                font-weight: 600;
                color: #303133;
                display: flex;
                align-items: center;
                gap: 12px;

                i {
                    color: #409eff;
                    font-size: 26px;
                }
            }
        }

        .product-main {
            .product-card {
                background: #fff;
                border-radius: 16px;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
                overflow: hidden;
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 0;

                @media (max-width: 992px) {
                    grid-template-columns: 1fr;
                }

                // 左侧图片区域
                .product-gallery {
                    background: linear-gradient(145deg, #f8f9fa 0%, #e9ecef 100%);
                    padding: 30px;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    min-height: 500px;

                    .gallery-wrapper {
                        width: 100%;
                        max-width: 450px;

                        :deep(.el-carousel) {
                            border-radius: 12px;
                            overflow: hidden;
                            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
                        }

                        :deep(.el-carousel__item) {
                            background: #fff;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                        }

                        :deep(.el-carousel__item img) {
                            max-width: 100%;
                            max-height: 400px;
                            object-fit: contain;
                        }
                    }
                }

                // 右侧信息区域
                .product-info {
                    padding: 35px;
                    display: flex;
                    flex-direction: column;
                    gap: 24px;

                    .product-title {
                        margin: 0;
                        font-size: 26px;
                        font-weight: 600;
                        color: #303133;
                        line-height: 1.4;
                        border-bottom: 3px solid #409eff;
                        padding-bottom: 16px;
                    }

                    // 价格区域
                    .price-section {
                        display: flex;
                        gap: 20px;
                        padding: 20px;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        border-radius: 12px;
                        color: #fff;

                        .price-item {
                            flex: 1;
                            display: flex;
                            flex-direction: column;
                            gap: 8px;

                            .price-label {
                                font-size: 14px;
                                opacity: 0.9;
                            }

                            .price-value {
                                font-size: 28px;
                                font-weight: 700;
                            }

                            &.current-price {
                                .price-value {
                                    color: #ffd700;
                                    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
                                }
                            }
                        }
                    }

                    // 信息网格
                    .info-grid {
                        display: grid;
                        grid-template-columns: repeat(2, 1fr);
                        gap: 16px;

                        .info-item {
                            display: flex;
                            flex-direction: column;
                            gap: 6px;
                            padding: 14px 16px;
                            background: #f8f9fa;
                            border-radius: 10px;
                            transition: all 0.3s ease;

                            &:hover {
                                background: #e9ecef;
                                transform: translateY(-2px);
                            }

                            .info-label {
                                font-size: 13px;
                                color: #909399;
                                display: flex;
                                align-items: center;
                                gap: 8px;

                                i {
                                    color: #409eff;
                                    width: 16px;
                                    text-align: center;
                                }
                            }

                            .info-value {
                                font-size: 15px;
                                color: #303133;
                                font-weight: 500;
                            }
                        }
                    }

                    // 操作按钮
                    .action-buttons {
                        display: flex;
                        flex-wrap: wrap;
                        gap: 12px;
                        padding-top: 16px;
                        border-top: 1px solid #ebeef5;

                        .btn-bid,
                        .btn-order {
                            height: 48px;
                            padding: 0 32px;
                            font-size: 16px;
                            font-weight: 500;
                            border-radius: 8px;
                            transition: all 0.3s ease;

                            &:hover {
                                transform: translateY(-2px);
                                box-shadow: 0 6px 16px rgba(64, 158, 255, 0.4);
                            }
                        }

                        .btn-consult {
                            height: 44px;
                            padding: 0 24px;
                            border-radius: 8px;
                        }

                        .btn-collect {
                            height: 44px;
                            padding: 0 24px;
                            border-radius: 8px;
                            border: 1px solid #e4e7ed;
                            background: #fff;
                            color: #606266;

                            :deep(.fa-star) {
                                color: #ffd700;
                            }

                            &:hover {
                                border-color: #ffd700;
                                color: #ffd700;
                            }
                        }

                        .btn-wait {
                            height: 44px;
                            border-radius: 8px;
                        }

                        .status-text.ended {
                            color: #909399;
                            font-size: 14px;
                            margin-left: 8px;
                        }
                    }
                }
            }

            // 商品详情区域
            .product-detail-section {
                margin-top: 24px;
                background: #fff;
                border-radius: 16px;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
                padding: 30px;

                .section-title {
                    font-size: 20px;
                    font-weight: 600;
                    color: #303133;
                    display: flex;
                    align-items: center;
                    gap: 10px;
                    padding-bottom: 16px;
                    border-bottom: 2px solid #ebeef5;
                    margin-bottom: 24px;

                    i {
                        color: #409eff;
                        font-size: 22px;
                    }
                }

                .product-detail-content {
                    font-size: 15px;
                    line-height: 1.8;
                    color: #606266;

                    :deep(img) {
                        max-width: 100%;
                        height: auto;
                        border-radius: 8px;
                        margin: 16px 0;
                    }

                    :deep(p) {
                        margin-bottom: 16px;
                    }
                }
            }
        }
    }
</style>
