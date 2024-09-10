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
      <table>  
        <thead>  
          <tr>  
            <th>时间戳</th>  
            <th>消息</th>  
          </tr>  
        </thead>  
        <tbody>  
          <tr v-for="log in logs" :key="log.timestamp">  
            <td>{{ log.timestamp }}</td>  
            <td>{{ log.message }}</td>  
          </tr>  
        </tbody>  
      </table>  
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
  border: 1px solid #D2B48C;  
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);  
  width: 80%;  
  max-width: 600px;  
  background-color: #F0E6D2;  
}  
  
.close {  
  color: #D2B48C;  
  float: right;  
  font-size: 30px;  
  font-weight: bold;  
  cursor: pointer;  
  transition: color 0.3s;  
}  
  
.close:hover,  
.close:focus {  
  color: #A08050;  
  text-decoration: none;  
}  
  
pre {  
  white-space: pre-wrap;  
  word-wrap: break-word;  
  font-family: monospace;  
  font-size: 14px;  
  color: #333;  
  background-color: #F5F5DC;  
  padding: 10px;  
  border-radius: 4px;  
  border: 1px solid #D2B48C;  
}  
  
.el-button--primary,  
.el-button--warning,  
.el-button--success {  
  background-color: #D2B48C;  
  border-color: #D2B48C;  
  color: white;  
  padding: 10px 20px;  
  border-radius: 4px;  
  transition: background-color 0.3s, border-color 0.3s;  
}  
  
.el-button--primary:hover,  
.el-button--primary:focus,  
.el-button--warning:hover,  
.el-button--warning:focus,  
.el-button--success:hover,  
.el-button--success:focus {  
  background-color: #A08050;  
  border-color: #A08050;  
} 
table {  
  width: 100%;  
  border-collapse: collapse;  
  margin-bottom: 20px;  
}  
  
th, td {  
  padding: 10px;  
  border: 1px solid #D2B48C;  
  text-align: left;  
  font-size: 14px;  
}  
  
th {  
  background-color: #F0E6D2;  
}  
  
tr:nth-child(even) {  
  background-color: #F9F2E7;  
}
</style>