import JSZip from "jszip";

// 导出选中的表为csv
export const exportTableToCSV = (database, tableName) => {
    if (!tableName) return;
    const csvContent = queryTable(database, tableName);
    downloadCSV(csvContent, `exported_${tableName}.csv`)
}

// 导出所有表为一个csv压缩包
export const exportAllTableToCSVZip = async (database, tableNameArray) => {
    try {
        const zip = new JSZip();
        tableNameArray.forEach((tableName) => {
            const csvContent = queryTable(database, tableName);
            zip.file(`${tableName}.csv`, csvContent);
        });
        const zipContent = await zip.generateAsync({ type: 'blob' });
        const url = URL.createObjectURL(zipContent);
        const link = document.createElement('a');
        link.href = url;
        link.download = 'exported_all.zip';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(url);
    } catch (error) {
        console.error('Error generating ZIP:', error);
    }
}

// 表转为csv内容
const queryTable = (database, tableName) => {
    const result = database.exec(`SELECT * FROM ${tableName};`);
    const headers = result[0].columns;
    const rows = result[0].values;
    let csvContent = headers.join(',') + '\n';
    for (let row of rows) {
        csvContent += row.join(',') + '\n';
    }
    return csvContent;
}

// 下载csv
const downloadCSV = (csvContent, filename) => {
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
};