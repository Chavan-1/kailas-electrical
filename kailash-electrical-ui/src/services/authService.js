import api from "./api";

export const login = async (loginData) => {
    
    const response = await api.post("/auth/login", loginData);
    return response.data;
};

export const register = async (registerData) => {
    
    const response = await api.post("/auth/register", registerData);
    return response.data;
};

export const forgotPassword = async (email) => {
    
    const response = await api.post("/auth/forgot-password", { email });
    return response.data;
};

export const resetPassword = async (token, newPassword, confirmPassword) => {
    
    const response = await api.post("/auth/reset-password", { token, newPassword, confirmPassword });
    return response.data;
};