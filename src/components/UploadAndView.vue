<script setup lang="ts">  
import { ref } from 'vue';  
import { genFileId } from 'element-plus';  
import type { UploadInstance, UploadProps, UploadRawFile } from 'element-plus';  
import { UploadFilled } from '@element-plus/icons-vue';  
  
const fileContent = ref(null);  
const imgSrc = ref(null);  
const uploadUrl = ref('http://localhost:3000/upload');  /* 后端端口 */
const upload = ref<UploadInstance>();  
  
const handleChange = (file: any) => {  
    const reader = new FileReader();  
    reader.onload = (e) => {  
        imgSrc.value = e.target.result;  
    };  
    reader.readAsDataURL(file.raw);  
    fileContent.value = file.raw;  
};  
  
const handleRemove: UploadProps['onRemove'] = (file, uploadFiles) => {  
    fileContent.value = null;  
    imgSrc.value = null;  
    console.log('remove');  
};  
  
const handleExceed: UploadProps['onExceed'] = (files) => {  
    upload.value!.clearFiles();  
    const file = files[0] as UploadRawFile;  
    file.uid = genFileId();  
    upload.value!.handleStart(file);  
};  
  
const submitUpload = async () => {  
    try {  
        if (!fileContent.value) {  
            throw new Error('No File!');  
        }  
        const formData = new FormData();  
        formData.append('file', fileContent.value);  
  
        const response = await fetch(uploadUrl.value, {  
            method: 'POST',  
            body: formData,  
        });  
  
        if (!response.ok) {  
            throw new Error(`HTTP error! Status: ${response.status}`);  
        }  
        const data = await response.json();  
        console.log('文件上传成功', data);  
    } catch (error) {  
        console.error('文件上传失败', error);  
    }  
};  
</script>  
  
<template>  
    <el-row class="upload-container">  
        <el-upload  
            ref="upload"  
            class="upload-db"  
            drag  
            :action="uploadUrl"  
            accept=".db, image/*"  
            :auto-upload="false"  
            :limit="1"  
            :on-exceed="handleExceed"  
            :on-change="handleChange"  
            :on-remove="handleRemove"  
        >  
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>  
            <div class="el-upload__text">  
                拖拽文件至此或<em>点击</em>以上传  
            </div>  
            <template #tip>  
                <div class="el-upload__tip">  
                    仅支持db文件, 最多一个文件，新文件会覆盖旧文件  
                </div>  
            </template>  
        </el-upload>  
        <el-button class="upload-button" type="success" @click="submitUpload">  
            上传至平台  
        </el-button>  
    </el-row>  
    <el-row class="image-container">  
        <div class="image-wrapper" v-if="imgSrc">  
            <img class="displayImage" :src="imgSrc" />  
        </div>  
        <div class="no-file" v-else>  
            暂无文件  
        </div>  
    </el-row>   
</template>  
  
<style scoped>  
.upload-container {  
    display: flex;  
    align-items: center;  
    justify-content: space-between;  
    padding: 30px;  
    background-color: #f5f5f5; /* 背景色 */  
    border-radius: 8px;   
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05); /* 阴影 */  
}  
  
.el-upload__tip{
    margin-left: 10px;
}
.upload-db {  
    flex: 1;  
    border: 2px dashed #909399; /* 边框色 */  
    border-radius: 8px;  
    cursor: pointer;  
    position: relative;  
    overflow: hidden;  
    background-color: #ffffff; /* 背景色 */  
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05); /* 阴影 */  
    transition: all 0.3s; /* 过渡动画 */  
}  
  
.upload-db:hover {  
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);  
}  
  
.upload-button {  
    background-color: #409EFF; /* 按钮背景色 */  
    color: #ffffff; /* 按钮文字色 */  
    padding: 20px 50px;  
    border-radius: 6px; 
    margin-left: 20px; 
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 阴影 */  
    transition: all 0.3s; /* 过渡动画 */

}  
  
.upload-button:hover {  
    background-color: #66b1ff;
}  
  
.image-container {  
    display: flex;  
    justify-content: center;  
    align-items: center;  
    margin-top: 20px;  
}  
  
.image-wrapper {  
    border: 2px solid #e4e7ed;
    border-radius: 8px;  
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    padding: 10px;  
    transition: all 0.3s; /* 过渡动画 */  
}  
  
.image-wrapper:hover {  
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1); 
}  
  
.displayImage {  
    width: 100%;  
    border-radius: 6px;  
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05); /* 阴影 */  
}  
  
.no-file {  
    font-size: 16px;  
    color: #999;  
    text-align: center;  
    padding: 20px;
}  
</style>