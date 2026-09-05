import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams } from "react-router-dom";
import { getServiceById, updateService } from "../../services/ServiceService";
import { Alert, Button, CircularProgress, Divider, FormControl, FormControlLabel, Paper, Switch, TextField, Typography } from "@mui/material";
import { ArrowBack, Save } from "@mui/icons-material";

const EditService = () => {

    const { id } = useParams();
    const navigate = useNavigate();
    const { t } = useTranslation();

    const [form, setForm] = useState({
        basePrice: "",
        estimatedDuration: "",
        active: true
    });

    const [loading, setLoading] = useState(true);
    const[saving, setSaving] = useState(false);
    const[error, setError] = useState("");

    useEffect(() => {

        loadService();

    }, [id]);

    const loadService = async () => {

        try {

            setLoading(true);
            setError("");

            const response = await getServiceById(id);

            console.log("service response:", response);

            if (response.success) {

                const service = response.data;

                setForm({
                    basePrice: service.basePrice ?? "",
                    estimatedDuration: service.estimatedDuration ?? "",
                    active: service.active ?? true
                });

            } else {

                setError(response.message || t("service.fetchError"));

            }

        } catch (error) {

            console.log("Fectch service error:", error);

            setError(error.response?.data?.message || t("service.fetchError"));

        } finally {

            setLoading(false);
        }
    }; 

    const handleChange = (event) => {

        const { name, value } = event.target;

        setForm((previous) => ({
            ...previous, [name]: value
        }));
    };

    const handleStatusChange = (event) => {

        setForm((previous) => ({
            ...previous, active: event.target.checked
        }));
    };

    const handleSubmit = async (event) => {

        event.preventDefault();

        try {

            setSaving(true);
            setError("");

            const payload = {
                basePrice: Number(form.basePrice),
                estimatedDuration: Number(form.estimatedDuration),
                active: form.active
            };

            console.log("Update service payload:", payload);

            const response = await updateService(id, payload);

            console.log("Update service response:", response);

            if (response.success) {

                alert(t("service.updateSuccess"));

                navigate(`/services/${id}`);

            } else {

                setError(response.message || t("service.updateError"));
            }

        } catch (error) {

            console.error("Update service error:", error)
            setError(error.response?.data?.message || t("service.updateError"));

        } finally {

            setSaving(false);
        }
    };

    if (loading) {

        return (

            <div style={centerStyle}>

                <CircularProgress />

                <Typography sx={{ mt: 2 }} color="text.secondary">
                    {t("service.loadingService")}
                </Typography>

            </div>
        );
    }

    return (

        <div style={pageStyle}>

            <div style={headerStyle}>

                <Button startIcon={<ArrowBack />} onClick={() => navigate(`/services/${id}`)}>
                    {t("common.back")}
                </Button>

                <Typography variant="h4" fontWeight={600}>
                    {t("service.editService")}
                </Typography>

            <div />

        </div>

        {error && (

            <Alert severity="error" sx={{ maxWidth: "900px", margin: "0 auto 20px" }}>
                {error}
            </Alert>
            
        )}

        <Paper elevation={3} sx={{ maxWidth: "900px", margin: "0 auto", padding: "30px", borderRadius: 3 }}>

            <form onSubmit={handleSubmit}>

                <Typography variant="h6" fontWeight={600} sx={{ mb: 3 }}>
                    {t("service.basicInformation")}
                </Typography>

                <div style={twoColumnStyle}>

                    <TextField 
                        label={t("service.basePrice")}
                        name="basePrice"
                        type="number"
                        value={form.basePrice}
                        onChange={handleChange}
                        fullWidth
                        required
                        slotProps={{ htmlInput: { min: 0, step: "0.01" } }}
                    />

                    <TextField 
                        label={t("service.duration")}
                        name="estimatedDuration"
                        type="number"
                        value={form.estimatedDuration}
                        onChange={handleChange}
                        fullWidth
                        required
                        slotProps={{ htmlInput: { min: 1 } }}
                        helperText={t("service.durationHint")}
                    />

                </div>

                <Paper variant="outlined" sx={{ mt: 4, p: 2, borderRadius: 2 }}>

                    <FormControlLabel 
                        control={
                            <Switch checked={form.active} onChange={handleStatusChange}/>
                        }
                        label={
                            form.active ? t("service.activeService") : t("service.inactiveService")
                        }
                    />
    
                </Paper>

                <div style={buttonStyle}>

                    <Button variant="outlined" onClick={() => navigate(`/services/${id}`)}>
                        {t("common.cancel")}
                    </Button>

                    <Button type="submit" variant="contained" startIcon={<Save />} disabled={saving}>
                        {saving ? t("common.saving") : t("common.saveChanges")}
                    </Button>

                </div>

            </form>

        </Paper>

        </div>
    );
};

const pageStyle = {
    padding: "30px",
    backgroundColor: "#f5f6fa",
    minHeight: "100vh"
};


const centerStyle = {
    minHeight: "70vh",
    display: "flex",
    flexDirection: "column",
    justifyContent: "center",
    alignItems: "center"
};


const headerStyle = {
    display: "grid",
    gridTemplateColumns: "1fr auto 1fr",
    alignItems: "center",
    marginBottom: "25px"
};


const twoColumnStyle = {
    display: "grid",
    gridTemplateColumns: "1fr 1fr",
    gap: "20px"
};


const buttonStyle = {
    display: "flex",
    justifyContent: "flex-end",
    gap: "15px",
    marginTop: "35px"
};


export default EditService;