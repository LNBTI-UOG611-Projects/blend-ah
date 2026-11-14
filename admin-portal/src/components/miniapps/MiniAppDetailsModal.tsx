import type { MiniApp } from '../../types/miniApp'

type Props = {
  app: MiniApp
  onClose: () => void
}

export default function MiniAppDetailsModal({
  app,
  onClose,
}: Props) {
  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div
        className="details-modal"
        role="dialog"
        aria-modal="true"
        aria-labelledby="mini-app-details-title"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="modal-header">
          <div>
            <span>MINI APP DETAILS</span>
            <h2 id="mini-app-details-title">{app.appName}</h2>
          </div>

          <button className="close-button" onClick={onClose}
            aria-label="Close details">
            ×
          </button>
        </div>

        <div className="modal-body">
          <p>{app.description || 'No description available.'}</p>

          <div className="detail-row">
            <span>Application ID</span>
            <strong>{app.appId}</strong>
          </div>

          <div className="detail-row">
            <span>Status</span>
            <strong>{app.status}</strong>
          </div>

          <div className="detail-row">
            <span>App URL</span>
            <a href={app.appUrl} target="_blank"
              rel="noopener noreferrer">
              {app.appUrl}
            </a>
          </div>

          <div className="detail-row">
            <span>Created Date</span>
            <strong>
              {app.createdAt
                ? new Date(app.createdAt).toLocaleString()
                : '—'}
            </strong>
          </div>
        </div>

        <div className="modal-footer">
          <button className="close-modal-button" onClick={onClose}>
            Close
          </button>
        </div>
      </div>
    </div>
  )
}