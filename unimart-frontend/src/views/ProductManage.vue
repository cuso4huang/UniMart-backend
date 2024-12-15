<template>
  <div class="product-manage">
    <nav-bar></nav-bar>
    <div class="content">
      <el-card>
        <div slot="header" class="header">
          <span>我的商品</span>
          <el-button type="primary" size="small" @click="$router.push('/product/publish')">
            发布商品
          </el-button>
        </div>
        
        <el-table
          v-loading="loading"
          :data="products"
          style="width: 100%">
          <el-table-column
            prop="name"
            label="商品名称">
          </el-table-column>
          <el-table-column
            prop="price"
            label="价格"
            width="120">
            <template slot-scope="scope">
              ¥{{ scope.row.price }}
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
            label="发布时间"
            width="180">
          </el-table-column>
          <el-table-column
            label="操作"
            width="200">
            <template slot-scope="scope">
              <el-button
                size="mini"
                @click="handleEdit(scope.row)">编辑</el-button>
              <el-button
                size="mini"
                :type="scope.row.status === 'ON_SALE' ? 'warning' : 'success'"
                @click="handleToggleStatus(scope.row)">
                {{ scope.row.status === 'ON_SALE' ? '下架' : '上架' }}
              </el-button>
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.row)">删除</el-button>
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
import { getProductList, deleteProduct, updateProduct } from '@/api/product'
import NavBar from '@/components/NavBar.vue'

export default {
  name: 'ProductManage',
  components: {
    NavBar
  },
  data() {
    return {
      loading: false,
      products: [],
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this.fetchProducts()
  },
  methods: {
    async fetchProducts() {
      this.loading = true
      try {
        const { data } = await getProductList({
          page: this.currentPage,
          size: this.pageSize,
          userId: this.$store.state.userInfo.id
        })
        this.products = data.list
        this.total = data.total
      } catch (error) {
        this.$message.error('获取商品列表失败')
      } finally {
        this.loading = false
      }
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this.fetchProducts()
    },
    handleEdit(product) {
      this.$router.push(`/product/edit/${product.id}`)
    },
    async handleToggleStatus(product) {
      try {
        const newStatus = product.status === 'ON_SALE' ? 'OFF_SALE' : 'ON_SALE'
        await updateProduct(product.id, { status: newStatus })
        this.$message.success('操作成功')
        this.fetchProducts()
      } catch (error) {
        this.$message.error('操作失败')
      }
    },
    async handleDelete(product) {
      try {
        await this.$confirm('确定要删除该商品吗？', '提示', {
          type: 'warning'
        })
        await deleteProduct(product.id)
        this.$message.success('删除成功')
        this.fetchProducts()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },
    getStatusType(status) {
      const statusMap = {
        'ON_SALE': 'success',
        'OFF_SALE': 'info',
        'SOLD': 'warning'
      }
      return statusMap[status]
    },
    getStatusText(status) {
      const statusMap = {
        'ON_SALE': '在售',
        'OFF_SALE': '已下架',
        'SOLD': '已售出'
      }
      return statusMap[status]
    }
  }
}
</script>

<style scoped>
.product-manage {
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

.pagination {
  margin-top: 20px;
  text-align: center;
}
</style> 