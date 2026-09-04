// src/services/api.js
import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  timeout: 30000,
});

// 详细的请求拦截器
api.interceptors.request.use(
  (config) => {
    console.log('🔍 请求详情:', {
      method: config.method?.toUpperCase(),
      url: config.url,
      fullURL: config.baseURL + config.url,
      headers: config.headers,
      data: config.data
    });
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 详细的响应拦截器
api.interceptors.response.use(
  (response) => {
    console.log('✅ 请求成功:', response.status, response.config.url);
    return response;
  },
  (error) => {
    const { response } = error;
    console.error('❌ 请求失败详情:', {
      URL: error.config?.url,
      Method: error.config?.method,
      Status: response?.status,
      StatusText: response?.statusText,
      Headers: response?.headers,
      Data: response?.data,
      FullError: error
    });
    
    // 处理 403 错误
    if (response?.status === 403) {
      let message = '访问被拒绝 (403)';
      if (response.data) {
        message += `: ${JSON.stringify(response.data)}`;
      }
      alert(message);
    }
    
    return Promise.reject(error);
  }
);

export const fileService = {
  upload: (file) => {
    const formData = new FormData();
    formData.append('file', file);
    return api.post('/file/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  }
};

export const musicService = {
  generate: (data) => api.post('/music/generate', data),
  uploadCover: (data) => api.post('/music/upload-cover', data),
  getStatus: (taskId) => api.get(`/music/status?taskId=${taskId}`)
};

// 添加测试方法
export const testService = {
  testConnection: () => api.get('/'), // 测试根路径
  testFileUpload: () => api.get('/file/upload'), // 测试文件上传端点是否存在
  testMusicGenerate: () => api.get('/music/generate') // 测试音乐生成端点是否存在
};

export default api;