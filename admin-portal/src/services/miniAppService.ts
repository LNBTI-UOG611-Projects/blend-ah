import type { MiniApp } from '../types/miniApp'

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