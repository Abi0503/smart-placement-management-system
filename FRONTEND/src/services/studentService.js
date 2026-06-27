import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/student';

const studentApi = axios.create({
  baseURL: API_BASE_URL,
});

// This runs before EVERY request made with studentApi.
// It grabs the token we saved in localStorage during login/register,
// and attaches it as "Authorization: Bearer <token>" -- this is exactly
// what your JwtFilter.java on the backend expects to see.
studentApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// GET /api/student/profile
export const getProfile = async () => {
  const response = await studentApi.get('/profile');
  return response.data;
};

// PUT /api/student/profile
export const updateProfile = async (data) => {
  const response = await studentApi.put('/profile', data);
  return response.data;
};

// POST /api/student/resume (multipart file upload)
export const uploadResume = async (file) => {
  const formData = new FormData();
  formData.append('file', file);

  const response = await studentApi.post('/resume', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  return response.data;
};

export default studentApi;