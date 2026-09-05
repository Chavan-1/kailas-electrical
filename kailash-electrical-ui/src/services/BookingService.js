import api from "./api"

export const getBookings = async (params = {}) => {
    const response = await api.get("/bookings", { params });
    return response.data;
};

export const getBookingById = async (id) => {
    const response = await api.get(`/bookings/${id}`);
    return response.data;
};

export const updateBooking = async (id, bookingData) => {
    const response = await api.put(`/bookings/${id}`, bookingData);
    return response.data;
};

export const updateBookingStatus = async (id, status) => {
    const response = await api.patch(`/bookings/${id}/status`, {status,});
    return response.data;
};

export const cancelBooking = async (id) => {
    const response = await api.delete(`/bookings/${id}`);
    return response.data;
};

export const getMyBookings = async () => {
    const response = await api.get("/my/bookings");
    return response.data;
};

export const getMyBooking = async (id) => {
    const response = await api.get(`/my/bookings/${id}`);
    return response.data;
};

export const cancelMyBooking = async (id) => {
    const response = await api.delete(`/my/bookings/${id}`);
    return response.data;
};

export const createBooking = async (bookingData) => {
    const response = await api.post("/bookings", bookingData);
    return response.data;
};

