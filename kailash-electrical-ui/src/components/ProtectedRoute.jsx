import { Navigate, useLocation } from "react-router-dom";
import { getRole, isLoggedIn } from "../utils/auth";

function ProtectedRoute({ children, allowedRoles }) {

    const location = useLocation();

    if (!isLoggedIn()) {
        return <Navigate to="/login" state={{ from: location }} replace />
    }

    const role = getRole();

    if (allowedRoles && !allowedRoles.includes(role)) {
        return <Navigate to="/unauthorized" replace/>
    }

    return children;
};

export default ProtectedRoute;