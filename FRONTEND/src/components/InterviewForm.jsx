import React, { useState } from 'react';

/**
 * Small form for scheduling an interview for a specific applicant.
 * Props:
 *   onSubmit: function called with the form data when submitted
 *   onCancel: function called when the user clicks Cancel
 *   saving: boolean, true while the schedule request is in progress
 */
function InterviewForm({ onSubmit, onCancel, saving }) {
  const [formData, setFormData] = useState({
    scheduledAt: '',
    mode: 'Online',
    locationOrLink: '',
    notes: '',
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // datetime-local input gives "YYYY-MM-DDTHH:mm" which Spring Boot's
    // LocalDateTime can parse directly -- no conversion needed.
    onSubmit(formData);
  };

  return (
    <form onSubmit={handleSubmit} className="job-form">
      <div className="form-group">
        <label>Date &amp; Time</label>
        <input
          type="datetime-local"
          name="scheduledAt"
          value={formData.scheduledAt}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-group">
        <label>Mode</label>
        <select name="mode" value={formData.mode} onChange={handleChange}>
          <option value="Online">Online</option>
          <option value="Offline">Offline</option>
        </select>
      </div>

      <div className="form-group">
        <label>{formData.mode === 'Online' ? 'Meeting Link' : 'Venue Address'}</label>
        <input
          type="text"
          name="locationOrLink"
          value={formData.locationOrLink}
          onChange={handleChange}
          placeholder={formData.mode === 'Online' ? 'e.g. https://meet.google.com/xyz' : 'e.g. Office address'}
        />
      </div>

      <div className="form-group">
        <label>Notes (optional)</label>
        <input
          type="text"
          name="notes"
          value={formData.notes}
          onChange={handleChange}
          placeholder="e.g. Bring your laptop, round 1 technical"
        />
      </div>

      <div className="job-form-actions">
        <button type="submit" className="btn-primary" disabled={saving}>
          {saving ? 'Scheduling...' : 'Schedule Interview'}
        </button>
        <button type="button" className="btn-secondary" onClick={onCancel}>
          Cancel
        </button>
      </div>
    </form>
  );
}

export default InterviewForm;