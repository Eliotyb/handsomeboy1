<template>
  <div class="order-list-container">
    <el-card class="list-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <div class="header-title">
            <el-icon class="title-icon"><Document /></el-icon>
            <span>订单管理</span>
          </div>
        </div>
      </template>
      <el-table :data="orders" style="width: 100%" stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="orderNo" label="订单号" width="180"></el-table-column>
        <el-table-column prop="userId" label="用户ID" width="100"></el-table-column>
        <el-table-column prop="productId" label="商品ID" width="100"></el-table-column>
        <el-table-column prop="price" label="价格" width="100">
          <template #default="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="80"></el-table-column>
        <el-table-column prop="totalPrice" label="总价格" width="100">
          <template #default="scope">
            ¥{{ scope.row.totalPrice }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
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

    <!-- 编辑订单对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="520px"
      :close-on-click-modal="false"
    >
      <el-form :model="form" label-width="80px" :rules="rules" ref="formRef">
        <el-form-item label="订单号">
          <el-input v-model="form.orderNo" disabled></el-input>
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input v-model="form.userId" disabled></el-input>
        </el-form-item>
        <el-form-item label="商品ID">
          <el-input v-model="form.productId" disabled></el-input>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model="form.price" disabled>
            <template #prepend>¥</template>
          </el-input>
        </el-form-item>
        <el-form-item label="数量">
          <el-input v-model="form.quantity" disabled></el-input>
        </el-form-item>
        <el-form-item label="总价格">
          <el-input v-model="form.totalPrice" disabled>
            <template #prepend>¥</template>
          </el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="待支付" :value="0"></el-option>
            <el-option label="已支付" :value="1"></el-option>
            <el-option label="已发货" :value="2"></el-option>
            <el-option label="已完成" :value="3"></el-option>
            <el-option label="已取消" :value="4"></el-option>
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
import { Document, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'OrderList',
  components: {
    Document,
    Edit,
    Delete
  },
  data() {
    return {
      orders: [],
      loading: false,
      submitLoading: false,
      dialogVisible: false,
      dialogTitle: '编辑订单',
      form: {
        id: '',
        orderNo: '',
        userId: '',
        productId: '',
        price: 0,
        quantity: 0,
        totalPrice: 0,
        status: 0
      },
      rules: {
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.loadOrders()
  },
  methods: {
    loadOrders() {
      this.loading = true
      axios.get('/api/orders')
        .then(response => {
          const res = response.data
          if (res.code === 200) {
            this.orders = res.data
          } else {
            ElMessage.error(res.message || '加载订单失败')
          }
        })
        .catch(error => {
          console.error('加载订单失败:', error)
          ElMessage.error('加载订单失败，请检查后端服务是否运行')
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleEdit(row) {
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleSave() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          axios.put(`/api/orders/${this.form.id}`, this.form)
            .then(response => {
              const res = response.data
              if (res.code === 200) {
                this.loadOrders()
                this.dialogVisible = false
                ElMessage.success('编辑订单成功')
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
      ElMessageBox.confirm('确定要删除这个订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.delete(`/api/orders/${id}`)
          .then(response => {
            const res = response.data
            if (res.code === 200) {
              this.loadOrders()
              ElMessage.success('删除订单成功')
            } else {
              ElMessage.error(res.message || '删除失败')
            }
          })
          .catch(error => {
            console.error('删除订单失败:', error)
            ElMessage.error('删除订单失败，请检查后端服务是否运行')
          })
      }).catch(() => {})
    },
    getStatusText(status) {
      const statusMap = {
        0: '待支付',
        1: '已支付',
        2: '已发货',
        3: '已完成',
        4: '已取消'
      }
      return statusMap[status] || '未知'
    },
    getStatusType(status) {
      const typeMap = {
        0: 'warning',
        1: 'primary',
        2: 'info',
        3: 'success',
        4: 'danger'
      }
      return typeMap[status] || 'info'
    }
  }
}
</script>

<style scoped>
.order-list-container {
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
  color: #909399;
  font-size: 20px;
}
</style>
