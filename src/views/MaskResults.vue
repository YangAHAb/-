<script setup>
import MenuBar from '../MenuBar.vue';
import { computed, onMounted, ref } from 'vue';
import { downloadFile, get, post } from '@/service/request';
import { store } from '@/main';

const userId = 123;
const taskId = 456;
const state = ref('')
const tableData = ref([])

const ruleData = computed(() => {
    let rule = {columnNames:{}, rule:{}};
    for(let item of tableData.value) {
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

const handleStartMask = async () => {
    try {
        ruleData.value['user_id'] = store.getUsername;
        ruleData.value['task_id'] = 456;
        console.log("ruleData:",ruleData.value);
        const response = await post(
            '/mask',
            ruleData.value
        );
        console.log('脱敏请求成功:', response.data);
        state.value = response.data.status
    } catch (error) {
        console.error('脱敏请求失败:', error);
    }
}

const handleMaskDownload = async () => {
    const result = await downloadFile('/download', {
        user_id: store.getUsername,
        task_id: 456 // 设置task_id
    },`${userId}_${taskId}_maskedDB.db`)
}

onMounted(() => {
    tableData.value = store.getIdentificationResults;
})
</script>

<template>
    <MenuBar />
    <el-row>
        <el-table :data="tableData">
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
            <!-- <el-table-column label="操作">
                <template #default>
                    <el-button link type="primary" size="small" @click="handleClick">
                        编辑
                    </el-button>
                    <el-button link type="primary" size="small">删除</el-button>
                </template>
            </el-table-column> -->
        </el-table>
    </el-row>
    <el-row justify="space-evenly">
        <el-col :span="12">
            <el-button type="primary" @click="handleStartMask">
                    开始脱敏
            </el-button>
        </el-col>
        <el-col :span="12">
            <el-button type="primary" @click="handleMaskDownload">
                    下载结果
            </el-button>
        </el-col>
    </el-row>
    <el-row>
        <p> state: {{ state }} </p>
    </el-row>
</template>

<style scoped>
</style>