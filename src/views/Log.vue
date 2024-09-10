<template>  
  <MenuBar class="semi-transparent-menu" />  
  <div class="container">  
    <!-- <MenuBar class="semi-transparent-menu" />   -->
    <div class="user-info-wrapper">  
      <div class="user-info">  
        <p class="welcome-text">欢迎, {{ username }}</p>  
        <button class="logout-button" @click="logout">退出登录</button>  
      </div>  
    </div>  
  </div>  
</template>  
  
<script setup>  
import { useRouter } from 'vue-router';  
import { useUserStore } from '@/store/store'; // 假设你的状态管理库文件在store/userStore.js  
import { computed } from 'vue';
import MenuBar from '../MenuBar.vue';
  
// 获取userStore实例  
const userStore = useUserStore();  
  
// 获取用户名（从状态管理库中）  
const username = computed(() => userStore.getUsername);  
  
// 获取router实例  
const router = useRouter();  
  
// 定义退出登录方法  
const logout = () => {  
  // 清空用户信息  
  userStore.removeInfo();  
  // 跳转到登录页面  
  router.push('/login');  
};  
</script>  
  
<style scoped>  
.container {  
  display: flex;  
  flex-direction: column;  
  align-items: center;  
  justify-content: center;  
  min-height: 100vh;  
  padding: 20px;  
}  
  
.semi-transparent-menu {  
  opacity: 0.8;  
}  
  
.user-info-wrapper {  
  width: 100%;  
  max-width: 400px;  
  padding: 20px;  
  background-color: #fff;  
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);  
  border-radius: 12px;  
  text-align: center;  
}  
  
.welcome-text {  
  font-size: 18px;  
  color: #333;  
  margin-bottom: 16px;  
}  
  
.logout-button {  
  background-color: #8b7359;  
  color: #fff;  
  border: none;  
  padding: 10px 20px;  
  font-size: 16px;  
  border-radius: 6px;  
  cursor: pointer;  
  transition: background-color 0.3s;  
}  
  
.logout-button:hover {  
  background-color: #8B4513;  
}  
</style>