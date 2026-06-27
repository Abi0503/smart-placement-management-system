import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/admin/auth';

const adminAuthApi = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' },
});

export const loginAdmin = async (data) => {
  const response = await adminAuthApi.post('/login', data);
  return response.data;
};

export default adminAuthApi;