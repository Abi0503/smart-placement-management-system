import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { loginCompany } from '../services/companyAuthService';

function CompanyLogin() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    email: '',
    password: '',
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
      const data = await loginCompany(formData);

      localStorage.setItem('token', data.token);
      localStorage.setItem('student', JSON.stringify(data)); // reuse the same key for simplicity
      localStorage.setItem('userType', 'COMPANY');

      navigate('/company/dashboard');
    } catch (err) {
      const message = err.response?.data?.message || 'Login failed. Please check your credentials.';
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h1>Smart Placement Management System</h1>
        <p className="subtitle">Company Login</p>

        {error && <div className="error-banner">{error}</div>}

        <form onSubmit={handleSubmit}>
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
              placeholder="Enter your password"
              required
            />
          </div>

          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>

        <div className="switch-link">
          New company? <Link to="/company/register">Register here</Link>
        </div>
        <div className="switch-link">
          Are you a student? <Link to="/login">Student Login</Link>
        </div>
      </div>
    </div>
  );
}

export default CompanyLogin;