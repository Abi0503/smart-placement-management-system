import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/jobs';

const jobApi = axios.create({
  baseURL: API_BASE_URL,
});

// Attach the JWT token automatically, same pattern as studentService.js / companyService.js
jobApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// POST /api/jobs
export const createJob = async (data) => {
  const response = await jobApi.post('', data);
  return response.data;
};

// GET /api/jobs/my-jobs
export const getMyJobs = async () => {
  const response = await jobApi.get('/my-jobs');
  return response.data;
};

// GET /api/jobs/open (Module 5: students browse all open jobs)
export const getOpenJobs = async () => {
  const response = await jobApi.get('/open');
  return response.data;
};

// PUT /api/jobs/{id}
export const updateJob = async (id, data) => {
  const response = await jobApi.put(`/${id}`, data);
  return response.data;
};

// DELETE /api/jobs/{id}
export const deleteJob = async (id) => {
  await jobApi.delete(`/${id}`);
};

export default jobApi;