import type { MiniApp } from '../../types/miniApp'

type Props = {
  apps: MiniApp[]
  onViewDetails: (app: MiniApp) => void
}

export default function MiniAppTable({
  apps,
  onViewDetails,
}: Props) {
  return (
    <div className="table-wrapper">
      <table className="mini-app-table">
        <thead>
          <tr>
            <th>MINI APP</th>
            <th>STATUS</th>
            <th>CREATED DATE</th>
            <th>ACTIONS</th>
          </tr>
        </thead>

        <tbody>
          {apps.map((app) => {
            const published = app.status === 'PUBLISHED'

            return (
              <tr key={app.appId}>
                <td>
                  <div className="app-info">
                    <div className="app-icon">
                      {app.iconUrl ? (
                        <img
                          src={app.iconUrl}
                          alt=""
                          onError={(e) => {
                            e.currentTarget.style.display = 'none'
                          }}
                        />
                      ) : '▣'}
                    </div>

                    <div>
                      <strong>{app.appName}</strong>
                      <span>{app.description || 'No description'}</span>
                    </div>
                  </div>
                </td>

                <td>
                  <span className={`status-badge ${
                    published ? 'status-active' : 'status-inactive'
                  }`}>
                    <span className="status-dot" />
                    {app.status}
                  </span>
                </td>

                <td>
                  {app.createdAt
                    ? new Date(app.createdAt).toLocaleDateString()
                    : '—'}
                </td>

                <td>
                  <div className="table-actions">
                    <button
                      className="view-button"
                      onClick={() => onViewDetails(app)}
                    >
                      Details
                    </button>

                    <button className="action-button" disabled
                      title="Coming soon">
                      Edit
                    </button>

                    <button
                      className="action-button"
                      disabled
                      title="Coming soon"
                    >
                      {published ? 'Disable' : 'Enable'}
                    </button>
                  </div>
                </td>
              </tr>
            )
          })}

          {apps.length === 0 && (
            <tr>
              <td colSpan={4} className="empty-state">
                No mini applications found.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  )
}