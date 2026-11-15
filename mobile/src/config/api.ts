import { Platform } from 'react-native';

// Android emulator uses 10.0.2.2 to reach your computer, iOS uses localhost
const DEFAULT_URL =
  Platform.OS === 'android' ? 'http://10.0.2.2:8080' : 'http://localhost:8080';

export const API_URL = process.env.EXPO_PUBLIC_API_URL || DEFAULT_URL;

export const ENDPOINTS = {
  health: `${API_URL}/api/health`,
  login: `${API_URL}/api/auth/login`,
  register: `${API_URL}/api/auth/register`,
};