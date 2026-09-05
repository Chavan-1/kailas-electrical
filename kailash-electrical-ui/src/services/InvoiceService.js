import api from "./api"

export const getInvoices = async (params = {}) => {
    
    const response = await api.get("/invoices", {params});
    return response.data; 
}

export const getMyInvoices = async () => {
    
    const response = await api.get("/my/invoices");
    return response.data; 
}

export const generateInvoice = async (bookingId) => {
    
    const response = await api.post("/invoices", {bookingId});
    return response.data; 
}

export const getInvoiceById = async (id) => {
    
    const response = await api.get(`/invoices/${id}`);
    return response.data;
}

export const getMyInvoiceById = async (id) => {
    
    const response = await api.get(`/my/invoices/${id}`);
    return response.data;
}

export const updatePaymentStatus = async (id, paymentStatus) => {
    
    const response = await api.put(`/invoices/${id}/payment-status`, {paymentStatus});
    return response.data;
}

export const downloadInvoice = async (id) => {
    
    const response = await api.get(`/invoices/${id}/download`, {responseType: "blob"});
    return response.data;
}

export const downloadMyInvoice = async (id) => {
    
    const response = await api.get(`/my/invoices/${id}/download`, {responseType: "blob"});
    return response.data;
}