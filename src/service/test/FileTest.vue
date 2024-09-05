<template>
    <div>
        <!-- 下载文件 -->
        <button @click="downloadFileHandle">下载文件</button>

        <!-- 上传单个文件 -->
        <button @click="uploadSingleFileHandle">上传单个文件</button>
        <input type="file" ref="singleFile" />

        <!-- 上传多个文件 -->
        <!-- <button @click="uploadMultipleFilesHandle">上传多个文件</button>
        <input type="file" ref="multipleFiles" multiple /> -->

        <!-- 上传文件和其他数据 -->
        <button @click="uploadFileWithDataHandle">上传文件和其他数据</button>
        <input type="file" ref="fileWithData" />
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { downloadFile, uploadFile, uploadFileWithData } from '@/service/request';

// 引用输入框
const singleFile = ref(null);
const multipleFiles = ref(null);
const fileWithData = ref(null);

const userId = 123;
const taskId = 456;

// 处理文件下载
async function downloadFileHandle() {
    try {
        const response = await downloadFile('/download', {
            user_id: userId,
            task_id: taskId
        }, userId.toString() + "_" + taskId.toString() + "_masked.db");

        console.log('downloaded:' + response.data);
    } catch (error) {
        console.error('download_err:', error);
    };
}

// 处理单个文件上传
async function uploadSingleFileHandle() {
    const file = singleFile.value?.files[0];
    if (file) {
        try {
            const response = await uploadFile('/upload', file);
            console.log('uploaded:', response.data);
        } catch (error) {
            console.error('upload_err:', error);
        }
    }
}

// 处理单个文件+用户信息上传
async function uploadFileWithDataHandle() {
    const file = fileWithData.value?.files[0];
    if (file) {
        try {
            const response = await uploadFileWithData('/uploadWithData', file, { "user_id": userId, "task_id": taskId });
            console.log('uploaded:', response.data);
        } catch (error) {
            console.error('upload_err:', error);
        }
    }
}

</script>

<style scoped>
button {
    margin: 5px;
}
</style>
