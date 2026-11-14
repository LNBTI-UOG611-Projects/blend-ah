import { NavLink } from 'react-router-dom'

export default function Sidebar() {
  return (
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
        <NavLink to="/" end className={({ isActive }) =>
          `nav-item ${isActive ? 'active' : ''}`
        }>
          <span className="nav-icon">▦</span>
          Dashboard
        </NavLink>

        <NavLink to="/mini-apps" className={({ isActive }) =>
          `nav-item ${isActive ? 'active' : ''}`
        }>
          <span className="nav-icon">▣</span>
          Mini Apps
        </NavLink>

        <NavLink to="/users" className={({ isActive }) =>
          `nav-item ${isActive ? 'active' : ''}`
        }>
          <span className="nav-icon">♙</span>
          User Management
        </NavLink>
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
  )
}