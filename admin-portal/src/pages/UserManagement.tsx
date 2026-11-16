
import { useEffect, useState } from 'react'
import { getUsers, deactivateUser, activateUser  } from '../services/userService'
import UserDetailsModal from '../components/users/UserDetailsModal'
import type { User } from '../types/user'
import './UserManagement.css'

export default function UserManagement() {
  const [users, setUsers] = useState<User[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [search, setSearch] = useState('')
  const [selectedUserId, setSelectedUserId] = useState<string | null>(null)
  const [updatingUserId, setUpdatingUserId] = useState<string | null>(null)

  useEffect(() => {
    getUsers()
      .then(setUsers)
      .catch(() => setError('Unable to load users'))
      .finally(() => setLoading(false))
  }, [])

  const filteredUsers = users.filter(user =>
    `${user.fullName} ${user.email}`
      .toLowerCase()
      .includes(search.toLowerCase())
  )


async function handleDeactivate(user: User) {
  if (!window.confirm(
    `Are you sure you want to deactivate ${user.fullName}?`
  )) {
    return
  }

  setUpdatingUserId(user.userId)

  try {
    const updatedUser = await deactivateUser(user.userId)

    setUsers(previous =>
      previous.map(existing =>
        existing.userId === updatedUser.userId
          ? updatedUser
          : existing
      )
    )
  } catch (error) {
    alert(
      error instanceof Error
        ? error.message
        : 'Unable to deactivate user'
    )
  } finally {
    setUpdatingUserId(null)
  }
}

async function handleActivate(user: User) {
  if (!window.confirm(
    `Are you sure you want to activate ${user.fullName}?`
  )) {
    return
  }

  setUpdatingUserId(user.userId)

  try {
    const updatedUser = await activateUser(user.userId)

    setUsers(previous =>
      previous.map(existing =>
        existing.userId === updatedUser.userId
          ? updatedUser
          : existing
      )
    )
  } catch (error) {
    alert(
      error instanceof Error
        ? error.message
        : 'Unable to activate user'
    )
  } finally {
    setUpdatingUserId(null)
  }
}



  return (
    <div className="users-page">
      <div className="users-header">
        <div>
          <h1>User Management</h1>
          <p>Manage registered Blend-ah users.</p>
        </div>
      </div>

      <div className="users-card">
        <div className="users-toolbar">
          <h2>Registered Users ({users.length})</h2>

          <input
            type="search"
            placeholder="Search users..."
            value={search}
            onChange={e => setSearch(e.target.value)}
          />
        </div>

        {loading ? (
          <p>Loading users...</p>
        ) : error ? (
          <p>{error}</p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Status</th>
                <th>Registered</th>
                <th>Actions</th>
              </tr>
            </thead>

            <tbody>
              {filteredUsers.map(user => (
                <tr key={user.userId}>
                  <td>{user.fullName}</td>
                  <td>{user.email}</td>
                  <td>{user.role}</td>
                  <td>
                    <span className={
                      user.userStatus
                        ? 'status active'
                        : 'status inactive'
                    }>
                      {user.userStatus ? 'Active' : 'Inactive'}
                    </span>
                  </td>
                  <td>
                    {user.createdAt
                      ? new Date(user.createdAt).toLocaleDateString()
                      : '—'}
                  </td>
                  <td>
                    <button
                      type="button"
                      className="view-details-btn"
                      onClick={() => setSelectedUserId(user.userId)}
                    >
                      View Details
                    </button>


              {user.userStatus && (
                    <button
                      type="button"
                      className="deactivate-btn"
                      disabled={updatingUserId === user.userId}
                      onClick={() => handleDeactivate(user)}
                    >
                      {updatingUserId === user.userId
                        ? 'Deactivating...'
                        : 'Deactivate'}
                    </button>
                )}


              {!user.userStatus && (
                <button
                  type="button"
                  className="activate-btn"
                  disabled={updatingUserId === user.userId}
                  onClick={() => handleActivate(user)}
                >
                  {updatingUserId === user.userId
                    ? 'Activating...'
                    : 'Activate'}
                </button>
              )}


                  </td>
                </tr>
              ))}

              {filteredUsers.length === 0 && (
                <tr>
                  <td colSpan={6}>No users found.</td>
                </tr>
              )}
            </tbody>
          </table>
        )}
      </div>

      {selectedUserId && (
        <UserDetailsModal
          userId={selectedUserId}
          onClose={() => setSelectedUserId(null)}
        />
      )}
    </div>
  )
}
