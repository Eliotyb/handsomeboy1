<template>
  <div class="admin-dashboard">
    <aside class="sidebar">
      <div class="sidebar-header"><div class="brand"><div class="brand-name">Pulse Admin</div><div class="brand-tag">数据管理</div></div></div>
      <nav class="sidebar-nav">
        <div v-for="t in tables" :key="t.name" class="nav-item" :class="{ active: activeTable === t.name }" @click="switchTable(t.name)">
          <el-icon class="nav-icon"><Grid /></el-icon><span>{{ t.label }}</span>
        </div>
      </nav>
      <div class="sidebar-footer">
        <el-button class="back-btn" @click="$router.push('/dashboard')">← 返回仪表盘</el-button>
      </div>
    </aside>

    <main class="main-content">
      <header class="top-header">
        <div class="header-search"><el-icon class="search-icon"><Search /></el-icon><input type="text" placeholder="搜索记录..." class="search-input" v-model="searchKey" @keyup.enter="loadData" /></div>
        <div class="header-actions">
          <el-button type="primary" size="small" @click="showAddDialog" :disabled="!activeTable">+ 新增</el-button>
          <el-button size="small" @click="loadData" :loading="loading">刷新</el-button>
        </div>
      </header>

      <div class="content-area">
        <div class="table-info-bar">
          <h2>{{ currentLabel }} <span class="record-count">({{ tableData.length }} 条)</span></h2>
          <div class="bar-actions">
            <el-button size="small" type="danger" plain :disabled="selectedRows.length === 0" @click="handleBatchDelete">删除选中 ({{ selectedRows.length }})</el-button>
            <el-button size="small" plain @click="exportCSV">导出CSV</el-button>
          </div>
        </div>

        <div class="data-table-wrapper" v-loading="loading">
          <el-table :data="paginatedData" border stripe max-height="calc(100vh - 280px)" 
            @selection-change="handleSelectionChange" empty-text="暂无数据"
            :header-cell-style="{ background:'#fff7ed', color:'#c2410c', fontWeight:'600' }">
            <el-table-column type="selection" width="45" />
            <el-table-column v-for="col in columns" :key="col" :prop="toCamel(col)" :label="col" show-overflow-tooltip min-width="120">
              <template #default="{ row }">
                <span v-if="isDateValue(row[toCamel(col)])">{{ formatDate(row[toCamel(col)]) }}</span>
                <span v-else-if="isBoolValue(row[toCamel(col)])">{{ row[toCamel(col)] ? '是' : '否' }}</span>
                <span v-else>{{ formatCell(row[toCamel(col)]) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="160" fixed="right" align="center">
              <template #default="{ row, $index }">
                <el-button size="small" link type="primary" @click="handleEdit(row)">编辑</el-button>
                <el-button size="small" link type="danger" @click="handleDelete(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="pagination-bar">
          <el-pagination background layout="total, sizes, prev, pager, next" :total="tableData.length"
            :page-sizes="[10, 20, 50]" :page-size="pageSize" :current-page="currentPage"
            @size-change="ps => { pageSize = ps; currentPage = 1 }" @current-change="cp => currentPage = cp" />
        </div>
      </div>
    </main>

    <!-- 编辑/新增对话框 -->
    <el-dialog :title="dialogTitle" v-model="editDialogVisible" width="560px" destroy-on-close>
      <el-form :model="editForm" label-width="130px" label-position="right">
        <el-form-item v-for="col in editableColumns" :key="col" :label="col">
          <el-input v-if="!isDateColumn(col) && !isIdColumn(col) && col !== 'password'" v-model="editForm[toCamel(col)]" />
          <el-input v-else-if="col === 'password'" v-model="editForm[toCamel(col)]" type="password" show-password />
          <el-date-picker v-else-if="isDateColumn(col)" v-model="editForm[toCamel(col)]" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%;" />
          <span v-else style="color:#999;">{{ editForm[toCamel(col)] || '-' }} (不可编辑)</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
import { Grid, Search } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'DbManager',
  components: { Grid, Search },
  data() {
    return {
      tables: [
        { name: 'user', label: '用户管理' }, { name: 'product', label: '商品管理' },
        { name: 'seckill_activity', label: '秒杀活动' }, { name: 'seckill_product', label: '秒杀商品' },
        { name: '`order`', label: '订单管理' }, { name: 'cart', label: '购物车' },
        { name: 'coupon', label: '优惠券' }, { name: 'user_coupon', label: '用户优惠券' },
        { name: 'order_logistics', label: '物流信息' }, { name: 'order_refund', label: '售后退款' },
        { name: 'order_review', label: '评价管理' }
      ],
      activeTable: '', currentLabel: '',
      columns: [], tableData: [], loading: false,
      searchKey: '', selectedRows: [], currentPage: 1, pageSize: 10,
      editDialogVisible: false, dialogTitle: '新增记录', editForm: {}, saving: false, isEdit: false
    }
  },
  computed: {
    paginatedData() {
      const start = (this.currentPage - 1) * this.pageSize
      let data = this.tableData
      if (this.searchKey) {
        const key = this.searchKey.toLowerCase()
        data = data.filter(r => JSON.stringify(r).toLowerCase().includes(key))
      }
      return data.slice(start, start + this.pageSize)
    },
    editableColumns() {
      return this.columns.filter(c => !this.isAutoColumn(c) && c !== 'id')
    }
  },
  mounted() { this.switchTable('user') },
  methods: {
    async switchTable(name) {
      this.activeTable = name
      this.currentLabel = this.tables.find(t => t.name === name)?.label || name
      this.currentPage = 1
      this.searchKey = ''
      await this.loadColumns()
      await this.loadData()
    },

    async loadColumns() {
      try {
        const res = await axios.get(`/api/tables/${this.activeTable}/columns`)
        if (res.data.code === 200) this.columns = res.data.data || []
        else this.columns = this.getDefaultColumns(this.activeTable)
      } catch (e) { this.columns = this.getDefaultColumns(this.activeTable) }
    },

    getDefaultColumns(name) {
      const map = {
        user: ['id','username','password','name','phone','address','role'],
        product: ['id','name','description','price','stock','image','status'],
        seckill_activity: ['id','name','start_time','end_time','status'],
        seckill_product: ['id','activity_id','product_id','seckill_price','seckill_stock','sold_count'],
        order: ['id','order_no','user_id','product_id','price','quantity','total_price','status','create_time'],
        cart: ['id','user_id','product_id','quantity','create_time'],
        coupon: ['id','title','description','coupon_type','value','min_amount','status'],
        user_coupon: ['id','user_id','coupon_id','status','receive_time'],
        order_logistics: ['id','order_id','logistics_company','logistics_no','status','current_location'],
        order_refund: ['id','order_id','refund_no','reason','refund_amount','status'],
        order_review: ['id','order_id','product_id','rating','content','status']
      }
      return map[name] || ['id']
    },

    async loadData() {
      this.loading = true
      try {
        const apiMap = { user:'/api/users', product:'/api/products', seckill_activity:'/api/seckill-activities',
          seckill_product:'/api/seckill-products', order:'/api/orders', cart:'/api/cart/list',
          coupon:'/api/coupons', user_coupon:'/api/coupons/user', order_logistics:'/api/logistics/order/1',
          order_refund:'/api/refunds/list', order_review:'/api/reviews/list' }
        let url = apiMap[this.activeTable] || `/api/${this.activeTable}`
        const res = await axios.get(url)
        if (res.data.code === 200) {
          const d = res.data.data
          this.tableData = Array.isArray(d) ? d : (d.records || [])
        }
      } catch (e) { console.error('加载失败:', e); this.tableData = [] }
      finally { this.loading = false }
    },

    toCamel(s) { return s.replace(/_([a-z])/g, (_, c) => c.toUpperCase()) },
    isDateColumn(c) { return /time|date/i.test(c) },
    isIdColumn(c) { return c === 'id' },
    isAutoColumn(c) { return /create_time|update_time|id/.test(c) },
    isDateValue(v) { return v instanceof Date || (typeof v === 'string' && /^\d{4}-\d{2}/.test(v)) },
    isBoolValue(v) { return typeof v === 'boolean' },
    formatDate(v) { if (!v) return '-'; return new Date(v).toLocaleString('zh-CN') },
    formatCell(v) { return v !== null && v !== undefined ? String(v) : '-' },

    handleSelectionChange(rows) { this.selectedRows = rows },

    showAddDialog() {
      this.editForm = {}
      this.editableColumns.forEach(c => { this.editForm[this.toCamel(c)] = '' })
      this.dialogTitle = `新增${this.currentLabel.replace('管理','')}`; this.isEdit = false; this.editDialogVisible = true
    },

    handleEdit(row) {
      this.editForm = { ...row }; this.dialogTitle = `编辑 ${this.currentLabel.replace('管理','')}`; this.isEdit = true; this.editDialogVisible = true
    },

    async saveEdit() {
      this.saving = true
      try {
        if (this.isEdit) {
          const apiMap = { user:`/api/users/${this.editForm.id}`, product:`/api/products/${this.editForm.id}`,
            order:`/api/orders/${this.editForm.id}`, coupon:`/api/coupons/${this.editForm.id}` }
          const url = apiMap[this.activeTable] || `/api/${this.activeTable}/${this.editForm.id}`
          await axios.put(url, this.editForm)
          ElMessage.success('更新成功')
        } else {
          const apiMap = { user:'/api/users/register', product:'/api/products', coupon:'/api/coupons' }
          const url = apiMap[this.activeTable] || `/api/${this.activeTable}`
          await axios.post(url, this.editForm)
          ElMessage.success('创建成功')
        }
        this.editDialogVisible = false; this.loadData()
      } catch(e) { ElMessage.error('操作失败: ' + (e.response?.data?.message || e.message)) }
      finally { this.saving = false }
    },

    handleDelete(row) {
      ElMessageBox.confirm(`确定删除该条${this.currentLabel}记录？`, '确认', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
        .then(async () => {
          const apiMap = { user:`/api/users/${row.id}`, product:`/api/products/${row.id}`, order:`/api/orders/${row.id}` }
          const url = apiMap[this.activeType] || `/api/${this.activeTable}/${row.id}`
          await axios.delete(url)
          ElMessage.success('已删除'); this.loadData()
        }).catch(()=>{})
    },

    async handleBatchDelete() {
      ElMessageBox.confirm(`确定删除选中的 ${this.selectedRows.length} 条记录？`, '批量删除', { type: 'error' })
        .then(async () => {
          for (const r of this.selectedRows) {
            await axios.delete(`/api/${this.activeTable}/${r.id}`)
          }
          ElMessage.success('批量删除成功'); this.loadData()
        }).catch(()=>{})
    },

    exportCSV() {
      const headers = this.columns.join(',')
      const rows = this.tableData.map(r => this.columns.map(c => `"${(r[this.toCamel(c)] ?? '')}"`).join(','))
      const csv = [headers, ...rows].join('\n')
      const blob = new Blob(['\ufeff' + csv], { type: 'text/csv;charset=utf-8;' })
      const a = document.createElement('a'); a.href = URL.createObjectURL(blob)
      a.download = `${this.activeTable}_${new Date().toISOString().slice(0,10)}.csv`; a.click()
      ElMessage.success('导出成功')
    }
  }
}
</script>

<style scoped>
.admin-dashboard { display: flex; min-height: 100vh; background: linear-gradient(135deg, #f8fafc 0%, #fff7f5 100%); }
.sidebar { width: 220px; background: #fff; border-right: 1px solid #f0f0f0; display: flex; flex-direction: column; padding: 24px 16px; overflow-y: auto; flex-shrink: 0; }
.sidebar-header { margin-bottom: 20px; }
.brand-name { font-size: 18px; font-weight: 800; color: #1e293b; }
.brand-tag { font-size: 11px; color: #ea580c; font-weight: 500; text-transform: uppercase; letter-spacing: 1px; }
.sidebar-nav { flex: 1; display: flex; flex-direction: column; gap: 4px; }
.nav-item { display: flex; align-items: center; gap: 10px; padding: 10px 12px; border-radius: 10px; cursor: pointer; font-size: 13px; color: #64748b; transition: all 0.2s; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.nav-item:hover { background: #fff7ed; color: #ea580c; }
.nav-item.active { background: linear-gradient(135deg,#fff7ed 0%,#ffedd5 100%); color: #c2410c; font-weight: 600; border-left: 3px solid #ea580c; }
.nav-icon { font-size: 16px; flex-shrink: 0; }
.sidebar-footer { padding-top: 16px; border-top: 1px solid #f3f4f6; }
.back-btn { width: 100%; height: 40px; border-radius: 20px; border: 1px solid #e5e7eb; background: #fff; color: #666; font-size: 13px; }

.main-content { flex: 1; display: flex; flex-direction: column; overflow: hidden; }
.top-header { height: 64px; background: #fff; border-bottom: 1px solid #f3f4f6; display: flex; align-items: center; justify-content: space-between; padding: 0 28px; flex-shrink: 0; }
.header-search { display: flex; align-items: center; background: #f8fafc; border-radius: 20px; padding: 8px 14px; gap: 8px; width: 280px; }
.search-input { flex: 1; border: none; background: transparent; font-size: 13px; outline: none; }
.header-actions { display: flex; gap: 8px; }

.content-area { flex: 1; overflow-y: auto; padding: 24px 28px; }
.table-info-bar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 18px; flex-wrap: wrap; gap: 12px; }
.table-info-bar h2 { margin: 0; font-size: 20px; font-weight: 700; color: #1e293b; }
.record-count { font-size: 14px; color: #94a3b8; font-weight: 400; }
.bar-actions { display: flex; gap: 8px; }

.data-table-wrapper { background: rgba(255,255,255,0.85); backdrop-filter: blur(10px); border-radius: 16px; padding: 16px; border: 1px solid rgba(255,255,255,0.8); }
.pagination-bar { display: flex; justify-content: flex-end; margin-top: 16px; padding-top: 12px; }
</style>
