<template> 
  <div class="login-register-container">  
    <el-tabs v-model="activeTab" @tab-click="handleTabClick" class="custom-tabs">  
      <el-tab-pane label="登录" name="login">  
        <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" label-width="100px" class="custom-form">  
          <el-form-item label="用户名" prop="username">  
            <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>  
          </el-form-item>  
          <el-form-item label="密码" prop="password">  
            <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"></el-input>  
          </el-form-item>  
          <el-form-item>  
            <el-button type="primary" @click="handleSubmit('loginForm')" class="custom-button">登录</el-button>  
          </el-form-item>  
        </el-form>  
      </el-tab-pane>  
      <el-tab-pane label="注册" name="register">  
        <el-form :model="registerForm" :rules="registerRules" ref="registerFormRef" label-width="100px" class="custom-form">  
          <el-form-item label="用户名" prop="username">  
            <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>  
          </el-form-item>  
          <el-form-item label="密码" prop="password">  
            <el-input v-model="registerForm.password" type="password" placeholder="请输入密码"></el-input>  
          </el-form-item>  
          <el-form-item label="确认密码" prop="confirmPassword">  
            <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码"></el-input>  
          </el-form-item>  
          <el-form-item>  
            <el-button type="primary" @click="handleSubmit('registerForm')" class="custom-button">注册</el-button>  
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
import LogService from '@/service/logservice';

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
    const status = response.data.status;  
    if (status !== 'success') {  
      throw new Error(response.data.message);  
    }  
    store.setUsername(loginForm.username);  
    ElMessage.success('登录成功！');  
    LogService.logToLocalStorage(`用户 ${loginForm.username} 登录成功`); // 添加日志记录  
    router.push('/main');  
  } catch (error) {  
    ElMessage.error('登录失败，请检查用户名和密码');  
    LogService.logToLocalStorage(`用户登录失败: ${error.message}`); // 添加错误日志记录  
  }  
};  
 
// 注册逻辑  
const handleRegister = async () => {  
  try {  
    const response = await post('/register', registerForm);  
    const status = response.data.status;  
    if (status !== 'success') {  
      throw new Error(response.data.message);  
    }  
    ElMessage.success('注册成功！');  
    LogService.logToLocalStorage(`用户 ${registerForm.username} 注册成功`); // 添加日志记录  
    // 清空表单  
    registerForm.username = '';  
    registerForm.email = '';  
    registerForm.password = '';  
    registerForm.confirmPassword = '';  
    activeTab.value = 'login';  
  } catch (error) {  
    ElMessage.error('注册失败，请检查输入的信息');  
    LogService.logToLocalStorage(`用户注册失败: ${error.message}`); // 添加错误日志记录  
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
    
.custom-tabs {    
  width: 450px;    
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);    
  border-radius: 12px;    
  overflow: hidden;    
  background-color: rgba(255, 255, 255, 0.2); /* 设置为白色半透明 */    
}    
    
.custom-tabs /deep/ .el-tabs__header {    
  background-color: #d2b48c;    
  padding: 15px 20px;    
}    
    
.custom-tabs /deep/ .el-tabs__item {    
  font-size: 18px;    
  color: #ffffff;    
  font-weight: 600;    
  margin: 0 15px;    
  transition: color 0.3s;    
}    
    
.custom-tabs /deep/ .el-tabs__item.is-active {    
  color: #393131;    
}    
    
.custom-tabs /deep/ .el-tabs__content {    
  padding: 30px;    
  background-color: #ffffff;    
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);    
}    
    
.custom-form {    
  display: flex;    
  flex-direction: column;    
}    
    
.custom-form .el-form-item {    
  margin-bottom: 25px;    
}    
    
.custom-form .el-input {    
  border-radius: 8px;    
  border: none; /* 移除边框 */  
  box-shadow: none;    
  padding: 0 20px;    
  height: 50px;    
  line-height: 50px;    
  font-size: 16px;    
}    
    
.custom-button {    
  width: 100%;    
  height: 50px;    
  border-radius: 8px;    
  font-size: 18px;    
  background-color: #d2b48c;    
  border-color: #d2b48c;    
  color: #ffffff;    
  transition: all 0.3s;    
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);    
}    
    
.custom-button:hover {    
  background-color: #bcaa86;    
  border-color: #bcaa86;    
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);    
} 

</style>