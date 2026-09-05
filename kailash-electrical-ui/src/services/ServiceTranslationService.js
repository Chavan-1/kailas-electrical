import api from "./api";

export const getServiceTranslations = async (serviceId) => {

    const response = await api.get(`/services/${serviceId}/translations`);
    return response.data;
}

export const addServiceTranslation = async (serviceId, translationData) => {

    const response = await api.post(`/services/${serviceId}/translations`,translationData);
    return response.data;
};

export const updateServiceTranslation = async (serviceId, language, translationData) => {

    const response = await api.put(`/services/${serviceId}/translations/${language}`, translationData);
    return response.data;
};