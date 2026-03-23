<template>
  <div class="seckill-activity-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>秒杀活动管理</span>
          <el-button type="primary" @click="handleAdd">添加秒杀活动</el-button>
        </div>
      </template>
      <el-table :data="activities" style="width: 100%">
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
            {{ scope.row.status === 0 ? '未开始' : scope.row.status === 1 ? '进行中' : '已结束' }}
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

    <!-- 添加/编辑秒杀活动对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
    >
      <el-form :model="form" label-width="80px">
        <el-form-item label="活动名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker
            v-model="form.startTime"
            type="datetime"
            placeholder="选择开始时间"
            style="width: 100%"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker
            v-model="form.endTime"
            type="datetime"
            placeholder="选择结束时间"
            style="width: 100%"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="未开始" value="0"></el-option>
            <el-option label="进行中" value="1"></el-option>
            <el-option label="已结束" value="2"></el-option>
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
  name: 'SeckillActivityList',
  data() {
    return {
      activities: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: '',
        name: '',
        startTime: '',
        endTime: '',
        status: 0
      }
    }
  },
  mounted() {
    this.loadActivities()
  },
  methods: {
    loadActivities() {
      axios.get('/api/seckill-activities').then(response => {
        this.activities = response.data
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
      if (this.form.id) {
        // 编辑
        axios.put(`/api/seckill-activities/${this.form.id}`, this.form).then(() => {
          this.loadActivities()
          this.dialogVisible = false
        })
      } else {
        // 添加
        axios.post('/api/seckill-activities', this.form).then(() => {
          this.loadActivities()
          this.dialogVisible = false
        })
      }
    },
    handleDelete(id) {
      this.$confirm('确定要删除这个秒杀活动吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.delete(`/api/seckill-activities/${id}`).then(() => {
          this.loadActivities()
        })
      })
    },
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return d.toLocaleString()
    }
  }
}
</script>

<style scoped>
.seckill-activity-list-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>