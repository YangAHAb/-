<template>
    <div>
        <button @click="identifyStart">开始识别</button>
        <button @click="maskStart">开始脱敏</button>
    </div>
</template>

<script setup>

import { get, post } from '@/service/request';

const userId = 123;
const taskId = 456;

// 处理识别请求
async function identifyStart() {
    try {
        const response = await get(
            '/identify',
            {
                user_id: userId,
                task_id: taskId
            }
        );
        console.log('识别请求成功:', response.data);
    } catch (error) {
        console.error('识别请求失败:', error);
    }
}

// 处理脱敏请求
async function maskStart() {
    try {
        const response = await get(
            '/mask',
            {
                user_id: userId,
                task_id: taskId
            }
        );
        console.log('脱敏请求成功:', response.data);
    } catch (error) {
        console.error('脱敏请求失败:', error);
    }
}
</script>

<style lang="scss" scoped></style>