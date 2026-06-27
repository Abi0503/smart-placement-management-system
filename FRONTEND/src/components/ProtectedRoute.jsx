import React from 'react';
import { Navigate } from 'react-router-dom';

// Wraps any page that should only be visible to logged-in students.
// If there's no token in localStorage, redirect to /login.
function ProtectedRoute({ children }) {
  const token = localStorage.getItem('token');

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  return children;
}

export default ProtectedRoute;