<script setup>
import MenuBar from '../MenuBar.vue';
import { downloadFile } from '@/service/request';
import { computed, onMounted, ref } from 'vue';
import { get, post } from '@/service/request';
import { store } from '@/main';
import tasklist from '../tasklist.vue';
import { useTaskStore } from '@/store/taskStore';   
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

const algorithmOptions = [
    {
        value: 1,
        label: '脱敏算法1',
    },
    {
        value: 2,
        label: '脱敏算法2',
    },
    {
        value: 3,
        label: '脱敏算法3',
    },
]

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

</script>

<template>
    <MenuBar />
    <el-container>
        <el-main>
            <el-row>
                <el-table :data="showTableData">
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

<style scoped></style>