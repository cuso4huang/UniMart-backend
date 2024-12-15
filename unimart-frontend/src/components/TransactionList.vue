<template>
  <div class="transaction-list">
    <el-table
      :data="transactions"
      v-loading="loading"
      style="width: 100%"
    >
      <el-table-column
        prop="transactionId"
        label="交易号"
        width="100"
      />
      <el-table-column
        label="商品信息"
        width="300"
      >
        <template slot-scope="scope">
          <div class="product-info">
            <div class="product-name">{{ scope.row.product.productName }}</div>
            <div class="product-price">¥{{ scope.row.totalAmount }}</div>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="paymentMethod"
        label="支付方式"
        width="120"
      >
        <template slot-scope="scope">
          {{ getPaymentMethodText(scope.row.paymentMethod) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="transactionStatus"
        label="交易状态"
        width="120"
      >
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.transactionStatus)">
            {{ getStatusText(scope.row.transactionStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="transactionTime"
        label="交易时间"
        width="180"
      >
        <template slot-scope="scope">
          {{ formatDate(scope.row.transactionTime) }}
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        width="200"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="viewDetail(scope.row.transactionId)"
          >
            查看详情
          </el-button>
          <el-button
            size="mini"
            type="primary"
            v-if="scope.row.transactionStatus === 'PENDING_PAYMENT'"
            @click="handlePay(scope.row.transactionId)"
          >
            支付
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getMyTransactions, payTransaction } from '@/api/transaction'
import { formatDate } from '@/utils/date'

export default {
  name: 'TransactionList',
  data() {
    return {
      transactions: [],
      loading: false
    }
  },
  created() {
    this.fetchTransactions()
  },
  methods: {
    formatDate,
    async fetchTransactions() {
      this.loading = true
      try {
        const response = await getMyTransactions()
        console.log('获取到的交易记录:', response)
        this.transactions = response
      } catch (error) {
        console.error('获取交易记录失败:', error)
        this.$message.error('获取交易记录失败')
      } finally {
        this.loading = false
      }
    },
    getPaymentMethodText(method) {
      const methodMap = {
        'WECHAT': '微信支付',
        'ALIPAY': '支付宝'
      }
      return methodMap[method] || method
    },
    getStatusText(status) {
      const statusMap = {
        'PENDING_PAYMENT': '待支付',
        'PAID': '已支付',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    },
    getStatusType(status) {
      const typeMap = {
        'PENDING_PAYMENT': 'warning',
        'PAID': 'success',
        'COMPLETED': 'success',
        'CANCELLED': 'info'
      }
      return typeMap[status] || ''
    },
    viewDetail(transactionId) {
      this.$router.push(`/transaction/${transactionId}`)
    },
    async handlePay(transactionId) {
      try {
        await payTransaction(transactionId)
        this.$message.success('支付成功')
        this.fetchTransactions()
      } catch (error) {
        console.error('支付失败:', error)
        this.$message.error('支付失败')
      }
    }
  }
}
</script>

<style scoped>
.transaction-list {
  margin: 20px 0;
}

.product-info {
  display: flex;
  flex-direction: column;
}

.product-name {
  font-size: 14px;
  color: #303133;
  margin-bottom: 5px;
}

.product-price {
  color: #f56c6c;
  font-weight: bold;
}

.el-button + .el-button {
  margin-left: 10px;
}
</style> 