import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/notifications';

const notificationApi = axios.create({
  baseURL: API_BASE_URL,
});

// Attach the JWT token automatically, same pattern as other services
notificationApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const getMyNotifications = async () => {
  const response = await notificationApi.get('');
  return response.data;
};

export const getUnreadCount = async () => {
  const response = await notificationApi.get('/unread-count');
  return response.data.unreadCount;
};

export const markAsRead = async (notificationId) => {
  const response = await notificationApi.put(`/${notificationId}/read`);
  return response.data;
};

export const markAllAsRead = async () => {
  await notificationApi.put('/read-all');
};

export default notificationApi;