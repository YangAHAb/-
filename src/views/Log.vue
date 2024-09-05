<script setup>  
import { reactive, ref } from 'vue';  
import { ElMessage } from 'element-plus';  
import axios from 'axios';  
import router from '@/router/router';  
import { store } from '@/main';  
import MenuBar from '../MenuBar.vue';  
import { post } from '@/service/request';  
import LogService from '@/service/logservice';  
  
// 获取当前用户名  
const currentUsername = store.getUsername();  
  
// 定义账户表单数据  
const accountForm = reactive({  
  username: currentUsername,  
  currentPassword: '',  
  newPassword: '',  
  confirmNewPassword: ''  
});  
  
// 定义账户表单验证规则  
const accountRules = reactive({  
  currentPassword: [  
    { required: true, message: '请输入当前密码', trigger: 'blur' }  
  ],  
  newPassword: [  
    { required: true, message: '请输入新密码', trigger: 'blur' }  
  ],  
  confirmNewPassword: [  
    { required: true, message: '请再次输入新密码', trigger: 'blur' },  
    { validator: (rule, value, callback) => {  
      if (value !== accountForm.newPassword) {  
        callback(new Error('两次输入的新密码不一致'));  
      } else {  
        callback();  
      }  
    }, trigger: 'blur' }  
  ]  
});  
  
// 表单引用  
const accountFormRef = ref(null);  
  
// 处理更新密码  
const handleUpdatePassword = () => {  
  accountFormRef.value.validate(async (valid) => {  
    if (valid) {  
      try {  
        const response = await post('/update-password', {  
          username: accountForm.username,  
          currentPassword: accountForm.currentPassword,  
          newPassword: accountForm.newPassword  
        });  
        const status = response.data.status;  
        if (status !== 'success') {  
          throw new Error(response.data.message);  
        }  
        ElMessage.success('密码更新成功！');  
        LogService.logToLocalStorage(`用户 ${accountForm.username} 更新了密码`);  
         router.push('/login');  
      } catch (error) {  
        ElMessage.error('密码更新失败，请检查输入的信息');  
        LogService.logToLocalStorage(`用户更新密码失败: ${error.message}`);  
      }  
    } else {  
      console.log('表单验证失败');  
      return false;  
    }  
  });  
};  
  
// 处理退出登录  
const handleLogout = () => {  
  // 清除用户信息
  store.logout();  
    
  // 重定向到登录页面  
  router.push('/login');  
    
  // 显示退出登录成功的消息  
  ElMessage.success('成功退出登录');  
};  
</script>  
  
<template>  
  <MenuBar />  
  <div class="account-container">  
    <h2>我的账户</h2>  
    <el-form :model="accountForm" :rules="accountRules" ref="accountFormRef" label-width="120px">  
      <el-form-item label="用户名" prop="username">  
        <el-input v-model="accountForm.username" readonly></el-input>  
      </el-form-item>  
      <el-form-item label="当前密码" prop="currentPassword">  
        <el-input v-model="accountForm.currentPassword" type="password" placeholder="请输入当前密码"></el-input>  
      </el-form-item>  
      <el-form-item label="新密码" prop="newPassword">  
        <el-input v-model="accountForm.newPassword" type="password" placeholder="请输入新密码"></el-input>  
      </el-form-item>  
      <el-form-item label="确认新密码" prop="confirmNewPassword">  
        <el-input v-model="accountForm.confirmNewPassword" type="password" placeholder="请再次输入新密码"></el-input>  
      </el-form-item>  
      <el-form-item>  
        <el-button type="primary" @click="handleUpdatePassword">更新密码</el-button>  
      </el-form-item>  
    </el-form>  
    <el-button type="danger" @click="handleLogout">退出登录</el-button> 
  </div>  
</template>  
  
<style scoped>  
.account-container {  
  display: flex;  
  justify-content: center;  
  align-items: center;  
  height: 100vh;  
  flex-direction: column;  
}  
  
.el-form {  
  width: 400px;  
  padding: 20px;  
  background-color: #fff;  
  border-radius: 4px;  
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);  
}  
  
h2 {  
  margin-bottom: 20px;  
}  
  

.el-button--danger {  
  margin-top: 20px;  
}  
</style>