
import type { MiniApp } from '../types/miniApp'

// View Mini-App Listing
export async function getMiniApps(search = ''): Promise<MiniApp[]> {
  const params = new URLSearchParams()

  if (search.trim()) {
    params.set('search', search.trim())
  }

  const url = `/api/mini-apps${params.size ? `?${params}` : ''}`
  const response = await fetch(url)

  if (!response.ok) {
    throw new Error(`Failed to fetch mini-apps (${response.status})`)
  }

  return response.json() as Promise<MiniApp[]>
}

// Data required to create a new Mini-App
export interface CreateMiniAppRequest {
  appName: string
  description: string
  iconUrl: string
  appUrl: string
}

// Add New Mini-App
export async function createMiniApp(
  data: CreateMiniAppRequest
): Promise<MiniApp> {
  const response = await fetch('/api/mini-apps', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'same-origin',
    body: JSON.stringify(data),
  })

  if (!response.ok) {
    throw new Error(`Failed to create mini-app (${response.status})`)
  }

  return response.json() as Promise<MiniApp>
}


 // Data required to update an existing Mini-App
export interface UpdateMiniAppRequest {
  appName: string
  description: string
  iconUrl: string
  appUrl: string
}

// Modify Existing Mini-App
export async function updateMiniApp(
  appId: number,
  data: UpdateMiniAppRequest
): Promise<MiniApp> {
  const response = await fetch(
    `/api/mini-apps/${appId}`,
    {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'same-origin',
      body: JSON.stringify(data),
    }
  )

  if (!response.ok) {
    throw new Error(
      `Failed to update mini-app (${response.status})`
    )
  }

  return response.json() as Promise<MiniApp>

}
 
 // Publish Mini App
export async function publishMiniApp(
  appId: number
): Promise<MiniApp> {
  const response = await fetch(
    `/api/mini-apps/${appId}/publish`,
    {
      method: 'PATCH',
      credentials: 'same-origin',
    }
  )

  if (!response.ok) {
    throw new Error(
      `Failed to publish mini-app (${response.status})`
    )
  }

  return response.json() as Promise<MiniApp>
}

// Deactivate Mini App
export async function deactivateMiniApp(
  appId: number
): Promise<MiniApp> {
  const response = await fetch(
    `/api/mini-apps/${appId}/deactivate`,
    {
      method: 'PATCH',
      credentials: 'same-origin',
    }
  )

  if (!response.ok) {
    throw new Error(
      `Failed to deactivate mini-app (${response.status})`
    )
  }

  return response.json() as Promise<MiniApp>
}

