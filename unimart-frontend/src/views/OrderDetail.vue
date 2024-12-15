<template>
  <div class="order-detail">
    <nav-bar></nav-bar>
    <div class="content">
      <el-card v-loading="loading">
        <div slot="header">
          <span>订单详情</span>
        </div>
        
        <template v-if="order">
          <div class="order-info">
            <h3>订单信息</h3>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="订单编号">{{ order.id }}</el-descriptions-item>
              <el-descriptions-item label="订单状态">
                <el-tag :type="getStatusType(order.status)">
                  {{ getStatusText(order.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ order.createTime }}</el-descriptions-item>
              <el-descriptions-item label="支付时间">{{ order.payTime || '-' }}</el-descriptions-item>
            </el-descriptions>
          </div>
          
          <div class="product-info">
            <h3>商品信息</h3>
            <el-card shadow="never" class="product-card">
              <div class="product-detail">
                <el-image
                  :src="order.product.image"
                  fit="cover"
                  class="product-image">
                </el-image>
                <div class="product-content">
                  <h4>{{ order.product.name }}</h4>
                  <p class="price">¥{{ order.product.price }}</p>
                </div>
              </div>
            </el-card>
          </div>
          
          <div class="seller-info">
            <h3>卖家信息</h3>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="卖家">{{ order.seller.username }}</el-descriptions-item>
              <el-descriptions-item label="联系方式">{{ order.seller.phone }}</el-descriptions-item>
            </el-descriptions>
          </div>
          
          <div class="action-buttons" v-if="order.status === 'PENDING'">
            <el-button type="success" @click="handleConfirm">确认收货</el-button>
            <el-button type="danger" @click="handleCancel">取消订单</el-button>
          </div>
        </template>
      </el-card>
    </div>
  </div>
</template>

<script>
import { getOrderDetail, updateOrderStatus } from '@/api/order'
import NavBar from '@/components/NavBar.vue'

export default {
  name: 'OrderDetail',
  components: {
    NavBar
  },
  data() {
    return {
      loading: false,
      order: null
    }
  },
  created() {
    this.fetchOrderDetail()
  },
  methods: {
    async fetchOrderDetail() {
      this.loading = true
      try {
        const { data } = await getOrderDetail(this.$route.params.id)
        this.order = data
      } catch (error) {
        this.$message.error('获取订单详情失败')
      } finally {
        this.loading = false
      }
    },
    getStatusType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'COMPLETED': 'success',
        'CANCELLED': 'info'
      }
      return statusMap[status]
    },
    getStatusText(status) {
      const statusMap = {
        'PENDING': '待收货',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return statusMap[status]
    },
    async handleConfirm() {
      try {
        await this.$confirm('确认已收到商品？', '提示', {
          type: 'warning'
        })
        await updateOrderStatus(this.order.id, 'COMPLETED')
        this.$message.success('确认收货成功')
        this.fetchOrderDetail()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('操作失败')
        }
      }
    },
    async handleCancel() {
      try {
        await this.$confirm('确定要取消订单吗？', '提示', {
          type: 'warning'
        })
        await updateOrderStatus(this.order.id, 'CANCELLED')
        this.$message.success('订单已取消')
        this.fetchOrderDetail()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('操作失败')
        }
      }
    }
  }
}
</script>

<style scoped>
.order-detail {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.content {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
}

.order-info,
.product-info,
.seller-info {
  margin-bottom: 30px;
}

h3 {
  margin-bottom: 20px;
  font-weight: 500;
  color: #303133;
}

.product-card {
  margin-top: 10px;
}

.product-detail {
  display: flex;
  align-items: center;
}

.product-image {
  width: 100px;
  height: 100px;
  margin-right: 20px;
}

.product-content h4 {
  margin: 0 0 10px;
  font-size: 16px;
}

.price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
}

.action-buttons {
  margin-top: 30px;
  text-align: center;
}

.action-buttons .el-button {
  margin: 0 10px;
}
</style> 