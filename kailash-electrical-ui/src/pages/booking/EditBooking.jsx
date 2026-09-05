import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams } from "react-router-dom";
import { getBookingById, updateBooking } from "../../services/BookingService";
import { Alert, Button, Paper, TextField, Typography } from "@mui/material";
import { ArrowBack, Save } from "@mui/icons-material";

const EditBooking = () => {

    const { t } = useTranslation();
    const { id } = useParams();
    const navigate = useNavigate();
    const [booking, setBooking] = useState(null);
    const [bookingDate, setBookingDate] = useState("");
    const [bookingTime, setBookingTime] = useState("");
    const [remarks, setRemarks] = useState("");
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    useEffect(() => {

        const loadBooking = async () => {

            try {

                setLoading(true);
                setError("");

                const response = await getBookingById(id);

                if (!response.success) {

                    setError(response.message || t("booking.failedToFetchBooking"));
                    return;
                }

                    const data = response.data;

                    setBooking(data);

                    setBookingDate(data.bookingDate || "");

                    setBookingTime(data.bookingTime ? data.bookingTime.substring(0, 5) : "");

                    setRemarks(data.remarks || "");

            } catch (error) {

                console.error("Failed to fetch booking:", error);
                setError(error.response?.data?.message || t("booking.failedToFetchBooking"));

            } finally {

                setLoading(false);

            }
        };

        loadBooking();
        
    }, [id]);


    const handleSubmit = async (event) => {

        event.preventDefault();

        setError("");
        setSuccess("");

        if (!bookingDate) {
            
            setError(t("booking.bookingDateRequired"));
            return;
        }

        if (!bookingTime) {
            
            setError(t("booking.bookingTimeRequired"));
            return;
        }

        try {

            setSaving(true);

            const bookingData = {
                bookingDate: bookingDate, 
                bookingTime: bookingTime, 
                remarks: remarks
            };

            console.log("Updating booking:", bookingData);

            const response = await updateBooking(id, bookingData);

            if (response.success) {

                setSuccess(response.message ||  t("booking.updateSuccess"));

                setTimeout(() => {
                    navigate(`/bookings/${id}`);
                }, 800);

            } else {

                setError(response.message ||  t("booking.updateFailed"))
            }

        } catch (error) {

            console.error("Updaye booking error: ", error);
            setError(error.response?.data?.message || t("booking.updateFailed"));

        } finally {

            setSaving(false);
        }

    };

    if (loading) {

        return (

            <div style={pageStyle}>
                <Typography>{t("booking.loadingBooking")}</Typography>
            </div>
        );

    }

    if (!booking) {
    
        return (

            <div style={pageStyle}>
                
                <Alert severity="error">
                    {error || t("booking.bookingNotFound")}
                </Alert>
            
                <Button
                    sx={{ mt: 2 }}
                    startIcon={<ArrowBack />}
                    onClick={() => navigate("/bookings")}
                >
                    {t("common.back")}
                </Button>

            </div>
        );

    }

    return (

        <div style={pageStyle}>

            <div style={headerStyle}>

                <div>

                    <Typography variant="h4" fontWeight={600}>
                        {t("booking.editBooking")}
                    </Typography>
                    
                    <Typography color="text.secondary" sx={{ mt: 0.5 }}>
                        {booking.bookingNumber}
                    </Typography>

                </div>

                <Button variant="outlined" startIcon={<ArrowBack />} onClick={() => navigate(`/bookings/${id}`)}>
                    {t("common.back")}
                </Button>
            
            </div>

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

            <Paper elevation={2} sx={{ p: 3, borderRadius: 2 }}>

                <form onSubmit={handleSubmit}>

                    <div style={formGridStyle}>

                        <TextField
                            label={t("booking.bookingDate")}
                            type="date"
                            value={bookingDate}
                            onChange={(e) =>
                                setBookingDate(e.target.value)
                            }
                            fullWidth
                            required
                            slotProps={{
                                inputLabel: {
                                    shrink: true
                                }
                            }}
                        />

                        <TextField
                            label={t("booking.bookingTime")}
                            type="time"
                            value={bookingTime}
                            onChange={(e) =>
                                setBookingTime(e.target.value)
                            }
                            fullWidth
                            required
                            slotProps={{
                                inputLabel: {
                                    shrink: true
                                }
                            }}
                        />

                    </div>

                    <TextField 
                        label={t("booking.remarks")}
                        value={remarks}
                        onChange={(e) => setRemarks(e.target.value)}
                        multiline
                        rows={4}
                        sx={{ mt: 3 }}
                        fullWidth
                        placeholder={t("booking.enterRemarks")}
                    />

                    <div style={{ marginTop: "25px", display: "flex", justifyContent: "flex-end", gap: "10px" }}>

                        <Button variant="outlined" onClick={() => navigate(`/bookings/${id}`)}>
                            {t("common.cancel")}
                        </Button>

                        <Button variant="contained" type="submit" startIcon={<Save />} disabled={saving}>
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

const headerStyle = {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "25px"
};

const formGridStyle = {
    display: "grid",
    gridTemplateColumns: "1fr 1fr",
    gap: "20px"
};

const buttonContainerStyle = {
    marginTop: "25px",
    display: "flex",
    justifyContent: "flex-end",
    gap: "10px"
};

export default EditBooking;