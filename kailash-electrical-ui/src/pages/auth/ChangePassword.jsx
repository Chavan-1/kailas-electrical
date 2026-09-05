import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { changePassword } from "../../services/ProfileService";
import { Alert, Box, Button, Card, CardContent, Divider, IconButton, InputAdornment, TextField, Typography } from "@mui/material";
import { ArrowBack, Lock, Save, Visibility, VisibilityOff } from "@mui/icons-material";
import { useTranslation } from "react-i18next";

function ChangePassword() {

    const navigate = useNavigate();
    const { t } = useTranslation();

    const [formData, setFormData] = useState({
        currentPassword: "",
        newPassword: "",
        confirmPassword: ""
    });

    const [showCurrentPassword, setShowCurrentPassword] = useState(false);
    const [showNewPassword, setShowNewPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    const handleChange = (event) => {

        const { name, value } = event.target;

        setFormData((previous) => ({
            ...previous,
            [name]: value
        }));
    };

    const handleSubmit = async (event) => {

        event.preventDefault();

        setError("");
        setSuccess("");

        if (!formData.currentPassword) {
            setError(t("profile.currentPasswordRequired"));
            return;
        }

        if (!formData.newPassword) {
            setError(t("profile.newPasswordRequireds"));
            return;
        }

        if (!formData.confirmPassword) {
            setError(t("profile.confirmPasswordRequired"));
            return;
        }

        if (formData.newPassword !== formData.confirmPassword) {
            setError(t("profile.passwordMismatch"));
            return;
        }

        if (formData.newPassword === formData.currentPassword) {
            setError(t("profile.samePassword"));
            return;
        }

        try {

            setLoading(true);

            const response = await changePassword({
                currentPassword: formData.currentPassword,
                newPassword: formData.newPassword
            });

            console.log("Change password response:", response);

            if (response.success) {

                setSuccess(response.message || t("profile.passwordChangeSuccess"));

                setFormData({
                    currentPassword: "",
                    newPassword: "",
                    confirmPassword: ""
                });

            } else {

                setError(response.message || t("profile.passwordChangeFailed"));
            }

        } catch (error) {

            console.error("Change password error:", error);

            setError(error.response?.data?.message || t("profile.passwordChangeFailed"));

        } finally {

            setLoading(false);
        }
    };

    return (

        <Box sx={pageStyle}>

            <Box sx={headerStyle}>

                <Box>

                    <Typography variant="h4" fontWeight={600}>
                        {t("profile.changePassword")}
                    </Typography>

                    <Typography variant="body1" color="text.secondary" sx={{ mt: 1 }}>
                        {t("profile.updatePasswordDescription")}
                    </Typography>

                </Box>

                <Button variant="outlined" startIcon={<ArrowBack />} onClick={() => navigate("/profile")}>
                    {t("profile.backToProfile")}
                </Button>

            </Box>

            {error && (
                <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError("")}>
                    {error}
                </Alert>
            )}

            {success && (
                <Alert severity="success" sx={{ mb: 2 }} onClose={() => setSuccess("")}>
                    {success}
                </Alert>
            )}

            <Card elevation={2} sx={{ maxWidth: 650 }}>

                <CardContent sx={{ p: 4 }}>
 
                    <Box sx={{ display: "flex", alignItems: "center", gap: 2, mb: 3 }}>

                        <Lock sx={{ fontSize: 40 }} />

                        <Box>

                            <Typography variant="h6" fontWeight={600}>
                                {t("profile.passwordSecurity")}
                            </Typography>

                            <Typography variant="body2" color="text.secondary">
                                {t("profile.passwordSecurityDescription")}
                            </Typography>

                        </Box>

                    </Box>

                    <Divider sx={{ mb: 3 }} />

                    <form onSubmit={handleSubmit}>

                        <TextField 
                            fullWidth
                            margin="normal"
                            label={t("profile.currentPassword")}
                            name="currentPassword"
                            type={showCurrentPassword ? t("profile.text") : t("profile.password")}
                            value={formData.currentPassword}
                            onChange={handleChange}
                            required
                            slotProps={{
                                input: {
                                    endAdornment: (
                                        <InputAdornment position="end">
                                            <IconButton edge="end" onClick={() => setShowCurrentPassword(!showCurrentPassword)}>
                                                {showCurrentPassword ? <VisibilityOff /> : <Visibility />}
                                            </IconButton>
                                        </InputAdornment>
                                    )
                                }
                            }}
                        />

                        <TextField 
                            fullWidth
                            margin="normal"
                            label={t("profile.newPassword")}
                            name="newPassword"
                            type={showNewPassword ? t("profile.text") : t("profile.password")}
                            value={formData.newPassword}
                            onChange={handleChange}
                            required
                            helperText={t("profile.strongPassword")}
                            slotProps={{
                                input: {
                                    endAdornment: (
                                        <InputAdornment position="end">
                                            <IconButton edge="end" onClick={() => setShowNewPassword(!showNewPassword)}>
                                                {showNewPassword ? <VisibilityOff /> : <Visibility />}
                                            </IconButton>
                                        </InputAdornment>
                                    )
                                }
                            }}
                        />

                        <TextField 
                            fullWidth
                            margin="normal"
                            label={t("profile.confirmNewPassword")}
                            name="confirmPassword"
                            type={showConfirmPassword ? "text" : "password"}
                            value={formData.confirmPassword}
                            onChange={handleChange}
                            required
                            helperText={t("profile.strongPassword")}
                            slotProps={{
                                input: {
                                    endAdornment: (
                                        <InputAdornment position="end">
                                            <IconButton edge="end" onClick={() => setShowConfirmPassword(!showConfirmPassword)}>
                                                {showConfirmPassword ? <VisibilityOff /> : <Visibility />}
                                            </IconButton>
                                        </InputAdornment>
                                    )
                                }
                            }}
                        />

                        <Box sx={{display: "flex", justifyContent: "flex-end", gap: 2, mt: 4}}>

                            <Button variant="outlined" onClick={() => navigate("/profile")} disabled={loading}>
                                {t("common.cancel")}
                            </Button>

                            <Button type="submit" variant="contained" startIcon={<Save />} disabled={loading}>
                                {loading ? t("profile.changing") : t("profile.changePassword")}
                            </Button>

                        </Box>

                    </form>

                </CardContent>

            </Card>

        </Box>
    );

}

const pageStyle = {
    padding: "30px",
    backgroundColor: "#f5f6fa",
    minHeight: "100vh"
};

const headerStyle = {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "25px"
};

export default ChangePassword;