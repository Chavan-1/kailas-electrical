import { Box, Button, Paper, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { getRole } from "../../utils/auth";
import { useTranslation } from "react-i18next";

function Unauthorized() {

    const { t } = useTranslation();
    const navigate = useNavigate();
    const role = getRole();

    const handleGoBack = () => {

        if (role === "ADMIN") {

            navigate("/dashboard");

        } else if (role === "CUSTOMER") {

            navigate("/customer-dashboard");

        } else {

            navigate("/login");
        }
    };

    return (

        <Box
            sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                minHeight: "100vh",
                alignItems: "center",
                backgroundColor: "#f5f6fa",
                padding: 3
            }}>

                <Paper elevation={3} 
                       sx={{ 
                            padding: 5, 
                            borderRadius: 3, 
                            align: "center",
                            maxWidth: 500,
                            width: "100%"
                            }}>

                    <Typography variant="h3" color="error" gutterBottom>
                        403
                    </Typography>

                    <Typography variant="h5" gutterBottom>
                        {t("service.accessDenied")}
                    </Typography>

                    <Typography color="text.secondary" sx={{ mb: 3 }}>
                        {t("auth.noPermission")}
                    </Typography>

                    <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
                        {t("auth.loggedInAs")}: <strong>{role}</strong>
                    </Typography>

                    <Button variant="contained" onClick={handleGoBack} >
                        {t("auth.goToDashboard")}
                    </Button>
                
                    
                </Paper>
        </Box>
    );
}

export default Unauthorized;
