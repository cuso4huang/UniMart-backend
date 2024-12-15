<template>
  <div class="product-list">
    <el-row :gutter="20">
      <el-col 
        :xs="24" 
        :sm="12" 
        :md="8" 
        :lg="6" 
        v-for="product in products" 
        :key="product.productId"
      >
        <el-card class="product-card" shadow="hover">
          <div class="product-image">
            <el-image 
              :src="product.image || require('@/assets/default-product.png')"
              fit="cover"
            >
              <div slot="error" class="image-slot">
                <i class="el-icon-picture-outline"></i>
              </div>
            </el-image>
          </div>
          <div class="product-info">
            <h3 class="product-name">{{ product.productName }}</h3>
            <div class="product-category">
              <el-tag size="small" effect="plain">{{ product.category?.categoryName }}</el-tag>
            </div>
            <div class="product-tags" v-if="product.tags && product.tags.length">
              <el-tag 
                v-for="tag in product.tags" 
                :key="tag.tagId"
                size="mini"
                type="info"
                effect="plain"
              >
                {{ tag.tagName }}
              </el-tag>
            </div>
            <div class="product-price">
              <span v-if="product.price > 0">¥{{ product.price.toFixed(2) }}</span>
              <span v-else class="negotiable">面议</span>
            </div>
            <div class="product-meta">
              <span class="publish-time">{{ formatDate(product.publishTime) }}</span>
              <span class="view-count">
                <i class="el-icon-view"></i> {{ product.viewCount }}
              </span>
            </div>
          </div>
          <div class="product-actions">
            <el-button 
              type="text" 
              size="small" 
              @click="$router.push(`/product/${product.productId}`)"
            >
              查看详情
            </el-button>
            <el-button 
              type="text" 
              size="small" 
              @click="$router.push(`/product/edit/${product.productId}`)"
            >
              编辑
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 无数据提示 -->
    <el-empty 
      v-if="!loading && products.length === 0"
      description="暂无商品"
    ></el-empty>
  </div>
</template>

<script>
import { formatDate } from '@/utils/date'

export default {
  name: 'ProductList',
  props: {
    products: {
      type: Array,
      default: () => []
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    formatDate
  }
}
</script>

<style scoped>
.product-list {
  margin: 20px 0;
}

.product-card {
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 16px rgba(0,0,0,0.1);
}

.product-image {
  height: 200px;
  overflow: hidden;
  border-radius: 8px 8px 0 0;
  background-color: #f5f7fa;
}

.product-image .el-image {
  width: 100%;
  height: 100%;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  color: #909399;
  font-size: 30px;
}

.product-info {
  padding: 15px;
}

.product-name {
  margin: 0 0 12px;
  font-size: 16px;
  color: #303133;
  line-height: 1.4;
  height: 44px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.product-category {
  margin-bottom: 10px;
}

.product-tags {
  margin-bottom: 12px;
}

.product-tags .el-tag {
  margin-right: 6px;
  margin-bottom: 6px;
}

.product-price {
  margin: 12px 0;
  font-size: 20px;
  font-weight: bold;
}

.product-price span {
  color: #f56c6c;
}

.product-price .negotiable {
  color: #909399;
  font-size: 16px;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #909399;
  font-size: 13px;
}

.view-count i {
  margin-right: 4px;
}

.product-actions {
  padding: 10px 15px;
  border-top: 1px solid #eee;
  text-align: right;
}

.product-actions .el-button {
  margin-left: 10px;
}
</style> 