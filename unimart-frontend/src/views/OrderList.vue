<template>
  <div class="order-list">
    <nav-bar></nav-bar>
    <div class="content">
      <el-card>
        <div slot="header">
          <span>我的订单</span>
        </div>
        <el-table
          v-loading="loading"
          :data="orders"
          style="width: 100%">
          <el-table-column
            prop="id"
            label="订单号"
            width="180">
          </el-table-column>
          <el-table-column
            prop="product.name"
            label="商品名称">
          </el-table-column>
          <el-table-column
            prop="product.price"
            label="价格"
            width="120">
            <template slot-scope="scope">
              ¥{{ scope.row.product.price }}
            </template>
          </el-table-column>
          <el-table-column
            prop="status"
            label="状态"
            width="120">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="createTime"
            label="创建时间"
            width="180">
          </el-table-column>
          <el-table-column
            label="操作"
            width="180">
            <template slot-scope="scope">
              <el-button
                size="mini"
                @click="handleDetail(scope.row.id)">查看详情</el-button>
              <el-button
                size="mini"
                type="success"
                v-if="scope.row.status === 'PENDING'"
                @click="handleConfirm(scope.row)">确认收货</el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="pagination">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="pageSize"
            :total="total"
            layout="prev, pager, next">
          </el-pagination>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { getOrderList, updateOrderStatus } from '@/api/order'
import NavBar from '@/components/NavBar.vue'

export default {
  name: 'OrderList',
  components: {
    NavBar
  },
  data() {
    return {
      loading: false,
      orders: [],
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this.fetchOrders()
  },
  methods: {
    async fetchOrders() {
      this.loading = true
      try {
        const { data } = await getOrderList({
          page: this.currentPage,
          size: this.pageSize
        })
        this.orders = data.list
        this.total = data.total
      } catch (error) {
        this.$message.error('获取订单列表失败')
      } finally {
        this.loading = false
      }
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this.fetchOrders()
    },
    handleDetail(id) {
      // 跳转到订单详情页面
      this.$router.push(`/order/${id}`)
    },
    async handleConfirm(order) {
      try {
        await updateOrderStatus(order.id, 'COMPLETED')
        this.$message.success('确认收货成功')
        this.fetchOrders()
      } catch (error) {
        this.$message.error('操作失败')
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
    }
  }
}
</script>

<style scoped>
.order-list {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.content {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}
</style> 