<template>
  <div class="product-detail">
    <nav-bar></nav-bar>
    <div class="content" v-loading="loading">
      <el-card v-if="product">
        <div class="product-layout">
          <div class="product-image">
            <el-image 
            :src="product.image ? `http://localhost:8080${product.image}` : require('@/assets/default-product.png')"

              fit="cover"
            ></el-image>
          </div>
          <div class="product-info">
            <h1>{{ product.productName }}</h1>
            
            <div class="price">¥{{ product.price || '面议' }}</div>
            
            <div class="category">
              <span class="label">分类：</span>
              <el-tag size="small">{{ product.category?.categoryName }}</el-tag>
            </div>
            
            <div class="tags" v-if="product.tags && product.tags.length">
              <span class="label">标签：</span>
              <el-tag 
                v-for="tag in product.tags" 
                :key="tag.tagId"
                size="small"
                type="info"
                style="margin-right: 5px"
              >
                {{ tag.tagName }}
              </el-tag>
            </div>
            
            <div class="description">
              <h3>商品描述</h3>
              <p>{{ product.productDescription }}</p>
            </div>
            
            <div class="seller-info">
              <h3>卖家信息</h3>
              <div class="seller">
                <el-avatar :size="40" :src="product.seller?.avatar || defaultAvatar"></el-avatar>
                <div class="seller-detail">
                  <div class="seller-name">{{ product.seller?.userName }}</div>
                  <div class="seller-rating">
                    <span>买家评分: {{ product.seller?.personalRatingBuyer || '暂无' }}</span>
                    <span>卖家评分: {{ product.seller?.personalRatingSeller || '暂无' }}</span>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="actions">
              <el-button 
                type="primary" 
                size="large" 
                @click="handlePurchase"
                :disabled="!userInfo || userInfo.id === product.sellID"
              >
                {{ getPurchaseButtonText }}
              </el-button>
              <el-button 
                type="primary" 
                size="large" 
                @click="handleContact"
                :disabled="!userInfo || userInfo.id === product.sellID"
              >
                联系卖家
              </el-button>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 购买确认对话框 -->
    <el-dialog
      title="确认购买"
      :visible.sync="dialogVisible"
      width="30%"
    >
      <div class="purchase-confirm">
        <p>商品：{{ product?.productName }}</p>
        <p>价格：¥{{ product?.price }}</p>
        <div class="payment-method">
          <p>支付方式：</p>
          <el-radio-group v-model="paymentMethod">
            <el-radio label="WECHAT">
              <i class="el-icon-money"></i> 微信支付
            </el-radio>
            <el-radio label="ALIPAY">
              <i class="el-icon-wallet"></i> 支付宝
            </el-radio>
          </el-radio-group>
        </div>
      </div>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button 
          type="primary" 
          @click="confirmPurchase"
          :loading="purchaseLoading"
        >
          确认购买
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getProductDetail } from '@/api/product'
import { createTransaction } from '@/api/transaction'
import { formatDate } from '@/utils/date'
import NavBar from '@/components/NavBar.vue'
import { mapState } from 'vuex'
import defaultAvatar from '@/assets/default-avatar.png'
import { sendMessage } from '@/api/message'

