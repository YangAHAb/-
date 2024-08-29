<script setup>
import { ref } from 'vue';
import { downloadFile, get, post } from '@/service/request';

const userId = 123;
const taskId = 456;
const state = ref('')

const handleStartMask = async () => {
    try {
        const response = await get(
            '/mask',
            {
                user_id: userId,
                task_id: taskId
            }
        );
        console.log('脱敏请求成功:', response.data);
        state.value = response.data.status
    } catch (error) {
        console.error('脱敏请求失败:', error);
    }
}

const handleMaskDownload = async () => {
    const result = await downloadFile('/download', {
        user_id: userId,
        task_id: taskId
    },`${userId}_${taskId}_maskedDB.db`)
}
</script>

<template>
    <el-row justify="space-evenly">
        <el-col :span="12">
            <el-button type="primary" @click="handleStartMask">
                    开始脱敏
            </el-button>
        </el-col>
        <el-col :span="12">
            <el-button type="primary" @click="handleMaskDownload">
                    下载结果
            </el-button>
        </el-col>
    </el-row>
    <el-row>
        <p> state: {{ state }} </p>
    </el-row>
</template>

<style scoped>
</style>