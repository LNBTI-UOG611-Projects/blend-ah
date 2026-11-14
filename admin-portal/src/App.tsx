import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import AdminLayout from './components/layout/AdminLayout'
import MiniAppManagement from './pages/MiniAppManagement'
import './App.css'

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<AdminLayout />}>
          <Route path="/" element={<Navigate to="/mini-apps" replace />} />
          <Route path="/mini-apps" element={<MiniAppManagement />} />
          <Route path="*" element={<Navigate to="/mini-apps" replace />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}