export default {
  name: 'ProductDetail',
  components: {
    NavBar
  },
  data() {
    return {
      loading: false,
      product: null,
      dialogVisible: false,
      purchaseLoading: false,
      sellerAvatar: defaultAvatar,
      sellerName: '',
      paymentMethod: 'WECHAT'
    }
  },
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo
    }),
    getPurchaseButtonText() {
      if (!this.userInfo) return '请先登录'
      if (this.userInfo.id === this.product?.sellID) return '这是你的商品'
      return '立即购买'
    }
  },
  created() {
    this.fetchProductDetail()
  },
  methods: {
    formatDate,
    async fetchProductDetail() {
      this.loading = true
      try {
        const productId = this.$route.params.id
        const response = await getProductDetail(productId)
        this.product = response
        // 这里可以添加获取卖家信息的逻辑
        this.sellerName = `用户${this.product.sellID}`
      } catch (error) {
        console.error('获取商品详情失败:', error)
        this.$message.error('获取商品详情失败')
      } finally {
        this.loading = false
      }
    },
    handlePurchase() {
      if (!this.userInfo) {
        this.$router.push('/login')
        return
      }
      this.dialogVisible = true
    },
    async confirmPurchase() {
      if (!this.userInfo) {
        this.$message.warning('请先登录')
        this.$router.push('/login')
        return
      }

      if (!this.paymentMethod) {
        this.$message.warning('请选择支付方式')
        return
      }

      this.purchaseLoading = true
      try {
        console.log('Creating transaction with data:', {
          productId: this.product.productId,
          sellerId: this.product.sellID,
          totalAmount: this.product.price,
          paymentMethod: this.paymentMethod
        })

        const response = await createTransaction({
          productId: this.product.productId,
          sellerId: this.product.sellID,
          totalAmount: this.product.price,
          paymentMethod: this.paymentMethod
        })

        console.log('Transaction created:', response)

        this.$message.success('创建交易成功')
        this.dialogVisible = false

        setTimeout(() => {
          this.$router.push(`/transaction/${response.transactionId}`)
        }, 500)
      } catch (error) {
        console.error('创建交易失败:', error.response || error)
        let errorMsg = '创建交易失败'
        if (error.response) {
          errorMsg = error.response.data?.message || errorMsg
          if (error.response.status === 401) {
            errorMsg = '请先登录'
            this.$router.push('/login')
          }
        }
        this.$message.error(errorMsg)
      } finally {
        this.purchaseLoading = false
      }
    },
    async contactSeller() {
      if (this.userInfo.id === this.product.sellID) {
        this.$message.warning('不能给自己发送消息')
        return
      }

      try {
        const { value } = await this.$prompt('请输入要发送给卖家的消息', '联系卖家', {
          confirmButtonText: '发送',
          cancelButtonText: '取消',
          inputPlaceholder: '请输入消息内容',
          inputPattern: /\S+/,
          inputErrorMessage: '消息不能为空'
        })

        await sendMessage({
          receiverId: this.product.sellID,
          content: value
        })

        this.$message({
          type: 'success',
          message: '消息已发送',
          duration: 2000
        })
      } catch (error) {
        if (error !== 'cancel') {  // 用户取消不提示错误
          console.error('发送消息失败:', error)
          this.$message.error('发送消息失败')
        }
      }
    },
    handleContact() {
      if (!this.userInfo) {
        this.$router.push('/login')
        return
      }
      this.$router.push(`/message/${this.product.sellID}/${this.product.productId}`)
    }
  }
}
</script>

<style scoped>
.product-detail {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.content {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.product-layout {
  display: flex;
  gap: 40px;
}

.product-image {
  flex: 0 0 500px;
}

.product-image .el-image {
  width: 100%;
  height: 500px;
  border-radius: 8px;
}

.product-info {
  flex: 1;
}

.product-info h1 {
  margin: 0 0 20px;
  font-size: 24px;
  color: #303133;
}

.price {
  font-size: 28px;
  color: #f56c6c;
  font-weight: bold;
  margin: 20px 0;
}

.category, .tags {
  margin: 15px 0;
}

.label {
  color: #909399;
  margin-right: 10px;
}

.description {
  margin: 30px 0;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.description h3 {
  margin: 0 0 10px;
  font-size: 16px;
  color: #303133;
}

.description p {
  margin: 0;
  color: #606266;
  line-height: 1.6;
}

.seller-info {
  margin-top: 20px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.seller-info h3 {
  margin: 0 0 15px;
  font-size: 16px;
  color: #303133;
}

.seller {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.seller-detail {
  margin-left: 15px;
}

.seller-name {
  font-size: 16px;
  color: #303133;
  margin-bottom: 5px;
}

.seller-rating {
  font-size: 13px;
  color: #909399;
}

.seller-rating span {
  margin-right: 15px;
}

.actions {
  margin-top: 30px;
}

.actions .el-button {
  margin-right: 15px;
  padding: 12px 30px;
}

.purchase-confirm {
  padding: 20px 0;
}

.purchase-confirm p {
  margin: 10px 0;
  font-size: 16px;
}

.payment-method {
  margin-top: 20px;
}

.payment-method p {
  margin-bottom: 10px;
  color: #606266;
}

.el-radio {
  margin-right: 20px;
  display: block;
  margin-bottom: 10px;
}

.el-radio i {
  margin-right: 5px;
  font-size: 16px;
}

.el-radio.is-checked {
  color: #409EFF;
}
</style> 