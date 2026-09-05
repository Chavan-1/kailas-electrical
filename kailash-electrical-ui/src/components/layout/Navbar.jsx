import { AppBar, Box, Button, Toolbar, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";
import LanguageSelector from "../common/LanguageSelector";
import { useTranslation } from "react-i18next";
import { logout } from "../../utils/auth";

function Navbar() {

    const navigate = useNavigate();

    const { t } = useTranslation();

    const handleLogout = () => {
        
        logout();

        navigate("/login", { replace: true });
    };

    return (

        <AppBar position="fixed" 
                sx={{ 
                    zIndex: (theme) => theme.zIndex.drawer + 1 
                }}
        >

            <Toolbar>

                <Typography variant="h6" 
                            sx={{ flexGrow: 1 }}
                >
                    {t("common.appName")}
                </Typography>
                
                <Box>
                    <LanguageSelector />
                    
                    <Button color="inherit" onClick={handleLogout}>
                        {t("common.logout")}
                    </Button>
                </Box>

            </Toolbar>

        </AppBar>
    );
}

export default Navbar;