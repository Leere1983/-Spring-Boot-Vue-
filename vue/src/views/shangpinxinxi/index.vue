<template>
    <div class="views-shangpinxinxi-index">
        <e-container>
            <!-- 页面标题 -->
            <div class="page-header">
                <h2 class="page-title">
                    <span class="title-icon">🏪</span>
                    商品列表
                </h2>
            </div>

            <!-- 搜索筛选区域 -->
            <div class="search-section">
                <div class="search-card">
                    <div class="search-item">
                        <span class="search-label">商品状态：</span>
                        <div class="tag-group">
                            <el-tag
                                v-for="(item, index) in statusOptions"
                                :key="index"
                                :type="search.shangpinzhuangtai === item.value ? 'success' : 'info'"
                                :effect="search.shangpinzhuangtai === item.value ? 'dark' : 'plain'"
                                size="large"
                                class="status-tag"
                                @click="selectRadio('shangpinzhuangtai', item.value)"
                            >
                                {{ item.label }}
                            </el-tag>
                        </div>
                    </div>

                    <div class="search-item">
                        <span class="search-label">分类：</span>
                        <div class="tag-group">
                            <el-tag
                                :type="!search.fenlei ? 'warning' : 'info'"
                                :effect="!search.fenlei ? 'dark' : 'plain'"
                                size="large"
                                class="status-tag"
                                @click="selectRadio('fenlei', '')"
                            >
                                全部
                            </el-tag>
                            <el-tag
                                v-for="r in mapshangpinfenlei1"
                                :key="r.id"
                                :type="search.fenlei == r.id ? 'warning' : 'info'"
                                :effect="search.fenlei == r.id ? 'dark' : 'plain'"
                                size="large"
                                class="status-tag"
                                @click="selectRadio('fenlei', r.id)"
                            >
                                {{ r.fenleimingcheng }}
                            </el-tag>
                        </div>
                    </div>

                    <div class="search-item search-input-row">
                        <span class="search-label">商品名称：</span>
                        <el-input
                            type="text"
                            v-model="search.shangpinmingcheng"
                            placeholder="请输入商品名称搜索"
                            class="search-input"
                            clearable
                            @keyup.enter="searchSubmit"
                        >
                            <template #prefix>
                                <span class="search-icon">🔍</span>
                            </template>
                        </el-input>
                        <el-button type="success" size="large" class="search-btn" @click="searchSubmit">
                            <span class="btn-icon">🔎</span> 搜索
                        </el-button>
                    </div>
                </div>
            </div>

            <!-- 商品列表区域 -->
            <div class="product-section">
                <div class="product-count">
                    <span class="count-text">共找到 <em>{{ totalCount }}</em> 件商品</span>
                </div>

                <el-row :gutter="24" class="product-grid">
                    <el-col
                        v-for="r in lists"
                        :xs="12"
                        :sm="8"
                        :md="6"
                        :key="r.id"
                    >
                        <article class="product-card" @click="$goto('/shangpinxinxi/detail?id='+r.id)">
                            <div class="product-image-wrapper">
                                <div class="product-status" v-if="r.shangpinzhuangtai">
                                    <span class="status-badge" :class="getStatusClass(r.shangpinzhuangtai)">
                                        {{ r.shangpinzhuangtai }}
                                    </span>
                                </div>
                                <e-img class="product-image" :src="r.shangpintupian" pb="80" :is-scale="true" />
                                <div class="image-overlay">
                                    <span class="view-detail">查看详情</span>
                                </div>
                            </div>
                            <div class="product-info">
                                <h4 class="product-name" :title="r.shangpinmingcheng">{{ r.shangpinmingcheng }}</h4>
                                <div class="product-meta">
                                    <div class="price-wrapper">
                                        <span class="price-label">起拍价</span>
                                        <span class="current-price" v-if="r.qipaijia">
                                            <span class="currency">￥</span>{{ r.qipaijia }}
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </article>
                    </el-col>
                </el-row>

                <!-- 空状态提示 -->
                <el-empty v-if="lists.length === 0 && !loading" description="暂无商品数据" class="empty-state" />
            </div>

            <!-- 分页区域 -->
            <div class="pagination-section" v-if="totalCount > 0">
                <el-pagination
                    @current-change="loadList"
                    :page-sizes="[12, 24, 36, 48, 60]"
                    v-model:current-page="search.page"
                    v-model:page-size="search.pagesize"
                    @size-change="sizeChange"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="totalCount"
                    class="custom-pagination"
                >
                </el-pagination>
            </div>
        </e-container>
    </div>
</template>

