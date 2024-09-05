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
<MenuBar />  
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
  background-color: #f5f5f5; /* 背景色 */  
  border-radius: 8px;  
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05); /* 阴影 */  
}  

.downloadbu{
  padding: 5px;

}
.el-input {  
  margin-right: 20px;  
}



</style>