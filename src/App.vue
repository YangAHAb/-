<script setup>
import MenuBar from './MenuBar.vue';
import { onMounted, ref } from 'vue'
import router from './router/router';

onMounted(() => {
    router.push("/upload")
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
    padding: 5px;
    background-color: #282c34;
    /*背景*/
}

.logo-container {
    display: flex;
    align-items: center;
}

.logo {
    height: 60px;
    margin-right: 20px;
    /* logo和网站名称之间的间距 */
}

.site-name {
    color: white;
    font-size: 24px;
    font-weight: bold;
    margin: 0;
    margin-left: 10px;
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