import { ENDPOINTS } from '../config/api';

export type LoginResponse = {
  message: string;
  userId: string;
  fullName: string;
  email: string;
  role: 'NORMAL_USER' | 'ADMIN';
};

async function postJson(url: string, body: unknown) {
  let response: Response;
  try {
    response = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    });
  } catch {
    throw new Error('Could not connect to the server. Is the backend running?');
  }

  // read as text first, because the server might not send JSON
  const text = await response.text();
  let data: any = null;
  try {
    data = text ? JSON.parse(text) : null;
  } catch {
    data = { message: text };
  }

  if (!response.ok) {
    throw new Error(data?.error || data?.message || `Request failed (HTTP ${response.status})`);
  }
  return data;
}

export const login = (email: string, password: string): Promise<LoginResponse> =>
  postJson(ENDPOINTS.login, { email, password });

export const register = (fullName: string, email: string, password: string) =>
  postJson(ENDPOINTS.register, { fullName, email, password });