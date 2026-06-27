import React from 'react';
import { useNavigate } from 'react-router-dom';

function Dashboard() {
  const navigate = useNavigate();
  const student = JSON.parse(localStorage.getItem('student'));

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('student');
    navigate('/login');
  };

  return (
    <div className="dashboard">
      <h2>Welcome, {student?.fullName} 👋</h2>
      <p>
        Email: {student?.email} <br />
        Role: {student?.role}
      </p>
      <p style={{ fontSize: '13px', color: '#94a3b8' }}>
        This is a placeholder dashboard. The Student Module (Profile, Skills, Resume Upload)
        will be built next and will replace this page.
      </p>
      <button className="btn-logout" onClick={handleLogout}>
        Logout
      </button>
    </div>
  );
}

export default Dashboard;