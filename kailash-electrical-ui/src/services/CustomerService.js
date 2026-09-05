import api from "./api";

export const getCustomers = async (params = {}) => {

    const response = await api.get("/customers", { params });
    return response.data;
};

export const getCustomerById = async (id) => {

    const response = await api.get(`/customers/${id}`);
    return response.data;
};

export const createCustomer = async (customerData) => {

    const response = await api.post("/customers", customerData);
    return response.data;
};

export const updateCustomer = async (id, customerData) => {

    const response = await api.put(`/customers/${id}`, customerData);
    return response.data;
};

export const deleteCustomer = async (id) => {

    const response = await api.delete(`/customers/${id}`);
    return response.data;
};

export const updateCustomerStatus = async (id, active) => {

    const response = await api.put(`/customers/${id}/status`, { active });
    return response.data;
};

export const updateActiveDeactiveStatusChange = async (id, active) => {

    const response = await api.patch(`/customers/${id}/status`, { active });
    return response.data;
};

export const getCustomerDetails = async (id) => {

    const response = await api.get(`/customers/${id}/details`);
    return response.data;
};