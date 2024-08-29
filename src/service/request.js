import axios from "axios";

export const instance = axios.create({
    baseURL: "http://localhost:30407/api",
    timeout: 0  // 0代表无限制
});

// 拦截器
instance.interceptors.request.use(
    config => {
        return config;
    },
    error => {
        console.log(error);
        return Promise.reject(error);
    }
);

instance.interceptors.response.use(
    res => {
        return res;
    },
    error => {
        console.log(error);
        return Promise.reject(error);
    }
);

export function get(url, params) {
    return instance.get(url, {
        params
    });
}

export function post(url, data) {
    return instance.post(url, data);
}

// 开始脱敏


// 下载文件
export async function downloadFile(url, params, fileName = 'downloaded_file.db') {
    try {
        const response = await instance.get(url, {
            params,
            responseType: 'blob' // 确保响应是以Blob形式返回
        });
        const downloadUrl = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = downloadUrl;
        link.setAttribute('download', fileName); // 设置下载文件的文件名
        document.body.appendChild(link);
        link.click();
        link.remove();
        // 释放 Blob 对象的 URL
        window.URL.revokeObjectURL(downloadUrl);
    } catch (error) {
        console.error('下载文件失败:', error);
        throw error;
    }
}

// 上传单个文件
export function uploadFile(url, file) {
    const formData = new FormData();
    formData.append('file', file);

    return instance.post(url, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    }).then(response => {
        console.log('文件上传成功:', response.data);
        return response.data;
    }).catch(error => {
        console.error('文件上传失败:', error);
        throw error;
    });
}

// 上传多个文件
export function uploadMultipleFiles(url, files) {
    const formData = new FormData();
    files.forEach((file, index) => {
        formData.append(`file${index + 1}`, file);
    });

    return instance.post(url, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    }).then(response => {
        console.log('多个文件上传成功:', response.data);
        return response.data;
    }).catch(error => {
        console.error('多个文件上传失败:', error);
        throw error;
    });
}

// 上传文件和其他数据
export function uploadFileWithData(url, file, additionalData) {
    const formData = new FormData();
    formData.append('file', file);

    // 添加其他数据
    Object.keys(additionalData).forEach(key => {
        formData.append(key, additionalData[key]);
    });

    return instance.post(url, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    }).then(response => {
        console.log('文件和数据上传成功:', response.data);
        return response.data;
    }).catch(error => {
        console.error('文件和数据上传失败:', error);
        throw error;
    });
}

