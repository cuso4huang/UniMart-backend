<template>
  <div class="transaction-list">
    <h2>我的交易记录</h2>
    <div class="transaction-items">
      <div v-for="transaction in transactions" :key="transaction.transactionId" class="transaction-item">
        <div class="transaction-info">
          <h3>交易号：{{ transaction.transactionId }}</h3>
          <p>商品：{{ transaction.productName }}</p>
          <p>金额：¥{{ transaction.totalAmount }}</p>
          <p>状态：{{ getStatusText(transaction.transactionStatus) }}</p>
          <p>创建时间：{{ formatDateTime(transaction.createTime) }}</p>
        </div>
        <div class="transaction-actions">
          <button 
            v-if="transaction.transactionStatus === 'PENDING_PAYMENT'"
            @click="handlePay(transaction.transactionId)"
          >
            支付
          </button>
          <button 
            v-if="transaction.transactionStatus === 'PAID'"
            @click="handleRefund(transaction.transactionId)"
          >
            申请退款
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getMyTransactions, payTransaction, refundTransaction } from '@/api/transaction'
import { formatDate } from '@/utils/date'

export default {
  name: 'TransactionList',
  data() {
    return {
      transactions: []
    }
  },
  created() {
    this.fetchTransactions()
  },
  methods: {
    async fetchTransactions() {
      try {
        const response = await getMyTransactions()
        this.transactions = response
      } catch (error) {
        console.error('获取交易列表失败:', error)
        this.$message.error('获取交易列表失败')
      }
    },
    async handlePay(transactionId) {
      try {
        await payTransaction(transactionId)
        this.$message.success('支付成功')
        this.fetchTransactions() // 刷新列表
      } catch (error) {
        console.error('支付失败:', error)
        this.$message.error('支付失败')
      }
    },
    async handleRefund(transactionId) {
      try {
        await refundTransaction(transactionId)
        this.$message.success('退款申请已提交')
        this.fetchTransactions() // 刷新列表
      } catch (error) {
        console.error('退款申请失败:', error)
        this.$message.error('退款申请失败')
      }
    },
    getStatusText(status) {
      const statusMap = {
        'PENDING_PAYMENT': '待支付',
        'PAID': '已支付',
        'REFUNDING': '退款中',
        'REFUNDED': '已退款',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    },
    formatDateTime(date) {
      return formatDate(date)
    }
  }
}
</script>

<style scoped>
.transaction-list {
  padding: 20px;
}

.transaction-items {
  margin-top: 20px;
}

.transaction-item {
  border: 1px solid #eee;
  padding: 15px;
  margin-bottom: 15px;
  border-radius: 4px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.transaction-info {
  flex: 1;
}

.transaction-actions {
  margin-left: 20px;
}

button {
  padding: 8px 15px;
  margin-left: 10px;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  background-color: #409EFF;
  color: white;
}

button:hover {
  background-color: #66b1ff;
}
</style> 