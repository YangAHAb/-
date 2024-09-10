<script setup>
import MenuBar from '../MenuBar.vue';
import { downloadFile } from '@/service/request';
import { computed, onMounted, ref, watch } from 'vue';
import { get, post } from '@/service/request';
import { store } from '@/main';
import tasklist from '../tasklist.vue';
import { useTaskStore } from '@/store/taskStore';


import { use } from 'echarts/core';
import { PieChart } from 'echarts/charts';
import {
	TitleComponent,
	TooltipComponent,
	LegendComponent
} from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';
import VChart from 'vue-echarts';

use([
	TitleComponent,
	TooltipComponent,
	LegendComponent,
	PieChart,
	CanvasRenderer
]);


const taskStore = useTaskStore();

const tableData = ref([])
const state = ref(true)
const testData = ref({
    columnNames: { table1: ['a', 'b', 'c', 'd'], table2: ['e', 'f', 'g', 'h'] },
    canMaskColumnNames: { table1: [true, false, true, false], table2: [true, true, true, false] }
})

const showTableData = computed(() => {
    let newTable = [];
    for(let item of tableData.value) {
        newTable.push({
            database: item.database,
            table: item.table,
            column: item.column,
            status: item.status?'敏感':'不敏感',
        })
    }
    return newTable;
})

// 图表相关
const allChartData = computed(() => {
    let data = [
        { value: 0, name: '敏感' },
        { value: 0, name: '不敏感'}
    ];
    for(let item of showTableData.value) {
        for(let e of data) {
            if(item.status === e.name)
                e.value++;
        }
    }
    return data;
})

const tableNameOpt = ref('');  
const tableNames = computed(() => {
    let nameArray = []
    
    for(let item of showTableData.value) {
        let flag = 0;
        if(nameArray.length === 0) {
            nameArray.push({value:item.table, label:item.table});
        }
        for(let e of nameArray) {
            if(item.table === e.value) {
                flag = 1;
                break;
            }
        }
        if(!flag) {
            nameArray.push({value:item.table, label:item.table});
        }
    }
    return nameArray;
})

// 数据库表选择下拉框
const handleTableSelectChange = async (value) => {
    let data = [
        { value: 0, name: '敏感' },
        { value: 0, name: '不敏感'}
    ];
    for(let item of showTableData.value) {
        if(item.table === value){
            for(let e of data) {
                if(item.status === e.name)
                    e.value++;
            }
        }
    }
    singleChartOption.value.series[0].data = data;
    singleChartOption.value.title.text = `${value}敏感数据分布`;
}

const allChartOption = ref({
    title: {
        text: '数据库敏感数据分布'
    },
    tooltip: {
        trigger: 'item'
    },
    legend: {
        top: '5%',
        left: 'center'
    },
    series: [
        {
            name: '敏感情况',
            type: 'pie',
            radius: ['55%', '70%'],
            avoidLabelOverlap: false,
            label: {
                show: false,
                position: 'center'
            },
            emphasis: {
                label: {
                    show: true,
                    fontSize: 40,
                    fontWeight: 'bold'
                }
            },
            labelLine: {
                show: false
            },
            data: allChartData.value
        }
    ]
})

const singleChartOption = ref({
    title: {
        text: '数据库敏感信息分布'
    },
    tooltip: {
        trigger: 'item'
    },
    legend: {
        top: '5%',
        left: 'center'
    },
    series: [
        {
            name: '敏感情况',
            type: 'pie',
            radius: ['55%', '70%'],
            avoidLabelOverlap: false,
            label: {
                show: false,
                position: 'center'
            },
            emphasis: {
                label: {
                    show: true,
                    fontSize: 40,
                    fontWeight: 'bold'
                }
            },
            labelLine: {
                show: false
            },
            data: [],
        }
    ]
})

// 监听
watch(allChartData, (newVal) => {
    allChartOption.value.series[0].data = newVal;
}, { deep: true });

const userId = 123;
const taskId = 456;

