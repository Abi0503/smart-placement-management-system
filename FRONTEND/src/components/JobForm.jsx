import React, { useState, useEffect } from 'react';

/**
 * Reusable form for creating a NEW job or editing an EXISTING one.
 * If `existingJob` is passed in, the form pre-fills with its data (edit mode).
 * If not, it starts blank (create mode).
 *
 * Props:
 *   existingJob: the job object to edit, or null/undefined for a new job
 *   onSubmit: function called with the form data when the user submits
 *   onCancel: function called when the user clicks Cancel
 *   saving: boolean, true while the save request is in progress
 */
function JobForm({ existingJob, onSubmit, onCancel, saving }) {
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    requiredSkills: '',
    location: '',
    jobType: 'Full-time',
    salary: '',
  });

  useEffect(() => {
    if (existingJob) {
      setFormData({
        title: existingJob.title || '',
        description: existingJob.description || '',
        requiredSkills: existingJob.requiredSkills || '',
        location: existingJob.location || '',
        jobType: existingJob.jobType || 'Full-time',
        salary: existingJob.salary ?? '',
      });
    }
  }, [existingJob]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const payload = {
      ...formData,
      salary: formData.salary ? parseFloat(formData.salary) : null,
    };
    onSubmit(payload);
  };

  return (
    <form onSubmit={handleSubmit} className="job-form">
      <div className="form-group">
        <label>Job Title</label>
        <input
          type="text"
          name="title"
          value={formData.title}
          onChange={handleChange}
          placeholder="e.g. Software Engineer Intern"
          required
        />
      </div>

      <div className="form-group">
        <label>Description</label>
        <input
          type="text"
          name="description"
          value={formData.description}
          onChange={handleChange}
          placeholder="Brief description of the role"
        />
      </div>

      <div className="form-group">
        <label>Required Skills (comma-separated)</label>
        <input
          type="text"
          name="requiredSkills"
          value={formData.requiredSkills}
          onChange={handleChange}
          placeholder="e.g. Java, Spring Boot, SQL"
        />
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Location</label>
          <input
            type="text"
            name="location"
            value={formData.location}
            onChange={handleChange}
            placeholder="e.g. Chennai / Remote"
          />
        </div>
        <div className="form-group">
          <label>Job Type</label>
          <select name="jobType" value={formData.jobType} onChange={handleChange}>
            <option value="Full-time">Full-time</option>
            <option value="Internship">Internship</option>
            <option value="Part-time">Part-time</option>
          </select>
        </div>
      </div>

      <div className="form-group">
        <label>Salary (per annum, optional)</label>
        <input
          type="number"
          step="0.01"
          name="salary"
          value={formData.salary}
          onChange={handleChange}
          placeholder="e.g. 600000"
        />
      </div>

      <div className="job-form-actions">
        <button type="submit" className="btn-primary" disabled={saving}>
          {saving ? 'Saving...' : existingJob ? 'Update Job' : 'Post Job'}
        </button>
        <button type="button" className="btn-secondary" onClick={onCancel}>
          Cancel
        </button>
      </div>
    </form>
  );
}

export default JobForm;