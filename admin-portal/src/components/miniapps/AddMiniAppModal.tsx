
import { useState } from 'react'
import type { ChangeEvent, FormEvent } from 'react'

import './AddMiniAppModal.css'

import {
  createMiniApp,
  type CreateMiniAppRequest,
} from '../../services/miniAppService'

type Props = {
  onClose: () => void
  onCreated?: () => void | Promise<void>
}

export default function AddMiniAppModal({
  onClose,
  onCreated,
}: Props) {
  const [formData, setFormData] = useState<CreateMiniAppRequest>({
    appName: '',
    description: '',
    iconUrl: '',
    appUrl: '',
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
      await createMiniApp({
        appName: formData.appName.trim(),
        description: formData.description.trim(),
        iconUrl: formData.iconUrl.trim(),
        appUrl: formData.appUrl.trim(),
      })

      await onCreated?.()
      onClose()
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : 'Failed to save Mini-App. Please try again.'
      )
    } finally {
      setIsSaving(false)
    }
  }

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div
        className="add-mini-app-modal"
        role="dialog"
        aria-modal="true"
        aria-labelledby="add-mini-app-title"
        onClick={(event) => event.stopPropagation()}
      >
        <div className="modal-header">
          <div>
            <span>MINI APP MANAGEMENT</span>
            <h2 id="add-mini-app-title">Add New Mini-App</h2>
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
              <label htmlFor="appName">Mini-App Name *</label>
              <input
                id="appName"
                name="appName"
                type="text"
                placeholder="Enter mini-app name"
                value={formData.appName}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="description">Description</label>
              <textarea
                id="description"
                name="description"
                placeholder="Enter mini-app description"
                rows={3}
                value={formData.description}
                onChange={handleChange}
              />
            </div>

            <div className="form-group">
              <label htmlFor="iconUrl">Icon URL</label>
              <input
                id="iconUrl"
                name="iconUrl"
                type="url"
                placeholder="https://example.com/icon.png"
                value={formData.iconUrl}
                onChange={handleChange}
              />
            </div>

            <div className="form-group">
              <label htmlFor="appUrl">App URL *</label>
              <input
                id="appUrl"
                name="appUrl"
                type="url"
                placeholder="https://example.com/app"
                value={formData.appUrl}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label>Status</label>
              <input
                type="text"
                value="UNPUBLISHED"
                readOnly
                aria-label="Default status"
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
              {isSaving ? 'Saving...' : 'Add Mini-App'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
