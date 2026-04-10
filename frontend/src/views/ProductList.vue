<template>
  <div class="product-list-container">
    <el-card class="list-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <div class="header-title">
            <el-icon class="title-icon"><Goods /></el-icon>
            <span>商品管理</span>
          </div>
          <el-button type="primary" @click="handleAdd" :icon="Plus">
            添加商品
          </el-button>
        </div>
      </template>
      <el-table :data="products" style="width: 100%" stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="商品名称" width="150"></el-table-column>
        <el-table-column prop="description" label="商品描述"></el-table-column>
        <el-table-column prop="price" label="价格" width="100">
          <template #default="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" size="small">
              {{ scope.row.status === 0 ? '下架' : '上架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)" :icon="Edit">
              编辑
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)" :icon="Delete">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑商品对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="520px"
      :close-on-click-modal="false"
    >
      <el-form :model="form" label-width="80px" :rules="rules" ref="formRef">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入商品名称"></el-input>
        </el-form-item>
        <el-form-item label="商品描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入商品描述"></el-input>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input v-model="form.price" type="number" step="0.01" placeholder="请输入价格">
            <template #prepend>¥</template>
          </el-input>
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input v-model="form.stock" type="number" placeholder="请输入库存"></el-input>
        </el-form-item>
        <el-form-item label="图片" prop="image">
          <el-input v-model="form.image" placeholder="请输入图片地址"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="下架" :value="0"></el-option>
            <el-option label="上架" :value="1"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave" :loading="submitLoading">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
import { Goods, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'ProductList',
  components: {
    Goods,
    Plus,
    Edit,
    Delete
  },
  data() {
    return {
      products: [],
      loading: false,
      submitLoading: false,
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: '',
        name: '',
        description: '',
        price: 0,
        stock: 0,
        image: '',
        status: 1
      },
      rules: {
        name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
        description: [{ required: true, message: '请输入商品描述', trigger: 'blur' }],
        price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
        stock: [{ required: true, message: '请输入库存', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.loadProducts()
  },
  methods: {
    loadProducts() {
      this.loading = true
      axios.get('/api/products')
        .then(response => {
          const res = response.data
          if (res.code === 200) {
            this.products = res.data
          } else {
            ElMessage.error(res.message || '加载商品失败')
          }
        })
        .catch(error => {
          console.error('加载商品失败:', error)
          ElMessage.error('加载商品失败，请检查后端服务是否运行')
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleAdd() {
      this.dialogTitle = '添加商品'
      this.form = {
        id: '',
        name: '',
        description: '',
        price: 0,
        stock: 0,
        image: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑商品'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleSave() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          const request = this.form.id 
            ? axios.put(`/api/products/${this.form.id}`, this.form)
            : axios.post('/api/products', this.form)
          
          request
            .then(response => {
              const res = response.data
              if (res.code === 200) {
                this.loadProducts()
                this.dialogVisible = false
                ElMessage.success(this.form.id ? '编辑商品成功' : '添加商品成功')
              } else {
                ElMessage.error(res.message || '操作失败')
              }
            })
            .catch(error => {
              console.error('操作失败:', error)
              ElMessage.error('操作失败，请检查后端服务是否运行')
            })
            .finally(() => {
              this.submitLoading = false
            })
        }
      })
    },
    handleDelete(id) {
      ElMessageBox.confirm('确定要删除这个商品吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.delete(`/api/products/${id}`)
          .then(response => {
            const res = response.data
            if (res.code === 200) {
              this.loadProducts()
              ElMessage.success('删除商品成功')
            } else {
              ElMessage.error(res.message || '删除失败')
            }
          })
          .catch(error => {
            console.error('删除商品失败:', error)
            ElMessage.error('删除商品失败，请检查后端服务是否运行')
          })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.product-list-container {
  padding: 0;
}

.list-card {
  border-radius: 12px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.title-icon {
  color: #67C23A;
  font-size: 20px;
}
</style>
