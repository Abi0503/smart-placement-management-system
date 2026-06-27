import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/applications';

const applicationApi = axios.create({
  baseURL: API_BASE_URL,
});

// Attach the JWT token automatically, same pattern as other services
applicationApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// POST /api/applications/apply/{jobId}  (student)
export const applyToJob = async (jobId) => {
  const response = await applicationApi.post(`/apply/${jobId}`);
  return response.data;
};

// GET /api/applications/my-applications  (student)
export const getMyApplications = async () => {
  const response = await applicationApi.get('/my-applications');
  return response.data;
};

// GET /api/applications/job/{jobId}  (company)
export const getApplicantsForJob = async (jobId) => {
  const response = await applicationApi.get(`/job/${jobId}`);
  return response.data;
};

// PUT /api/applications/{id}/status  (company)
export const updateApplicationStatus = async (applicationId, status) => {
  const response = await applicationApi.put(`/${applicationId}/status`, { status });
  return response.data;
};

export default applicationApi;