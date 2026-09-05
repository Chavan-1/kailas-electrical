import { useEffect, useState } from "react";
import { getProfile, updateProfile } from "../../services/ProfileService";
import { useTranslation } from "react-i18next";
import { Alert, Box, Button, Card, CardContent, CircularProgress, Divider, Grid, TextField, Typography } from "@mui/material";
import { Cancel, Edit, Email, Lock, Password, Person, Phone, Save } from "@mui/icons-material";
import { useNavigate } from "react-router-dom";


function Profile() {

    const navigate = useNavigate();
    const { t } = useTranslation();
    const [profile, setProfile] = useState(null);
    const [formData, setFormData] = useState({fullName: "", mobileNumber: ""});
    const [editing, setEditing] = useState(false);
    const [loading, setLoading] = useState(false);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    useEffect(() => {
        loadProfile();
    }, []);

    const loadProfile = async () => {

        try {

            setLoading(true);
            setError("");

            const response = await getProfile();

            console.log("Profile API response:", response);

            if (response.success) {

                setProfile(response.data);

                setFormData({
                    fullName: response.data.fullName || "",
                    mobileNumber: response.data.mobileNumber || ""
                });

            } else {

                setError(response.message || t("profile.loadProfileFailed"));
            }

        } catch (error) {

            console.error("Failed to load profile:", error);

            setError(error.response?.data?.message || t("profile.loadProfileFailed"));

        } finally {

            setLoading(false);

        }
    };

    const handleChange = (event) => {

        const { name, value } = event.target;

        setFormData((previous) => ({
                    ...previous,
                    [name]: value
                }));
    };

    const handleEdit = () => {

        setSuccess("");
        setError("");
        setEditing(true);
    };

    const handleCancel = () => {

        setFormData({
                    fullName: profile?.fullName || "",
                    mobileNumber: profile?.mobileNumber || ""
        });

        setError("");
        setEditing(false);
    };

    const handleSubmit = async (event) => {

        event.preventDefault();

        try {

            setSaving(true);
            setError("");
            setSuccess("");

            const response = await updateProfile(formData);

            console.log("Update profile response:", response);

            if (response.success) {

                setProfile(response.data);

                setFormData({
                    fullName: response.data.fullName || "",
                    mobileNumber: response.data.mobileNumber || ""
                });

                setEditing(false);

                setSuccess(response.message || t("profile.updateSuccess"));

            } else {

                setError(response.message || t("profile.updateFailed"));
            }

        } catch (error) {

            console.error("Update profile error:", error);

            setError(error.response?.data?.message || t("profile.updateFailed"));

        } finally {

            setSaving(false);
        }
    };

    if (loading) {

        return (

            <Box sx={{ 
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                minHeight: "60vh"
             }}>

                <CircularProgress />

            </Box>
        );
    }

    if (!profile) {

        return (

            <Box sx={pageStyle}>

                <Alert severity="error">
                    {error || t("profile.profileNotFound")}
                </Alert>

            </Box>
        );
    }

    return (

        <Box sx={pageStyle}>

            <Box sx={headerStyle}>

                <Box>

                    <Typography variant="h4" fontWeight={600}>
                        {t("profile.myProfile")}
                    </Typography>

                    <Typography
                        variant="body1"
                        color="text.secondary"
                        sx={{ mt: 1 }}
                    >
                        {t("profile.viewUpdateProfile")}
                    </Typography>

                </Box>

                {!editing && (
                    <Button
                        variant="contained"
                        startIcon={<Edit />}
                        onClick={handleEdit}
                    >
                        {t("profile.editProfile")}
                    </Button>
                )}

            </Box>

            {error && (
                <Alert
                    severity="error"
                    sx={{ mb: 2 }}
                    onClose={() => setError("")}
                >
                    {error}
                </Alert>
            )}

            {success && (
                <Alert
                    severity="success"
                    sx={{ mb: 2 }}
                    onClose={() => setSuccess("")}
                >
                    {success}
                </Alert>
            )}

            <Grid container spacing={3}>

                <Grid size={{ xs: 12, md: 8 }}>

                    <Card elevation={2}>

                        <CardContent sx={{ p: 4 }}>

                            <Box
                                sx={{
                                    display: "flex",
                                    alignItems: "center",
                                    gap: 2,
                                    mb: 3
                                }}
                            >

                                <Person sx={{ fontSize: 45 }} />

                                <Box>

                                    <Typography
                                        variant="h6"
                                        fontWeight={600}
                                    >
                                        {t("profile.personalInformation")}
                                    </Typography>

                                    <Typography
                                        variant="body2"
                                        color="text.secondary"
                                    >
                                        {t("profile.accountInformationText")}
                                    </Typography>

                                </Box>

                            </Box>

                            <Divider sx={{ mb: 3 }} />

                            <form onSubmit={handleSubmit}>

                                <Grid container spacing={3}>

                                    <Grid size={{ xs: 12 }}>

                                        <TextField
                                            fullWidth
                                            label={t("customer.fullName")}
                                            name="fullName"
                                            value={formData.fullName}
                                            onChange={handleChange}
                                            disabled={!editing}
                                            required
                                        />

                                    </Grid>

                                    <Grid size={{ xs: 12 }}>

                                        <TextField
                                            fullWidth
                                            label={t("customer.email")}
                                            value={profile.email || ""}
                                            disabled
                                        />

                                    </Grid>

                                    <Grid size={{ xs: 12 }}>

                                        <TextField
                                            fullWidth
                                            label={t("customer.phoneNumber")}
                                            name="mobileNumber"
                                            value={formData.mobileNumber}
                                            onChange={handleChange}
                                            disabled={!editing}
                                            required
                                        />

                                    </Grid>

                                    <Grid size={{ xs: 12 }}>

                                        <TextField
                                            fullWidth
                                            label={t("profile.role")}
                                            value={profile.role || ""}
                                            disabled
                                        />

                                    </Grid>

                                </Grid>

                                {editing && (

                                    <Box
                                        sx={{
                                            display: "flex",
                                            justifyContent: "flex-end",
                                            gap: 2,
                                            mt: 4
                                        }}
                                    >

                                        <Button
                                            variant="outlined"
                                            startIcon={<Cancel />}
                                            onClick={handleCancel}
                                            disabled={saving}
                                        >
                                            {t("common.cancel")}
                                        </Button>

                                        <Button
                                            type="submit"
                                            variant="contained"
                                            startIcon={<Save />}
                                            disabled={saving}
                                        >
                                            {saving
                                                ? t("common.saving")
                                                : t("common.saveChanges")}
                                        </Button>

                                    </Box>

                                )}

                            </form>

                        </CardContent>

                    </Card>

                </Grid>

                <Grid size={{ xs: 12, md: 4 }}>

                    <Card elevation={2}>

                        <CardContent sx={{ p: 4 }}>

                            <Typography variant="h6" fontWeight={600} sx={{ mb: 3 }}>
                                {t("profile.accountInformation")}
                            </Typography>

                            <Box sx={infoItemStyle}>
                                <Email />

                                <Box>
                                    <Typography variant="caption">
                                        {t("customer.email")}
                                    </Typography>

                                    <Typography variant="body2">
                                        {profile.email}
                                    </Typography>
                                </Box>
                            </Box>

                            <Box sx={infoItemStyle}>
                                <Phone />

                                <Box>
                                    <Typography variant="caption">
                                        {t("customer.phoneNumber")}
                                    </Typography>

                                    <Typography variant="body2">
                                        {profile.mobileNumber}
                                    </Typography>
                                </Box>
                            </Box>

                            <Box sx={infoItemStyle}>
                                <Person />

                                <Box>
                                    <Typography variant="caption">
                                        {t("profile.role")}
                                    </Typography>

                                    <Typography variant="body2">
                                        {profile.role}
                                    </Typography>
                                </Box>
                            </Box>

                            <Box sx={infoItemStyle}>
                                <Password />

                                <Box>

                                    <Button fullWidth
                                            variant="contained"
                                            sx={{ mt: 2 }}
                                            onClick={() => navigate("/change-password")}>
                                        {t("profile.changePassword")}
                                    </Button>

                                </Box>
                            </Box>

                        </CardContent>

                    </Card>

                </Grid>

            </Grid>

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

const infoItemStyle = {
    display: "flex",
    alignItems: "center",
    gap: 2,
    mb: 3
};

export default Profile;