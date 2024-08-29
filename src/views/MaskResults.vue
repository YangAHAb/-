<script setup>
import { ref } from 'vue';
import { downloadFile } from '@/service/request';

const userId = 123;
const taskId = 456;

const handleMaskDownload = async () => {
    const result = await downloadFile('/download', {
        user_id: userId,
        task_id: taskId
    })
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
</template>

<style scoped>
</style>