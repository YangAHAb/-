<script setup>  
import MenuBar from './MenuBar.vue';  
import tasklist from './tasklist.vue';  
import { onMounted, ref } from 'vue'  
import router from './router/router';  
  
onMounted(() => {  
    router.push("/login")  
})  
</script>  
  
<template>   
    <div class="main-layout">  
        <el-container style="height: 100vh">  
            <!-- LOGO 网站名称 菜单 -->  
            <div class="logo-container">  
                <img src="/logo.png" alt="Logo" class="logo">  
                <h1 class="site-name">数据脱敏管理平台</h1>  
            </div>  
              
            <el-main>  
                <RouterView />  
                <tasklist  v-if="$route.meta.showTasklist" :tasks="tasksFromStore"    
    :isVisible="isTaskListVisible"    
    @update:isVisible="isTaskListVisible = $event"    
    @reupload="handleReupload" />  
            </el-main>  
            <el-footer>隐私数据脱敏平台 脱敏组</el-footer>  
        </el-container>  
    </div>  
</template>  
  
<style scoped>    
.main-layout {    
    display: flex;  
    flex-direction: column;  
    height: 100vh;  
    background-image: url('/images/back.png');  
    background-size: cover; 
    background-position: center;
    background-repeat: no-repeat; 
}  
  
.logo-container {    
    display: flex;    
    align-items: center;    
    justify-content: flex-start;    
    padding: 5px;    
    background-color: #D3C6A6; /* 浅卡其色背景 */    
}  
  
.logo {    
    height: 60px;    
    margin-right: 20px;    
}  
  
.site-name {    
    color: #796C5A; /* 深卡其色文本 */    
    font-size: 24px;    
    font-weight: bold;    
    margin: 0;    
    margin-left: 10px;    
}  
  
/* footer样式 */    
.el-footer {    
    background-color: #A89F83; /* 中卡其色背景 */    
    color: #F5F1E5; /* 奶油色文本 */    
    font-size: 14px;    
    text-align: center;    
    padding: 10px 0;    
    margin-top: 20px;    
    border-top: 1px solid #796C5A; /* 深卡其色边框 */    
}  
  
.RouterView {    
    height: 100%;    
    background-color: #D3C6A6; /* 浅卡其色背景，可能需要根据实际情况调整是否需要 */    
}  
  
/* 可选：为按钮添加卡其色样式 */    
.button-highlight {    
    background-color: #796C5A; /* 深卡其色背景 */    
    color: #F5F1E5; /* 奶油色文本 */    
    border: none;    
    padding: 10px;    
    cursor: pointer;    
}  
  
/* 可选：为警告或错误提示添加样式，这里使用深卡其色的变种 */    
.warning-text {    
    color: #5D5347; /* 更深的卡其色调 */    
}    
</style>