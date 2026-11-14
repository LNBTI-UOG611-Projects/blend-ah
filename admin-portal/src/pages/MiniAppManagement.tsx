import { useEffect, useState } from 'react'
import Header from '../components/layout/Header'
import MiniAppFilters from '../components/miniapps/MiniAppFilters'
import MiniAppTable from '../components/miniapps/MiniAppTable'
import MiniAppDetailsModal from '../components/miniapps/MiniAppDetailsModal'
import { getMiniApps } from '../services/miniAppService'
import type { MiniApp } from '../types/miniApp'
import './MiniAppManagement.css' 

export default function MiniAppManagement() {
  const [apps, setApps] = useState<MiniApp[]>([])
  const [search, setSearch] = useState('')
  const [status, setStatus] = useState('ALL')
  const [selectedApp, setSelectedApp] = useState<MiniApp | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    let cancelled = false

    async function loadApps() {
      setLoading(true)
      setError(null)

      try {
        const data = await getMiniApps()

        if (!cancelled) {
          setApps(data)
        }
      } catch (err) {
        if (!cancelled) {
          setError(
            err instanceof Error
              ? err.message
              : 'Unable to load mini-apps'
          )
        }
      } finally {
        if (!cancelled) setLoading(false)
      }
    }

    loadApps()

    return () => {
      cancelled = true
    }
  }, [])

  const filteredApps = apps.filter((app) => {
    const query = search.trim().toLowerCase()

    const matchesSearch =
      app.appName.toLowerCase().includes(query) ||
      (app.description ?? '').toLowerCase().includes(query)

    const matchesStatus =
      status === 'ALL' || app.status === status

    return matchesSearch && matchesStatus
  })

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

          <button className="primary-button" disabled
            title="Coming soon">
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

          {loading ? (
            <p className="page-message">Loading mini-apps...</p>
          ) : error ? (
            <p className="page-message error-message">{error}</p>
          ) : (
            <>
              <MiniAppTable
                apps={filteredApps}
                onViewDetails={setSelectedApp}
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
    </>
  )
}