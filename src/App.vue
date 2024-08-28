<script setup>  
    import { onMounted, ref } from 'vue'  
    import router from './router';  
    const activeIndex = ref('/main')  
    function handleSelect(index) {  
        activeIndex.value = index  
    }  
  
    onMounted(() => {  
        router.push("/main")  
    })  
</script>  
  
<template>  
    <div class="main-layout">  
        <el-container style="height: 100vh">  
            <!-- LOGO 网站名称 菜单 -->  
            <div class="logo-container">  
                    <img src="/public/logo.png" alt="Logo" class="logo">  
                    <h1 class="site-name">数据脱敏管理平台</h1> 
            </div>  
            <el-header class="logo-header">  
                <el-menu  
                    :default-active="activeIndex"  
                    class="main-header-menu"  
                    mode="horizontal"  
                    @select="handleSelect"  
                    router  
                >  
                    <el-menu-item index="/main">首页</el-menu-item>  
                    <el-menu-item index="/upload">数据管理</el-menu-item>  
                    <el-menu-item index="/mask">敏感数据识别</el-menu-item>  
                    <el-menu-item index="/test1">数据脱敏</el-menu-item>
                    <el-menu-item index="/login">账户</el-menu-item> 

                </el-menu>  
            </el-header>  
  
            <el-main>  
                <RouterView />  
            </el-main>  
            <el-footer>隐私数据脱敏平台 脱敏组</el-footer>  
        </el-container>  
    </div>  
</template>  
  
<style scoped> 
.main-layout .logo-container {  
    display: flex;  
    align-items: center;
    justify-content: flex-start;
    padding: 10px;  
    background-color: #282c34; /*背景*/  
}  
  
.logo-container {  
    display: flex;  
    align-items: center;  
}  
  
.logo {  
    height: 60px;  
    margin-right: 20px; /* logo和网站名称之间的间距 */  
}  
  
.site-name {  
    color: white;  
    font-size: 24px;  
    font-weight: bold;  
    margin: 0;  
    margin-left: 10px; 
}  
  
    
/* 菜单项样式 */  
.el-menu-item {  
    font-size: 16px;  
    margin: 0 50px; /* 菜单项之间的间距 */  
    transition: background-color 0.3s, color 0.3s; /* 平滑过渡效果 */  
}  
  
/* 菜单激活样式 */  
.el-menu-item.is-active {  
    background-color: rgba(64, 158, 255, 0.2);
    color: #409eff;
}  
  
/* 菜单悬停样式 */  
.el-menu-item:hover {  
    color: #fff;
}


/* footer样式 */  
.el-footer {  
    /* 背景色*/  
    background-color: #333;
      
    /*文本色*/  
    color: #fff;
      
    /*字体样式 */  
    font-size: 14px;
    text-align: center; 
      
    /*内边距*/  
    padding: 10px 0;
      
    /*底部外边距*/  
    margin-top: 20px;
      
    /* 边框样式*/  
    border-top: 1px solid #444;
}  
</style>