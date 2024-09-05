<script setup>  
import tasklist from './tasklist.vue';  
import FloatingTaskList from './service/tasklist.vue';  
import LogService from './service/logservice'; 
import { ref } from 'vue';  
  
const isTaskListVisible = ref(false);  
const showLogsModal = ref(false);  
const logs = ref([]);   
  
const toggleTaskList = () => {  
  isTaskListVisible.value = !isTaskListVisible.value;  
};  
  
const close = () => {  
  isTaskListVisible.value = false;  
};  
  
const previewLogs = () => {  
  showLogsModal.value = true;  
  logs.value = LogService.getLogs();   
};  
  
const exportLogs = () => {  
  LogService.exportLogs();   
};  
  
const closeLogsModal = () => {  
  showLogsModal.value = false;  
};  
</script>  
  
<template>  
  <div style="position: fixed; top: 20px; right: 20px;">  
    <el-button type="primary" @click="toggleTaskList">任务列表</el-button>  
    <el-button type="warning" @click="previewLogs">预览日志</el-button>  
  </div>  
    
  <FloatingTaskList v-if="isTaskListVisible" :tasks="tasks" :isVisible="isTaskListVisible" @update:isVisible="isTaskListVisible = $event" />  
  
  
  <div v-if="showLogsModal" class="modal">  
    <div class="modal-content">  
      <span class="close" @click="closeLogsModal">&times;</span>  
      <h2>日志预览</h2>  
      <pre>{{ logs }}</pre>  
      <el-button type="success" @click="exportLogs">下载日志</el-button> 
    </div>  
  </div>  
</template>  
  
<style>  

.modal {  
  display: flex;  
  position: fixed;  
  z-index: 1000; 
  left: 0;  
  top: 0;  
  width: 100%;  
  height: 100%;  
  overflow: auto;  
  background-color: rgba(0, 0, 0, 0.6); 
}  
  
 
.modal-content {  
  margin: auto;  
  padding: 20px;  
  border-radius: 8px;  
  border: 1px solid #ccc;  
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);  
  width: 80%;  
  max-width: 600px; 
  background-color: white;  
}  
  

.close {  
  color: #333;   
  float: right;  
  font-size: 30px; 
  font-weight: bold;  
  cursor: pointer;  
  transition: color 0.3s;  
}  
  

.close:hover,  
.close:focus {  
  color: red; 
  text-decoration: none;  
}  
  
 
pre {  
  white-space: pre-wrap;  
  word-wrap: break-word;   
  font-family: monospace; 
  font-size: 14px; 
  color: #333; 
  background-color: #f5f5f5; 
  padding: 10px;   
  border-radius: 4px;
  border: 1px solid #ddd; 
}  
  
.el-button--success {  
  background-color: #4caf50;  
  border-color: #4caf50; 
  color: white;
  padding: 10px 20px; 
  border-radius: 4px;
  transition: background-color 0.3s, border-color 0.3s;  
}  
  
 
.el-button--success:hover,  
.el-button--success:focus {  
  background-color: #45a049;   
  border-color: #45a049; 
}  
</style>