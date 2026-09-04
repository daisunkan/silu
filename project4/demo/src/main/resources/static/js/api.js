// API 调用封装
const API_BASE = '/api';

class ApiService {
    // 健康检查
    static async healthCheck() {
        try {
            const response = await axios.get(`${API_BASE}/health`);
            return response.data;
        } catch (error) {
            console.error('健康检查失败:', error);
            throw error;
        }
    }

    // 测试端点
    static async test() {
        try {
            const response = await axios.get(`${API_BASE}/test`);
            return response.data;
        } catch (error) {
            console.error('测试端点调用失败:', error);
            throw error;
        }
    }

    // 生成图片
    static async generateImage(formData) {
        try {
            const response = await axios.post(`${API_BASE}/generate-image`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                },
                timeout: 120000 // 2分钟超时
            });
            return response.data;
        } catch (error) {
            console.error('风格迁移失败:', error);
            if (error.response) {
                throw new Error(error.response.data.message || error.response.data.error || '风格迁移失败');
            } else if (error.request) {
                throw new Error('网络错误，请检查连接');
            } else {
                throw new Error('请求配置错误');
            }
        }
    }
}

export default ApiService;