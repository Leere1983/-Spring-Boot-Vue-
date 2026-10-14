<template>
  <div class="ai-qa-entry">
    <el-button type="primary" class="ai-float-btn" @click="visible = true">AI问答</el-button>
    <el-dialog v-model="visible" title="智能问答" width="500px">
      <div class="ai-body">
        <div class="ai-answer" v-if="answer">
          <div class="label">回答：</div>
          <div class="text-answer">{{ answer }}</div>
          
          <!-- 商品列表 -->
          <div v-if="products && products.length > 0" class="product-list">
            <div 
              v-for="(product, index) in products" 
              :key="index" 
              class="product-card"
              @click="goToProductDetail(product.detailUrl)"
            >
              <div class="product-image-wrapper">
                <img 
                  v-if="product.imageUrl" 
                  :src="product.imageUrl" 
                  :alt="product.name"
                  class="product-image"
                  loading="lazy"
                  @click.stop="previewImage(product.imageUrl)"
                />
                <div v-else class="no-image">暂无图片</div>
              </div>
              <div class="product-info">
                <div class="product-name">{{ product.name }}</div>
                <div class="product-price">
                  <span class="label">起拍价：</span>
                  <span class="price">¥{{ product.startPrice }}</span>
                </div>
                <div class="product-price">
                  <span class="label">当前价：</span>
                  <span class="price current">¥{{ product.currentPrice }}</span>
                </div>
                <div class="product-status" :class="getStatusClass(product.status)">
                  {{ product.status }}
                </div>
              </div>
            </div>
          </div>
          
          <div v-if="!products || products.length === 0" class="content" v-html="formattedAnswer"></div>
        </div>
        <el-input
            v-model="question"
            type="textarea"
            :rows="4"
            maxlength="500"
            show-word-limit
            placeholder="请输入你的问题，AI将智能读取相关数据为你解答，例如：搜索手表"
        />
      </div>
      <template #footer>
        <el-button @click="visible = false">关闭</el-button>
        <el-button type="primary" :loading="loading" @click="ask">发送</el-button>
      </template>
    </el-dialog>
    
    <!-- 图片预览模态框 -->
    <div v-if="previewImageUrl" class="image-preview-overlay" @click="previewImageUrl = ''">
      <div class="preview-content" @click.stop>
        <button class="close-btn" @click="previewImageUrl = ''">×</button>
        <img :src="previewImageUrl" alt="图片预览" class="preview-image" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import http from "@/utils/ajax/http";
import { ElMessage } from "element-plus";
import { useRouter } from 'vue-router';

const router = useRouter();

const visible = ref(false);
const loading = ref(false);
const question = ref("");
const answer = ref("");
const products = ref([]);
const previewImageUrl = ref("");

const formattedAnswer = computed(() => {
  const raw = answer.value || "";
  let result = raw
      .replace(/\*\*(.*?)\*\*/g, "<strong>$1</strong>")
      .replace(/^\s*-\s+/gm, "• ")
      .replace(/\n/g, "<br/>");
  return result;
});

const getStatusClass = (status) => {
  switch (status) {
    case '进行中': return 'status-active';
    case '已结束': return 'status-ended';
    case '待审核': return 'status-pending';
    default: return '';
  }
};

const goToProductDetail = (url) => {
  const [path, queryStr] = url.split('?');
  const query = {};
  if (queryStr) {
    queryStr.split('&').forEach(param => {
      const [key, value] = param.split('=');
      query[key] = value;
    });
  }
  router.push({ path, query });
  visible.value = false;
};

const previewImage = (url) => {
  previewImageUrl.value = url;
};

const ask = () => {
  const q = question.value.trim();
  if (!q) {
    ElMessage.warning("请先输入问题");
    return;
  }
  loading.value = true;
  products.value = [];
  http.post("/api/ai/qa", { question: q, scene: "auction" })
      .then((res) => {
        if (res.code === 0) {
          answer.value = (res.data && res.data.answer) || "暂无回答";
          if (res.data && res.data.products) {
            products.value = res.data.products;
          }
        } else {
          ElMessage.error(res.msg || "问答失败");
        }
      })
      .catch(() => {
        ElMessage.error("问答服务异常，请稍后再试");
      })
      .finally(() => {
        loading.value = false;
      });
};
</script>

<style scoped>
.ai-float-btn {
  position: fixed;
  right: 24px;
  bottom: 24px;
  z-index: 1200;
  border-radius: 999px;
  box-shadow: 0 8px 20px rgba(22, 119, 255, 0.25);
}

.ai-body {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.ai-answer {
  border: 1px solid #d7e7ff;
  background: #f4f9ff;
  border-radius: 8px;
  padding: 10px 12px;
}

.label {
  font-weight: 600;
  color: #1f2d3d;
  margin-bottom: 8px;
}

.text-answer {
  line-height: 1.6;
  color: #2f3e4d;
  margin-bottom: 12px;
}

.content {
  line-height: 1.9;
  color: #2f3e4d;
  white-space: pre-wrap;
  word-break: break-word;
}

/* 商品列表样式 */
.product-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.product-card {
  display: flex;
  gap: 12px;
  padding: 10px;
  background: #fff;
  border-radius: 8px;
  cursor: pointer;
  border: 1px solid #e8e8e8;
  transition: all 0.3s ease;
}

.product-card:hover {
  border-color: #1890ff;
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.2);
}

.product-image-wrapper {
  width: 80px;
  height: 80px;
  flex-shrink: 0;
  border-radius: 6px;
  overflow: hidden;
  background: #f5f5f5;
}

.product-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  cursor: zoom-in;
}

.no-image {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
  font-size: 12px;
}

.product-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.product-name {
  font-weight: 600;
  color: #1f2d3d;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-price {
  display: flex;
  align-items: baseline;
  gap: 4px;
  font-size: 13px;
}

.product-price .label {
  color: #8c8c8c;
  margin-bottom: 0;
  font-weight: normal;
}

.product-price .price {
  color: #f5222d;
  font-weight: 600;
}

.product-price .current {
  color: #1890ff;
}

.product-status {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  width: fit-content;
}

.status-active {
  background: #f6ffed;
  color: #52c41a;
}

.status-ended {
  background: #fff2f0;
  color: #ff4d4f;
}

.status-pending {
  background: #fffbe6;
  color: #faad14;
}

/* 图片预览模态框样式 */
.image-preview-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.preview-content {
  position: relative;
  max-width: 90vw;
  max-height: 90vh;
  padding: 20px;
  animation: scaleIn 0.3s ease;
}

@keyframes scaleIn {
  from { transform: scale(0.8); opacity: 0; }
  to { transform: scale(1); opacity: 1; }
}

.close-btn {
  position: absolute;
  top: -40px;
  right: 0;
  width: 36px;
  height: 36px;
  border: none;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.2);
  color: #fff;
  font-size: 24px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.3s ease;
}

.close-btn:hover {
  background-color: rgba(255, 255, 255, 0.3);
  transform: rotate(90deg);
}

.preview-image {
  max-width: 100%;
  max-height: 85vh;
  border-radius: 8px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.4);
}
</style>
