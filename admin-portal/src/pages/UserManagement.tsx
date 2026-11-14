
import { useEffect, useState } from 'react'
import './UserManagement.css'

interface User {
  userId: string
  fullName: string
  email: string
  role: string
  profileImageUrl: string | null
  createdAt: string | null
  userStatus: boolean
}

export default function UserManagement() {
  const [users, setUsers] = useState<User[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [search, setSearch] = useState('')

  useEffect(() => {
    fetch('/api/admin/users')
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to fetch users')
        }
        return response.json()
      })
      .then((data: User[]) => setUsers(data))
      .catch(() => setError('Unable to load users'))
      .finally(() => setLoading(false))
  }, [])

  const filteredUsers = users.filter(user =>
    `${user.fullName} ${user.email}`
      .toLowerCase()
      .includes(search.toLowerCase())
  )

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
              </tr>
            </thead>

            <tbody>
              {filteredUsers.map(user => (
                <tr key={user.userId}>
                  <td>{user.fullName}</td>
                  <td>{user.email}</td>
                  <td>{user.role}</td>
                  <td>
                    <span
                      className={
                        user.userStatus
                          ? 'status active'
                          : 'status inactive'
                      }
                    >
                      {user.userStatus ? 'Active' : 'Inactive'}
                    </span>
                  </td>
                  <td>
                    {user.createdAt
                      ? new Date(user.createdAt).toLocaleDateString()
                      : '—'}
                  </td>
                </tr>
              ))}

              {filteredUsers.length === 0 && (
                <tr>
                  <td colSpan={5}>No users found.</td>
                </tr>
              )}
            </tbody>
          </table>
        )}
      </div>
    </div>
  )
}
