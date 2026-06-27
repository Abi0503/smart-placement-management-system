import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { loginStudent } from '../services/authService';

function Login() {
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
      const data = await loginStudent(formData);

      localStorage.setItem('token', data.token);
      localStorage.setItem('student', JSON.stringify(data));
      localStorage.setItem('userType', 'STUDENT');

      navigate('/dashboard');
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
        <p className="subtitle">Login to your student account</p>

        {error && <div className="error-banner">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="you@example.com"
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
          Don't have an account? <Link to="/register">Register here</Link>
        </div>
        <div className="switch-link">
          Are you a company? <Link to="/company/login">Company Login</Link>
        </div>
        <div className="switch-link">
          <Link to="/admin/login">Admin Login</Link>
        </div>
      </div>
    </div>
  );
}

export default Login;