const handleGetIdentification = async () => {  
    try {  
        const response = await get(  
            '/identify',  
            {  
                user_id: store.getUsername,  
                task_id: 456  
            }  
        );  
        console.log('识别请求成功:', response.data);  

        const result = response.data;  
        tableData.value = [];  
        for (const key in result.columnNames) {  
            for (let i = 0; i < result.columnNames[key].length; i++) {  
                tableData.value.push({  
                    database: 'testDB', 
                    table: key,  
                    column: result.columnNames[key][i],  
                    algorithm: 1,  
                    state: result.canMaskColumnNames[key][i],  
                    status: result.canMaskColumnNames[key][i]  
                });  
            }  
        }  
        store.setIdentificationResults(tableData.value);  
        const taskName = 'testDB';   
        taskStore.updateTaskStatusByName(taskName, '已识别');  

    } catch (error) {  
        console.error('识别请求失败:', error);  
    }  
}

onMounted(() => {

    store.setIdentificationResults(tableData.value);
})

const getStatusClass = ({ row, column, rowIndex, columnIndex }) => {  
    return row.status === '敏感' ? 'sensitive-cell' : '';  
}; 

</script>

<template>    
    <MenuBar class="semi-transparent-menu" /> 
    <br>   
    <el-container style="background-color: #f0e6d2;"> 
        <el-main>    
            <el-row v-if="showTableData.length > 0" style="margin-bottom: 20px;">    
                <el-col :span="12">    
                    <v-chart class="chart" :option="allChartOption" autoresize />    
                </el-col>    
                <el-col :span="12">    
                    <el-select v-model="tableNameOpt" placeholder="--请选择--" style="width: 50%; margin-bottom: 20px;" @change="handleTableSelectChange">    
                        <el-option    
                            v-for="item in tableNames"    
                            :key="item.value"    
                            :label="item.label"    
                            :value="item.value"    
                        />    
                    </el-select>    
                    <v-chart class="chart" :option="singleChartOption" autoresize />    
                </el-col>    
            </el-row>    
            <el-row>    
                <el-table :data="showTableData" max-height="400" style="background-color: #FFF5EE; color: #333;"> <!-- 表格背景色设置为浅卡其色，字体颜色为深灰色 -->    
                    <el-table-column prop="database" label="数据库" />    
                    <el-table-column prop="table" label="表" />    
                    <el-table-column prop="column" label="列" />    
                    <el-table-column prop="status" label="识别结果" :cell-class-name="getStatusClass" />    
                </el-table>    
            </el-row>    
            <el-row style="margin-top: 20px;">    
                <el-button type="primary" @click="handleGetIdentification" style="background-color: #BDB76B; border-color: #8B864E;"> <!-- 按钮背景色和边框色设置为卡其色系的深色调 -->    
                    获取识别结果    
                </el-button>    
            </el-row>    
        </el-main>    
    </el-container>    
</template>
<style scoped>        
.app-container {    
  padding-top: 40px; /* 添加顶部内边距，使内容与菜单栏保持一定距离 */    
  background-color: #F5F5DC; /* 添加背景色以匹配整体风格 */  
}    
    
.chart {        
  height: 300px;        
  background-color: #FFF5EE; /* 图表背景色与表格一致 */        
  border: 1px solid #D2B48C; /* 边框颜色调整 */    
}        
        
/* 可选：为容器添加一些内边距 */        
.main-container {        
  padding: 20px;        
  background-color: #b3a395; /* 保持与整体背景色一致 */  
}        
              
.el-button--primary:hover {        
  background-color: #D2B48C; /* 鼠标悬停时的背景色调整 */        
}        
        
.el-button--primary:active {        
  background-color: #8B864E; /* 按下按钮时的背景色调整 */        
}      
      
/* 添加敏感列的样式 */      
.sensitive-cell {      
  background-color: #D2B48C; /* 敏感列背景色与按钮悬停色一致 */      
  color: #000; /* 字体颜色为黑色，可选 */      
}     
    
.semi-transparent-menu {      
  opacity: 0.8;    
}    
    
.footer-section {    
  text-align: center; /* 将按钮居中 */    
  margin-top: 20px; /* 与表格内容保持一定距离 */    
  background-color: #F5F5DC; /* 保持与整体背景色一致，如果需要的话 */  
}    
</style>