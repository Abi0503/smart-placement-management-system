import axios from 'axios';

// This is the base URL of your Spring Boot backend.
// Make sure your backend is running on port 8080 before testing the frontend.
const API_BASE_URL = 'http://localhost:8080/api/auth';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Calls POST /api/auth/register
export const registerStudent = async (data) => {
  const response = await api.post('/register', data);
  return response.data; // { token, id, fullName, email, role }
};

// Calls POST /api/auth/login
export const loginStudent = async (data) => {
  const response = await api.post('/login', data);
  return response.data;
};

export default api;