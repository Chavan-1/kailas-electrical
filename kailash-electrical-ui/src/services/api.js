import axios from "axios";
import { getToken, logout } from "../utils/auth";

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

api.interceptors.request.use(
    
    (config) => {

        const token = getToken();

        console.log("Token being sent: ", token);
        
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },

    (error) => {
        return Promise.reject(error);
    }
);

api.interceptors.response.use(

    (response) => response,

    (error) => {

        if (error.response?.status === 401) {

            console.log("Unauthorized access - logging out");

            logout();

            window.location.href = "/login";
        }

        return Promise.reject(error);
    }
);

api.interceptors.request.use(

    (config) => {

        const language = localStorage.getItem("language") || "en";

        config.headers["Accept-Language"] = language;

        return config;
    },

    (error) => {

        return Promise.reject(error);
    }
);

export default api;