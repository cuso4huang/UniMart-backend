<template>
  <div class="publish-product">
    <nav-bar></nav-bar>
    <div class="content">
      <el-card class="form-card">
        <div slot="header">
          <span>发布商品</span>
        </div>
        
        <el-form 
          ref="productForm"
          :model="productForm"
          :rules="rules"
          label-width="100px"
          v-loading="isSubmitting"
        >
          <el-form-item label="商品名称" prop="productName">
            <el-input 
              v-model="productForm.productName"
              placeholder="请输入商品名称"
              maxlength="50"
              show-word-limit
            ></el-input>
          </el-form-item>

          <el-form-item label="商品描述" prop="productDescription">
            <el-input 
              type="textarea"
              v-model="productForm.productDescription"
              placeholder="请详细描述商品的具体情况，例如：使用时长、新旧程度、是否有损坏等"
              :rows="4"
              maxlength="500"
              show-word-limit
            ></el-input>
          </el-form-item>

          <el-form-item label="商品价格" prop="price">
            <el-input-number 
              v-model="productForm.price"
              :min="0"
              :precision="2"
              :step="0.01"
              controls-position="right"
              placeholder="请输入商品价格"
            ></el-input-number>
          </el-form-item>

          <el-form-item label="商品分类" prop="categoryId">
            <el-select 
              v-model="productForm.categoryId"
              placeholder="请选择商品分类"
            >
              <el-option
                v-for="item in categories"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="商品图片" prop="images">
            <el-upload
              action="/api/upload"
              list-type="picture-card"
              :on-preview="handlePictureCardPreview"
              :on-remove="handleRemove"
              :on-success="handleUploadSuccess"
              :before-upload="beforeUpload"
              :headers="uploadHeaders"
              :file-list="productForm.images.map(url => ({
                url,
                name: url.split('/').pop()
              }))"
              :limit="5"
              multiple
            >
              <i class="el-icon-plus"></i>
            </el-upload>
            <el-dialog :visible.sync="dialogVisible">
              <img width="100%" :src="dialogImageUrl" alt="">
            </el-dialog>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm" :loading="isSubmitting">发布商品</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import { publishProduct } from '@/api/product'
import { getToken } from '@/utils/auth'
import { BASE_URL } from '@/utils/request'

export default {
  name: 'PublishProduct',
  components: {
    NavBar
  },
  data() {
    return {
      isSubmitting: false,
      dialogImageUrl: '',
      dialogVisible: false,
      productForm: {
        productName: '',
        productDescription: '',
        price: 0,
        categoryId: '',
        images: []
      },
      rules: {
        productName: [
          { required: true, message: '请输入商品名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字', trigger: 'blur' }
        ],
        productDescription: [
          { required: true, message: '请输入商品描述', trigger: 'blur' },
          { min: 10, max: 500, message: '长度在 10 到 500 个字符', trigger: 'blur' }
        ],
        price: [
          { required: true, message: '请输入商品价格', trigger: 'blur' },
          { type: 'number', message: '价格必须为数字', trigger: 'blur' },
          { validator: (rule, value, callback) => {
            if (value <= 0) {
              callback(new Error('价格必须大于0'))
            } else {
              callback()
            }
          }, trigger: 'blur' }
        ],
        categoryId: [
          { required: true, message: '请选择商品分类', trigger: 'change' }
        ],
        images: [
          { 
            validator: (rule, value, callback) => {
              if (this.productForm.images.length === 0) {
                callback(new Error('请至少上传一张商品图片'))
              } else {
                callback()
              }
            }, 
            trigger: 'change' 
          }
        ]
      },
      categories: [
        { id: 1, name: '教材书籍' },
        { id: 2, name: '电子产品' },
        { id: 3, name: '日常用品' },
        { id: 4, name: '其他' }
      ]
    }
  },
  computed: {
    uploadHeaders() {
      return {
        Authorization: getToken()
      }
    }
  },
  methods: {
    handleRemove(file) {
      const fileUrl = file.url || (file.response && file.response.url)
      const index = this.productForm.images.indexOf(fileUrl)
      if (index !== -1) {
        this.productForm.images.splice(index, 1)
        this.$refs.productForm.validateField('images')
      }
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    handleUploadSuccess(response) {
      if (response.url) {
        const fullUrl = `${BASE_URL}${response.url}`
        this.productForm.images.push(fullUrl)
        // 更新文件列表显示
        this.fileList.push({
          name: response.url.split('/').pop(),
          url: fullUrl
        })
      }
    },
    beforeUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isImage) {
        this.$message.error('只能上传图片文件!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过 2MB!')
        return false
      }
      return true
    },
    async submitForm() {
      try {
        await this.$refs.productForm.validate()
        
        if (this.productForm.images.length === 0) {
          this.$message.warning('请至少上传一张商品图片')
          return
        }
        
        this.isSubmitting = true
        // 提交时去掉 BASE_URL 前缀
        const imageUrl = this.productForm.images[0].replace(BASE_URL, '')
        await publishProduct({
          productName: this.productForm.productName,
          productDescription: this.productForm.productDescription,
          price: this.productForm.price,
          categoryId: this.productForm.categoryId,
          image: imageUrl // 目前只取第一张图片
        })
        this.$message.success('商品发布成功')
        this.$router.push('/my')
      } catch (error) {
        console.error('发布商品失败:', error)
        this.$message.error(error.response?.data?.message || '发布失败，请稍后重试')
      } finally {
        this.isSubmitting = false
      }
    },
    resetForm() {
      this.$refs.productForm.resetFields()
      this.productForm.images = []
    }
  }
}
</script>

<style scoped>
.publish-product {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.content {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
}

.form-card {
  margin-bottom: 20px;
}

.el-upload-list--picture-card .el-upload-list__item {
  width: 100px;
  height: 100px;
}

.el-upload--picture-card {
  width: 100px;
  height: 100px;
  line-height: 100px;
}

.el-input-number {
  width: 200px;
}
</style> 