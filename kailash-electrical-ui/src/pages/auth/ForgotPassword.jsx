import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { forgotPassword } from "../../services/authService";
import { Alert, Box, Button, Paper, TextField, Typography } from "@mui/material";
import { useTranslation } from "react-i18next";

const ForgotPassword = () => {

    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState("");
    const { t } = useTranslation();

    const handleSubmit = async (event) => {

        event.preventDefault();
        
        setError("");
        setSuccess("");

        if (!email.trim()) {
            setError(t("auth.emailRequired"));
            return;
        }

        try {
            
            setLoading(true);

            const response = await forgotPassword(email.trim());

            if (response.success) {

                setSuccess(response.message || t("auth.passwordResetLinkSent"));

            } else {

                setError(response.message || t("auth.failedToSendResetInstructions"));
            }

            
        } catch (error) {

            setError(error.response?.data?.message || t("auth.failedToSendResetInstructions"));

        } finally {

            setLoading(false);
        }

    };

    return (

        <Box 
            sx={{
                minHeight: "100vh",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                backgroundColor: "#f5f6fa"
             }}>
            
            <Paper elevation={3} sx={{ width: 420, p: 4, borderRadius: 3 }}>

                <Typography variant="h4" fontWeight={600} sx={{ mb: 1 }}>
                    {t("auth.forgotPassword")}
                </Typography>

                <Typography color="text.secondary" sx={{ mb: 3 }}>
                    {t("auth.enterRegisteredEmail")}
                </Typography>

                {error && (
                    <Alert severity="error" sx={{ mb: 2 }}>
                        {error}
                    </Alert>
                )}

                {success && (
                    <Alert severity="success" sx={{ mb: 2 }}>
                        {success}
                    </Alert>
                )}

                <Box component="form" onSubmit={handleSubmit}>

                    <TextField 
                        label={t("auth.email")}
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        fullWidth
                        required
                    />

                    <Button 
                        type="submit"
                        variant="contained"
                        fullWidth
                        sx={{ mt: 3 }}
                        disabled={loading}
                    >
                        {loading ? t("auth.sending") : t("auth.sendResetLink")}
                    </Button>

                    <Button variant="text" fullWidth sx={{ mt: 1 }} onClick={() => navigate("/login")}>
                        {t("auth.backToLogin")}
                    </Button>

                </Box>

            </Paper>

        </Box>

    );

};

export default ForgotPassword;