<script setup>
import MenuBar from '../MenuBar.vue';
import tasklist from '../tasklist.vue';
import { computed, onMounted, ref } from 'vue';
import { downloadFile, get, instance, post } from '@/service/request';
import { store } from '@/main';
import { useTaskStore } from '@/store/taskStore';
import initSqlJs from 'sql.js';
import { exportAllTableToCSVZip, exportTableToCSV } from '@/service/csvservice';

const SQL = initSqlJs({  
    locateFile: (file) => `/node_modules/sql.js/dist/${file}`  
});

// 数据库展示
const db = ref(null);
const tableNameOpt = ref('');  
const tableNames = ref([]);   
const tableColumns = ref([]);
const tableData = ref([]);


// csv导出相关
const chosenTableName = ref('');
const dbBlob = ref(null);

const taskStore = useTaskStore();

const userId = 123;
const taskId = 456;
const state = ref('')
const tableColData = ref([])

const ruleData = computed(() => {
    let rule = {columnNames:{}, rule:{}};
    for(let item of tableColData.value) {
        if (!rule.columnNames[item.table]) {
            rule.columnNames[item.table] = [];
        }
        if (!rule.rule[item.table]) {
            rule.rule[item.table] = [];
        }
        rule.columnNames[item.table].push(item.column);
        rule.rule[item.table].push(item.state?item.algorithm:-1);
    }
    return rule;
})

const algorithmOptions = [
    {
        value: 1,
        label: '特殊字符覆盖',
    },
    {
        value: 2,
        label: '加密',
    },
    {
        value: 3,
        label: '随机置乱',
    },
]

const handleStartMask = async () => {  
    try {  
        ruleData.value['user_id'] = store.getUsername;  
        ruleData.value['task_id'] = 456;  
        console.log("ruleData:", ruleData.value);  
        // const response = await post('/mask', ruleData.value);
        const response = await get('/mask', { user_id: store.getUsername, task_id: 456 });
        console.log('脱敏请求成功:', response.data);  
        state.value = response.data.status;  
        taskStore.markAllTasksAsMasked(); 
    } catch (error) {  
        console.error('脱敏请求失败:', error);  
    }  
};

// 从后端获取结果，使用sql.js解析
const handleMaskResult = async () => {
    try {
        tableNames.value = [];
        const result = await instance.get('/download',{ params: { user_id: store.getUsername, task_id: 456 }, responseType: 'arraybuffer' });
        dbBlob.value = new Blob([result.data]);
        const UintDB = new Uint8Array(result.data);
        db.value = new (await SQL).Database(UintDB);
        const tables = await db.value.exec('SELECT name FROM sqlite_master WHERE type="table";');
        const tableNamesText = tables[0].values.map(row => row[0]);
        for(let tableName of tableNamesText) {
            tableNames.value.push({value:tableName, label:tableName});
        }
    } catch (error) {
        console.log(error);
    }
}

// 数据库表选择下拉框
const handleTableSelectChange = async (value) => {
    chosenTableName.value = value;
    tableData.value=[];
    tableColumns.value=[];
    const result = await db.value.exec(`SELECT * FROM ${value};`);
    console.log(result);
    const columns = result[0].columns;
    for(let col of columns) {
        tableColumns.value.push({prop:col, label:col});
    }
    const values = result[0].values;
    for(let value of values) {
        let obj = {};
        for(let i=0;i<columns.length;i++) {
            obj[columns[i]] = value[i];
        }
        tableData.value.push(obj);
    }
}

// 处理导出按钮
const handleCommand = (command) => {
    switch(command)
    {
        case 'a':
            exportTableToCSV(db.value, chosenTableName.value);
            break;
        case 'b':
            let tables = []
            tableNames.value.forEach((element) => {
                tables.push(element.value);
            })
            exportAllTableToCSVZip(db.value, tables);
            break;
        case 'c':
        {
            const url = URL.createObjectURL(dbBlob.value);
            const link = document.createElement('a');
            link.href = url;
            link.download = 'exported_db.db';
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            URL.revokeObjectURL(url);
        }
        default:
            break;
    }
}

const handleMaskDownload = async () => {
    const result = await downloadFile('/download', {
        user_id: store.getUsername,
        task_id: 456 // 设置task_id
    },`${userId}_${taskId}_maskedDB.db`)
    console.log(result.data);
}

onMounted(() => {
    tableColData.value = store.getIdentificationResults;
})
</script>

<template>
    <MenuBar />
    <tesklist/>
    <el-row>
        <el-table :data="tableColData" max-height="400">
            <el-table-column prop="database" label="数据库" />
            <el-table-column prop="table" label="表" />
            <el-table-column prop="column" label="列" />
            <el-table-column label="脱敏算法">
                <template #default="{ row }">
                    <el-select
                    v-model="row.algorithm"
                    placeholder="请选择"
                    :disabled="!row.status"
                    >    
                        <el-option
                        v-for="item in algorithmOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                        />
                    </el-select>
                </template>
            </el-table-column>
            <el-table-column prop="state" label="启用状态">
                <template #default="{ row }">
                    <el-switch v-model="row.state" :disabled="!row.status"/>
                </template>
            </el-table-column>
        </el-table>
    </el-row>
    <el-row justify="space-evenly">
        <el-col :span="12">
            <el-button type="primary" @click="handleStartMask">
                    开始脱敏
            </el-button>
        </el-col>
        <el-col :span="12">
            <el-button type="primary" @click="handleMaskResult">
                    查看结果
            </el-button>
        </el-col>
    </el-row>
    <el-row>
        <p> state: {{ state }} </p>
    </el-row>
    <el-row>
        <div class="db-wrapper" v-if="tableNames.length !== 0">
            <el-row>
                <el-select v-model="tableNameOpt" placeholder="--请选择--" style="width: 90%" @change="handleTableSelectChange">
                    <el-option
                        v-for="item in tableNames"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                    />
                </el-select>
                <el-dropdown @command="handleCommand" style="width: 10%">
                    <el-button style="width: 100%">导出</el-button>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="a" >选中的表导出为CSV</el-dropdown-item>
                            <el-dropdown-item command="b" >全部表导出为CSV</el-dropdown-item>
                            <el-dropdown-item command="c" >导出数据库文件</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </el-row>
            <el-row>
                <el-table :data="tableData" stripe style="width: 100%;" max-height="400">
                    <el-table-column
                    v-for="(col, index) in tableColumns"
                    :key="index"
                    :prop="col.prop"
                    :label="col.label"
                    >
                    </el-table-column>
                </el-table>
            </el-row>
        </div>
        <div class="no-file" v-else>  
            暂无文件  
        </div>
    </el-row>
</template>

<style scoped>
.no-file {  
    font-size: 16px;  
    color: #999;  
    text-align: center;  
    padding: 20px;
}

.db-wrapper {
    width: 100%;
    border: 2px solid #e4e7ed;
    border-radius: 8px;  
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    padding: 10px;  
    transition: all 0.3s; /* 过渡动画 */
}

</style>