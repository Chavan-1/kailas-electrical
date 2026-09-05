import './App.css'
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import Login from './pages/auth/Login'
import ProtectedRoute from './components/ProtectedRoute'
import Dashboard from './pages/Dashboard'
import CustomerList from './pages/customer/CustomerList'
import MainLayout from './components/layout/MainLayout'
import Services from './pages/service/Services'
import Bookings from './pages/booking/Bookings'
import CreateBooking from './pages/booking/CreateBooking'
import Register from './pages/auth/Register'
import Unauthorized from './pages/auth/Unauthorized'
import CustomerDashboard from './pages/customer/CustomerDashboard'
import CreateCustomer from './pages/customer/CreateCustomer'
import CustomerDetails from './pages/customer/CustomerDetails'
import EditCustomer from './pages/customer/EditCustomer'
import CreateService from './pages/service/CreateService'
import ServiceDetails from './pages/service/ServiceDetails'
import EditService from './pages/service/EditService'
import BookingDetails from './pages/booking/BookingDetails'
import EditBooking from './pages/booking/EditBooking'
import Profile from './pages/auth/Profile'
import ChangePassword from './pages/auth/ChangePassword'
import InvoiceList from './pages/Invoice/InvoiceList'
import InvoiceDetails from './pages/Invoice/InvoiceDetails'
import ForgotPassword from './pages/auth/ForgotPassword'
import ResetPassword from './pages/auth/ResetPassword'
import PublicRoute from './components/layout/PublicRoute'

function App() {

  return (
    <BrowserRouter>
      <Routes>

        <Route path="/" element={<Navigate to="/login" replace />} />
        
        <Route path="/login" element={
            <PublicRoute>
              <Login />
            </PublicRoute>
          } 
        />

        <Route path="/forgot-password" element={<ForgotPassword />} />

        <Route path="/reset-password" element={<ResetPassword />} />

        <Route path="/register" element={<Register />} />

        <Route path="/unauthorized" element={<Unauthorized />} />

        <Route path="/dashboard" element={
            <ProtectedRoute allowedRoles={["ADMIN"]}>
              <MainLayout>
                <Dashboard />
              </MainLayout>
            </ProtectedRoute>
          }
         />

         <Route path="/customer-dashboard" element={
            <ProtectedRoute allowedRoles={["CUSTOMER"]}>
              <MainLayout>
                <CustomerDashboard />
              </MainLayout>
            </ProtectedRoute>
          }
         />

         <Route path="/profile" element={
            <ProtectedRoute allowedRoles={["CUSTOMER"]}>
              <MainLayout>
                <Profile />
              </MainLayout>
            </ProtectedRoute>
          }
         />

         <Route path="/change-password" element={
            <ProtectedRoute allowedRoles={["CUSTOMER"]}>
              <MainLayout>
                <ChangePassword />
              </MainLayout>
            </ProtectedRoute>
          }
         />

         <Route path="/customers" element={
            <ProtectedRoute allowedRoles={["ADMIN"]}>
              <MainLayout>
                <CustomerList />
              </MainLayout>
            </ProtectedRoute>
          }
         />

         <Route path="/customers/create" element={
            <ProtectedRoute allowedRoles={["ADMIN"]}>
              <MainLayout>
                <CreateCustomer />
              </MainLayout>
            </ProtectedRoute>
          }
         />

         <Route path="/customers/:id" element={
            <ProtectedRoute allowedRoles={["ADMIN"]}>
              <MainLayout>
                <CustomerDetails />
              </MainLayout>
            </ProtectedRoute>
          }
         />

         <Route path="/customers/:id/edit" element={
            <ProtectedRoute allowedRoles={["ADMIN"]}>
              <MainLayout>
                <EditCustomer />
              </MainLayout>
            </ProtectedRoute>
          }
         />

         <Route path="/services" element={
            <ProtectedRoute allowedRoles={["ADMIN", "CUSTOMER"]}>
                <MainLayout>
                  <Services />
                </MainLayout>
              </ProtectedRoute>
          } />
          
          <Route path="/services/create" element={
            <ProtectedRoute allowedRoles={["ADMIN"]}>
                <MainLayout>
                  <CreateService />
                </MainLayout>
              </ProtectedRoute>
          } />

          <Route path="/services/:id" element={
            <ProtectedRoute allowedRoles={["ADMIN", "CUSTOMER"]}>
                <MainLayout>
                  <ServiceDetails />
                </MainLayout>
              </ProtectedRoute>
          } />

          <Route path="/services/:id/edit" element={
            <ProtectedRoute allowedRoles={["ADMIN"]}>
                <MainLayout>
                  <EditService />
                </MainLayout>
              </ProtectedRoute>
          } />

         <Route path="/bookings" element={
            <ProtectedRoute allowedRoles={["ADMIN", "CUSTOMER"]}>
                <MainLayout>
                  <Bookings />
                </MainLayout>
              </ProtectedRoute>
          } />

          <Route path="/bookings/create" element={
            <ProtectedRoute allowedRoles={["CUSTOMER"]}>
                <MainLayout>
                  <CreateBooking />
                </MainLayout>
              </ProtectedRoute>
          } />

          <Route path="/bookings/:id" element={
            <ProtectedRoute allowedRoles={["ADMIN", "CUSTOMER"]}>
                <MainLayout>
                  <BookingDetails />
                </MainLayout>
              </ProtectedRoute>
          } />

          <Route path="/bookings/:id/edit" element={
            <ProtectedRoute allowedRoles={["ADMIN"]}>
                <MainLayout>
                  <EditBooking />
                </MainLayout>
              </ProtectedRoute>
          } />

          <Route path="/invoices" element={
            <ProtectedRoute allowedRoles={["ADMIN"]}>
                <MainLayout>
                  <InvoiceList />
                </MainLayout>
              </ProtectedRoute>
          } />

          <Route path="/invoices/:id" element={
            <ProtectedRoute allowedRoles={["ADMIN"]}>
                <MainLayout>
                  <InvoiceDetails />
                </MainLayout>
              </ProtectedRoute>
          } />

          <Route path="/my-invoices" element={
            <ProtectedRoute allowedRoles={["CUSTOMER"]}>
                <MainLayout>
                  <InvoiceList />
                </MainLayout>
              </ProtectedRoute>
          } />

          <Route path="/my-invoices/:id" element={
            <ProtectedRoute allowedRoles={["CUSTOMER"]}>
                <MainLayout>
                  <InvoiceDetails />
                </MainLayout>
              </ProtectedRoute>
          } />

          <Route path="*" element={
              <Navigate to="/login" 
                        replace 
              />
            }
          />
          
      </Routes>
    </BrowserRouter>
  )
}

export default App
