import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/company';

const companyApi = axios.create({
  baseURL: API_BASE_URL,
});

// Attach the JWT token automatically, same pattern as studentService.js
companyApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const getCompanyProfile = async () => {
  const response = await companyApi.get('/profile');
  return response.data;
};

export const updateCompanyProfile = async (data) => {
  const response = await companyApi.put('/profile', data);
  return response.data;
};

export default companyApi;