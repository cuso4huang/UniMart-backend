<template>
  <div class="my-products">
    <nav-bar></nav-bar>
    <div class="content">
      <el-card>
        <div slot="header" class="header">
          <span>我的商品</span>
          <el-button 
            type="primary" 
            icon="el-icon-plus"
            @click="$router.push('/publish')"
          >
            发布新商品
          </el-button>
        </div>
        
        <div v-loading="loading">
          <div v-if="products.length > 0" class="product-list">
            <el-card 
              v-for="product in products" 
              :key="product.productId"
              class="product-card"
              shadow="hover"
            >
              <div class="product-image-container">
                <img 
                  :src="product.image ? `http://localhost:8080${product.image}` : require('@/assets/default-product.png')"
                  class="product-image"
                  @click="handleImageClick(product)"
                >
                <div class="product-tags">
                  <el-tag 
                    v-for="tag in product.tags"
                    :key="tag.tagId"
                    size="small"
                    effect="plain"
                  >
                    {{ tag.tagName }}
                  </el-tag>
                </div>
              </div>
              <div class="product-info">
                <h3 class="product-name">{{ product.productName }}</h3>
                <p class="product-description">{{ product.productDescription }}</p>
                <div class="product-meta">
                  <el-tag size="mini" type="info">
                    {{ product.category.categoryName }}
                  </el-tag>
                  <span class="publish-time">
                    发布于 {{ formatDate(product.publishTime) }}
                  </span>
                </div>
                <div class="product-footer">
                  <div class="product-price">¥ {{ product.price.toFixed(2) }}</div>
                  <div class="product-stats">
                    <el-tooltip content="浏览次数" placement="top">
                      <span class="view-count">
                        <i class="el-icon-view"></i>
                        {{ product.viewCount }}
                      </span>
                    </el-tooltip>
                  </div>
                </div>
                <div class="product-actions">
                  <el-button 
                    type="text" 
                    icon="el-icon-edit"
                    @click="handleEdit(product)"
                  >
                    编辑
                  </el-button>
                  <el-button 
                    type="text" 
                    icon="el-icon-delete"
                    class="delete-btn"
                    @click="handleDelete(product)"
                  >
                    删除
                  </el-button>
                </div>
              </div>
            </el-card>
          </div>
          <el-empty v-else description="暂无商品，快去发布吧"></el-empty>
        </div>
      </el-card>
      
      <!-- 图片预览对话框 -->
      <el-dialog :visible.sync="previewVisible" append-to-body>
        <img width="100%" :src="previewImage" alt="">
      </el-dialog>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import { getMyProducts, deleteProduct } from '@/api/product'
import { formatDate } from '@/utils/date'
import { BASE_URL } from '@/utils/request'

export default {
  name: 'MyProducts',
  components: {
    NavBar
  },
  data() {
    return {
      products: [],
      loading: false,
      previewVisible: false,
      previewImage: ''
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
        const response = await getMyProducts()
        this.products = response || []
      } catch (error) {
        console.error('获取商品列表失败:', error)
        this.$message.error('获取商品列表失败')
      } finally {
        this.loading = false
      }
    },
    handleImageClick(product) {
      this.previewImage = product.image ? `${BASE_URL}${product.image}` : require('@/assets/default-product.png')
      this.previewVisible = true
    },
    handleEdit(product) {
      this.$router.push(`/product/edit/${product.productId}`)
    },
    async handleDelete(product) {
      try {
        await this.$confirm('确定要删除该商品吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await deleteProduct(product.productId)
        this.$message.success('删除成功')
        await this.fetchProducts()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除商品失败:', error)
          this.$message.error(error.response?.data?.message || '删除失败')
        }
      }
    }
  }
}
</script>

<style scoped>
.my-products {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.content {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.product-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.product-card {
  display: flex;
  flex-direction: column;
  height: 100%;
  transition: transform 0.3s;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.product-image-container {
  position: relative;
  width: 100%;
  height: 200px;
}

.product-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px 4px 0 0;
  cursor: pointer;
}

.product-tags {
  position: absolute;
  top: 10px;
  left: 10px;
  display: flex;
  gap: 5px;
  flex-wrap: wrap;
}

.product-info {
  flex: 1;
  padding: 15px;
}

.product-name {
  margin: 0 0 10px;
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.product-description {
  margin: 0 0 10px;
  font-size: 14px;
  color: #606266;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.product-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.publish-time {
  font-size: 12px;
  color: #909399;
}

.product-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.product-price {
  font-size: 18px;
  color: #f56c6c;
  font-weight: bold;
}

.product-stats {
  display: flex;
  align-items: center;
  gap: 10px;
}

.view-count {
  font-size: 12px;
  color: #909399;
}

.view-count i {
  margin-right: 3px;
}

.product-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  border-top: 1px solid #ebeef5;
  padding-top: 10px;
  margin-top: 10px;
}

.delete-btn {
  color: #f56c6c;
}

.delete-btn:hover {
  color: #f78989;
}

.el-tag {
  margin-right: 5px;
  margin-bottom: 5px;
}
</style> 