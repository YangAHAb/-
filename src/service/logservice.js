class LogService {  
    constructor() {  
      this.logs = [];  
    }  
    
    loadLogs() {  
      this.logs = JSON.parse(localStorage.getItem('logs') || '[]');  
    }  
    
    logToLocalStorage(message) {  
      this.loadLogs();  
      this.logs.push({ timestamp: new Date().toISOString(), message });  
      localStorage.setItem('logs', JSON.stringify(this.logs));  
    }  
    
    exportLogs() {  
      const logsToExport = this.logs.map(log => `${log.timestamp}: ${log.message}`).join('\n');  
      const blob = new Blob([logsToExport], { type: 'text/plain;charset=utf-8;' });  
      const anchor = document.createElement('a');  
      anchor.href = URL.createObjectURL(blob);  
      anchor.download = 'logs.txt';  
      anchor.click();  
      URL.revokeObjectURL(anchor.href);  
    }  
    
    getLogs() {  
      this.loadLogs();  
      return this.logs;  
    }  
  }  
    
  export default new LogService();



  /*import LogService from './logService';  
  
export default {  
    methods: {    
    recordAction(action, detail = '') {  
      const message = `${new Date().toISOString()}: ${action} - ${detail}`;  
      LogService.logToLocalStorage(message);  
    },  
  
    previewLogs() {  
      this.showLogsModal = !this.showLogsModal;  
      this.logs = LogService.getLogs(); // 获取日志以显示  
    },  
  
    exportLogs() {  
      LogService.exportLogs(); // 导出日志  
    }  
  }  
};*/ 