
import { useState } from 'react'
import type { ChangeEvent, FormEvent } from 'react'

import './AddMiniAppModal.css'

import type { MiniApp } from '../../types/miniApp'

export type EditMiniAppData = {
  appName: string
  description: string
  iconUrl: string
  appUrl: string
}

type Props = {
  app: MiniApp
  onClose: () => void
  onSave: (
    appId: number,
    data: EditMiniAppData
  ) => void | Promise<void>
}

export default function EditMiniAppModal({
  app,
  onClose,
  onSave,
}: Props) {
  const [formData, setFormData] = useState<EditMiniAppData>({
    appName: app.appName,
    description: app.description ?? '',
    iconUrl: app.iconUrl ?? '',
    appUrl: app.appUrl,
  })

  const [error, setError] = useState<string | null>(null)
  const [isSaving, setIsSaving] = useState(false)

  function handleChange(
    event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) {
    const { name, value } = event.target

    setFormData((previous) => ({
      ...previous,
      [name]: value,
    }))
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setError(null)

    if (!formData.appName.trim()) {
      setError('Mini-App name is required.')
      return
    }

    if (!formData.appUrl.trim()) {
      setError('App URL is required.')
      return
    }

    try {
      const appUrl = new URL(formData.appUrl.trim())

      if (!['http:', 'https:'].includes(appUrl.protocol)) {
        setError('App URL must use HTTP or HTTPS.')
        return
      }

      if (formData.iconUrl.trim()) {
        const iconUrl = new URL(formData.iconUrl.trim())

        if (!['http:', 'https:'].includes(iconUrl.protocol)) {
          setError('Icon URL must use HTTP or HTTPS.')
          return
        }
      }
    } catch {
      setError('Please enter valid URLs.')
      return
    }

    setIsSaving(true)

    try {
      await onSave(app.appId, {
        appName: formData.appName.trim(),
        description: formData.description.trim(),
        iconUrl: formData.iconUrl.trim(),
        appUrl: formData.appUrl.trim(),
      })

      onClose()
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : 'Failed to update Mini-App. Please try again.'
      )
    } finally {
      setIsSaving(false)
    }
  }

  return (
    <div
      className="modal-backdrop"
      onClick={() => {
        if (!isSaving) onClose()
      }}
    >
      <div
        className="add-mini-app-modal"
        role="dialog"
        aria-modal="true"
        aria-labelledby="edit-mini-app-title"
        onClick={(event) => event.stopPropagation()}
      >
        <div className="modal-header">
          <div>
            <span>MINI APP MANAGEMENT</span>
            <h2 id="edit-mini-app-title">Edit Mini-App</h2>
          </div>

          <button
            type="button"
            className="close-button"
            onClick={onClose}
            aria-label="Close form"
            disabled={isSaving}
          >
            ×
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="modal-body">
            <div className="form-group">
              <label htmlFor="editAppName">Mini-App Name *</label>
              <input
                id="editAppName"
                name="appName"
                type="text"
                value={formData.appName}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="editDescription">Description</label>
              <textarea
                id="editDescription"
                name="description"
                rows={3}
                value={formData.description}
                onChange={handleChange}
              />
            </div>

            <div className="form-group">
              <label htmlFor="editIconUrl">Icon URL</label>
              <input
                id="editIconUrl"
                name="iconUrl"
                type="url"
                value={formData.iconUrl}
                onChange={handleChange}
              />
            </div>

            <div className="form-group">
              <label htmlFor="editAppUrl">App URL *</label>
              <input
                id="editAppUrl"
                name="appUrl"
                type="url"
                value={formData.appUrl}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="editStatus">Status</label>
              <input
                id="editStatus"
                type="text"
                value={app.status}
                readOnly
              />
            </div>

            {error && (
              <p className="form-error" role="alert">
                {error}
              </p>
            )}
          </div>

          <div className="modal-footer">
            <button
              type="button"
              className="cancel-button"
              onClick={onClose}
              disabled={isSaving}
            >
              Cancel
            </button>

            <button
              type="submit"
              className="primary-button"
              disabled={isSaving}
            >
              {isSaving ? 'Saving...' : 'Save Changes'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
