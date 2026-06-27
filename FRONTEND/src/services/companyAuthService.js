import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/company/auth';

const companyAuthApi = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' },
});

// POST /api/company/auth/register
export const registerCompany = async (data) => {
  const response = await companyAuthApi.post('/register', data);
  return response.data;
};

// POST /api/company/auth/login
export const loginCompany = async (data) => {
  const response = await companyAuthApi.post('/login', data);
  return response.data;
};

export default companyAuthApi;