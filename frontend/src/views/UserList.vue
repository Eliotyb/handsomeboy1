<template>
  <div class="user-list-container">
    <el-card class="list-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <div class="header-title">
            <el-icon class="title-icon"><User /></el-icon>
            <span>用户管理</span>
          </div>
          <el-button type="primary" @click="handleAdd" :icon="Plus">
            添加用户
          </el-button>
        </div>
      </template>
      <el-table :data="users" style="width: 100%" stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="username" label="用户名" width="120"></el-table-column>
        <el-table-column prop="name" label="姓名" width="100"></el-table-column>
        <el-table-column prop="phone" label="电话" width="130"></el-table-column>
        <el-table-column prop="address" label="地址"></el-table-column>
        <el-table-column prop="role" label="角色" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.role === 1 ? 'success' : 'info'" size="small">
              {{ scope.row.role === 0 ? '普通用户' : '管理员' }}
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

    <!-- 添加/编辑用户对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="520px"
      :close-on-click-modal="false"
    >
      <el-form :model="form" label-width="80px" :rules="rules" ref="formRef">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!form.id">
          <el-input v-model="form.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入电话"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" type="textarea" placeholder="请输入地址"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="普通用户" :value="0"></el-option>
            <el-option label="管理员" :value="1"></el-option>
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
import { User, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'UserList',
  components: {
    User,
    Plus,
    Edit,
    Delete
  },
  data() {
    return {
      users: [],
      loading: false,
      submitLoading: false,
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: '',
        username: '',
        password: '',
        name: '',
        phone: '',
        address: '',
        role: 0
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入电话', trigger: 'blur' }],
        address: [{ required: true, message: '请输入地址', trigger: 'blur' }],
        role: [{ required: true, message: '请选择角色', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.loadUsers()
  },
  methods: {
    loadUsers() {
      this.loading = true
      axios.get('/api/users')
        .then(response => {
          const res = response.data
          if (res.code === 200) {
            this.users = res.data
          } else {
            ElMessage.error(res.message || '加载用户失败')
          }
        })
        .catch(error => {
          console.error('加载用户失败:', error)
          ElMessage.error('加载用户失败，请检查后端服务是否运行')
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleAdd() {
      this.dialogTitle = '添加用户'
      this.form = {
        id: '',
        username: '',
        password: '',
        name: '',
        phone: '',
        address: '',
        role: 0
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑用户'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleSave() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          const request = this.form.id 
            ? axios.put(`/api/users/${this.form.id}`, this.form)
            : axios.post('/api/users', this.form)
          
          request
            .then(response => {
              const res = response.data
              if (res.code === 200) {
                this.loadUsers()
                this.dialogVisible = false
                ElMessage.success(this.form.id ? '编辑用户成功' : '添加用户成功')
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
      ElMessageBox.confirm('确定要删除这个用户吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.delete(`/api/users/${id}`)
          .then(response => {
            const res = response.data
            if (res.code === 200) {
              this.loadUsers()
              ElMessage.success('删除用户成功')
            } else {
              ElMessage.error(res.message || '删除失败')
            }
          })
          .catch(error => {
            console.error('删除用户失败:', error)
            ElMessage.error('删除用户失败，请检查后端服务是否运行')
          })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.user-list-container {
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
  color: #409EFF;
  font-size: 20px;
}
</style>