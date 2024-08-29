<script setup>
import { column } from 'element-plus/es/components/table-v2/src/common';
import { onMounted, ref } from 'vue';

const tableData = ref([])
const state = ref(true)

onMounted(()=> {
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
                <el-table :data="tableData" >
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
        </el-main>
    </el-container>
</template>

<style scoped>

</style>