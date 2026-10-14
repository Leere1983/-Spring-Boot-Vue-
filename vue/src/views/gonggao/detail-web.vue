<template>
    <div class="views-gonggao-web-detail">
        <e-container>
            <!-- 返回按钮和标题栏 -->
            <div class="detail-header">
                <div class="back-nav">
                    <el-button class="back-btn" @click="goBack" text bg icon="ArrowLeft">
                        返回
                    </el-button>
                </div>
                <div class="breadcrumb-path">
                    <span class="path-item">首页</span>
                    <span class="path-separator">/</span>
                    <span class="path-item">公告详情</span>
                </div>
            </div>

            <!-- 公告内容卡片 -->
            <div class="detail-card">
                <div class="article-header">
                    <h1 class="article-title" v-html="map.biaoti"></h1>

                    <div class="article-meta">
                        <div class="meta-info">
                            <span class="meta-icon">📅</span>
                            <span class="meta-label">发布日期：</span>
                            <span class="meta-value">{{ map.faburiqi }}</span>
                        </div>
                        <div class="meta-actions">
                            <e-collect module="shoucang" :form="{biaoid:'xwid',biao:'biao',biaoti:'biaoti'}" :biaoid="$route.query.id" biao="gonggao" :biaoti="map.biaoti">
                                <template #icon="{isCollect}">
                                    <i class="fa" :class="isCollect ? 'fa-star' : 'fa-star-o'"></i>
                                </template>
                                <template #default="{isCollect}">
                                    <span class="collect-btn" :class="{ active: isCollect }">
                                        {{ isCollect ? '已收藏' : '收藏' }}
                                    </span>
                                </template>
                            </e-collect>
                        </div>
                    </div>
                </div>

                <el-divider class="divider" />

                <div class="article-content" v-html="map.neirong"></div>

                <!-- 上下篇导航 -->
                <div class="article-navigation">
                    <div class="nav-item prev-item">
                        <span class="nav-label">上一篇</span>
                        <template v-if="prev?.id">
                            <router-link :to="'/gonggao/detail?id='+prev.id" class="nav-link" :title="prev.biaoti">
                                <span class="nav-arrow">←</span>
                                <span class="nav-title" v-html="prev.biaoti"></span>
                            </router-link>
                        </template>
                        <template v-else>
                            <span class="nav-link no-link">
                                <span class="nav-arrow">←</span>
                                <span class="nav-title">没有了</span>
                            </span>
                        </template>
                    </div>
                    <div class="nav-item next-item">
                        <span class="nav-label">下一篇</span>
                        <template v-if="next?.id">
                            <router-link :to="'/gonggao/detail?id='+next.id" class="nav-link" :title="next.biaoti">
                                <span class="nav-title" v-html="next.biaoti"></span>
                                <span class="nav-arrow">→</span>
                            </router-link>
                        </template>
                        <template v-else>
                            <span class="nav-link no-link">
                                <span class="nav-title">没有了</span>
                                <span class="nav-arrow">→</span>
                            </span>
                        </template>
                    </div>
                </div>
            </div>
        </e-container>
    </div>
</template>

<script setup>
    import http from "@/utils/ajax/http";
    import DB from "@/utils/db";
    import { useRouter } from "vue-router";

    import { ref, reactive, watch, computed } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { extend } from "@/utils/extend";
    import { useGonggaoFindById, canGonggaoFindById } from "@/module";

    const route = useRoute();
    const router = useRouter();
    const props = defineProps({
        id: {
            type: [Number, String],
        },
        isShowBtn: {
            type: Boolean,
            default: true,
        },
    });

    // 返回上一页
    const goBack = () => {
        if (window.history.length > 1) {
            router.back();
        } else {
            router.push("/");
        }
    };

    /**
     * 获取详情页面的一行数据，当 url 参数 id 变更时，当 url 参数 id 变更时，自动更新 map 中的数据
     * @type {EGonggao}
     */
    const map = useGonggaoFindById(props.id);
    watch(
        () => props.id,
        (id) => {
            canGonggaoFindById(id).then((res) => {
                extend(map, res);
            });
        }
    );
    // end 获取详情页面的一行数据

    const prev = ref({});
    const next = ref({});
    watch(
        () => route.query.id,
        async (id) => {
            prev.value = await DB.name("gonggao").where("id", "<", id).order("id desc").find();
            next.value = await DB.name("gonggao").where("id", ">", id).order("id asc").find();
        },
        { immediate: true }
    );
</script>

