<template>
    <div>
        <!-- 测试 Spring Boot 后端的 API -->
        <button @click="getMyHandle">发送封装的 GET 请求</button>
        <button @click="postMyHandle">发送封装的 POST 请求</button>
    </div>
</template>

<script setup>
import { get, post } from '@/service/request';

// 处理 GET 请求
async function getMyHandle() {
    try {
        const response = await get(
            '/hello',
        );
        console.log('GET 请求成功:', response.data);
    } catch (error) {
        console.error('GET 请求失败:', error);
    }
}

// 登录：api/login



</script>

<style lang="scss" scoped></style>
