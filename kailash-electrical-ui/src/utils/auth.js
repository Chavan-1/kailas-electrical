export const saveAuth = (loginResponse) => {

    const data = loginResponse.data;

    sessionStorage.setItem("token", data.token);
    sessionStorage.setItem("userId", data.userId);
    sessionStorage.setItem("fullName", data.fullName);
    sessionStorage.setItem("email", data.email);
    sessionStorage.setItem("role", data.role);
};

export const getToken = () => {

    return sessionStorage.getItem("token");
};

export const getRole = () => {

    return sessionStorage.getItem("role");
};

export const getUser = () => {

    return {
        userId: sessionStorage.getItem("userId"),
        fullName: sessionStorage.getItem("fullName"),
        email: sessionStorage.getItem("email"),
        role: sessionStorage.getItem("role")
    };
};

export const isLoggedIn = () => {

    return !!getToken();
};

export const logout = () => {

    sessionStorage.removeItem("token");
    sessionStorage.removeItem("userId");
    sessionStorage.removeItem("fullName");
    sessionStorage.removeItem("email");
    sessionStorage.removeItem("role");
};