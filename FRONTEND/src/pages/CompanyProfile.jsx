import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getCompanyProfile, updateCompanyProfile } from '../services/companyService';
import { getMyJobs, createJob, updateJob, deleteJob } from '../services/jobService';
import { getApplicantsForJob, updateApplicationStatus } from '../services/applicationService';
import { scheduleInterview, getCompanyInterviews, updateInterviewResult } from '../services/interviewService';
import JobForm from '../components/JobForm';
import InterviewForm from '../components/InterviewForm';
import NotificationBell from '../components/NotificationBell';

function CompanyProfile() {
  const navigate = useNavigate();

  const [profile, setProfile] = useState(null);
  const [formData, setFormData] = useState({});
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  // Module 4: Jobs
  const [jobs, setJobs] = useState([]);
  const [jobsLoading, setJobsLoading] = useState(true);
  const [showJobForm, setShowJobForm] = useState(false);
  const [editingJob, setEditingJob] = useState(null);
  const [jobSaving, setJobSaving] = useState(false);

  // Module 5: Applicants
  const [viewingApplicantsForJob, setViewingApplicantsForJob] = useState(null);
  const [applicants, setApplicants] = useState([]);
  const [applicantsLoading, setApplicantsLoading] = useState(false);
  const [updatingStatusId, setUpdatingStatusId] = useState(null);

  // Interview Module: scheduling + results
  const [companyInterviews, setCompanyInterviews] = useState([]);
  const [schedulingForApplicationId, setSchedulingForApplicationId] = useState(null);
  const [interviewSaving, setInterviewSaving] = useState(false);
  const [updatingResultId, setUpdatingResultId] = useState(null);

  useEffect(() => {
    loadProfile();
    loadJobs();
    loadCompanyInterviews();
  }, []);

  const loadProfile = async () => {
    try {
      const data = await getCompanyProfile();
      setProfile(data);
      setFormData(data);
    } catch (err) {
      if (err.response?.status === 401 || err.response?.status === 403) {
        navigate('/company/login');
      } else {
        setError('Could not load company profile.');
      }
    } finally {
      setLoading(false);
    }
  };

  const loadJobs = async () => {
    try {
      const data = await getMyJobs();
      setJobs(data);
    } catch (err) {
      console.error('Could not load jobs', err);
    } finally {
      setJobsLoading(false);
    }
  };

  const loadCompanyInterviews = async () => {
    try {
      const data = await getCompanyInterviews();
      setCompanyInterviews(data);
    } catch (err) {
      console.error('Could not load interviews', err);
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setSaving(true);
    setMessage('');
    setError('');

    try {
      const updated = await updateCompanyProfile(formData);
      setProfile(updated);
      setFormData(updated);
      setMessage('Company profile updated successfully!');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update profile.');
    } finally {
      setSaving(false);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('student');
    localStorage.removeItem('userType');
    navigate('/company/login');
  };

  // ===================== Module 4: Job handlers =====================

  const handleOpenNewJobForm = () => {
    setEditingJob(null);
    setShowJobForm(true);
  };

  const handleOpenEditJobForm = (job) => {
    setEditingJob(job);
    setShowJobForm(true);
  };

  const handleCancelJobForm = () => {
    setShowJobForm(false);
    setEditingJob(null);
  };

  const handleJobSubmit = async (jobData) => {
    setJobSaving(true);
    setError('');
    setMessage('');

    try {
      if (editingJob) {
        await updateJob(editingJob.id, jobData);
        setMessage('Job updated successfully!');
      } else {
        await createJob(jobData);
        setMessage('Job posted successfully!');
      }
      setShowJobForm(false);
      setEditingJob(null);
      loadJobs();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save job.');
    } finally {
      setJobSaving(false);
    }
  };

  const handleDeleteJob = async (jobId) => {
    if (!window.confirm('Are you sure you want to delete this job posting?')) {
      return;
    }
    try {
      await deleteJob(jobId);
      setMessage('Job deleted.');
      loadJobs();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete job.');
    }
  };

  // ===================== Module 5: Applicant handlers =====================

  const handleViewApplicants = async (job) => {
    setViewingApplicantsForJob(job);
    setApplicantsLoading(true);
    setError('');

    try {
      const data = await getApplicantsForJob(job.id);
      setApplicants(data);
    } catch (err) {
      setError(err.response?.data?.message || 'Could not load applicants.');
    } finally {
      setApplicantsLoading(false);
    }
  };

  const handleCloseApplicants = () => {
    setViewingApplicantsForJob(null);
    setApplicants([]);
    setSchedulingForApplicationId(null);
  };

  const handleStatusChange = async (applicationId, newStatus) => {
    setUpdatingStatusId(applicationId);
    setError('');

    try {
      const updated = await updateApplicationStatus(applicationId, newStatus);
      setApplicants((prev) =>
        prev.map((a) => (a.id === applicationId ? updated : a))
      );
      setMessage('Applicant status updated.');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update status.');
    } finally {
      setUpdatingStatusId(null);
    }
  };

  // ===================== Interview Module handlers =====================

  // Returns the interview already scheduled for this application, if any
  const getInterviewForApplication = (applicationId) => {
    return companyInterviews.find((i) => i.applicationId === applicationId);
  };

  const handleOpenScheduleForm = (applicationId) => {
    setSchedulingForApplicationId(applicationId);
  };

  const handleCancelScheduleForm = () => {
    setSchedulingForApplicationId(null);
  };

  const handleScheduleSubmit = async (interviewData) => {
    setInterviewSaving(true);
    setError('');
    setMessage('');

    try {
      await scheduleInterview(schedulingForApplicationId, interviewData);
      setMessage('Interview scheduled successfully!');
      setSchedulingForApplicationId(null);
      loadCompanyInterviews();
      // Refresh applicants list too, since application status moves to SHORTLISTED
      if (viewingApplicantsForJob) {
        handleViewApplicants(viewingApplicantsForJob);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to schedule interview.');
    } finally {
      setInterviewSaving(false);
    }
  };

  const handleResultChange = async (interviewId, field, value) => {
    setUpdatingResultId(interviewId);
    setError('');

    try {
      const existing = companyInterviews.find((i) => i.id === interviewId);
      const status = field === 'status' ? value : existing.status;
      const result = field === 'result' ? value : existing.result;

      const updated = await updateInterviewResult(interviewId, status, result);
      setCompanyInterviews((prev) =>
        prev.map((i) => (i.id === interviewId ? updated : i))
      );
      setMessage('Interview updated.');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update interview.');
    } finally {
      setUpdatingResultId(null);
    }
  };

  if (loading) {
    return <div className="dashboard"><p>Loading company profile...</p></div>;
  }

  return (
    <div className="profile-page">
      <div className="profile-header">
        <div>
          <h2>{profile?.companyName} 🏢</h2>
          <p className="profile-email">{profile?.email}</p>
        </div>
        <div className="header-actions">
          <NotificationBell />
          <button className="btn-logout" onClick={handleLogout}>Logout</button>
        </div>
      </div>

      {message && <div className="success-banner">{message}</div>}
      {error && <div className="error-banner">{error}</div>}

      <form onSubmit={handleSave} className="profile-form">
        <h3>Company Details</h3>
        <div className="form-group">
          <label>Company Name</label>
          <input type="text" name="companyName" value={formData.companyName || ''} onChange={handleChange} />
        </div>
        <div className="form-row">
          <div className="form-group">
            <label>Website</label>
            <input type="text" name="website" value={formData.website || ''} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Industry</label>
            <input type="text" name="industry" value={formData.industry || ''} onChange={handleChange} />
          </div>
        </div>
        <div className="form-group">
          <label>Description</label>
          <input type="text" name="description" value={formData.description || ''} onChange={handleChange} />
        </div>

        <h3>Contact Person</h3>
        <div className="form-row">
          <div className="form-group">
            <label>Contact Person Name</label>
            <input type="text" name="contactPersonName" value={formData.contactPersonName || ''} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Contact Phone Number</label>
            <input type="tel" name="contactPhoneNumber" value={formData.contactPhoneNumber || ''} onChange={handleChange} />
          </div>
        </div>

        <button type="submit" className="btn-primary" disabled={saving}>
          {saving ? 'Saving...' : 'Save Profile'}
        </button>
      </form>

      {/* ===================== Module 4: Job Postings ===================== */}
      <div className="jobs-section">
        <div className="jobs-section-header">
          <h3>My Job Postings</h3>
          {!showJobForm && (
            <button className="btn-primary" onClick={handleOpenNewJobForm}>
              + Post New Job
            </button>
          )}
        </div>

        {showJobForm && (
          <JobForm
            existingJob={editingJob}
            onSubmit={handleJobSubmit}
            onCancel={handleCancelJobForm}
            saving={jobSaving}
          />
        )}

        {jobsLoading ? (
          <p>Loading jobs...</p>
        ) : jobs.length === 0 ? (
          <p className="resume-status">No jobs posted yet. Click "Post New Job" to add one.</p>
        ) : (
          <div className="job-list">
            {jobs.map((job) => (
              <div key={job.id} className="job-card">
                <div className="job-card-header">
                  <h4>{job.title}</h4>
                  <span className={`job-status job-status-${job.status?.toLowerCase()}`}>{job.status}</span>
                </div>
                <p className="job-card-meta">
                  {job.jobType} · {job.location || 'Location not specified'}
                  {job.salary ? ` · ₹${job.salary.toLocaleString()}/year` : ''}
                </p>
                {job.description && <p className="job-card-description">{job.description}</p>}
                {job.requiredSkills && (
                  <p className="job-card-skills"><strong>Skills:</strong> {job.requiredSkills}</p>
                )}
                <div className="job-card-actions">
                  <button className="link-button" onClick={() => handleViewApplicants(job)}>View Applicants</button>
                  <button className="link-button" onClick={() => handleOpenEditJobForm(job)}>Edit</button>
                  <button className="link-button link-button-danger" onClick={() => handleDeleteJob(job.id)}>Delete</button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* ===================== Module 5 + Interview: Applicants Panel ===================== */}
      {viewingApplicantsForJob && (
        <div className="jobs-section">
          <div className="jobs-section-header">
            <h3>Applicants for "{viewingApplicantsForJob.title}"</h3>
            <button className="btn-secondary" onClick={handleCloseApplicants}>Close</button>
          </div>

          {applicantsLoading ? (
            <p>Loading applicants...</p>
          ) : applicants.length === 0 ? (
            <p className="resume-status">No applications yet for this job.</p>
          ) : (
            <div className="job-list">
              {applicants.map((app) => {
                const interview = getInterviewForApplication(app.id);
                return (
                  <div key={app.id} className="job-card">
                    <div className="job-card-header">
                      <h4>{app.studentName}</h4>
                      <span className={`job-status job-status-${app.status?.toLowerCase()}`}>{app.status}</span>
                    </div>
                    <p className="job-card-meta">{app.studentEmail}</p>
                    <p className="job-card-meta">
                      Applied on {new Date(app.appliedDate).toLocaleDateString()}
                    </p>
                    <div className="form-group" style={{ marginTop: '10px', maxWidth: '220px' }}>
                      <label>Update Status</label>
                      <select
                        value={app.status}
                        onChange={(e) => handleStatusChange(app.id, e.target.value)}
                        disabled={updatingStatusId === app.id}
                      >
                        <option value="APPLIED">Applied</option>
                        <option value="SHORTLISTED">Shortlisted</option>
                        <option value="SELECTED">Selected</option>
                        <option value="REJECTED">Rejected</option>
                      </select>
                    </div>

                    {/* ===================== Interview section per applicant ===================== */}
                    {interview ? (
                      <div className="interview-box">
                        <p className="job-card-meta">
                          📅 Interview: {new Date(interview.scheduledAt).toLocaleString()} ({interview.mode})
                        </p>
                        {interview.locationOrLink && (
                          <p className="job-card-meta">📍 {interview.locationOrLink}</p>
                        )}
                        <div className="form-row" style={{ marginTop: '8px' }}>
                          <div className="form-group">
                            <label>Interview Status</label>
                            <select
                              value={interview.status}
                              onChange={(e) => handleResultChange(interview.id, 'status', e.target.value)}
                              disabled={updatingResultId === interview.id}
                            >
                              <option value="SCHEDULED">Scheduled</option>
                              <option value="COMPLETED">Completed</option>
                              <option value="CANCELLED">Cancelled</option>
                            </select>
                          </div>
                          <div className="form-group">
                            <label>Result</label>
                            <select
                              value={interview.result}
                              onChange={(e) => handleResultChange(interview.id, 'result', e.target.value)}
                              disabled={updatingResultId === interview.id}
                            >
                              <option value="PENDING">Pending</option>
                              <option value="PASSED">Passed</option>
                              <option value="FAILED">Failed</option>
                            </select>
                          </div>
                        </div>
                      </div>
                    ) : schedulingForApplicationId === app.id ? (
                      <InterviewForm
                        onSubmit={handleScheduleSubmit}
                        onCancel={handleCancelScheduleForm}
                        saving={interviewSaving}
                      />
                    ) : (
                      <div className="job-card-actions">
                        <button className="link-button" onClick={() => handleOpenScheduleForm(app.id)}>
                          Schedule Interview
                        </button>
                      </div>
                    )}
                  </div>
                );
              })}
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default CompanyProfile;