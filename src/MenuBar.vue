<template>
<div class="main-layout"> 
 <el-container style="height: 8vh"> 
    <el-header class="logo-header">  
      <el-menu  
        :default-active="activeIndex"  
        class="main-header-menu"  
        mode="horizontal"  
        @select="handleSelect"  
        router  
      >  
        <el-menu-item index="/main">首页</el-menu-item>  
        <el-menu-item index="/upload">数据文件上传</el-menu-item>  
        <el-menu-item index="/identification">敏感数据识别</el-menu-item>  
        <el-menu-item index="/mask">数据脱敏结果</el-menu-item>  
        <el-menu-item index="/log">账户</el-menu-item>  
      </el-menu>  
    </el-header>
</el-container>
</div>  
  </template>  
    
    <script setup>  
    import { ref, watch } from 'vue';  
    import { useRoute } from 'vue-router';  
    import router from './router/router';  
      
    const route = useRoute();  
    const activeIndex = ref(route.path);  
      
    watch(() => route.path, (newPath) => {  
      activeIndex.value = newPath;  
    });  
      
    function handleSelect(index) {  
      activeIndex.value = index;  
      router.push(index); 
    }  
    </script>
    
    <style scoped>  
    .main-layout .logo-header {  
      background-color: #f3f5f800;  
      padding: 0 20px;  
    }  
      
    .main-header-menu {  
      display: flex;  
      justify-content: space-between;  
      align-items: center;  
      height: 100%;  
    }  
      
    .el-menu-item {  
      font-size: 16px;  
      padding: 0 20px;  
      transition: background-color 0.3s, color 0.3s;  
      border-radius: 5px; /* 添加圆角 */  
    }  
      
    /* 菜单激活样式 */  
    .el-menu-item.is-active {  
      background-color: rgba(64, 158, 255, 0.2);  
      color: #409eff;  
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2); /* 添加阴影 */  
    }  
      
    /* 菜单悬停样式 */  
    .el-menu-item:hover {  
      color: #fff;  
      background-color: rgba(64, 158, 255, 0.1);  
    }  
      

      
    /* 添加媒体查询，以便在小屏幕设备上更好地显示 */  
    @media (max-width: 768px) {  
      .main-header-menu {  
        flex-direction: column;  
      }  
      
      .el-menu-item {  
        margin: 10px 0;  
      }  
    }  
    </style>