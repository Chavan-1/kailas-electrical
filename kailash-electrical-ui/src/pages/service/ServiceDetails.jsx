import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams } from "react-router-dom";
import { getServiceById } from "../../services/ServiceService";
import { Alert, Button, Chip, CircularProgress, Divider, Paper, Typography } from "@mui/material";
import { AccessTime, ArrowBack, Cancel, CheckCircle, CurrencyRupee, Edit } from "@mui/icons-material";
import { getRole } from "../../utils/auth";

const ServiceDetails = () => {

    const { id } = useParams();
    const navigate = useNavigate();
    const { t } = useTranslation();

    const [service, setService] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const role = getRole();
    const isAdmin = role === "ADMIN";

    const loadService = async () => {

        try {

            setLoading(true);
            setError("");

            const response = await getServiceById(id);

            console.log("Service details response:", response);

            if (response.success) {

                setService(response.data);

            } else {

                setError(
                    response.message ||
                    t("service.fetchError")
                );
            }

        } catch (error) {

            console.error("Failed to load service:", error);
            setError(error.response?.data?.message || t("service.fetchError"));

        } finally {

            setLoading(false);
        }
    };

    useEffect(() => {
        loadService();
    }, [id]);

    if (loading) {

        return (

            <div style={centerStyle}>

                <CircularProgress />

                <Typography sx={{ mx: 2 }} color="text.secondary">
                    {t("service.loadingService")}
                </Typography>

            </div>
        );
    }

    if (error) {

        return (

            <div style={pageStyle}>

                <Alert severity="error">{error}</Alert>

                <Button sx={{ mx: 2 }} startIcon={<ArrowBack />} onClick={() => navigate("/services")}>
                    {t("common.back")}
                </Button>

            </div>
        );
    }

    if (!service) return null;

    return (

        <div style={pageStyle}>

            <div style={headerStyle}>

                <Button startIcon={<ArrowBack />} onClick={() => navigate("/services")}>
                    {t("common.back")}
                </Button>

                <Typography variant="h4" fontWeight={600}>
                    {t("service.serviceDetails")}
                </Typography>
                
                {isAdmin && (
                    <Button variant="contained" startIcon={<Edit />} onClick={() => navigate(`/services/${id}/edit`)}>
                        {t("common.edit")}
                    </Button>
                )}

            </div>

            <Paper elevation={3}
                   sx={{
                        maxWidth: "900px",
                        margin: "0 auto",
                        borderRadius: 3,
                        overflow: "hidden"
                   }}>

                    <div style={serviceHeaderStyle}>

                        <Typography variant="h4" fontWeight={600}>
                            {t(service.serviceName)}
                        </Typography>

                        <Chip 
                            icon={service.active ? <CheckCircle /> : <Cancel />}
                            label={service.active ? t("common.active") : t("common.inactive")}
                            color={service.active ? "success" : "default"}
                        />

                    </div>

                    <Divider />

                    <div style={sectionStyle}>

                        <Typography variant="h6" fontWeight={600} sx={{ mb: 1 }}>
                            {t(service.description)}
                        </Typography>

                        <Typography color="text.secondary" sx={{ lineHeight: 1.8 }}>
                            {service.description || "-"}
                        </Typography>

                    </div>

                    <Divider />

                    <div style={sectionStyle}>

                        <Typography variant="h6" fontWeight={600} sx={{ mb: 3 }}>
                            {t(service.basicInformation)}
                        </Typography>

                        <div style={infoGridStyle}>

                            <InfoCard 
                                icon={<CurrencyRupee />}
                                label={t("service.basePrice")}
                                value={`₹${Number(
                                        service.basePrice
                                    ).toLocaleString("en-IN")}`}
                            />

                            <InfoCard
                                icon={<AccessTime />}
                                label={t("service.duration")}
                                value={`${service.estimatedDuration} ${t(
                                    "service.minutes"
                                )}`}
                            />

                            <InfoCard
                                icon={
                                    service.active
                                        ? <CheckCircle />
                                        : <Cancel />
                                }
                                label={t("common.status")}
                                value={
                                    service.active
                                        ? t("common.active")
                                        : t("common.inactive")
                                }
                            />

                            <InfoCard
                                label={t("common.id")}
                                value={service.id}
                            />

                        </div>

                    </div>

            </Paper>

        </div>
    );

};

const InfoCard = ({ icon, label, value }) => {

    return (

        <Paper variant="outlined" sx={{ p: 2.5, borderRadius: 2 }}>

            <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>

                {icon}

                <Typography variant="body2" color="text.secondary">
                    {label}
                </Typography>

            </div>

            <Typography variant="h6" fontWeight={600} sx={{ mt: 1 }}>
                {value}
            </Typography>

        </Paper>
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


const serviceHeaderStyle = {
    padding: "30px",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    gap: "20px"
};


const sectionStyle = {
    padding: "30px"
};


const infoGridStyle = {
    display: "grid",
    gridTemplateColumns: "repeat(2, 1fr)",
    gap: "20px"
};


export default ServiceDetails;