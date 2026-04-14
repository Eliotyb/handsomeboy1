<template>
  <div class="order-manage-container">
    <el-card class="main-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-icon class="title-icon"><Document /></el-icon>
            <span>订单管理</span>
          </div>
          <div class="header-right">
            <el-input v-model="searchKeyword" placeholder="搜索订单号/用户..." clearable style="width: 220px;" @clear="loadOrders" @keyup.enter="loadOrders">
              <template #prefix><el-icon><Search /></el-icon></template>
            </el-input>
            <el-select v-model="filterStatus" placeholder="全部状态" clearable style="width: 140px; margin-left: 10px;" @change="loadOrders">
              <el-option label="全部状态" value="" />
              <el-option label="待支付" :value="0" />
              <el-option label="已支付/待发货" :value="1" />
              <el-option label="已发货/待收货" :value="2" />
              <el-option label="已完成" :value="3" />
              <el-option label="已取消" :value="4" />
              <el-option label="退款中" :value="5" />
              <el-option label="已退款" :value="6" />
            </el-select>
            <el-button type="primary" @click="loadOrders" :icon="Search">查询</el-button>
          </div>
        </div>
      </template>

      <el-table :data="orders" stripe border style="width: 100%" v-loading="loading"
        :header-cell-style="{ background: '#fff7ed', color: '#c2410c', fontWeight: '600' }"
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="45" />
        <el-table-column prop="id" label="ID" width="65" align="center" />
        <el-table-column prop="orderNo" label="订单号" width="200" show-overflow-tooltip>
          <template #default="{ row }"><span style="color:#2563eb;font-weight:600;">{{ row.orderNo }}</span></template>
        </el-table-column>
        <el-table-column prop="userId" label="用户ID" width="80" align="center" />
        <el-table-column label="收货信息" min-width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <div v-if="row.receiverName">{{ row.receiverName }} / {{ row.receiverPhone }}</div>
            <div v-else style="color:#ccc;">-</div>
          </template>
        </el-table-column>
        <el-table-column label="金额" width="110" align="right">
          <template #default="{ row }"><strong style="color:#d94545;">¥{{ (row.totalPrice || row.price * row.quantity).toFixed(2) }}</strong></template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small" effect="dark">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="物流" width="140" show-overflow-tooltip>
          <template #default="{ row }">
            <span v-if="row.logisticsCompany">{{ row.logisticsCompany }} / {{ row.logisticsNo }}</span>
            <span v-else style="color:#ccc;">-</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="165">
          <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right" align="center">
          <template #default="{ row }">
            <el-button size="small" link type="primary" @click="handleView(row)">详情</el-button>
            <el-button size="small" link type="primary" @click="handleEdit(row)" v-if="row.status <= 2">编辑</el-button>
            <el-dropdown trigger="click" @command="(cmd) => handleStatusAction(cmd, row)">
              <el-button size="small" link type="warning">状态操作 ▼</el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="pay" :disabled="row.status !== 0">确认付款</el-dropdown-item>
                  <el-dropdown-item command="ship" :disabled="row.status !== 1">发货</el-dropdown-item>
                  <el-dropdown-item command="receive" :disabled="row.status !== 2">确认收货</el-dropdown-item>
                  <el-dropdown-item command="cancel" :disabled="row.status > 1">取消订单</el-dropdown-item>
                  <el-dropdown-item command="refund" :disabled="row.status > 3">申请退款</el-dropdown-item>
                  <el-dropdown-item command="delete" divided :style="{color:'#f56c6c'}">删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <div class="table-footer">
        <div class="batch-actions">
          <el-button size="small" :disabled="selectedIds.length === 0" @click="handleBatchDelete" type="danger" plain>批量删除 ({{ selectedIds.length }})</el-button>
          <el-button size="small" :disabled="selectedIds.length === 0" @click="handleBatchShip" type="warning" plain>批量发货</el-button>
        </div>
        <el-pagination background layout="total, sizes, prev, pager, next, jumper" :total="totalRecords"
          :page-sizes="[10, 20, 50]" :page-size="pageSize" :current-page="currentPage"
          @size-change="handleSizeChange" @current-change="handlePageChange" style="justify-content: flex-end;" />
      </div>
    </el-card>

    <!-- 订单详情/编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" ref="formRef">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="订单号"><el-input v-model="form.orderNo" disabled /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户ID"><el-input v-model="form.userId" disabled /></el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="商品ID"><el-input v-model="form.productId" disabled /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数量"><el-input-number v-model="form.quantity" :min="1" style="width:100%;" /></el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="单价">¥{{ form.price?.toFixed(2) }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总价">¥{{ (form.totalPrice || form.price * form.quantity)?.toFixed(2) }}</el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="left">收货信息</el-divider>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="收货人"><el-input v-model="form.receiverName" placeholder="收货人姓名" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话"><el-input v-model="form.receiverPhone" placeholder="手机号码" /></el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="收货地址"><el-input v-model="form.receiverAddress" type="textarea" :rows="2" placeholder="详细地址" /></el-form-item>
        <el-divider content-position="left">物流与状态</el-divider>
        <el-row :gutter="16">
          <el-col :span="8">
            <el-form-item label="支付方式">
              <el-select v-model="form.payMethod" style="width:100%;">
                <el-option label="微信支付" value="wechat" /><el-option label="支付宝" value="alipay" /><el-option label="余额" value="balance" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="物流公司"><el-input v-model="form.logisticsCompany" placeholder="顺丰速运等" /></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="物流单号"><el-input v-model="form.logisticsNo" placeholder="快递单号" /></el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="订单状态" prop="status">
          <el-select v-model="form.status" style="width:100%;">
            <el-option label="待支付" :value="0" /><el-option label="已支付" :value="1" />
            <el-option label="已发货" :value="2" /><el-option label="已完成" :value="3" />
            <el-option label="已取消" :value="4" /><el-option label="退款中" :value="5" />
            <el-option label="已退款" :value="6" /><el-option label="退款拒绝" :value="7" />
          </el-select>
        </el-form-item>
        <el-form-item label="取消原因" v-if="form.status === 4"><el-input v-model="form.cancelReason" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleSave" :loading="submitLoading">保存修改</el-button>
      </template>
    </el-dialog>

    <!-- 发货对话框 -->
    <el-dialog title="发货操作" v-model="shipDialogVisible" width="450px">
      <el-form :model="shipForm" label-width="90px">
        <el-form-item label="物流公司"><el-select v-model="shipForm.logisticsCompany" style="width:100%;">
          <el-option label="顺丰速运" value="顺丰速运" /><el-option label="圆通快递" value="圆通快递" />
          <el-option label="中通快递" value="中通快递" /><el-option label="韵达快递" value="韵达快递" />
          <el-option label="京东物流" value="京东物流" /><el-option label="EMS邮政" value="EMS邮政" />
        </el-select></el-form-item>
        <el-form-item label="物流单号"><el-input v-model="shipForm.logisticsNo" placeholder="请输入快递单号" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="shipDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmShip" :loading="submitLoading">确认发货</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
