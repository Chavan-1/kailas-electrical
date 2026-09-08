import { Alert, Box, Button, Card, CardContent, IconButton, InputAdornment, TextField, Typography } from "@mui/material";

import { ElectricalServices, EmailOutlined, LockOutlined, Visibility, VisibilityOff, CheckCircleOutlined } from "@mui/icons-material";

import { useState } from "react";
import { login } from "../../services/authService";
import { saveAuth } from "../../utils/auth";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";

function Login() {

    const { t } = useTranslation();
    const navigate = useNavigate();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const [showPassword, setShowPassword] = useState(false);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (event) => {

        event.preventDefault();

        setLoading(true);
        setError("");

        try {

            const response = await login({
                email: email,
                password: password
            });

            console.log("Login Response: ", response);

            if (!response.success) {
                setError(response.message || t("auth.loginFailedCredentials"));
                return;
            }

            saveAuth(response);

            const role = response.data.role;

            if (role === "ADMIN") {

                navigate("/dashboard", { replace: true });

            } else if (role === "CUSTOMER") {

                navigate("/customer-dashboard", { replace: true });

            } else {

                setError(t("auth.unknownUserRole"));
            }

        } catch (error) {

            console.log("Login failed", error);

            if (error.response?.data?.message) {

                setError(error.response.data.message);

            } else {

                setError(t("auth.loginFailed"));
            }

        } finally {

            setLoading(false);
        }
    };

    return (

        <Box
            sx={{
                minHeight: "100vh",
                display: "flex",
                background:"linear-gradient(135deg, #f5f7fb 0%, #e8eef7 100%)",
            }}
        >

            <Box
                sx={{
                    flex: 1,
                    display: { xs: "none", md: "flex"},
                    flexDirection: "column",
                    justifyContent: "center",
                    px: { md: 6, lg: 10 },
                    background: "linear-gradient(135deg, #0f3d56 0%, #1976a8 100%)",
                    color: "white",
                    position: "relative",
                    overflow: "hidden",
                }}
            >

                {/* Decorative circles */}
                <Box
                    sx={{
                        position: "absolute",
                        width: 350,
                        height: 350,
                        borderRadius: "50%",
                        background: "rgba(255,255,255,0.06)",
                        top: -120,
                        right: -100,
                    }}
                />

                <Box
                    sx={{
                        position: "absolute",
                        width: 250,
                        height: 250,
                        borderRadius: "50%",
                        background: "rgba(255,255,255,0.05)",
                        bottom: -80,
                        left: -80,
                    }}
                />

                <Box sx={{ position: "relative", zIndex: 1 }}>

                    <Box
                        sx={{
                            display: "flex",
                            alignItems: "center",
                            gap: 1.5,
                            mb: 4,
                        }}
                    >

                        <Box
                            sx={{
                                width: 52,
                                height: 52,
                                borderRadius: 2,
                                display: "flex",
                                alignItems: "center",
                                justifyContent: "center",
                                backgroundColor: "rgba(255,255,255,0.15)",
                            }}
                        >
                            <ElectricalServices fontSize="large" />
                        </Box>

                        <Box>
                            <Typography variant="h5" fontWeight={700}>
                                {t("common.appName")}
                            </Typography>

                            <Typography  variant="body2" sx={{ opacity: 0.8 }}>
                                {t("auth.systemTitle")}
                            </Typography>
                        </Box>

                    </Box>

                    <Typography
                        variant="h2"
                        fontWeight={700}
                        sx={{
                            fontSize: {
                                md: "2.5rem",
                                lg: "3.2rem",
                            },
                            lineHeight: 1.15,
                            mb: 2,
                        }}
                    >
                        {t("auth.professionalElectricalServices")}
                        <br />
                        {t("auth.servicesMadeSimple")}
                    </Typography>

                    <Typography
                        variant="body1"
                        sx={{
                            maxWidth: 500,
                            opacity: 0.85,
                            lineHeight: 1.8,
                            mb: 4,
                        }}
                    >
                        {t("auth.manageCustomersBookings")}
                    </Typography>

                    {/* Features */}
                    <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>

                        {[
                            t("auth.easyCustomerBookingManagement"),
                            t("auth.trackServicesPayments"),
                            t("auth.generateProfessionalInvoices"),
                        ].map((feature) => (

                            <Box
                                key={feature}
                                sx={{
                                    display: "flex",
                                    alignItems: "center",
                                    gap: 1.5,
                                }}
                            >

                                <CheckCircleOutlined
                                    sx={{
                                        fontSize: 21,
                                        opacity: 0.9,
                                    }}
                                />

                                <Typography variant="body2">
                                    {feature}
                                </Typography>

                            </Box>

                        ))}

                    </Box>

                </Box>

            </Box>


            <Box
                sx={{
                    flex: 1,
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    px: 2,
                    py: 4,
                }}
            >

                <Card
                    elevation={0}
                    sx={{
                        width: "100%",
                        maxWidth: 450,
                        borderRadius: 4,
                        border: "1px solid",
                        borderColor: "divider",
                        boxShadow: "0 20px 50px rgba(0,0,0,0.08)",
                    }}
                >

                    <CardContent sx={{ p: { xs: 3, sm: 4 } }}>

                        {/* Mobile logo */}
                        <Box
                            sx={{
                                display: {
                                    xs: "flex",
                                    md: "none",
                                },
                                alignItems: "center",
                                justifyContent: "center",
                                gap: 1,
                                mb: 3,
                            }}
                        >

                            <ElectricalServices
                                sx={{
                                    fontSize: 32,
                                    color: "primary.main",
                                }}
                            />

                            <Typography variant="h6" fontWeight={700}>
                                {t("common.appName")}
                            </Typography>

                        </Box>


                        {/* Heading */}
                        <Typography variant="h4" fontWeight={700} sx={{ mb: 1 }}>
                            {t("common.login")}
                        </Typography>

                        <Typography variant="body2" color="text.secondary" sx={{ mb: 3.5 }}>
                            {t("auth.welcomeBack")}
                        </Typography>


                        {/* Form */}
                        <Box component="form" onSubmit={handleSubmit}>

                            {/* Email */}
                            <TextField
                                fullWidth
                                label={t("customer.email")}
                                type="email"
                                value={email}
                                onChange={(event) =>
                                    setEmail(event.target.value)
                                }
                                required
                                margin="normal"
                                autoComplete="email"
                                InputProps={{
                                    startAdornment: (
                                        <InputAdornment position="start">
                                            <EmailOutlined
                                                fontSize="small"
                                                color="action"
                                            />
                                        </InputAdornment>
                                    ),
                                }}
                            />


                            {/* Password */}
                            <TextField
                                fullWidth
                                label={t("auth.password")}
                                type={showPassword ? "text" : "password"}
                                value={password}
                                onChange={(event) =>
                                    setPassword(event.target.value)
                                }
                                required
                                margin="normal"
                                autoComplete="current-password"
                                InputProps={{
                                    startAdornment: (
                                        <InputAdornment position="start">
                                            <LockOutlined
                                                fontSize="small"
                                                color="action"
                                            />
                                        </InputAdornment>
                                    ),

                                    endAdornment: (
                                        <InputAdornment position="end">
                                            <IconButton
                                                onClick={() =>
                                                    setShowPassword(!showPassword)
                                                }
                                                edge="end"
                                            >
                                                {showPassword
                                                    ? <VisibilityOff />
                                                    : <Visibility />
                                                }
                                            </IconButton>
                                        </InputAdornment>
                                    ),
                                }}
                            />


                            {/* Forgot Password */}
                            <Box
                                sx={{
                                    display: "flex",
                                    justifyContent: "flex-end",
                                    mt: 1,
                                    mb: 2,
                                }}
                            >

                                <Button
                                    type="button"
                                    variant="text"
                                    size="small"
                                    onClick={() =>
                                        navigate("/forgot-password")
                                    }
                                    sx={{
                                        textTransform: "none",
                                        fontWeight: 600,
                                    }}
                                >
                                    {t("auth.forgotPassword")}?
                                </Button>

                            </Box>


                            {/* Error */}
                            {error && (

                                <Alert severity="error"
                                        sx={{
                                            mb: 2,
                                            borderRadius: 2,
                                        }}
                                >
                                    {error}
                                </Alert>

                            )}


                            {/* Login */}
                            <Button
                                fullWidth
                                type="submit"
                                variant="contained"
                                size="large"
                                disabled={loading}
                                sx={{
                                    height: 48,
                                    borderRadius: 2,
                                    textTransform: "none",
                                    fontSize: "1rem",
                                    fontWeight: 600,
                                    boxShadow: "none",
                                    "&:hover": {
                                        boxShadow: "0 6px 16px rgba(25,118,210,0.25)",
                                    },
                                }}
                            >
                                {loading
                                    ? t("auth.loggingIn")
                                    : t("common.login")
                                }
                            </Button>


                            {/* Register */}
                            <Box
                                sx={{
                                    display: "flex",
                                    justifyContent: "center",
                                    alignItems: "center",
                                    gap: 0.5,
                                    mt: 3,
                                }}
                            >

                                <Typography
                                    variant="body2"
                                    color="text.secondary"
                                >
                                    {t("auth.noAccount")}
                                </Typography>

                                <Button
                                    type="button"
                                    variant="text"
                                    onClick={() =>
                                        navigate("/register")
                                    }
                                    disabled={loading}
                                    sx={{
                                        textTransform: "none",
                                        fontWeight: 600,
                                        minWidth: "auto",
                                        p: 0.5,
                                    }}
                                >
                                    {t("auth.register")}
                                </Button>

                            </Box>

                        </Box>

                    </CardContent>

                </Card>

            </Box>

        </Box>
    );
}

export default Login;