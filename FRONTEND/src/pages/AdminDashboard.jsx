import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  getDashboardStats, getAllStudents, getAllCompanies, getAllJobs,
  deleteStudent, deleteCompany, deleteJob
} from '../services/adminService';

function AdminDashboard() {
  const navigate = useNavigate();

  const [stats, setStats] = useState(null);
  const [activeTab, setActiveTab] = useState('overview'); // overview | students | companies | jobs
  const [students, setStudents] = useState([]);
  const [companies, setCompanies] = useState([]);
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      const data = await getDashboardStats();
      setStats(data);
    } catch (err) {
      if (err.response?.status === 401 || err.response?.status === 403) {
        navigate('/admin/login');
      } else {
        setError('Could not load dashboard stats.');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleTabChange = async (tab) => {
    setActiveTab(tab);
    setError('');
    try {
      if (tab === 'students' && students.length === 0) {
        setStudents(await getAllStudents());
      } else if (tab === 'companies' && companies.length === 0) {
        setCompanies(await getAllCompanies());
      } else if (tab === 'jobs' && jobs.length === 0) {
        setJobs(await getAllJobs());
      }
    } catch (err) {
      setError('Could not load data.');
    }
  };

  const handleDeleteStudent = async (id) => {
    if (!window.confirm('Delete this student account? This cannot be undone.')) return;
    try {
      await deleteStudent(id);
      setStudents((prev) => prev.filter((s) => s.id !== id));
      setMessage('Student deleted.');
      loadStats();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete student.');
    }
  };

  const handleDeleteCompany = async (id) => {
    if (!window.confirm('Delete this company account? This cannot be undone.')) return;
    try {
      await deleteCompany(id);
      setCompanies((prev) => prev.filter((c) => c.id !== id));
      setMessage('Company deleted.');
      loadStats();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete company.');
    }
  };

  const handleDeleteJob = async (id) => {
    if (!window.confirm('Delete this job posting? This cannot be undone.')) return;
    try {
      await deleteJob(id);
      setJobs((prev) => prev.filter((j) => j.id !== id));
      setMessage('Job deleted.');
      loadStats();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete job.');
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('student');
    localStorage.removeItem('userType');
    navigate('/admin/login');
  };

  if (loading) {
    return <div className="dashboard"><p>Loading admin dashboard...</p></div>;
  }

  return (
    <div className="profile-page" style={{ maxWidth: '900px' }}>
      <div className="profile-header">
        <div>
          <h2>Admin Dashboard 🛠️</h2>
          <p className="profile-email">System Overview &amp; Management</p>
        </div>
        <button className="btn-logout" onClick={handleLogout}>Logout</button>
      </div>

      {message && <div className="success-banner">{message}</div>}
      {error && <div className="error-banner">{error}</div>}

      <div className="tab-bar">
        <button className={`tab-button ${activeTab === 'overview' ? 'tab-active' : ''}`} onClick={() => handleTabChange('overview')}>Overview</button>
        <button className={`tab-button ${activeTab === 'students' ? 'tab-active' : ''}`} onClick={() => handleTabChange('students')}>Students</button>
        <button className={`tab-button ${activeTab === 'companies' ? 'tab-active' : ''}`} onClick={() => handleTabChange('companies')}>Companies</button>
        <button className={`tab-button ${activeTab === 'jobs' ? 'tab-active' : ''}`} onClick={() => handleTabChange('jobs')}>Jobs</button>
      </div>

      {activeTab === 'overview' && stats && (
        <div className="stats-grid">
          <div className="stat-card">
            <p className="stat-number">{stats.totalStudents}</p>
            <p className="stat-label">Total Students</p>
          </div>
          <div className="stat-card">
            <p className="stat-number">{stats.totalCompanies}</p>
            <p className="stat-label">Total Companies</p>
          </div>
          <div className="stat-card">
            <p className="stat-number">{stats.totalJobs}</p>
            <p className="stat-label">Total Jobs</p>
          </div>
          <div className="stat-card">
            <p className="stat-number">{stats.openJobs}</p>
            <p className="stat-label">Open Jobs</p>
          </div>
          <div className="stat-card">
            <p className="stat-number">{stats.totalApplications}</p>
            <p className="stat-label">Total Applications</p>
          </div>
          <div className="stat-card">
            <p className="stat-number">{stats.totalInterviews}</p>
            <p className="stat-label">Interviews Scheduled</p>
          </div>
          <div className="stat-card">
            <p className="stat-number">{stats.selectedCount}</p>
            <p className="stat-label">Students Selected</p>
          </div>
        </div>
      )}

      {activeTab === 'students' && (
        <div className="job-list">
          {students.map((s) => (
            <div key={s.id} className="job-card">
              <div className="job-card-header">
                <h4>{s.fullName}</h4>
                <button className="link-button link-button-danger" onClick={() => handleDeleteStudent(s.id)}>Delete</button>
              </div>
              <p className="job-card-meta">{s.email} · {s.department || 'No department'}</p>
              <p className="job-card-meta">{s.collegeName || 'No college'} · CGPA: {s.cgpa ?? 'N/A'}</p>
            </div>
          ))}
          {students.length === 0 && <p className="resume-status">No students registered yet.</p>}
        </div>
      )}

      {activeTab === 'companies' && (
        <div className="job-list">
          {companies.map((c) => (
            <div key={c.id} className="job-card">
              <div className="job-card-header">
                <h4>{c.companyName}</h4>
                <button className="link-button link-button-danger" onClick={() => handleDeleteCompany(c.id)}>Delete</button>
              </div>
              <p className="job-card-meta">{c.email} · {c.industry || 'No industry specified'}</p>
            </div>
          ))}
          {companies.length === 0 && <p className="resume-status">No companies registered yet.</p>}
        </div>
      )}

      {activeTab === 'jobs' && (
        <div className="job-list">
          {jobs.map((j) => (
            <div key={j.id} className="job-card">
              <div className="job-card-header">
                <h4>{j.title}</h4>
                <button className="link-button link-button-danger" onClick={() => handleDeleteJob(j.id)}>Delete</button>
              </div>
              <p className="job-card-meta">{j.companyName} · {j.jobType} · {j.location}</p>
              <span className={`job-status job-status-${j.status?.toLowerCase()}`}>{j.status}</span>
            </div>
          ))}
          {jobs.length === 0 && <p className="resume-status">No jobs posted yet.</p>}
        </div>
      )}
    </div>
  );
}

export default AdminDashboard;