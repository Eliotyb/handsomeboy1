<template>
  <div class="product-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>商品管理</span>
          <el-button type="primary" @click="handleAdd">添加商品</el-button>
        </div>
      </template>
      <el-table :data="products" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="商品名称"></el-table-column>
        <el-table-column prop="description" label="商品描述"></el-table-column>
        <el-table-column prop="price" label="价格" width="100"></el-table-column>
        <el-table-column prop="stock" label="库存" width="100"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            {{ scope.row.status === 0 ? '下架' : '上架' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑商品对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
    >
      <el-form :model="form" label-width="80px">
        <el-form-item label="商品名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="商品描述">
          <el-input v-model="form.description" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model="form.price" type="number" step="0.01"></el-input>
        </el-form-item>
        <el-form-item label="库存">
          <el-input v-model="form.stock" type="number"></el-input>
        </el-form-item>
        <el-form-item label="图片">
          <el-input v-model="form.image"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="下架" value="0"></el-option>
            <el-option label="上架" value="1"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ProductList',
  data() {
    return {
      products: [],
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
      }
    }
  },
  mounted() {
    this.loadProducts()
  },
  methods: {
    loadProducts() {
      axios.get('/api/products').then(response => {
        this.products = response.data
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
      if (this.form.id) {
        // 编辑
        axios.put(`/api/products/${this.form.id}`, this.form).then(() => {
          this.loadProducts()
          this.dialogVisible = false
        })
      } else {
        // 添加
        axios.post('/api/products', this.form).then(() => {
          this.loadProducts()
          this.dialogVisible = false
        })
      }
    },
    handleDelete(id) {
      this.$confirm('确定要删除这个商品吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.delete(`/api/products/${id}`).then(() => {
          this.loadProducts()
        })
      })
    }
  }
}
</script>

<style scoped>
.product-list-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>