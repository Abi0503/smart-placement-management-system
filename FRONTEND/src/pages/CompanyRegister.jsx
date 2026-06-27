import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { registerCompany } from '../services/companyAuthService';

function CompanyRegister() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    companyName: '',
    email: '',
    password: '',
    website: '',
    industry: '',
    description: '',
    contactPersonName: '',
    contactPhoneNumber: '',
  });

  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const data = await registerCompany(formData);

      localStorage.setItem('token', data.token);
      localStorage.setItem('student', JSON.stringify(data));
      localStorage.setItem('userType', 'COMPANY');

      navigate('/company/dashboard');
    } catch (err) {
      const message = err.response?.data?.message || 'Registration failed. Please try again.';
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h1>Smart Placement Management System</h1>
        <p className="subtitle">Create your company account</p>

        {error && <div className="error-banner">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Company Name</label>
            <input
              type="text"
              name="companyName"
              value={formData.companyName}
              onChange={handleChange}
              placeholder="e.g. Acme Technologies Pvt Ltd"
              required
            />
          </div>

          <div className="form-group">
            <label>Company Email</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="hr@company.com"
              required
            />
          </div>

          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="At least 6 characters"
              required
              minLength={6}
            />
          </div>

          <div className="form-group">
            <label>Website</label>
            <input
              type="text"
              name="website"
              value={formData.website}
              onChange={handleChange}
              placeholder="https://www.company.com"
            />
          </div>

          <div className="form-group">
            <label>Industry</label>
            <input
              type="text"
              name="industry"
              value={formData.industry}
              onChange={handleChange}
              placeholder="e.g. IT Services, Manufacturing"
            />
          </div>

          <div className="form-group">
            <label>Contact Person Name</label>
            <input
              type="text"
              name="contactPersonName"
              value={formData.contactPersonName}
              onChange={handleChange}
              placeholder="HR Manager name"
            />
          </div>

          <div className="form-group">
            <label>Contact Phone Number</label>
            <input
              type="tel"
              name="contactPhoneNumber"
              value={formData.contactPhoneNumber}
              onChange={handleChange}
              placeholder="10-digit phone number"
            />
          </div>

          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Creating account...' : 'Register'}
          </button>
        </form>

        <div className="switch-link">
          Already registered? <Link to="/company/login">Login here</Link>
        </div>
        <div className="switch-link">
          Are you a student? <Link to="/register">Student Register</Link>
        </div>
      </div>
    </div>
  );
}

export default CompanyRegister;