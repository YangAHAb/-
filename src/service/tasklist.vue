<script>
  import { useTaskStore } from '../store/taskStore';  
  import { computed } from 'vue';   
  import { defineProps } from 'vue';  
 
const props = defineProps({  
    tasks: Array  
});  
  export default {  
    props: {  
      tasks: Array,  
      isVisible: Boolean  
    },  
    data() {  
      return {  
        dragPosition: { x: 900, y: 300 }, // 初始位置  
        dragging: false,  
        startX: 0,  
        startY: 0  
      };  
    },
    setup() {  
      const store = useTaskStore();  
      const tasksFromStore = computed(() => store.tasks);    
      return {  
        tasksFromStore,   
      };  
    },    
    methods: { 
      reupload(task) {  
 
 this.$emit('reupload', task);  
},   
      close() {  
        this.$emit('update:isVisible', false);  
      }, 
      initDrag(event) {  
        this.startX = event.clientX - this.dragPosition.x;  
        this.startY = event.clientY - this.dragPosition.y;  
        this.dragging = true;  
    
        document.addEventListener('mousemove', this.doDrag);  
        document.addEventListener('mouseup', this.stopDrag);  
      },  
      doDrag(event) {  
        if (!this.dragging) return;  
    
        this.dragPosition.x = event.clientX - this.startX;  
        this.dragPosition.y = event.clientY - this.startY;  
      },  
      stopDrag() {  
        document.removeEventListener('mousemove', this.doDrag);  
        document.removeEventListener('mouseup', this.stopDrag);  
        this.dragging = false;  
     
        this.constrainPosition();  
      },  
      constrainPosition() {  
        const rect = document.body.getBoundingClientRect();  
        const dialogRect = this.$el.getBoundingClientRect();  
    
        if (this.dragPosition.x < 0) this.dragPosition.x = 0;  
        if (this.dragPosition.y < 0) this.dragPosition.y = 0;  
        if (this.dragPosition.x + dialogRect.width > rect.width) {  
          this.dragPosition.x = rect.width - dialogRect.width;  
        }  
        if (this.dragPosition.y + dialogRect.height > rect.height) {  
          this.dragPosition.y = rect.height - dialogRect.height;  
        }  
      }  
    }  
  };
  </script>  
  

<template>  
  <div  
    v-if="isVisible"  
    class="floating-task-list"  
    @mousedown="initDrag"  
    :style="{ top: dragPosition.y + 'px', left: dragPosition.x + 'px' }"  
  >  
    <h3 class="title">任务列表</h3>  
    <table class="task-table">  
      <thead>  
        <tr>  
          <th>任务名称</th>  
          <th>任务状态</th>  
          <th></th>  
        </tr>  
      </thead>  
      <tbody>  
        <tr v-if="tasksFromStore.length === 0">  
          <td colspan="3" class="no-tasks">暂无任务</td>  
        </tr>  
        <tr v-for="task in tasksFromStore" :key="task.id" class="task-item">  
          <td :class="`task-state-${task.state}`">{{ task.name }}</td>  
          <td>{{ task.state }}</td>  
          <td>  
            <button v-if="task.state === '上传失败'" @click="reupload(task)" class="reupload-button">重新上传</button>  
          </td>  
        </tr>  
      </tbody>  
    </table>  
    <button class="close-button" @click="close">关闭</button>  
  </div>  
</template>  
  
<style scoped>  
  .floating-task-list {  
    position: fixed;  
    width: 350px;  
    background: linear-gradient(135deg, #f5f7fa, #c3cfe2);  
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);  
    padding: 20px;  
    border-radius: 10px;  
    z-index: 1000;  
    cursor: move;  
  }  
  
  .title {  
    font-size: 24px;  
    font-weight: bold;  
    color: #333;  
    margin-bottom: 15px;  
  }  
  
  .task-table {  
    width: 100%;  
    border-collapse: collapse;  
  }  
  
  .task-table th, .task-table td {  
    padding: 10px;  
    text-align: left;  
    border-bottom: 1px solid #ddd;  
  }  
  
  .task-table thead th {  
    background-color: #eaeff5;  
    color: #333;  
  }  
  
  .no-tasks {  
    text-align: center;  
    padding: 20px;  
    color: #999;  
  }  
  
  .task-item {  
    transition: background-color 0.3s;  
  }  
  
  .task-item:hover {  
    background-color: #f9f9f9;  
  }  
  
  .task-state-active {  
    color: #4caf50;  
  }  
  
  .task-state-pending {  
    color: #ff9800;  
  }  
  
  .task-state-completed {  
    color: #9e9e9e;  
    text-decoration: line-through;  
  }  
  
  .reupload-button {  
    background-color: #a1f9a4;  
    color: white;  
    border: none;  
    border-radius: 5px;  
    padding: 5px 10px;  
    cursor: pointer;  
    transition: background-color 0.3s;  
  }  
  
  .reupload-button:hover {  
    background-color: #349939;  
  }  
  
  .close-button {  
    background-color: #ab3f38;  
    color: white;  
    border: none;  
    border-radius: 5px;  
    padding: 10px 20px;  
    cursor: pointer;  
    float: right;  
    transition: background-color 0.3s;  
  }  
  
  .close-button:hover {  
    background-color: #833736;  
  } 

</style>