import { Document, Search, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'OrderManage',
  components: { Document, Search },
  data() {
    return {
      orders: [], loading: false, submitLoading: false,
      searchKeyword: '', filterStatus: '', selectedIds: [],
      currentPage: 1, pageSize: 10, totalRecords: 0,
      dialogVisible: false, dialogTitle: '编辑订单', currentOrder: null,
      form: { id:'', orderNo:'', userId:'', productId:'', price:0, quantity:1, totalPrice:0, status:0,
        receiverName:'', receiverPhone:'', receiverAddress:'', payMethod:'wechat',
        logisticsCompany:'', logisticsNo:'', cancelReason:'' },
      shipDialogVisible: false, shipForm: { logisticsCompany:'顺丰速运', logisticsNo:'' }
    }
  },
  mounted() { this.loadOrders() },
  methods: {
    async loadOrders() {
      this.loading = true
      try {
        const params = { page: this.currentPage, size: this.pageSize }
        if (this.searchKeyword) params.keyword = this.searchKeyword
        if (this.filterStatus !== '' && this.filterStatus != null) params.status = this.filterStatus
        const res = await axios.get('/api/orders', { params })
        if (res.data.code === 200) {
          const data = res.data.data
          this.orders = Array.isArray(data) ? data : (data.records || [])
          this.totalRecords = Array.isArray(data) ? data.length : (data.total || 0)
        }
      } catch (e) { console.error('加载失败:', e); this.orders = [] }
      finally { this.loading = false }
    },
    formatTime(t) { if (!t) return '-'; const d = new Date(t); return d.toLocaleString('zh-CN') },
    getStatusText(s) { const m = { 0:'待支付',1:'待发货',2:'待收货',3:'已完成',4:'已取消',5:'退款中',6:'已退款',7:'拒绝' }; return m[s]||'未知' },
    getTypeMap() { return { 0:'warning',1:'',2:'info',3:'success',4:'danger',5:'warning',6:'info',7:'danger' } },
    getStatusType(s) { return this.getTypeMap()[s] || 'info' },

    handleSelectionChange(rows) { this.selectedIds = rows.map(r => r.id) },
    handleSizeChange(s) { this.pageSize = s; this.loadOrders() },
    handlePageChange(p) { this.currentPage = p; this.loadOrders() },

    handleView(row) { this.handleEdit(row) },
    handleEdit(row) {
      this.currentOrder = row
      this.form = { ...row }
      this.dialogTitle = `编辑订单 - ${row.orderNo}`
      this.dialogVisible = true
    },
    async handleSave() {
      this.submitLoading = true
      try {
        await axios.put(`/api/orders/${this.form.id}`, this.form)
        ElMessage.success('保存成功')
        this.dialogVisible = false
        this.loadOrders()
      } catch(e) { ElMessage.error('保存失败') }
      finally { this.submitLoading = false }
    },

    async handleStatusAction(action, row) {
      switch(action) {
        case 'pay':
          await this.doApi(`/api/orders/${row.id}/pay`, {}, '确认付款')
          break
        case 'ship':
          this.currentOrder = row
          this.shipForm = { logisticsCompany: row.logisticsCompany || '顺丰速运', logisticsNo: row.logisticsNo || '' }
          this.shipDialogVisible = true
          break
        case 'receive':
          await this.doApi(`/api/orders/${row.id}/receive`, {}, '确认收货')
          break
        case 'cancel':
          ElMessageBox.confirm('确定取消该订单吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
            .then(() => this.doApi(`/api/orders/${row.id}/cancel`, { reason: '管理员取消' }, '已取消')).catch(()=>{})
          break
        case 'refund':
          ElMessage.info('请到售后管理页面处理退款申请')
          break
        case 'delete':
          ElMessageBox.confirm('确定删除该订单？此操作不可恢复！', '警告', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'error', confirmButtonClass: 'el-button--danger' })
            .then(async () => {
              await axios.delete(`/api/orders/${row.id}`)
              ElMessage.success('已删除'); this.loadOrders()
            }).catch(()=>{})
          break
      }
    },

    async doApi(url, body, msg) {
      try { await axios.put(url, body); ElMessage.success(msg); this.loadOrders() }
      catch(e) { ElMessage.error('操作失败') }
    },

    async confirmShip() {
      if (!this.shipForm.logisticsNo.trim()) { ElMessage.warning('请输入物流单号'); return }
      this.submitLoading = true
      try {
        await axios.put(`/api/orders/${this.currentOrder.id}/ship`, this.shipForm)
        ElMessage.success('发货成功')
        this.shipDialogVisible = false
        this.loadOrders()
      } catch(e) { ElMessage.error('发货失败') }
      finally { this.submitLoading = false }
    },

    async handleBatchDelete() {
      ElMessageBox.confirm(`确定删除选中的 ${this.selectedIds.length} 条记录？`, '批量删除', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'error' })
        .then(async () => {
          for (const id of this.selectedIds) { await axios.delete(`/api/orders/${id}`) }
          ElMessage.success('批量删除成功'); this.loadOrders()
        }).catch(()=>{})
    },
    async handleBatchShip() {
      this.shipForm = { logisticsCompany: '顺丰速运', logisticsNo: '' }
      this.shipDialogVisible = true
    }
  }
}
</script>

<style scoped>
.order-manage-container { padding: 0; }
.main-card { border-radius: 12px; }
.card-header { display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 12px; }
.header-left { display: flex; align-items: center; gap: 8px; font-size: 16px; font-weight: 600; color: #303133; }
.header-right { display: flex; align-items: center; gap: 8px; flex-wrap: wrap; }
.title-icon { color: #ea580c; font-size: 20px; }

.table-footer { display: flex; justify-content: space-between; align-items: center; margin-top: 20px; padding-top: 16px; border-top: 1px solid #f0f0f0; flex-wrap: wrap; gap: 12px; }
.batch-actions { display: flex; gap: 8px; }
</style>