<script setup>
    import http from "@/utils/ajax/http";
    import DB from "@/utils/db";
    import router from "@/router";

    import { ref, reactive, watch, unref, onBeforeMount, computed } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { canShangpinxinxiSelect, useShangpinxinxiSelect, canShangpinxinxiDelete } from "@/module";
    import { extend } from "@/utils/extend";
    import { ElMessageBox, ElMessage } from "element-plus";

    const route = useRoute();
    const search = reactive({
        issh: "是",
        shangpinbianhao: "",
        fenlei: "",
        shangpinmingcheng: "",
        shangpinzhuangtai: "",
        page: 1, // 当前页
        pagesize: 12, // 每页行数
        orderby: "id", // 排序字段
        sort: "desc", // 排序类型
    });
    extend(search, route.query);
    // 链接参数变化时更新这些内容
    watch(
        () => route.query,
        () => {
            extend(search, route.query);
            loadList(1);
        },
        { deep: true }
    );

    // 状态选项
    const statusOptions = ref([
        { label: '全部', value: '' },
        { label: '竞拍中', value: '竞拍中' },
        { label: '已结束', value: '已结束' },
        { label: '已成交', value: '已成交' },
        { label: '已流拍', value: '已流拍' },
    ]);

    // 获取状态样式类名
    const getStatusClass = (status) => {
        const classMap = {
            '竞拍中': 'status-auctioning',
            '已结束': 'status-ended',
            '已成交': 'status-sold',
            '已流拍': 'status-failed',
        };
        return classMap[status] || '';
    };

    // 总行数
    const totalCount = ref(0);
    /**
     * 列表数据
     * @type {EShangpinxinxi[]}
     */
    const lists = ref([]);
    // 加载状态
    const loading = ref(false);

    // 排序操作
    const sortChange = (e) => {
        console.log(e);
        if (e.order == null) {
            search.orderby = "id";
            search.sort = "desc";
        } else {
            search.orderby = e.prop;
            search.sort = e.order == "ascending" ? "asc" : "desc";
        }
        loadList(1);
    };
    // 设置页数多少
    const sizeChange = (e) => {
        search.pagesize = e;
        loadList(1);
    };

    // 加载商品信息列表方法
    const loadList = (page) => {
        // 加载
        if (unref(loading)) return;
        loading.value = true;
        search.page = page;

        http.post("/api/shangpinxinxi/selectPages", search).then(
            (res) => {
                loading.value = false;
                if (res.code == 0) {
                    var data = res.data;
                    lists.value = data.lists.records;
                    totalCount.value = data.lists.total;
                }
            },
            (err) => {
                loading.value = false;
                ElMessage.error(err.message);
            }
        );
    };

    onBeforeMount(() => {
        loadList(1);
    });
    const selectRadio = (target, name) => {
        search[target] = name;
        searchSubmit(1);
    };

    const searchSubmit = (page = 1) => {
        loadList(1);
    };
    const mapshangpinfenlei1 = DB.name("shangpinfenlei").field("id,fenleimingcheng").order("id desc").selectRef();
</script>

