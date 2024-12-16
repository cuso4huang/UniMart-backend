<template>
  <div class="home">
    <nav-bar></nav-bar>
    <div class="content">
      <!-- 搜索栏 -->
      <div class="search-section">
        <el-input
          v-model="searchQuery"
          placeholder="搜索你想要的商品"
          class="search-input"
          clearable
          prefix-icon="el-icon-search"
          @keyup.enter.native="handleSearch"
        >
          <el-button 
            slot="append"
            type="primary"
            @click="handleSearch"
          >
            搜索
          </el-button>
        </el-input>
      </div>

      <!-- 分类选择 -->
      <div class="filter-section">
        <el-card shadow="never">
          <div class="categories">
            <span class="label">分类：</span>
            <el-radio-group v-model="selectedCategory" @change="handleCategoryChange" size="medium">
              <el-radio-button label="">全部</el-radio-button>
              <el-radio-button 
                v-for="category in categories" 
                :key="category.id" 
                :label="category.id"
              >
                {{ category.name }}
              </el-radio-button>
            </el-radio-group>
          </div>
        </el-card>
      </div>

      <!-- 商品列表 -->
      <div class="product-section" v-loading="loading">
        <el-row :gutter="20">
          <el-col 
            :xs="24" 
            :sm="12" 
            :md="8" 
            :lg="6" 
            v-for="product in products" 
            :key="product.productId"
          >
            <el-card 
              class="product-card" 
              shadow="hover" 
              @click.native="viewProduct(product.productId)"
            >
              <div class="product-image">
                <el-image 
                  :src="product.image ? `http://localhost:8080${product.image}` : require('@/assets/default-product.png')"
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
            </el-card>
          </el-col>
        </el-row>

        <!-- 无数据提示 -->
        <el-empty 
          v-if="!loading && products.length === 0"
          description="暂无商品"
        ></el-empty>

        <!-- 加载更多 -->
        <div class="load-more" v-if="products.length > 0">
          <el-button 
            type="text"
            :loading="loading"
            @click="loadMore"
          >
            加载更多
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getProductList, getProductsByCategory, searchProducts } from '@/api/product'
import { getTransactionsByProduct } from '@/api/transaction'
import { formatDate } from '@/utils/date'
import NavBar from '@/components/NavBar.vue'

export default {
  name: 'Home',
  components: {
    NavBar
  },
  data() {
    return {
      loading: false,
      products: [],
      searchQuery: '',
      selectedCategory: '',
      categories: [
        { id: 1, name: '教材书籍' },
        { id: 2, name: '电子产品' },
        { id: 3, name: '日常用品' },
        { id: 4, name: '其他' }
      ]
    }
  },
  created() {
    this.fetchProducts()
  },
  methods: {
    formatDate,
    async fetchProducts() {
      this.loading = true
      try {
        let response
        if (this.selectedCategory) {
          response = await getProductsByCategory(this.selectedCategory)
        } else if (this.searchQuery.trim()) {
          response = await searchProducts(this.searchQuery.trim())
        } else {
          response = await getProductList()
        }
        
        this.products = await this.filterSoldProducts(response)
      } catch (error) {
        console.error('获取商品列表失败:', error)
        this.$message.error('获取商品列表失败')
      } finally {
        this.loading = false
      }
    },
    
    async filterSoldProducts(products) {
      try {
        const productsWithStatus = await Promise.all(
          products.map(async (product) => {
            const transactions = await getTransactionsByProduct(product.productId)
            const isSold = transactions.some(t => 
              t.transactionStatus === 'PAID' || t.transactionStatus === 'COMPLETED'
            )
            return { ...product, isSold }
          })
        )
        
        return productsWithStatus.filter(product => !product.isSold)
      } catch (error) {
        console.error('过滤商品失败:', error)
        return products
      }
    },
    
    handleCategoryChange() {
      this.searchQuery = ''  // 切换分类时清空搜索
      this.fetchProducts()
    },
    
    async handleSearch() {
      if (!this.searchQuery.trim()) {
        this.selectedCategory = ''  // 清空分类选择
      }
      await this.fetchProducts()
    },
    
    viewProduct(productId) {
      this.$router.push(`/product/${productId}`)
    }
  }
}
</script>

<style scoped>
.home {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.search-section {
  margin: 20px auto 30px;
  max-width: 600px;
}

.search-input :deep(.el-input__inner) {
  height: 50px;
  line-height: 50px;
  font-size: 16px;
  border-radius: 25px;
}

.search-input :deep(.el-input-group__append) {
  border-top-right-radius: 25px;
  border-bottom-right-radius: 25px;
  overflow: hidden;
}

.search-input :deep(.el-button) {
  padding: 12px 25px;
  font-size: 16px;
}

.filter-section {
  margin-bottom: 30px;
}

.filter-section .el-card {
  border-radius: 8px;
  background-color: #fff;
}

.categories {
  padding: 10px 0;
}

.label {
  margin-right: 15px;
  color: #606266;
  font-size: 15px;
}

.product-section {
  min-height: 300px;
}

.product-card {
  margin-bottom: 25px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
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

.load-more {
  text-align: center;
  margin: 30px 0;
}

.load-more .el-button {
  font-size: 15px;
}

.category-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.category-item {
  padding: 8px 16px;
  cursor: pointer;
  transition: all 0.3s;
}

.category-item:hover {
  background-color: #f5f5f5;
}
</style> 