import React, { useEffect, useState, useRef } from 'react';
import { getMyNotifications, getUnreadCount, markAsRead, markAllAsRead } from '../services/notificationService';

/**
 * A bell icon with unread count badge. Click to open a dropdown panel
 * showing recent notifications. Works for BOTH students and companies --
 * it just calls /api/notifications, which returns whichever account
 * is currently logged in (identified by the JWT token).
 */
function NotificationBell() {
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [isOpen, setIsOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const panelRef = useRef(null);

  useEffect(() => {
    loadUnreadCount();
    // Poll every 30 seconds so the badge updates without needing a page refresh
    const interval = setInterval(loadUnreadCount, 30000);
    return () => clearInterval(interval);
  }, []);

  // Close the dropdown if the user clicks outside it
  useEffect(() => {
    const handleClickOutside = (e) => {
      if (panelRef.current && !panelRef.current.contains(e.target)) {
        setIsOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const loadUnreadCount = async () => {
    try {
      const count = await getUnreadCount();
      setUnreadCount(count);
    } catch (err) {
      console.error('Could not load unread count', err);
    }
  };

  const handleToggle = async () => {
    const opening = !isOpen;
    setIsOpen(opening);

    if (opening) {
      setLoading(true);
      try {
        const data = await getMyNotifications();
        setNotifications(data);
      } catch (err) {
        console.error('Could not load notifications', err);
      } finally {
        setLoading(false);
      }
    }
  };

  const handleNotificationClick = async (notification) => {
    if (!notification.isRead) {
      try {
        await markAsRead(notification.id);
        setNotifications((prev) =>
          prev.map((n) => (n.id === notification.id ? { ...n, isRead: true } : n))
        );
        setUnreadCount((prev) => Math.max(0, prev - 1));
      } catch (err) {
        console.error('Could not mark as read', err);
      }
    }
  };

  const handleMarkAllRead = async () => {
    try {
      await markAllAsRead();
      setNotifications((prev) => prev.map((n) => ({ ...n, isRead: true })));
      setUnreadCount(0);
    } catch (err) {
      console.error('Could not mark all as read', err);
    }
  };

  const formatTimeAgo = (dateString) => {
    const date = new Date(dateString);
    const now = new Date();
    const diffMs = now - date;
    const diffMins = Math.floor(diffMs / 60000);

    if (diffMins < 1) return 'just now';
    if (diffMins < 60) return `${diffMins}m ago`;
    const diffHours = Math.floor(diffMins / 60);
    if (diffHours < 24) return `${diffHours}h ago`;
    const diffDays = Math.floor(diffHours / 24);
    return `${diffDays}d ago`;
  };

  return (
    <div className="notification-bell-wrapper" ref={panelRef}>
      <button className="notification-bell-button" onClick={handleToggle}>
        🔔
        {unreadCount > 0 && <span className="notification-badge">{unreadCount}</span>}
      </button>

      {isOpen && (
        <div className="notification-panel">
          <div className="notification-panel-header">
            <span>Notifications</span>
            {notifications.length > 0 && (
              <button className="link-button" onClick={handleMarkAllRead}>Mark all read</button>
            )}
          </div>

          <div className="notification-list">
            {loading ? (
              <p className="notification-empty">Loading...</p>
            ) : notifications.length === 0 ? (
              <p className="notification-empty">No notifications yet.</p>
            ) : (
              notifications.map((n) => (
                <div
                  key={n.id}
                  className={`notification-item ${!n.isRead ? 'notification-unread' : ''}`}
                  onClick={() => handleNotificationClick(n)}
                >
                  <p className="notification-title">{n.title}</p>
                  <p className="notification-message">{n.message}</p>
                  <p className="notification-time">{formatTimeAgo(n.createdAt)}</p>
                </div>
              ))
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default NotificationBell;