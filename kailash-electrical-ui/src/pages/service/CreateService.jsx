import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { createService } from "../../services/ServiceService";
import { Alert, Button, Divider, MenuItem, Paper, TextField, Typography } from "@mui/material";
import { ArrowBack, Save } from "@mui/icons-material";

const CreateService = () => {

    const navigate = useNavigate();
    const { t } = useTranslation();

    const [form, setForm] = useState({

        basePrice: "",
        estimatedDuration: "",
        languageCode: "EN",
        serviceName: "",
        description: ""
    });

    const [error, setError] = useState("");
    const [saving, setSaving] = useState(false);

    const handleChange = (event) => {
        
        const { name, value } = event.target;

        setForm((previous) => ({
            ...previous,
            [name]: value
        }));
    };

    const handleTranslationChange = (index, field, value) => {

        setForm((previous) => {

            const translations = [...previous.translations];

            translations[index] = {
                ...translations[index],
                [field]: value
            };

            return {
                ...previous,
                translations
            };
        });
    };

    const handleSubmit = async (event) => {

        event.preventDefault();

        try {

            setError("");
            setSaving(true);

            const payload = {

                basePrice: Number(form.basePrice),
                estimatedDuration: Number(form.estimatedDuration),
                translations: [{
                    languageCode: form.languageCode,
                    serviceName: form.serviceName.trim(),
                    description: form.description.trim()
                }]
            };

            console.log("Create service payload:", payload);

            const response = await createService(payload);

            if (response.success) {

                alert(t("service.createSuccess"));

                navigate("/services");

            } else {

                setError(response.message || t("service.createError"));
            }
        } catch (error) {

            console.error("Create service error:", error);

            if (error.response?.status === 403) {
                
                setError(t("service.accessDenied"));

            } else {
                setError(error.response?.data?.message || ("service.createError"));
            }

        } finally {

            setSaving(false);
        }
    };

    return (

        <div style={pageStyle}>
            
            <div style={headerStyle}>
                
                <Button startIcon={<ArrowBack />} onClick={() => navigate("/services")}>
                    {t("common.back")}
                </Button>

                <Typography variant="h4">
                    {t("service.createService")}
                </Typography>

            <div />

        </div>

        {error && (
            <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>
        )}

        <Paper elevation={3}
               sx={{ 
                    maxWidth: "900px",
                    margin: "0 auto",
                    padding: "30px",
                    borderRadius: 3
                }}>

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

                <Divider sx={{ my: 4 }} />

                <Typography variant="h6" sx={{ mb: 1 }} fontWeight={600}>
                    {t("service.serviceInformation")}
                </Typography>

                <Paper variant="outlined" sx={{ p: 3, borderRadius: 2 }}>

                    <TextField
                            select
                            label={t("service.language")}
                            name="languageCode"
                            value={form.languageCode}
                            onChange={handleChange}
                            fullWidth
                            sx={{ mb: 3 }}
                    >
                        <MenuItem value="EN">{t("service.english")}</MenuItem>
                        <MenuItem value="HI">{t("service.hindi")}</MenuItem>
                        <MenuItem value="MR">{t("service.marathi")}</MenuItem>

                    </TextField>

                    <TextField
                        label={t("service.serviceName")}
                        name="serviceName"
                        value={form.serviceName}
                        onChange={handleChange}
                        fullWidth
                        required
                        sx={{ mb: 3 }}
                    />

                    <TextField
                        label={t("service.description")}
                        name="description"
                        value={form.description}
                        onChange={handleChange}
                        multiline
                        fullWidth
                        rows={4}
                    />

                </Paper>

                <div style={buttonStyle}>

                    <Button variant="outlined" onClick={() => navigate("/services")}>
                        {t("common.cancel")}
                    </Button>

                    <Button type="submit"
                            variant="contained"
                            startIcon={<Save />}
                            disabled={saving}
                        >
                            {saving ? t("common.saving") : t("common.save")}
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
    marginTop: "30px"
};

export default CreateService;