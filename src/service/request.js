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

// 下载文件
export function downloadFile(url, params, fileName = 'downloaded_file.db') {
    return instance.get(url, {
        params,
        responseType: 'blob' // 确保响应是以Blob形式返回
    }).then(response => {
        const downloadUrl = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = downloadUrl;
        link.setAttribute('download', fileName); // 设置下载文件的文件名
        document.body.appendChild(link);
        link.click();
        link.remove();
        // 释放 Blob 对象的 URL
        window.URL.revokeObjectURL(downloadUrl);

        return response;
    } catch (error) {
        throw error;
    });
}

// 上传单个文件
export async function uploadFile(url, file) {
    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await instance.post(url, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
        return response;
    } catch (error) {
        throw error;
    }
}

// 上传多个文件
export async function uploadMultipleFiles(url, files) {
    const formData = new FormData();
    files.forEach((file, index) => {
        formData.append(`file${index + 1}`, file);
    });

    try {
        const response = await instance.post(url, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });

        return response;
    } catch (error) {
        throw error;
    }
}

// 上传文件和其他数据
export async function uploadFileWithData(url, file, additionalData) {
    const formData = new FormData();
    formData.append('file', file);

    // 添加其他数据
    Object.keys(additionalData).forEach(key => {
        formData.append(key, additionalData[key]);
    });

    try {
        const response = await instance.post(url, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });

        return response;
    } catch (error) {
        throw error;
    }
}

