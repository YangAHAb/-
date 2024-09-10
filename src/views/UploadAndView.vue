<script setup lang="ts">  
import MenuBar from '../MenuBar.vue';  
import { ref } from 'vue';  
import { ElMessage, genFileId } from 'element-plus';  
import type { UploadInstance, UploadProps, UploadRawFile } from 'element-plus';  
import { UploadFilled } from '@element-plus/icons-vue';  
import initSqlJs from 'sql.js';  
import { uploadFileWithData } from '@/service/request';  
import { store } from '@/main';  
import { useTaskStore } from '../store/taskStore';  
import { exportAllTableToCSVZip, exportTableToCSV } from '@/service/csvservice';
  
const SQL = initSqlJs({  
    locateFile: (file) => `/node_modules/sql.js/dist/${file}`  
});  

// 数据库展示相关
const db = ref(null);  
const dbOutput = ref('');  
const tableNameOpt = ref('');  
const tableNames = ref([]);  
const tableData = ref([]);  
const tableColumns = ref([]);  
const fileName = ref('');

// csv导出相关
const chosenTableName = ref('');

const taskStore = useTaskStore(); 
  
const dialogVisible = ref(false);  
const uploadStatus = ref('');  
  
// 文件上传相关
const fileContent = ref(null);  
const imgSrc = ref(null);  
const uploadUrl = ref('http://localhost:3000/upload');  
const upload = ref<UploadInstance>();

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
        default:
            break;
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

// 上传的文件改变
const handleChange = (file: any) => {
    dbOutput.value = ''
    try {
        const reader = new FileReader()
        reader.onload = async (e) => {
            const UintDB = new Uint8Array(e.target.result as ArrayBuffer);
            db.value = new (await SQL).Database(UintDB);
            const tables = await db.value.exec('SELECT name FROM sqlite_master WHERE type="table";');
            const tableNamesText = tables[0].values.map(row => row[0]);
            for(let tableName of tableNamesText) {
                tableNames.value.push({value:tableName, label:tableName});
            }
        }
        reader.readAsArrayBuffer(file.raw)
    } 
    catch (error) {
        dbOutput.value = `Error: ${error.message}`;
    }
 
    fileName.value = file.raw.name;
    fileContent.value = file.raw; 
};

// 上传的文件移除
const handleRemove: UploadProps['onRemove'] = (file, uploadFiles) => {  
    fileContent.value = null;  
    imgSrc.value = null;
    dbOutput.value = '';
    tableNames.value=[];
    tableNameOpt.value='';
    tableData.value=[];
    tableColumns.value=[];
    console.log('remove');  
};  
  
// 上传的文件被替换
const handleExceed: UploadProps['onExceed'] = (files) => {
    fileContent.value = null;  
    imgSrc.value = null;
    dbOutput.value = '';
    tableNames.value=[];
    tableNameOpt.value='';
    tableData.value=[];
    tableColumns.value=[];  
    upload.value!.clearFiles();  
    const file = files[0] as UploadRawFile;  
    file.uid = genFileId();  
    upload.value!.handleStart(file);  
};  
  
// 文件内容上传至后端
const submitUpload = async () => {  
  try {  
    if (!fileContent.value) {  
      throw new Error('No File!');  
    }  
    dialogVisible.value = true;  
    uploadStatus.value = '正在上传...';  
      
    const result = await uploadFileWithData('/uploadWithData', fileContent.value, { "user_id": store.getUsername, "task_id": 456 });  
    uploadStatus.value = '文件上传成功';  
    ElMessage.success('文件上传成功');  
  
    // 检查任务列表中是否有同名的上传失败任务，如果有则更新状态，否则添加新任务  
    taskStore.updateTaskStatusByName(fileName.value, '上传成功');  
    const existingFailedTask = taskStore.tasks.find(task => task.name === fileName.value && task.state === '上传失败');  
    if (!existingFailedTask) {  
      taskStore.addTask({ FILENAME: fileName.value, STATUS: '上传成功' });  
    }  
  
    console.log('文件上传成功', result);  
  } catch (error) {  
    uploadStatus.value = '文件上传失败';  
    ElMessage.error(`文件上传失败：${error.message}`);  
    console.error('文件上传失败', error);  
  
    // 添加任务到任务栏，附带失败状态  
    taskStore.addTask({ FILENAME: fileName.value, STATUS: '上传失败' });  
  } finally {  
    setTimeout(() => {  
      dialogVisible.value = false;  
    }, 2000);  
  }  
};
</script>  
  
