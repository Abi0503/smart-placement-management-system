import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/interviews';

const interviewApi = axios.create({
  baseURL: API_BASE_URL,
});

// Attach the JWT token automatically, same pattern as other services
interviewApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// POST /api/interviews/schedule/{applicationId}  (company)
export const scheduleInterview = async (applicationId, data) => {
  const response = await interviewApi.post(`/schedule/${applicationId}`, data);
  return response.data;
};

// GET /api/interviews/my-interviews  (student)
export const getMyInterviews = async () => {
  const response = await interviewApi.get('/my-interviews');
  return response.data;
};

// GET /api/interviews/company-interviews  (company)
export const getCompanyInterviews = async () => {
  const response = await interviewApi.get('/company-interviews');
  return response.data;
};

// PUT /api/interviews/{id}/result  (company)
export const updateInterviewResult = async (interviewId, status, result) => {
  const response = await interviewApi.put(`/${interviewId}/result`, { status, result });
  return response.data;
};

export default interviewApi;