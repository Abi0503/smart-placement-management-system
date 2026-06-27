import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getProfile, updateProfile, uploadResume } from '../services/studentService';
import studentApi from '../services/studentService';
import { getOpenJobs } from '../services/jobService';
import { applyToJob, getMyApplications } from '../services/applicationService';
import { getMyInterviews } from '../services/interviewService';
import NotificationBell from '../components/NotificationBell';

function Profile() {
  const navigate = useNavigate();

  const [profile, setProfile] = useState(null);
  const [formData, setFormData] = useState({});
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [resumeFile, setResumeFile] = useState(null);
  const [uploadingResume, setUploadingResume] = useState(false);

  // Module 5: Jobs + Applications
  const [activeTab, setActiveTab] = useState('profile'); // 'profile' | 'jobs' | 'applications' | 'interviews'
  const [openJobs, setOpenJobs] = useState([]);
  const [jobsLoading, setJobsLoading] = useState(true);
  const [myApplications, setMyApplications] = useState([]);
  const [applicationsLoading, setApplicationsLoading] = useState(true);
  const [applyingJobId, setApplyingJobId] = useState(null);

  // Interview Module
  const [myInterviews, setMyInterviews] = useState([]);
  const [interviewsLoading, setInterviewsLoading] = useState(true);

  useEffect(() => {
    loadProfile();
    loadOpenJobs();
    loadMyApplications();
    loadMyInterviews();
  }, []);

  const loadProfile = async () => {
    try {
      const data = await getProfile();
      setProfile(data);
      setFormData(data);
    } catch (err) {
      if (err.response?.status === 401 || err.response?.status === 403) {
        navigate('/login');
      } else {
        setError('Could not load profile. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  const loadOpenJobs = async () => {
    try {
      const data = await getOpenJobs();
      setOpenJobs(data);
    } catch (err) {
      console.error('Could not load jobs', err);
    } finally {
      setJobsLoading(false);
    }
  };

  const loadMyApplications = async () => {
    try {
      const data = await getMyApplications();
      setMyApplications(data);
    } catch (err) {
      console.error('Could not load applications', err);
    } finally {
      setApplicationsLoading(false);
    }
  };

  const loadMyInterviews = async () => {
    try {
      const data = await getMyInterviews();
      setMyInterviews(data);
    } catch (err) {
      console.error('Could not load interviews', err);
    } finally {
      setInterviewsLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setSaving(true);
    setMessage('');
    setError('');

    try {
      const payload = {
        ...formData,
        tenthPercentage: formData.tenthPercentage ? parseFloat(formData.tenthPercentage) : null,
        twelfthPercentage: formData.twelfthPercentage ? parseFloat(formData.twelfthPercentage) : null,
        cgpa: formData.cgpa ? parseFloat(formData.cgpa) : null,
        passingYear: formData.passingYear ? parseInt(formData.passingYear) : null,
      };

      const updated = await updateProfile(payload);
      setProfile(updated);
      setFormData(updated);
      setMessage('Profile updated successfully!');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update profile.');
    } finally {
      setSaving(false);
    }
  };

  const handleResumeChange = (e) => {
    setResumeFile(e.target.files[0]);
  };

  const handleResumeUpload = async () => {
    if (!resumeFile) {
      setError('Please choose a PDF file first.');
      return;
    }

    setUploadingResume(true);
    setMessage('');
    setError('');

    try {
      const updated = await uploadResume(resumeFile);
      setProfile(updated);
      setFormData(updated);
      setResumeFile(null);
      setMessage('Resume uploaded successfully!');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to upload resume.');
    } finally {
      setUploadingResume(false);
    }
  };

  const handleViewResume = async () => {
    try {
      const response = await studentApi.get('/resume', { responseType: 'blob' });
      const fileURL = window.URL.createObjectURL(response.data);
      window.open(fileURL, '_blank');
    } catch (err) {
      setError('Could not open resume.');
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('student');
    localStorage.removeItem('userType');
    navigate('/login');
  };

  const handleApply = async (jobId) => {
    setApplyingJobId(jobId);
    setError('');
    setMessage('');

    try {
      await applyToJob(jobId);
      setMessage('Application submitted successfully!');
      loadMyApplications();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to apply.');
    } finally {
      setApplyingJobId(null);
    }
  };

  const hasAppliedToJob = (jobId) => {
    return myApplications.some((app) => app.jobId === jobId);
  };

  if (loading) {
    return <div className="dashboard"><p>Loading your profile...</p></div>;
  }

  return (
    <div className="profile-page">
      <div className="profile-header">
        <div>
          <h2>Welcome, {profile?.fullName} 👋</h2>
          <p className="profile-email">{profile?.email}</p>
        </div>
        <div className="header-actions">
          <NotificationBell />
          <button className="btn-logout" onClick={handleLogout}>Logout</button>
        </div>
      </div>

      {message && <div className="success-banner">{message}</div>}
      {error && <div className="error-banner">{error}</div>}

      {/* ===================== Tabs ===================== */}
      <div className="tab-bar">
        <button
          className={`tab-button ${activeTab === 'profile' ? 'tab-active' : ''}`}
          onClick={() => setActiveTab('profile')}
        >
          My Profile
        </button>
        <button
          className={`tab-button ${activeTab === 'jobs' ? 'tab-active' : ''}`}
          onClick={() => setActiveTab('jobs')}
        >
          Browse Jobs
        </button>
        <button
          className={`tab-button ${activeTab === 'applications' ? 'tab-active' : ''}`}
          onClick={() => setActiveTab('applications')}
        >
          My Applications
        </button>
        <button
          className={`tab-button ${activeTab === 'interviews' ? 'tab-active' : ''}`}
          onClick={() => setActiveTab('interviews')}
        >
          My Interviews
        </button>
      </div>

      {/* ===================== Tab: Profile ===================== */}
      {activeTab === 'profile' && (
        <>
          <form onSubmit={handleSave} className="profile-form">
            <h3>Basic Details</h3>
            <div className="form-row">
              <div className="form-group">
                <label>Full Name</label>
                <input type="text" name="fullName" value={formData.fullName || ''} onChange={handleChange} />
              </div>
              <div className="form-group">
                <label>Phone Number</label>
                <input type="tel" name="phoneNumber" value={formData.phoneNumber || ''} onChange={handleChange} />
              </div>
            </div>
            <div className="form-group">
              <label>Department</label>
              <input type="text" name="department" value={formData.department || ''} onChange={handleChange} />
            </div>

            <h3>Academic Details</h3>
            <div className="form-row">
              <div className="form-group">
                <label>College Name</label>
                <input type="text" name="collegeName" value={formData.collegeName || ''} onChange={handleChange} />
              </div>
              <div className="form-group">
                <label>Branch</label>
                <input type="text" name="branch" value={formData.branch || ''} onChange={handleChange} />
              </div>
            </div>
            <div className="form-row">
              <div className="form-group">
                <label>10th Percentage</label>
                <input type="number" step="0.01" name="tenthPercentage" value={formData.tenthPercentage ?? ''} onChange={handleChange} />
              </div>
              <div className="form-group">
                <label>12th Percentage</label>
                <input type="number" step="0.01" name="twelfthPercentage" value={formData.twelfthPercentage ?? ''} onChange={handleChange} />
              </div>
            </div>
            <div className="form-row">
              <div className="form-group">
                <label>CGPA</label>
                <input type="number" step="0.01" name="cgpa" value={formData.cgpa ?? ''} onChange={handleChange} />
              </div>
              <div className="form-group">
                <label>Passing Year</label>
                <input type="number" name="passingYear" value={formData.passingYear ?? ''} onChange={handleChange} />
              </div>
            </div>

            <h3>Skills</h3>
            <div className="form-group">
              <label>Skills (comma-separated)</label>
              <input
                type="text"
                name="skills"
                placeholder="e.g. Java, React, SQL, Spring Boot"
                value={formData.skills || ''}
                onChange={handleChange}
              />
            </div>

            <button type="submit" className="btn-primary" disabled={saving}>
              {saving ? 'Saving...' : 'Save Profile'}
            </button>
          </form>

          <div className="resume-section">
            <h3>Resume</h3>
            {profile?.resumeFileName ? (
              <p className="resume-status">
                ✅ Resume uploaded: <button type="button" className="link-button" onClick={handleViewResume}>View Resume</button>
              </p>
            ) : (
              <p className="resume-status">No resume uploaded yet.</p>
            )}

            <input type="file" accept="application/pdf" onChange={handleResumeChange} />
            <button
              type="button"
              className="btn-primary"
              style={{ marginTop: '10px' }}
              onClick={handleResumeUpload}
              disabled={uploadingResume}
            >
              {uploadingResume ? 'Uploading...' : 'Upload Resume (PDF)'}
            </button>
          </div>
        </>
      )}

      {/* ===================== Tab: Browse Jobs ===================== */}
      {activeTab === 'jobs' && (
        <div className="jobs-section">
          <h3>Open Job Postings</h3>
          {jobsLoading ? (
            <p>Loading jobs...</p>
          ) : openJobs.length === 0 ? (
            <p className="resume-status">No open jobs right now. Check back later!</p>
          ) : (
            <div className="job-list">
              {openJobs.map((job) => (
                <div key={job.id} className="job-card">
                  <div className="job-card-header">
                    <h4>{job.title}</h4>
                    <span className="job-status job-status-open">{job.status}</span>
                  </div>
                  <p className="job-card-meta">
                    {job.companyName} · {job.jobType} · {job.location || 'Location not specified'}
                    {job.salary ? ` · ₹${job.salary.toLocaleString()}/year` : ''}
                  </p>
                  {job.description && <p className="job-card-description">{job.description}</p>}
                  {job.requiredSkills && (
                    <p className="job-card-skills"><strong>Skills:</strong> {job.requiredSkills}</p>
                  )}
                  <div className="job-card-actions">
                    {hasAppliedToJob(job.id) ? (
                      <span className="applied-badge">✅ Already Applied</span>
                    ) : (
                      <button
                        className="btn-primary"
                        style={{ padding: '8px 18px', fontSize: '13px' }}
                        onClick={() => handleApply(job.id)}
                        disabled={applyingJobId === job.id}
                      >
                        {applyingJobId === job.id ? 'Applying...' : 'Apply Now'}
                      </button>
                    )}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* ===================== Tab: My Applications ===================== */}
      {activeTab === 'applications' && (
        <div className="jobs-section">
          <h3>My Applications</h3>
          {applicationsLoading ? (
            <p>Loading applications...</p>
          ) : myApplications.length === 0 ? (
            <p className="resume-status">You haven't applied to any jobs yet.</p>
          ) : (
            <div className="job-list">
              {myApplications.map((app) => (
                <div key={app.id} className="job-card">
                  <div className="job-card-header">
                    <h4>{app.jobTitle}</h4>
                    <span className={`job-status job-status-${app.status?.toLowerCase()}`}>{app.status}</span>
                  </div>
                  <p className="job-card-meta">{app.companyName}</p>
                  <p className="job-card-meta">
                    Applied on {new Date(app.appliedDate).toLocaleDateString()}
                  </p>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* ===================== Tab: My Interviews ===================== */}
      {activeTab === 'interviews' && (
        <div className="jobs-section">
          <h3>My Interviews</h3>
          {interviewsLoading ? (
            <p>Loading interviews...</p>
          ) : myInterviews.length === 0 ? (
            <p className="resume-status">No interviews scheduled yet.</p>
          ) : (
            <div className="job-list">
              {myInterviews.map((interview) => (
                <div key={interview.id} className="job-card">
                  <div className="job-card-header">
                    <h4>{interview.jobTitle}</h4>
                    <span className={`job-status job-status-${interview.status?.toLowerCase()}`}>{interview.status}</span>
                  </div>
                  <p className="job-card-meta">{interview.companyName}</p>
                  <p className="job-card-meta">
                    📅 {new Date(interview.scheduledAt).toLocaleString()} · {interview.mode}
                  </p>
                  {interview.locationOrLink && (
                    <p className="job-card-meta">📍 {interview.locationOrLink}</p>
                  )}
                  {interview.notes && (
                    <p className="job-card-description">{interview.notes}</p>
                  )}
                  <p className="job-card-meta">
                    <strong>Result:</strong>{' '}
                    <span className={`job-status job-status-${interview.result?.toLowerCase()}`}>
                      {interview.result}
                    </span>
                  </p>
                </div>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default Profile;