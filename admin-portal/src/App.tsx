
import { useMemo, useState } from 'react'
import './App.css'

type MiniApp = {
  appId: string
  appName: string
  description: string
  category: string
  status: 'ACTIVE' | 'INACTIVE'
  createdAt: string
}

const sampleApps: MiniApp[] = [
  {
    appId: 'APP-001',
    appName: 'Food Delivery',
    description: 'Order food from your favourite restaurants.',
    category: 'Food & Dining',
    status: 'ACTIVE',
    createdAt: '2026-10-20',
  },
  {
    appId: 'APP-002',
    appName: 'Online Shopping',
    description: 'Discover and shop for products online.',
    category: 'Shopping',
    status: 'ACTIVE',
    createdAt: '2026-10-22',
  },
]

function App() {
  const [apps] = useState<MiniApp[]>(sampleApps)
  const [search, setSearch] = useState('')
  const [category, setCategory] = useState('All Categories')
  const [status, setStatus] = useState('All Statuses')
  const [selectedApp, setSelectedApp] = useState<MiniApp | null>(null)

  const filteredApps = useMemo(() => {
    return apps.filter((app) => {
      const matchesSearch =
        app.appName.toLowerCase().includes(search.toLowerCase()) ||
        app.description.toLowerCase().includes(search.toLowerCase())

      const matchesCategory =
        category === 'All Categories' || app.category === category

      const matchesStatus =
        status === 'All Statuses' || app.status === status

      return matchesSearch && matchesCategory && matchesStatus
    })
  }, [apps, search, category, status])

  const activeCount = apps.filter((app) => app.status === 'ACTIVE').length
  const inactiveCount = apps.filter((app) => app.status === 'INACTIVE').length

  return (
    <div className="admin-layout">
      <aside className="sidebar">
        <div className="brand">
          <div className="brand-logo">B</div>
          <div>
            <h2>Blend-Ah</h2>
            <span>Admin Portal</span>
          </div>
        </div>

        <div className="sidebar-label">MAIN MENU</div>

        <nav className="sidebar-nav">
          <a href="#dashboard" className="nav-item">
            <span className="nav-icon">▦</span>
            Dashboard
          </a>
          <a href="#mini-apps" className="nav-item active">
            <span className="nav-icon">▣</span>
            Mini Apps
            <span className="nav-arrow">›</span>
          </a>
          <a href="#users" className="nav-item">
            <span className="nav-icon">♙</span>
            User Management
          </a>
          <a href="#settings" className="nav-item">
            <span className="nav-icon">⚙</span>
            Settings
          </a>
        </nav>

        <div className="sidebar-bottom">
          <div className="admin-profile">
            <div className="avatar">A</div>
            <div>
              <strong>Administrator</strong>
              <span>System Admin</span>
            </div>
          </div>
        </div>
      </aside>

      <main className="main-content">
        <header className="topbar">
          <div>
            <span className="breadcrumb">Admin / Mini Apps</span>
            <h1>Mini App Management</h1>
          </div>
          <div className="topbar-right">
            <button className="notification-button" aria-label="Notifications">
              ♧
            </button>
            <div className="topbar-avatar">A</div>
          </div>
        </header>

        <section className="page-content" id="mini-apps">
          <div className="page-heading">
            <div>
              <h2>Mini App Listing</h2>
              <p>View and manage all mini applications in Blend-Ah.</p>
            </div>
            <span className="page-tag">Application Management</span>
          </div>

          <div className="stats-grid">
            <div className="stat-card">
              <div className="stat-icon blue">▣</div>
              <div>
                <span>Total Mini Apps</span>
                <h3>{apps.length}</h3>
              </div>
            </div>

            <div className="stat-card">
              <div className="stat-icon green">✓</div>
              <div>
                <span>Active Apps</span>
                <h3>{activeCount}</h3>
              </div>
            </div>

            <div className="stat-card">
              <div className="stat-icon orange">◷</div>
              <div>
                <span>Inactive Apps</span>
                <h3>{inactiveCount}</h3>
              </div>
            </div>
          </div>

          <section className="listing-card">
            <div className="listing-header">
              <div>
                <h3>All Mini Applications</h3>
                <p>Browse and view the available mini applications.</p>
              </div>
              <span className="result-count">{filteredApps.length} Applications</span>
            </div>

            <div className="filter-bar">
              <div className="search-box">
                <span>⌕</span>
                <input
                  type="text"
                  placeholder="Search mini apps..."
                  value={search}
                  onChange={(event) => setSearch(event.target.value)}
                />
              </div>

              <select
                value={category}
                onChange={(event) => setCategory(event.target.value)}
                aria-label="Filter by category"
              >
                <option>All Categories</option>
                <option>Food & Dining</option>
                <option>Shopping</option>
              </select>

              <select
                value={status}
                onChange={(event) => setStatus(event.target.value)}
                aria-label="Filter by status"
              >
                <option>All Statuses</option>
                <option value="ACTIVE">Active</option>
                <option value="INACTIVE">Inactive</option>
              </select>
            </div>

            <div className="table-wrapper">
              <table>
                <thead>
                  <tr>
                    <th>MINI APP</th>
                    <th>CATEGORY</th>
                    <th>STATUS</th>
                    <th>CREATED DATE</th>
                    <th>ACTION</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredApps.map((app) => (
                    <tr key={app.appId}>
                      <td>
                        <div className="app-info">
                          <div className="app-icon">
                            {app.appName === 'Food Delivery' ? '🍔' : '🛍️'}
                          </div>
                          <div>
                            <strong>{app.appName}</strong>
                            <span>{app.description}</span>
                          </div>
                        </div>
                      </td>
                      <td>{app.category}</td>
                      <td>
                        <span
                          className={`status-badge ${
                            app.status === 'ACTIVE' ? 'status-active' : 'status-inactive'
                          }`}
                        >
                          <span className="status-dot"></span>
                          {app.status}
                        </span>
                      </td>
                      <td>{app.createdAt}</td>
                      <td>
                        <button
                          className="view-button"
                          onClick={() => setSelectedApp(app)}
                        >
                          View Details
                        </button>
                      </td>
                    </tr>
                  ))}
                  {filteredApps.length === 0 && (
                    <tr>
                      <td colSpan={5} className="empty-state">
                        No mini applications found.
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>

            <div className="table-footer">
              Showing {filteredApps.length} of {apps.length} applications
            </div>
          </section>
        </section>
      </main>

      {selectedApp && (
        <div className="modal-backdrop" onClick={() => setSelectedApp(null)}>
          <div
            className="details-modal"
            onClick={(event) => event.stopPropagation()}
          >
            <div className="modal-header">
              <div>
                <span>MINI APP DETAILS</span>
                <h2>{selectedApp.appName}</h2>
              </div>
              <button
                className="close-button"
                onClick={() => setSelectedApp(null)}
                aria-label="Close details"
              >
                ×
              </button>
            </div>

            <div className="modal-body">
              <p>{selectedApp.description}</p>
              <div className="detail-row">
                <span>Application ID</span>
                <strong>{selectedApp.appId}</strong>
              </div>
              <div className="detail-row">
                <span>Category</span>
                <strong>{selectedApp.category}</strong>
              </div>
              <div className="detail-row">
                <span>Status</span>
                <strong>{selectedApp.status}</strong>
              </div>
              <div className="detail-row">
                <span>Created Date</span>
                <strong>{selectedApp.createdAt}</strong>
              </div>
            </div>

            <div className="modal-footer">
              <button
                className="close-modal-button"
                onClick={() => setSelectedApp(null)}
              >
                Close
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default App
