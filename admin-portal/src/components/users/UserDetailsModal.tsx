
import { useEffect, useState } from 'react'
import { getUserById } from '../../services/userService'
import type { User } from '../../types/user'
import './UserDetailsModal.css'

interface Props {
  userId: string
  onClose: () => void
}

export default function UserDetailsModal({
  userId,
  onClose,
}: Props) {
  const [user, setUser] = useState<User | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    let cancelled = false

    getUserById(userId)
      .then(data => {
        if (!cancelled) setUser(data)
      })
      .catch(err => {
        if (!cancelled) {
          setError(err instanceof Error ? err.message : 'Request failed')
        }
      })
      .finally(() => {
        if (!cancelled) setLoading(false)
      })

    return () => {
      cancelled = true
    }
  }, [userId])

  return (
    <div className="user-modal-backdrop" onClick={onClose}>
      <div
        className="user-modal"
        role="dialog"
        aria-modal="true"
        aria-labelledby="user-details-title"
        onClick={e => e.stopPropagation()}
      >
        <div className="user-modal-header">
          <h2 id="user-details-title">User Details</h2>
          <button onClick={onClose} aria-label="Close">×</button>
        </div>

        {loading ? (
          <p>Loading user details...</p>
        ) : error ? (
          <p role="alert">{error}</p>
        ) : user && (
          <div className="user-modal-body">
            <div className="user-modal-profile">
              {user.profileImageUrl ? (
                <img
                  src={user.profileImageUrl}
                  alt="Profile"
                  onError={e => {
                    e.currentTarget.style.display = 'none'
                  }}
                />
              ) : (
                <div className="user-modal-avatar">
                  {user.fullName.charAt(0).toUpperCase()}
                </div>
              )}
              <h3>{user.fullName}</h3>
              <span>{user.email}</span>
            </div>

            <div className="user-detail-row">
              <span>User ID</span>
              <strong>{user.userId}</strong>
            </div>

            <div className="user-detail-row">
              <span>Full Name</span>
              <strong>{user.fullName}</strong>
            </div>

            <div className="user-detail-row">
              <span>Email</span>
              <strong>{user.email}</strong>
            </div>

            <div className="user-detail-row">
              <span>Role</span>
              <strong>{user.role}</strong>
            </div>

            <div className="user-detail-row">
              <span>Account Status</span>
              <strong className={
                user.userStatus ? 'user-active' : 'user-inactive'
              }>
                {user.userStatus ? 'Active' : 'Inactive'}
              </strong>
            </div>

            <div className="user-detail-row">
              <span>Registered</span>
              <strong>
                {user.createdAt
                  ? new Date(user.createdAt).toLocaleString()
                  : '—'}
              </strong>
            </div>
          </div>
        )}

        <div className="user-modal-footer">
          <button onClick={onClose}>Close</button>
        </div>
      </div>
    </div>
  )
}
