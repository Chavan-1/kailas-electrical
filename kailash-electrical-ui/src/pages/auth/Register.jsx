import {
    Box,
    Button,
    Paper,
    TextField,
    Typography,
    IconButton,
    InputAdornment
} from "@mui/material";

import {
    Visibility,
    VisibilityOff
} from "@mui/icons-material";

import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { register } from "../../services/authService";
import { useTranslation } from "react-i18next";

function Register() {

    const navigate = useNavigate();
    const { t } = useTranslation();

    const [fullName, setFullName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [mobileNumber, setMobileNumber] = useState("");
    const [address, setAddress] = useState("");

    const [showPassword, setShowPassword] = useState(false);

    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [loading, setLoading] = useState(false);

    const validateForm = () => {

        const trimmedFullName = fullName.trim();
        const trimmedEmail = email.trim();
        const trimmedMobileNumber = mobileNumber.trim();
        const trimmedAddress = address.trim();

        if (!trimmedFullName) {
            setError(t("auth.fullNameRequired"));
            return false;
        }

        if (trimmedFullName.length < 2) {
            setError(t("auth.fullNameMinLength"));
            return false;
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!trimmedEmail) {
            setError(t("auth.emailRequired"));
            return false;
        }

        if (!emailRegex.test(trimmedEmail)) {
            setError(t("auth.invalidEmail"));
            return false;
        }

        // Mobile Number
        const mobileRegex = /^[6-9]\d{9}$/;

        if (!trimmedMobileNumber) {
            setError(t("auth.mobileRequired"));
            return false;
        }

        if (!mobileRegex.test(trimmedMobileNumber)) {
            setError(t("auth.invalidMobile"));
            return false;
        }

        if (!password) {
            setError(t("auth.passwordRequired"));
            return false;
        }

        if (password.length < 8) {
            setError(t("auth.passwordMinLength"));
            return false;
        }

        if (!trimmedAddress) {
            setError(t("auth.addressRequired"));
            return false;
        }

        if (trimmedAddress.length < 10) {
            setError(t("auth.addressMinLength"));
            return false;
        }

        return true;
    };

    const handleSubmit = async (event) => {

        event.preventDefault();

        setError("");
        setSuccess("");

        const isValid = validateForm();

        if (!isValid) return;

        setLoading(true);

        try {

            const registrationData = {
                fullName: fullName.trim(),
                email: email.trim(),
                password: password,
                mobileNumber: mobileNumber.trim(),
                address: address.trim()
            };

            console.log("Registration request:", registrationData);

            const response = await register(registrationData);

            console.log( "Register Response:", response);

            if (response.success) {

                setSuccess( response.message || t("auth.registrationSuccess"));

                setFullName("");
                setEmail("");
                setPassword("");
                setMobileNumber("");
                setAddress("");

                setTimeout(() => {
                    navigate("/login");
                }, 2000);

            } else {

                setError(response.message || t("auth.registrationFailed"));
            }

        } catch (error) {

            console.error( "Registration failed:", error);

            setError( error.response?.data?.message || t("auth.registrationFailed"));

        } finally {

            setLoading(false);
        }
    };

    return (

        <Box
            sx={{
                minHeight: "100vh",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                backgroundColor: "#f5f6fa",
                padding: 2
            }}
        >

            <Paper
                elevation={3}
                sx={{
                    width: 400,
                    maxWidth: "100%",
                    padding: 4,
                    borderRadius: 3
                }}
            >

                <Typography variant="h4" textalign="center" marginbottom={3}>
                    {t("auth.customerRegistration")}
                </Typography>

                <Box component="form" onSubmit={handleSubmit} noValidate>

                    <TextField
                        label={t("auth.fullName")}
                        fullWidth
                        value={fullName}
                        onChange={(event) => {
                            setFullName(event.target.value);
                            setError("");
                        }}
                        margin="normal"
                        required
                        helperText={t("auth.enterFullName")}
                    />

                    <TextField
                        label={t("auth.email")}
                        type="email"
                        fullWidth
                        value={email}
                        onChange={(event) => {
                            setEmail(event.target.value);
                            setError("");
                        }}
                        margin="normal"
                        required
                        helperText={t("auth.emailExample")}
                    />

                    <TextField
                        label={t("auth.mobileNumber")}
                        fullWidth
                        value={mobileNumber}
                        onChange={(event) => {
                            const value = event.target.value
                                    .replace(/\D/g, "")
                                    .slice(0, 10);
                            setMobileNumber(value);
                            setError("");
                        }}
                        margin="normal"
                        required
                        inputprops={{ maxLength: 10, inputMode: "numeric" }}
                        helperText={t("auth.enterMobile")}
                    />

                    <TextField
                        label={t("profile.password")}
                        type={showPassword ? t("profile.text") : t("profile.password")}
                        fullWidth
                        value={password}
                        onChange={(event) => {
                            setPassword(event.target.value);
                            setError("");
                        }}
                        margin="normal"
                        required
                        helperText={t("auth.passwordHint")}
                        inputprops={{
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
                            )
                        }}
                    />

                    <TextField
                        label={t("customer.address")}
                        fullWidth
                        multiline
                        rows={3}
                        value={address}
                        onChange={(event) => {
                            setAddress(event.target.value);
                            setError("");
                        }}
                        margin="normal"
                        required
                        helperText={t("auth.enterAddress")}
                    />

                    {error && (
                        <Typography color="error" sx={{ marginTop: 2, fontSize: "14px" }}>
                            {error}
                        </Typography>
                    )}

                    {success && (
                        <Typography color="success.main" sx={{ marginTop: 2, fontSize: "14px" }}>
                            {success}
                        </Typography>
                    )}

                    <Button
                        type="submit"
                        variant="contained"
                        fullWidth
                        disabled={loading}
                        sx={{ marginTop: 3, padding: "12px" }}
                    >
                        {loading ? t("auth.registering") : t("auth.register") }
                    </Button>

                    <Button
                        variant="text"
                        fullWidth
                        sx={{ marginTop: 1 }}
                        onClick={() => navigate("/login")}
                        disabled={loading}
                    >
                        {t("auth.alreadyHaveAccount")}
                    </Button>

                </Box>

            </Paper>

        </Box>
    );
}

export default Register;