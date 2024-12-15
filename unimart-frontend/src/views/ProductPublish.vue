<template>
  <div class="product-publish">
    <nav-bar></nav-bar>
    <div class="content">
      <el-card>
        <div slot="header">
          <span>发布商品</span>
        </div>
        <el-form
          :model="form"
          :rules="rules"
          ref="form"
          label-width="100px"
          v-loading="loading">
          <el-form-item label="商品名称" prop="name">
            <el-input v-model="form.name"></el-input>
          </el-form-item>
          
          <el-form-item label="商品价格" prop="price">
            <el-input-number 
              v-model="form.price" 
              :precision="2" 
              :step="0.1" 
              :min="0">
            </el-input-number>
          </el-form-item>
          
          <el-form-item label="商品分类" prop="category">
            <el-select v-model="form.category" placeholder="请选择商品分类">
              <el-option
                v-for="item in categories"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item label="商品图片" prop="images">
            <el-upload
              class="upload-demo"
              action="/api/upload"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :on-success="handleUploadSuccess"
              :before-upload="beforeUpload"
              multiple
              :limit="5"
              list-type="picture">
              <el-button size="small" type="primary">点击上传</el-button>
              <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过2MB</div>
            </el-upload>
          </el-form-item>
          
          <el-form-item label="商品描述" prop="description">
            <el-input
              type="textarea"
              v-model="form.description"
              :rows="4">
            </el-input>
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="handlePublish" :loading="publishing">
              发布商品
            </el-button>
            <el-button @click="$router.push('/product/manage')">取消</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script>
import { createProduct } from '@/api/product'
import NavBar from '@/components/NavBar.vue'

export default {
  name: 'ProductPublish',
  components: {
    NavBar
  },
  data() {
    return {
      loading: false,
      publishing: false,
      form: {
        name: '',
        price: 0,
        category: '',
        images: [],
        description: ''
      },
      categories: [
        { value: 'BOOKS', label: '书籍教材' },
        { value: 'ELECTRONICS', label: '电子产品' },
        { value: 'CLOTHING', label: '服装衣物' },
        { value: 'DAILY', label: '日常用品' },
        { value: 'OTHERS', label: '其他' }
      ],
      rules: {
        name: [
          { required: true, message: '请输入商品名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        price: [
          { required: true, message: '请输入商品价格', trigger: 'blur' }
        ],
        category: [
          { required: true, message: '请选择商品分类', trigger: 'change' }
        ],
        images: [
          { required: true, message: '请上传至少一张商品图片', trigger: 'change' }
        ],
        description: [
          { required: true, message: '请输入商品描述', trigger: 'blur' },
          { min: 10, max: 500, message: '长度在 10 到 500 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    handlePreview(file) {
      // 处理图片预览
    },
    handleRemove(file, fileList) {
      this.form.images = fileList.map(file => file.url)
    },
    handleUploadSuccess(response, file, fileList) {
      this.form.images = fileList.map(file => file.url)
    },
    beforeUpload(file) {
      const isJPGOrPNG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPGOrPNG) {
        this.$message.error('只能上传 JPG 或 PNG 格式的图片!')
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过 2MB!')
      }
      return isJPGOrPNG && isLt2M
    },
    async handlePublish() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          this.publishing = true
          try {
            await createProduct(this.form)
            this.$message.success('商品发布成功')
            this.$router.push('/product/manage')
          } catch (error) {
            this.$message.error(error.message || '发布失败')
          } finally {
            this.publishing = false
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.product-publish {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.content {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
}

.el-upload__tip {
  line-height: 1.2;
  margin-top: 5px;
}
</style> 