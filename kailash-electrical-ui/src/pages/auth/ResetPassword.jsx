import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useSearchParams } from "react-router-dom";
import { resetPassword } from "../../services/authService";
import { Alert, Box, Button, Paper, TextField, Typography } from "@mui/material";

const ResetPassword = () => {

    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const token = searchParams.get("token");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState("");
    const { t } = useTranslation();

    const handleSubmit = async (event) => {

        event.preventDefault();

        setError("");
        setSuccess("");

        if (!token) {

            setError(t("auth.invalidResetLink"));
            return;
        }

        if (newPassword.length < 8) {

            setError(t("auth.passwordHint"));
            return;
        }

        if (newPassword !== confirmPassword) {

            setError(t("profile.passwordMismatch"));
            return;
        }

        try {

            setLoading(true);

            const response = await resetPassword(token, newPassword, confirmPassword);

            if (response.success) {

                setSuccess(response.message || t("auth.passwordResetSuccess"));

                setTimeout(() => {
                    navigate("/login");
                }, 1500);

            } else {

                setError(response.message || t("auth.failedToResetPassword"));

            }

        } catch (error) {

            setError(error.response?.data?.message || t("auth.failedToResetPassword"));

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

                <Typography variant="h4" fontWeight={600} sx={{ mb: 3 }}>
                    {t("auth.resetPassword")}
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
                        label={t("profile.newPassword")}
                        type="password"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                        fullWidth
                        required
                        sx={{ mb: 2 }}
                    />

                    <TextField 
                        label={t("profile.confirmNewPassword")}
                        type="password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
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
                        {loading ? t("auth.resetting") : t("auth.resetPassword")}
                    </Button>

                    <Button variant="text" fullWidth sx={{ mt: 1 }} onClick={() => navigate("/login")}>
                        {t("auth.backToLogin")}
                    </Button>

                </Box>

            </Paper>

        </Box>

    );

};

export default ResetPassword;