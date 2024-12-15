<template>
  <div class="transaction-detail">
    <nav-bar></nav-bar>
    <div class="content" v-loading="loading">
      <el-card v-if="transaction">
        <div class="transaction-info">
          <h2>交易详情</h2>
          <!-- 基本信息 -->
          <div class="info-section">
            <h3>基本信息</h3>
            <div class="info-item">
              <span class="label">交易号：</span>
              <span>{{ transaction.transactionId }}</span>
            </div>
            <div class="info-item">
              <span class="label">交易状态：</span>
              <el-tag :type="getStatusType">{{ getStatusText }}</el-tag>
            </div>
            <div class="info-item">
              <span class="label">交易时间：</span>
              <span>{{ formatDate(transaction.transactionTime) }}</span>
            </div>
            <div class="info-item">
              <span class="label">支付方式：</span>
              <span>{{ getPaymentMethodText }}</span>
            </div>
            <div class="info-item">
              <span class="label">交易金额：</span>
              <span class="amount">¥{{ transaction.totalAmount }}</span>
            </div>
          </div>

          <!-- 商品信息 -->
          <div class="info-section">
            <h3>商品信息</h3>
            <div class="info-item">
              <span class="label">商品名称：</span>
              <span>{{ transaction.product.productName }}</span>
            </div>
            <div class="info-item">
              <span class="label">商品描述：</span>
              <span>{{ transaction.product.productDescription }}</span>
            </div>
            <div class="info-item">
              <span class="label">商品分类：</span>
              <el-tag size="small">{{ transaction.product.category.categoryName }}</el-tag>
            </div>
            <div class="info-item" v-if="transaction.product.tags.length">
              <span class="label">商品标签：</span>
              <el-tag 
                v-for="tag in transaction.product.tags" 
                :key="tag.tagId"
                size="small"
                type="info"
                style="margin-right: 5px"
              >
                {{ tag.tagName }}
              </el-tag>
            </div>
          </div>

          <!-- 交易双方信息 -->
          <div class="info-section">
            <h3>交易双方</h3>
            <div class="info-item">
              <span class="label">买家：</span>
              <span>{{ transaction.buyer.userName }} ({{ transaction.buyer.account }})</span>
            </div>
            <div class="info-item">
              <span class="label">卖家：</span>
              <span>{{ transaction.seller.userName }} ({{ transaction.seller.account }})</span>
            </div>
          </div>
        </div>

        <!-- 支付按钮 -->
        <div class="actions" v-if="transaction.transactionStatus === 'PENDING_PAYMENT'">
          <el-button type="primary" @click="showPaymentDialog" size="large">
            立即支付
          </el-button>
        </div>
      </el-card>
    </div>

    <!-- 支付确认对话框 -->
    <el-dialog
      title="确认支付"
      :visible.sync="paymentDialogVisible"
      width="30%"
    >
      <div class="payment-confirm">
        <div class="payment-info">
          <p>商品：{{ transaction?.product.productName }}</p>
          <p class="amount">支付金额：¥{{ transaction?.totalAmount }}</p>
          <p>支付方式：{{ getPaymentMethodText }}</p>
        </div>
        <div class="payment-notice">
          <i class="el-icon-warning-outline"></i>
          <span>这��一个模拟支付过程，点击确认即视为支付成功</span>
        </div>
      </div>
      <span slot="footer">
        <el-button @click="paymentDialogVisible = false">取 消</el-button>
        <el-button 
          type="primary" 
          @click="confirmPay"
          :loading="payLoading"
        >
          确认支付
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getTransactionDetail, payTransaction } from '@/api/transaction'
import { formatDate } from '@/utils/date'
import NavBar from '@/components/NavBar.vue'

export default {
  name: 'TransactionDetail',
  components: {
    NavBar
  },
  data() {
    return {
      loading: false,
      payLoading: false,
      transaction: null,
      paymentDialogVisible: false
    }
  },
  computed: {
    getPaymentMethodText() {
      const methodMap = {
        'WECHAT': '微信支付',
        'ALIPAY': '支付宝'
      }
      return methodMap[this.transaction?.paymentMethod] || this.transaction?.paymentMethod
    },
    getStatusText() {
      const statusMap = {
        'PENDING_PAYMENT': '待支付',
        'PAID': '已支付',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return statusMap[this.transaction?.transactionStatus] || this.transaction?.transactionStatus
    },
    getStatusType() {
      const typeMap = {
        'PENDING_PAYMENT': 'warning',
        'PAID': 'success',
        'COMPLETED': 'success',
        'CANCELLED': 'info'
      }
      return typeMap[this.transaction?.transactionStatus] || ''
    }
  },
  created() {
    this.fetchTransactionDetail()
  },
  methods: {
    formatDate,
    async fetchTransactionDetail() {
      this.loading = true
      try {
        const transactionId = this.$route.params.id
        const response = await getTransactionDetail(transactionId)
        this.transaction = response
      } catch (error) {
        console.error('获取交易详情失败:', error)
        const errorMsg = error.response?.data?.message || '获取交易详情失败'
        this.$message.error(errorMsg)
        
        if (error.response?.status === 404) {
          this.$router.push('/')
        }
      } finally {
        this.loading = false
      }
    },
    showPaymentDialog() {
      this.paymentDialogVisible = true
    },
    async confirmPay() {
      this.payLoading = true
      try {
        await payTransaction(this.transaction.transactionId)
        this.$message.success('支付成功')
        this.paymentDialogVisible = false
        await this.fetchTransactionDetail()  // 刷新交易状态
      } catch (error) {
        console.error('支付失败:', error)
        this.$message.error(error.response?.data?.message || '支付失败')
      } finally {
        this.payLoading = false
      }
    }
  }
}
</script>

<style scoped>
.transaction-detail {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.content {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
}

.info-section {
  margin: 20px 0;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
}

.info-section h3 {
  margin: 0 0 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
  color: #303133;
  font-size: 16px;
}

.info-item {
  margin: 12px 0;
  font-size: 14px;
  line-height: 1.8;
}

.label {
  display: inline-block;
  width: 100px;
  color: #909399;
}

.amount {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
}

.actions {
  margin-top: 30px;
  text-align: center;
}

.payment-confirm {
  padding: 20px 0;
}

.payment-info {
  margin-bottom: 20px;
}

.payment-info p {
  margin: 10px 0;
  font-size: 14px;
}

.payment-notice {
  padding: 10px;
  background-color: #fdf6ec;
  border-radius: 4px;
  color: #e6a23c;
  font-size: 13px;
  display: flex;
  align-items: center;
}

.payment-notice i {
  margin-right: 5px;
  font-size: 16px;
}

.el-button--large {
  padding: 12px 30px;
  font-size: 14px;
}

.el-tag {
  margin-right: 5px;
}
</style> 