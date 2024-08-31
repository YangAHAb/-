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
        <!-- <button @click="uploadFileWithDataHandle">上传文件和其他数据</button>
        <input type="file" ref="fileWithData" /> -->
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { downloadFile, uploadFile} from '@/service/request';

// 引用输入框
const singleFile = ref(null);
const multipleFiles = ref(null);
const fileWithData = ref(null);

const userId = 123;
const taskId = 456;

// 处理文件下载
function downloadFileHandle() {
    downloadFile('/download', {
        user_id: userId,
        task_id: taskId
    }, userId.toString() + "_" + taskId.toString() + "_masked.db").then(() => {
        console.log('文件下载成功');
    }).catch(error => {
        console.error('文件下载失败:', error);
    });
}

// 处理单个文件上传
function uploadSingleFileHandle() {
    const file = singleFile.value?.files[0];
    if (file) {
        uploadFile('/upload', file).then(() => {
            console.log('文件上传成功');
        }).catch(error => {
            console.error('文件上传失败:', error);
        });
    }
}

</script>

<style scoped>
button {
    margin: 5px;
}
</style>