<style scoped lang="scss">
    .views-gonggao-web-detail {
        min-height: 100vh;
        background: linear-gradient(135deg, #f5f7fa 0%, #e4e8ec 100%);
        padding: 30px 0;

        // 顶部导航栏
        .detail-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 24px;
            padding: 16px 20px;
            background: #ffffff;
            border-radius: 12px;
            box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);

            .back-nav {
                .back-btn {
                    font-size: 15px;
                    font-weight: 500;
                    padding: 10px 20px;
                    color: #666;
                    transition: all 0.3s ease;

                    &:hover {
                        color: #409eff;
                        transform: translateX(-4px);
                    }
                }
            }

            .breadcrumb-path {
                display: flex;
                align-items: center;
                gap: 8px;
                font-size: 14px;
                color: #999;

                .path-item {
                    font-weight: 500;

                    &:last-child {
                        color: #409eff;
                    }
                }

                .path-separator {
                    color: #ccc;
                }
            }
        }

        // 详情卡片
        .detail-card {
            background: #ffffff;
            border-radius: 16px;
            padding: 40px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
            border: 1px solid rgba(0, 0, 0, 0.04);

            // 文章头部
            .article-header {
                margin-bottom: 20px;

                .article-title {
                    font-size: 28px;
                    font-weight: 700;
                    color: #1a1a2e;
                    margin: 0 0 20px 0;
                    line-height: 1.4;
                    word-break: break-all;
                }

                .article-meta {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    flex-wrap: wrap;
                    gap: 16px;

                    .meta-info {
                        display: flex;
                        align-items: center;
                        gap: 8px;
                        font-size: 15px;
                        color: #666;

                        .meta-icon {
                            font-size: 18px;
                        }

                        .meta-label {
                            font-weight: 500;
                        }

                        .meta-value {
                            color: #333;
                            font-weight: 600;
                        }
                    }

                    .meta-actions {
                        .collect-btn {
                            display: inline-flex;
                            align-items: center;
                            gap: 6px;
                            padding: 8px 16px;
                            border-radius: 20px;
                            background: #f5f5f5;
                            color: #666;
                            font-size: 14px;
                            font-weight: 500;
                            cursor: pointer;
                            transition: all 0.3s ease;

                            .fa {
                                font-size: 16px;
                            }

                            &:hover {
                                background: #fff7e6;
                                color: #faad14;
                            }

                            &.active {
                                background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
                                color: #b8860b;
                                box-shadow: 0 4px 12px rgba(255, 215, 0, 0.3);

                                .fa {
                                    color: #b8860b;
                                }
                            }
                        }
                    }
                }
            }

            // 分割线
            .divider {
                margin: 24px 0 32px 0;
            }

            // 文章内容
            .article-content {
                font-size: 16px;
                line-height: 1.8;
                color: #333;
                min-height: 200px;

                :deep(img) {
                    max-width: 100%;
                    height: auto;
                    border-radius: 8px;
                    margin: 16px 0;
                    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                }

                :deep(p) {
                    margin-bottom: 16px;
                }

                :deep(h1), :deep(h2), :deep(h3), :deep(h4), :deep(h5), :deep(h6) {
                    margin-top: 24px;
                    margin-bottom: 16px;
                    font-weight: 600;
                    color: #1a1a2e;
                }

                :deep(ul), :deep(ol) {
                    margin-bottom: 16px;
                    padding-left: 24px;
                }

                :deep(li) {
                    margin-bottom: 8px;
                }

                :deep(blockquote) {
                    margin: 20px 0;
                    padding: 16px 20px;
                    background: #f8f9fa;
                    border-left: 4px solid #409eff;
                    border-radius: 4px;
                    color: #666;
                }

                :deep(table) {
                    width: 100%;
                    border-collapse: collapse;
                    margin: 20px 0;

                    th, td {
                        border: 1px solid #e1e1e1;
                        padding: 12px 16px;
                        text-align: left;
                    }

                    th {
                        background: #f5f7fa;
                        font-weight: 600;
                    }
                }
            }

            // 上下篇导航
            .article-navigation {
                display: flex;
                gap: 16px;
                margin-top: 40px;
                padding-top: 32px;
                border-top: 1px solid #e8e8e8;

                .nav-item {
                    flex: 1;
                    display: flex;
                    flex-direction: column;
                    gap: 8px;

                    .nav-label {
                        font-size: 13px;
                        color: #999;
                        font-weight: 500;
                        text-transform: uppercase;
                        letter-spacing: 0.5px;
                    }

                    .nav-link {
                        display: flex;
                        align-items: center;
                        gap: 10px;
                        padding: 16px;
                        background: #f8f9fa;
                        border-radius: 12px;
                        color: #333;
                        font-size: 15px;
                        font-weight: 500;
                        transition: all 0.3s ease;
                        text-decoration: none;

                        &:hover {
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            color: #fff;
                            transform: translateX(8px);
                            box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);

                            .nav-arrow {
                                color: #fff;
                            }
                        }

                        &.no-link {
                            background: #f0f0f0;
                            color: #bbb;
                            cursor: default;

                            &:hover {
                                background: #f0f0f0;
                                color: #bbb;
                                transform: none;
                                box-shadow: none;
                            }
                        }

                        .nav-arrow {
                            font-size: 18px;
                            font-weight: 700;
                            color: #409eff;
                            transition: color 0.3s ease;
                        }

                        .nav-title {
                            flex: 1;
                            overflow: hidden;
                            text-overflow: ellipsis;
                            white-space: nowrap;
                        }
                    }
                }

                .prev-item {
                    .nav-link {
                        .nav-arrow {
                            margin-right: 4px;
                        }
                    }
                }

                .next-item {
                    .nav-link {
                        flex-direction: row-reverse;

                        .nav-arrow {
                            margin-left: 4px;
                        }
                    }
                }
            }
        }

        // 响应式适配
        @media screen and (max-width: 768px) {
            padding: 16px 0;

            .detail-header {
                flex-direction: column;
                gap: 12px;
                align-items: flex-start;

                .breadcrumb-path {
                    width: 100%;
                    justify-content: flex-start;
                }
            }

            .detail-card {
                padding: 24px 20px;

                .article-header {
                    .article-title {
                        font-size: 22px;
                    }

                    .article-meta {
                        flex-direction: column;
                        align-items: flex-start;
                    }
                }

                .article-content {
                    font-size: 15px;
                }

                .article-navigation {
                    flex-direction: column;
                    gap: 12px;
                    margin-top: 24px;
                    padding-top: 24px;

                    .nav-item {
                        .nav-link {
                            font-size: 14px;
                            padding: 12px;

                            &:hover {
                                transform: translateY(-4px);
                            }
                        }

                        &.next-item {
                            .nav-link {
                                flex-direction: row;
                            }
                        }
                    }
                }
            }
        }
    }
</style>
