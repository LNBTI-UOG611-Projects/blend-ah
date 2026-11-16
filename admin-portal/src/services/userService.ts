
import type { User } from '../types/user'

export async function getUsers(): Promise<User[]> {
  const response = await fetch('/api/admin/users')

  if (!response.ok) {
    throw new Error('Failed to fetch users')
  }

  return response.json() as Promise<User[]>
}

export async function getUserById(userId: string): Promise<User> {
  const response = await fetch(
    `/api/admin/users/${encodeURIComponent(userId)}`
  )

  if (response.status === 404) {
    throw new Error('User not found')
  }

  if (!response.ok) {
    throw new Error('Failed to fetch user details')
  }

  return response.json() as Promise<User>
}


export async function deactivateUser(userId: string): Promise<User> {
  const response = await fetch(
    `/api/admin/users/${encodeURIComponent(userId)}/deactivate`,
    { method: 'PATCH' }
  )

  if (!response.ok) {
    throw new Error('Failed to deactivate user')
  }

  return response.json() as Promise<User>
}


export async function activateUser(userId: string): Promise<User> {
  const response = await fetch(
    `/api/admin/users/${encodeURIComponent(userId)}/activate`,
    { method: 'PATCH' }
  )

  if (!response.ok) {
    throw new Error('Failed to activate user')
  }

  return response.json() as Promise<User>
}
