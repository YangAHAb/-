<script setup lang="ts">
import MenuBar from '../MenuBar.vue';  
import { ref } from 'vue';  
import { downloadFile } from '../service/request'; // 确保路径正确  
import { onMounted } from 'vue';
import tasklist from '../tasklist.vue';  
  
const downloadUrl = ref('http://localhost:3000/download'); // 后端提供下载文件的URL  
const fileList = ref([]); // 文件列表  
const selectedFileName = ref(''); // 要下载的文件名  
  
// 获取文件列表  
const getFileList = async () => {  
  try {  
    const response = await fetch('http://localhost:3000/files');  
    if (!response.ok) {  
      throw new Error(`HTTP error! Status: ${response.status}`);  
    }  
    const fileNames = await response.json();  
    fileList.value = fileNames;  
  } catch (error) {  
    console.error('获取文件列表失败', error);  
  }  
};  
  
// 下载文件  
const downloadSelectedFile = async () => {  
  if (selectedFileName.value) {  
    try {  
      await downloadFile(downloadUrl.value, { fileName: selectedFileName.value }, selectedFileName.value);  
    } catch (error) {  
      console.error('下载文件失败:', error);  
    }  
  } else {  
    alert('请选择要下载的文件！');  
  }  
};  
  
// 获取文件列表  
onMounted(() => {  
  getFileList();  
});  
</script>
  
<template>    
  <MenuBar class="semi-transparent-menu" />   
  <div class="download-container">    
    <el-select v-model="selectedFileName" placeholder="请选择文件">    
      <el-option    
        v-for="file in fileList"    
        :key="file"    
        :label="file"    
        :value="file">    
      </el-option>    
    </el-select>    
    <el-button class="downloadbu" type="success" @click="downloadSelectedFile">下载文件</el-button>    
  </div>    
</template>    
    
<style scoped>    
.download-container {    
  display: flex;    
  align-items: center;    
  justify-content: center;    
  padding: 30px;    
  background-color: transparent;  
  border-radius: 12px;    
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);    
  flex-direction: column;    
  gap: 20px;    
  max-width: 400px;    
  margin: 40px auto 0; /* 增加顶部边距，水平居中 */    
}    
    
.downloadbu {    
  padding: 10px 20px;    
  background-color: #8B4513;    
  border-color: #8B4513;    
  color: white;    
  border-radius: 8px;    
  font-size: 16px;    
  transition: background-color 0.3s, border-color 0.3s;    
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 添加按钮阴影效果 */    
}    
    
.downloadbu:hover {    
  background-color: #6B3E08;    
  border-color: #6B3E08;    
}    
    
.el-select {    
  width: 100%;    
  max-width: 400px;    
  background-color: #F9F9F9; /* 为选择框添加浅色背景 */    
  border-radius: 8px;    
  padding: 10px;    
}    
    
.el-input__inner {    
  border-radius: 8px;    
  padding: 10px;    
  font-size: 16px;    
  border-color: #D2B48C;    
}    
    
.el-input__inner:focus {    
  border-color: #8B4513;    
  box-shadow: 0 0 5px rgba(139, 69, 19, 0.5); /* 添加聚焦时的阴影效果 */    
}    
    
.semi-transparent-menu {    
  opacity: 0.8;    
}    
</style>