<template>
  <div class="seckill-activity-list-container">
    <el-card class="list-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <div class="header-title">
            <el-icon class="title-icon"><Timer /></el-icon>
            <span>秒杀活动管理</span>
          </div>
          <el-button type="primary" @click="handleAdd" :icon="Plus">
            添加秒杀活动
          </el-button>
        </div>
      </template>
      <el-table :data="activities" style="width: 100%" stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="活动名称"></el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="200">
          <template #default="scope">
            {{ formatDate(scope.row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间" width="200">
          <template #default="scope">
            {{ formatDate(scope.row.endTime) }}
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

    <!-- 添加/编辑秒杀活动对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="520px"
      :close-on-click-modal="false"
    >
      <el-form :model="form" label-width="90px" :rules="rules" ref="formRef">
        <el-form-item label="活动名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入活动名称"></el-input>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="form.startTime"
            type="datetime"
            placeholder="选择开始时间"
            style="width: 100%"
            value-format="YYYY-MM-DD HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="form.endTime"
            type="datetime"
            placeholder="选择结束时间"
            style="width: 100%"
            value-format="YYYY-MM-DD HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="未开始" :value="0"></el-option>
            <el-option label="进行中" :value="1"></el-option>
            <el-option label="已结束" :value="2"></el-option>
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
import { Timer, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'SeckillActivityList',
  components: {
    Timer,
    Plus,
    Edit,
    Delete
  },
  data() {
    return {
      activities: [],
      loading: false,
      submitLoading: false,
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: '',
        name: '',
        startTime: '',
        endTime: '',
        status: 0
      },
      rules: {
        name: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
        startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
        endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.loadActivities()
  },
  methods: {
    loadActivities() {
      this.loading = true
      axios.get('/api/seckill-activities')
        .then(response => {
          const res = response.data
          if (res.code === 200) {
            this.activities = res.data
          } else {
            ElMessage.error(res.message || '加载秒杀活动失败')
          }
        })
        .catch(error => {
          console.error('加载秒杀活动失败:', error)
          ElMessage.error('加载秒杀活动失败，请检查后端服务是否运行')
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleAdd() {
      this.dialogTitle = '添加秒杀活动'
      this.form = {
        id: '',
        name: '',
        startTime: '',
        endTime: '',
        status: 0
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑秒杀活动'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleSave() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          const request = this.form.id 
            ? axios.put(`/api/seckill-activities/${this.form.id}`, this.form)
            : axios.post('/api/seckill-activities', this.form)
          
          request
            .then(response => {
              const res = response.data
              if (res.code === 200) {
                this.loadActivities()
                this.dialogVisible = false
                ElMessage.success(this.form.id ? '编辑秒杀活动成功' : '添加秒杀活动成功')
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
      ElMessageBox.confirm('确定要删除这个秒杀活动吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.delete(`/api/seckill-activities/${id}`)
          .then(response => {
            const res = response.data
            if (res.code === 200) {
              this.loadActivities()
              ElMessage.success('删除秒杀活动成功')
            } else {
              ElMessage.error(res.message || '删除失败')
            }
          })
          .catch(error => {
            console.error('删除秒杀活动失败:', error)
            ElMessage.error('删除秒杀活动失败，请检查后端服务是否运行')
          })
      }).catch(() => {})
    },
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return d.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
    },
    getStatusText(status) {
      const statusMap = {
        0: '未开始',
        1: '进行中',
        2: '已结束'
      }
      return statusMap[status] || '未知'
    },
    getStatusType(status) {
      const typeMap = {
        0: 'info',
        1: 'success',
        2: 'warning'
      }
      return typeMap[status] || 'info'
    }
  }
}
</script>

<style scoped>
.seckill-activity-list-container {
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
  color: #E6A23C;
  font-size: 20px;
}
</style>
