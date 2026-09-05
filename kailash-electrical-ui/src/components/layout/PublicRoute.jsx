import { Navigate } from "react-router-dom";
import { getRole, isLoggedIn } from "../../utils/auth";

function PublicRoute({ children }) {

    if (isLoggedIn()) {

        const role = getRole();
        
        return <Navigate 
                    to={
                        role === "ADMIN" 
                            ? "/dashboard" 
                            : "/customer-dashboard"
                    }
                    replace 
                />
    }

    return children;
}

export default PublicRoute;
        