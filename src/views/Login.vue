<template>
  <!-- <MenuBar /> -->
  <div class="login-register-container">
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="登录" name="login">
        <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" label-width="80px">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSubmit('loginForm')">登录</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="注册" name="register">
        <el-form :model="registerForm" :rules="registerRules" ref="registerFormRef" label-width="80px">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
          </el-form-item>
          <!-- <el-form-item label="邮箱" prop="email">
            <el-input v-model="registerForm.email" placeholder="请输入邮箱"></el-input>
          </el-form-item> -->
          <el-form-item label="密码" prop="password">
            <el-input v-model="registerForm.password" type="password" placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSubmit('registerForm')">注册</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import axios from 'axios';
import router from '@/router/router';
import { store } from '@/main';
import MenuBar from '../MenuBar.vue';
import { post } from '@/service/request';

// 定义表单数据
const loginForm = reactive({
  username: '',
  password: ''
});

const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
});

// 定义活动标签
const activeTab = ref('login');

// 切换标签时处理逻辑
const handleTabClick = (tab) => {
  activeTab.value = tab.name;
};

// 登录表单验证规则
const loginRules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
});

// 注册表单验证规则
const registerRules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: ['blur', 'change'] }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: (rule, value, callback) => {
      if (value !== registerForm.password) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    }, trigger: 'blur' }
  ]
});

// 表单引用
const loginFormRef = ref(null);
const registerFormRef = ref(null);

// 处理表单提交
const handleSubmit = (formName) => {
  const formRef = formName === 'loginForm' ? loginFormRef : registerFormRef;
  formRef.value.validate((valid) => {
    if (valid) {
      if (formName === 'loginForm') {
        handleLogin();
      } else {
        handleRegister();
      }
    } else {
      console.log('表单验证失败');
      return false;
    }
  });
};

// 登录逻辑
const handleLogin = async () => {
  try {
    const response = await post('/login', loginForm);
    // const token = response.data.token; // 假设后端返回的是一个 token
    const status = response.data.status;
    if(status !== 'success') {
        throw new Error(response.data.message)
    }
    store.setUsername(loginForm.username)
    ElMessage.success('登录成功！');
    // 在这里添加跳转到主页或其他逻辑
    router.push('/main')
  } catch (error) {
    ElMessage.error('登录失败，请检查用户名和密码');
  }
};

// 注册逻辑
const handleRegister = async () => {
  try {
    const response = await post('/register', registerForm);
    const status = response.data.status;
    if(status !== 'success') {
        throw new Error(response.data.message)
    }
    ElMessage.success('注册成功！');
    // 清空表单
    registerForm.username = '';
    registerForm.email = '';
    registerForm.password = '';
    registerForm.confirmPassword = '';
    activeTab.value = 'login';
  } catch (error) {
    ElMessage.error('注册失败，请检查输入的信息');
  }
};

onMounted(() => {
  // 初始化时的任何操作
});
</script>

<style scoped>
.login-register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.el-tabs__content {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>