<style scoped lang="scss">
    .views-shangpinxinxi-index {
        min-height: 100vh;
        background: linear-gradient(135deg, #f5f7fa 0%, #e4e8ec 100%);
        padding: 20px 0;

        // 页面标题
        .page-header {
            margin-bottom: 24px;
            padding: 20px 0;

            .page-title {
                font-size: 28px;
                font-weight: 600;
                color: #1a1a2e;
                margin: 0;
                display: flex;
                align-items: center;
                gap: 12px;

                .title-icon {
                    font-size: 32px;
                }
            }
        }

        // 搜索区域
        .search-section {
            margin-bottom: 30px;

            .search-card {
                background: #ffffff;
                border-radius: 16px;
                padding: 24px;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
                border: 1px solid rgba(0, 0, 0, 0.04);
            }

            .search-item {
                display: flex;
                align-items: center;
                margin-bottom: 20px;
                gap: 16px;

                &:last-child {
                    margin-bottom: 0;
                }

                .search-label {
                    font-size: 15px;
                    font-weight: 500;
                    color: #333;
                    min-width: 80px;
                }

                .tag-group {
                    display: flex;
                    flex-wrap: wrap;
                    gap: 10px;
                    flex: 1;

                    .status-tag {
                        cursor: pointer;
                        transition: all 0.3s ease;
                        font-weight: 500;
                        padding: 6px 16px;

                        &:hover {
                            transform: translateY(-2px);
                            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
                        }
                    }
                }
            }

            .search-input-row {
                .search-input {
                    width: 300px;
                    margin-right: 12px;

                    :deep(.el-input__wrapper) {
                        border-radius: 8px;
                        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
                        transition: all 0.3s ease;

                        &:hover {
                            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                        }
                    }
                }

                .search-icon {
                    font-size: 16px;
                }

                .search-btn {
                    padding: 12px 28px;
                    font-size: 15px;
                    font-weight: 500;
                    border-radius: 8px;
                    transition: all 0.3s ease;

                    .btn-icon {
                        margin-right: 6px;
                    }

                    &:hover {
                        transform: translateY(-2px);
                        box-shadow: 0 6px 16px rgba(103, 194, 58, 0.4);
                    }
                }
            }
        }

        // 商品列表区域
        .product-section {
            .product-count {
                margin-bottom: 20px;
                padding: 12px 20px;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                border-radius: 12px;
                color: #fff;

                .count-text {
                    font-size: 15px;
                    font-weight: 500;

                    em {
                        font-style: normal;
                        font-size: 20px;
                        font-weight: 700;
                        margin: 0 4px;
                    }
                }
            }

            .product-grid {
                margin-bottom: 24px;
            }

            .empty-state {
                padding: 60px 0;
            }
        }

        // 商品卡片
        .product-card {
            background: #ffffff;
            border-radius: 16px;
            overflow: hidden;
            margin-bottom: 24px;
            cursor: pointer;
            transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
            box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
            border: 1px solid rgba(0, 0, 0, 0.04);
            position: relative;

            &:hover {
                transform: translateY(-8px);
                box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);

                .image-overlay {
                    opacity: 1;
                }

                .product-image {
                    transform: scale(1.08);
                }
            }

            .product-image-wrapper {
                position: relative;
                overflow: hidden;
                background: #f8f9fa;

                .product-status {
                    position: absolute;
                    top: 12px;
                    left: 12px;
                    z-index: 10;

                    .status-badge {
                        display: inline-block;
                        padding: 4px 12px;
                        border-radius: 20px;
                        font-size: 12px;
                        font-weight: 600;
                        color: #fff;
                        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);

                        &.status-auctioning {
                            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
                        }

                        &.status-ended {
                            background: linear-gradient(135deg, #868f96 0%, #596167 100%);
                        }

                        &.status-sold {
                            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
                        }

                        &.status-failed {
                            background: linear-gradient(135deg, #434343 0%, #0f0f0f 100%);
                        }
                    }
                }

                .product-image {
                    width: 100%;
                    aspect-ratio: 1;
                    object-fit: cover;
                    transition: transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
                }

                .image-overlay {
                    position: absolute;
                    bottom: 0;
                    left: 0;
                    right: 0;
                    top: 0;
                    background: linear-gradient(to bottom, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0.7) 100%);
                    display: flex;
                    align-items: flex-end;
                    justify-content: center;
                    padding-bottom: 20px;
                    opacity: 0;
                    transition: opacity 0.3s ease;

                    .view-detail {
                        color: #fff;
                        font-size: 15px;
                        font-weight: 600;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        padding: 10px 24px;
                        border-radius: 25px;
                        box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
                    }
                }
            }

            .product-info {
                padding: 16px;

                .product-name {
                    font-size: 15px;
                    font-weight: 600;
                    color: #1a1a2e;
                    margin: 0 0 12px 0;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    display: -webkit-box;
                    -webkit-line-clamp: 2;
                    -webkit-box-orient: vertical;
                    line-height: 1.5;
                    min-height: 45px;
                    transition: color 0.3s ease;
                }

                .product-meta {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;

                    .price-wrapper {
                        display: flex;
                        flex-direction: column;
                        gap: 4px;

                        .price-label {
                            font-size: 12px;
                            color: #999;
                            font-weight: 500;
                        }

                        .current-price {
                            font-size: 22px;
                            font-weight: 700;
                            color: #ff4757;
                            display: flex;
                            align-items: baseline;

                            .currency {
                                font-size: 14px;
                                margin-right: 2px;
                            }
                        }
                    }
                }
            }
        }

        // 分页区域
        .pagination-section {
            padding: 24px 0;
            display: flex;
            justify-content: center;

            .custom-pagination {
                :deep(.el-pagination__total) {
                    font-weight: 500;
                    color: #666;
                }

                :deep(.el-pager li) {
                    border-radius: 8px;
                    min-width: 36px;
                    height: 36px;
                    line-height: 36px;
                    font-weight: 500;
                    transition: all 0.3s ease;

                    &:hover {
                        transform: translateY(-2px);
                    }

                    &.is-active {
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        border: none;
                    }
                }

                :deep(.el-pagination__sizes) {
                    .el-select {
                        margin-right: 8px;
                    }
                }
            }
        }

        // 响应式适配
        @media screen and (max-width: 768px) {
            .page-header {
                .page-title {
                    font-size: 22px;
                }
            }

            .search-section {
                .search-card {
                    padding: 16px;
                }

                .search-item {
                    flex-direction: column;
                    align-items: flex-start;

                    .search-label {
                        margin-bottom: 8px;
                    }

                    .tag-group {
                        width: 100%;
                    }
                }

                .search-input-row {
                    flex-direction: column;
                    align-items: stretch;

                    .search-input {
                        width: 100% !important;
                        margin-right: 0;
                        margin-bottom: 12px;
                    }

                    .search-btn {
                        width: 100%;
                    }
                }
            }

            .product-card {
                .product-info {
                    .product-name {
                        min-height: auto;
                    }
                }
            }
        }
    }
</style>