<template>
 <MenuBar class="semi-transparent-menu" /> 
 
    <el-row class="upload-container">  
        <el-upload  
            ref="upload"  
            class="upload-db"  
            drag  
            :action="uploadUrl"  
            accept=".db, image/*"  
            :auto-upload="false"  
            :limit="1"  
            :on-exceed="handleExceed"  
            :on-change="handleChange"  
            :on-remove="handleRemove"  
        >  
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>  
            <div class="el-upload__text">  
                拖拽文件至此或<em>点击</em>以上传  
            </div>  
            <template #tip>  
                <div class="el-upload__tip">  
                    仅支持db文件, 最多一个文件，新文件会覆盖旧文件  
                </div>  
            </template>  
        </el-upload>  
        <el-button class="upload-button" type="success" @click="submitUpload">  
            上传至平台  
        </el-button>  
    </el-row>  
    <el-row class="image-container">  
        <div class="image-wrapper" v-if="imgSrc">  
            <img class="displayImage" :src="imgSrc" />  
        </div>
        <div class="db-wrapper" v-else-if="tableNames.length !== 0">
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
.semi-transparent-menu {  
  opacity: 0.8;  
  margin-bottom: 20px;  
}  
  
.upload-container {  
  display: flex;  
  align-items: center;  
  justify-content: space-between;  
  padding: 30px 50px;  /* 增加左右内边距 */  
  background-color: #f0e6d2;  
  border-radius: 8px;  
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);  
}  
  
.el-upload__tip {  
  margin-left: 10px;  
}  
  
.upload-db {  
  flex: 1;  
  border: 2px dashed #b3a395;  
  border-radius: 8px;  
  cursor: pointer;  
  position: relative;  
  overflow: hidden;  
  background-color: #fff5ee;  
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);  
  transition: all 0.3s;  
}  
  
.upload-db:hover {  
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);  
}  
  
.upload-button {  
  background-color: #a58b6d;  
  color: #ffffff;  
  padding: 10px 20px;  
  border-radius: 6px;  
  margin-left: 30px;  
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);  
  transition: all 0.3s;  
}  
  
.upload-button:hover {  
  background-color: #8b7359;  
}  
  
.image-container {  
  display: flex;  
  justify-content: center;  
  align-items: center;  
  margin-top: 30px;  
  padding: 0 50px;  /* 增加左右内边距 */  
}  
  
.image-wrapper {  
  border: 2px solid #d2c0a5;  
  border-radius: 8px;  
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);  
  padding: 10px;  
  transition: all 0.3s;  
}  
  
.image-wrapper:hover {  
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);  
}  
  
.displayImage {  
  width: 100%;  
  border-radius: 6px;  
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);  
}  
  
.no-file {  
  font-size: 16px;  
  color: #6d5945;  
  text-align: center;  
  padding: 20px;  
}  
  
.db-wrapper {  
  width: 100%;  
  max-width: 800px;  
  border: 2px solid #d2c0a5;  
  border-radius: 8px;  
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);  
  padding: 20px;  
  transition: all 0.3s;  
}  
  
.el-select, .el-dropdown {  
  width: 48%;  
  margin-right: 4%;  
}  
  
.el-dropdown {  
  margin-right: 0;  
}  
</style>