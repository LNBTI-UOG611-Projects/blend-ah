
import { useEffect, useState } from 'react'

import Header from '../components/layout/Header'
import MiniAppFilters from '../components/miniapps/MiniAppFilters'
import MiniAppTable from '../components/miniapps/MiniAppTable'
import MiniAppDetailsModal from '../components/miniapps/MiniAppDetailsModal'
import AddMiniAppModal from '../components/miniapps/AddMiniAppModal'
import EditMiniAppModal from '../components/miniapps/EditMiniAppModal'

import {
  getMiniApps,
  updateMiniApp,
  publishMiniApp,
  deactivateMiniApp,
} from '../services/miniAppService'

import type { EditMiniAppData } from '../components/miniapps/EditMiniAppModal'
import type { MiniApp } from '../types/miniApp'

import './MiniAppManagement.css'

export default function MiniAppManagement() {
  const [apps, setApps] = useState<MiniApp[]>([])
  const [search, setSearch] = useState('')
  const [status, setStatus] = useState('ALL')

  const [selectedApp, setSelectedApp] =
    useState<MiniApp | null>(null)

  const [editingApp, setEditingApp] =
    useState<MiniApp | null>(null)

  const [showAddModal, setShowAddModal] = useState(false)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  // Track Mini App currently being updated
  const [updatingAppId, setUpdatingAppId] =
    useState<number | null>(null)

  const [actionError, setActionError] =
    useState<string | null>(null)

  // Load Mini Apps
  async function loadApps() {
    setLoading(true)
    setError(null)

    try {
      const data = await getMiniApps()
      setApps(data)
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : 'Unable to load mini-apps'
      )
      throw err
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    void loadApps().catch(() => {})
  }, [])

  // Modify Mini App
  async function handleUpdateMiniApp(
    appId: number,
    data: EditMiniAppData
  ) {
    const updatedApp = await updateMiniApp(appId, data)

    setApps((previous) =>
      previous.map((app) =>
        app.appId === appId ? updatedApp : app
      )
    )
  }

  // Publish Mini App
  async function handlePublish(app: MiniApp) {
    if (updatingAppId !== null) return

    const confirmed = window.confirm(
      `Are you sure you want to publish "${app.appName}"?`
    )

    if (!confirmed) return

    setUpdatingAppId(app.appId)
    setActionError(null)

    try {
      const updatedApp = await publishMiniApp(app.appId)

      setApps((previous) =>
        previous.map((item) =>
          item.appId === app.appId ? updatedApp : item
        )
      )
    } catch (err) {
      setActionError(
        err instanceof Error
          ? err.message
          : 'Unable to publish mini-app'
      )
    } finally {
      setUpdatingAppId(null)
    }
  }

  // Deactivate Mini App
  async function handleDeactivate(app: MiniApp) {
    if (updatingAppId !== null) return

    const confirmed = window.confirm(
      `Are you sure you want to deactivate "${app.appName}"?`
    )

    if (!confirmed) return

    setUpdatingAppId(app.appId)
    setActionError(null)

    try {
      const updatedApp = await deactivateMiniApp(app.appId)

      setApps((previous) =>
        previous.map((item) =>
          item.appId === app.appId ? updatedApp : item
        )
      )
    } catch (err) {
      setActionError(
        err instanceof Error
          ? err.message
          : 'Unable to deactivate mini-app'
      )
    } finally {
      setUpdatingAppId(null)
    }
  }

  // Search and Status Filter
  const filteredApps = apps.filter((app) => {
    const query = search.trim().toLowerCase()

    const matchesSearch =
      app.appName.toLowerCase().includes(query) ||
      (app.description ?? '').toLowerCase().includes(query)

    const matchesStatus =
      status === 'ALL' || app.status === status

    return matchesSearch && matchesStatus
  })

  // Statistics
  const publishedCount = apps.filter(
    (app) => app.status === 'PUBLISHED'
  ).length

  const unpublishedCount = apps.filter(
    (app) => app.status === 'UNPUBLISHED'
  ).length

  return (
    <>
      <Header
        title="Mini App Management"
        breadcrumb="Admin / Mini Apps"
      />

      <section className="page-content mini-app-page">
        <div className="page-heading">
          <div>
            <h2>Mini App Listing</h2>
            <p>View and manage applications in Blend-Ah.</p>
          </div>

          <button
            type="button"
            className="primary-button"
            onClick={() => setShowAddModal(true)}
          >
            + Add Mini-App
          </button>
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
              <span>Published Apps</span>
              <h3>{publishedCount}</h3>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon orange">◷</div>
            <div>
              <span>Unpublished Apps</span>
              <h3>{unpublishedCount}</h3>
            </div>
          </div>
        </div>

        <section className="listing-card">
          <div className="listing-header">
            <div>
              <h3>All Mini Applications</h3>
              <p>Browse available mini applications.</p>
            </div>

            <span className="result-count">
              {filteredApps.length} Applications
            </span>
          </div>

          <MiniAppFilters
            search={search}
            status={status}
            onSearchChange={setSearch}
            onStatusChange={setStatus}
          />

          {actionError && (
            <p
              className="page-message error-message"
              role="alert"
            >
              {actionError}
            </p>
          )}

          {loading ? (
            <p className="page-message">
              Loading mini-apps...
            </p>
          ) : error ? (
            <p className="page-message error-message">
              {error}
            </p>
          ) : (
            <>
              <MiniAppTable
                apps={filteredApps}
                onViewDetails={setSelectedApp}
                onEdit={setEditingApp}
                onPublish={handlePublish}
                onDeactivate={handleDeactivate}
                updatingAppId={updatingAppId}
              />

              <div className="table-footer">
                Showing {filteredApps.length} of {apps.length} applications
              </div>
            </>
          )}
        </section>
      </section>

      {selectedApp && (
        <MiniAppDetailsModal
          app={selectedApp}
          onClose={() => setSelectedApp(null)}
        />
      )}

      {showAddModal && (
        <AddMiniAppModal
          onClose={() => setShowAddModal(false)}
          onCreated={loadApps}
        />
      )}

      {editingApp && (
        <EditMiniAppModal
          key={editingApp.appId}
          app={editingApp}
          onClose={() => setEditingApp(null)}
          onSave={handleUpdateMiniApp}
        />
      )}
    </>
  )
}
