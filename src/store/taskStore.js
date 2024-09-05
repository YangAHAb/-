import { defineStore } from 'pinia';  
import LogService from '../service/logservice'; // 确保路径正确  
  
export const useTaskStore = defineStore('taskStore', {  
  state: () => ({  
    tasks: [],  
  }),  
  actions: {    
    addTask({ FILENAME, STATUS }) {  
      const existingTask = this.tasks.find(task => task.name === FILENAME && task.state === '上传失败');  
      if (!existingTask) {  
        const newTask = { id: this.tasks.length + 1, name: FILENAME, state: STATUS };  
        this.tasks.push(newTask);  
        LogService.logToLocalStorage(`Added task: ${FILENAME} with status ${STATUS}`);  
      }  
    },  
    removeTask(taskId) {  
      const task = this.tasks.find(task => task.id === taskId);  
      this.tasks = this.tasks.filter(task => task.id !== taskId);  
      if (task) {  
        LogService.logToLocalStorage(`Removed task: ${task.name} with id ${taskId}`);  
      }  
    },  
    editTask(taskId, newName, newState) {  
      const task = this.tasks.find(task => task.id === taskId);  
      if (task) {  
        task.name = newName;  
        task.state = newState;  
        LogService.logToLocalStorage(`Edited task: ${taskId} - New name: ${newName}, New state: ${newState}`);  
      }  
    },  
    clearTasks() {  
      const clearedTasks = [...this.tasks]; // 复制当前任务列表以便在日志中记录  
      this.tasks = [];  
      LogService.logToLocalStorage(`Cleared all tasks. Cleared tasks: ${JSON.stringify(clearedTasks)}`);  
    },  
    getTaskById(id) {  
      return this.tasks.find(task => task.id === id);  
    },  
    updateTaskStatusByName(name, status) {  
      this.tasks = this.tasks.map(task => {  
        if (task.name === name) {  
          LogService.logToLocalStorage(`Updated status of task ${name} to ${status}`);  
          return { ...task, state: status };  
        }  
        return task;  
      });  
    },  
    markAllTasksAsMasked() {  
      this.tasks = this.tasks.map(task => {  
        LogService.logToLocalStorage(`Masked task: ${task.name}`);  
        return { ...task, state: '已脱敏' };  
      });  
    },  
  },  
});