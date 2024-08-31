<script setup>
import { downloadFile } from '@/service/request';
import { onMounted, ref } from 'vue';
import { get, post } from '@/service/request';

const tableData = ref([])
const state = ref(true)
const testData = ref({
    columnNames: { table1: ['a', 'b', 'c', 'd'], table2: ['e', 'f', 'g', 'h'] },
    canMaskColumnNames: { table1: [true, false, true, false], table2: [true, true, true, false] }
})

const userId = 123;
const taskId = 456;

const handleGetIdentification = async () => {
    try {
        const response = await get(
            '/identify',
            {
                user_id: userId,
                task_id: taskId
            }
        );
        console.log('识别请求成功:', response.data);

        const result = response.data;
        // result = testData.value
        /*
        columnNames:{"tableName":["column", ...], ...}
        canMaskColumnNames: {"tableName": ["bool", ... ], ...}
        */
        tableData.value = []
        for (const key in result.columnNames) {
            for (let i = 0; i < result.columnNames[key].length; i++) {
                if (result.canMaskColumnNames[key][i]) {
                    tableData.value.push({
                        database: 'testDB',
                        table: key,
                        column: result.columnNames[key][i],
                        algorithm: 'testAlgo',
                        state: true,
                    })
                }
            }
        }
    } catch (error) {
        console.error('识别请求失败:', error);
    }
}

onMounted(() => {
    tableData.value = [
        {
            database: 'testDB',
            table: 'testTable',
            column: 'testColumn',
            algorithm: 'testAlgo',
            state: true,
        },
        {
            database: 'testDB2',
            table: 'testTable2',
            column: 'testColumn2',
            algorithm: 'testAlgo2',
            state: false,
        }
    ]
})

</script>

<template>
    <el-container>
        <el-main>
            <el-row>
                <el-table :data="tableData">
                    <el-table-column prop="database" label="数据库" />
                    <el-table-column prop="table" label="表" />
                    <el-table-column prop="column" label="列" />
                    <el-table-column prop="algorithm" label="脱敏算法" />
                    <el-table-column prop="state" label="启用状态">
                        <template v-slot="{ row }">
                            <el-switch v-model="row.state" />
                        </template>
                    </el-table-column>
                    <el-table-column label="操作">
                        <template #default>
                            <el-button link type="primary" size="small" @click="handleClick">
                                编辑
                            </el-button>
                            <el-button link type="primary" size="small">删除</el-button>
                        </template>
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