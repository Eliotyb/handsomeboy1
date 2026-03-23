<template>
  <div class="order-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单管理</span>
        </div>
      </template>
      <el-table :data="orders" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="orderNo" label="订单号"></el-table-column>
        <el-table-column prop="userId" label="用户ID" width="100"></el-table-column>
        <el-table-column prop="productId" label="商品ID" width="100"></el-table-column>
        <el-table-column prop="price" label="价格" width="100"></el-table-column>
        <el-table-column prop="quantity" label="数量" width="100"></el-table-column>
        <el-table-column prop="totalPrice" label="总价格" width="100"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            {{ getStatusText(scope.row.status) }}
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

    <!-- 编辑订单对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
    >
      <el-form :model="form" label-width="80px">
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
          <el-input v-model="form.price" disabled></el-input>
        </el-form-item>
        <el-form-item label="数量">
          <el-input v-model="form.quantity" disabled></el-input>
        </el-form-item>
        <el-form-item label="总价格">
          <el-input v-model="form.totalPrice" disabled></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="待支付" value="0"></el-option>
            <el-option label="已支付" value="1"></el-option>
            <el-option label="已发货" value="2"></el-option>
            <el-option label="已完成" value="3"></el-option>
            <el-option label="已取消" value="4"></el-option>
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
  name: 'OrderList',
  data() {
    return {
      orders: [],
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
      }
    }
  },
  mounted() {
    this.loadOrders()
  },
  methods: {
    loadOrders() {
      axios.get('/api/orders').then(response => {
        this.orders = response.data
      })
    },
    handleEdit(row) {
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleSave() {
      axios.put(`/api/orders/${this.form.id}`, this.form).then(() => {
        this.loadOrders()
        this.dialogVisible = false
      })
    },
    handleDelete(id) {
      this.$confirm('确定要删除这个订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.delete(`/api/orders/${id}`).then(() => {
          this.loadOrders()
        })
      })
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
    }
  }
}
</script>

<style scoped>
.order-list-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>