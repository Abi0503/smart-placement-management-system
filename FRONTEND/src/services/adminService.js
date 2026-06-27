import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/admin';

const adminApi = axios.create({
  baseURL: API_BASE_URL,
});

adminApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const getDashboardStats = async () => {
  const response = await adminApi.get('/dashboard-stats');
  return response.data;
};

export const getAllStudents = async () => {
  const response = await adminApi.get('/students');
  return response.data;
};

export const getAllCompanies = async () => {
  const response = await adminApi.get('/companies');
  return response.data;
};

export const getAllJobs = async () => {
  const response = await adminApi.get('/jobs');
  return response.data;
};

export const deleteStudent = async (id) => {
  await adminApi.delete(`/students/${id}`);
};

export const deleteCompany = async (id) => {
  await adminApi.delete(`/companies/${id}`);
};

export const deleteJob = async (id) => {
  await adminApi.delete(`/jobs/${id}`);
};

export default adminApi;