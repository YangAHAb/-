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
// ref:https://vue-echarts.dev/#codegen


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
    // tableData.value = [
    //     {
    //         database: 'testDB',
    //         table: 'testTable',
    //         column: 'testColumn',
    //         algorithm: 1,
    //         state: true,
    //         status: true,
    //     },
    //     {
    //         database: 'testDB2',
    //         table: 'testTable2',
    //         column: 'testColumn2',
    //         algorithm: 2,
    //         state: false,
    //         status: false,
    //     }
    // ]
    store.setIdentificationResults(tableData.value);
})

</script>

<template>
    <MenuBar />
    <el-container>
        <el-main>
            <el-row v-if="showTableData.length > 0">
                <el-col :span="12">
                    <v-chart class="chart" :option="allChartOption" autoresize />
                </el-col>
                <el-col :span="12">
                    <el-select v-model="tableNameOpt" placeholder="--请选择--" style="width: 50%" @change="handleTableSelectChange">
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
                <el-table :data="showTableData" max-height="400">
                    <el-table-column prop="database" label="数据库" />
                    <el-table-column prop="table" label="表" />
                    <el-table-column prop="column" label="列" />
                    <el-table-column prop="status" label="识别结果">
                    </el-table-column>
                </el-table>
            </el-row>
            <el-row>
                <el-button type="primary" @click="handleGetIdentification">
                    获取识别结果
                </el-button>
            </el-row>
        </el-main>
    </el-container>
</template>

<style scoped>
.chart {
  height: 300px;
}
</style>