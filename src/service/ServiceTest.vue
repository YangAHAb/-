<template>
    <div>
        <!-- 测试 Spring Boot 后端的 API -->
        <button @click="getMyHandle">发送封装的 GET 请求</button>
        <button @click="postMyHandle">发送封装的 POST 请求</button>
    </div>
</template>

<script setup>
import { get, post } from './request';

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

// 处理 POST 请求
async function postMyHandle() {
    try {
        const response = await post(
            '/api/login',  // 假设你有一个 POST API 地址
            {
                privateKey: "your_private_key"
            }
        );
        if (response.data.valid) {
            console.log('Private key is valid');
        } else {
            console.log('Private key is not valid');
        }
    } catch (error) {
        console.error('POST 请求失败:', error);
    }
}
</script>

<style lang="scss" scoped></style>
