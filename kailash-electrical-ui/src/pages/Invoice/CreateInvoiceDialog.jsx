import { useState } from "react";
import { useTranslation } from "react-i18next";
import { createInvoice } from "../../services/InvoiceService";
import { Alert, Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, Divider, TextField, Typography } from "@mui/material";

const CreateInvoiceDialog = ({
    open,
    onClose,
    booking, 
    onSuccess
}) => {

    const { t } = useTranslation();
    const [notes, setNotes] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const formatCurrency = (amount) => {

        return new Intl.NumberFormat(
            "en-IN",
            {style: "currency", currency: "INT"}
        ).format(amount || 0);
    };

    const handleGenerate = async () => {

        try {

            setLoading(true);
            setError("");

            const invoiceData = {bookingId: booking.id, notes: notes};

            console.log("Generating invoice:", invoiceData);

            const response = await createInvoice(invoiceData);

            console.log("Create invoice response:", response);

            if (!response.success) {

                setError(response.message || t("invoice.generateFailed"));

                return;
            }

            setNotes("");

            onSuccess(response.data);

        } catch (error) {

            console.error("Failed to generate invoice:", error);

            setError(error.response?.data?.message || t("invoice.generateFailed"));

        } finally {
            
            setLoading(false);

        }

    };
    
    const handleClose = () => {

        if (loading) return;

        setError("");
        setNotes("");
        onClose();
    };

    if (!booking) return null;

    return (

        <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">

            <DialogTitle>t("invoice.generateInvoice")</DialogTitle>

            <DialogContent>

                {error && (

                    <Alert severity="error" sx={{ mb: 2 }}>
                        {error}
                    </Alert>

                )}

                <Box sx={{ mb: 2 }}>

                    <Typography variant="body2" color="text.secondary">
                        {t("customer.bookingNumber")}
                    </Typography>

                    <Typography fontWeight={600}>
                        {booking.bookingNumber}
                    </Typography>

                </Box>

                <Box sx={{ mb: 2 }}>

                    <Typography variant="body2" color="text.secondary">
                        {t("customer.customer")}
                    </Typography>

                    <Typography fontWeight={600}>
                        {booking.customerName}
                    </Typography>

                </Box>

                <Typography variant="h6" sx={{ mb: 1 }}>
                    {t("service.services")}
                </Typography>  

                {booking.services?.map((service) => (
                   
                   <Box key={service.serviceId}
                        sx={{ display: "flex", justifyContent: "space-between", py: 1 }}>

                        <Typography>
                            {service.serviceName}
                        </Typography>

                        <Typography>
                            {formatCurrency(service.priceAtBookingTime)}
                        </Typography>

                   </Box>
                   
                ))}     

                <Divider sx={{ my: 2 }} />     

                <Box sx={{ display: "flex", justifyContent: "space-between" }}>

                    <Typography fontWeight={600}>
                        {t("invoice.subtotal")}
                    </Typography>

                    <Typography fontWeight={600}>
                        {formatCurrency(booking.estimatedPrice)}
                    </Typography>

                </Box>    

                <Box sx={{ display: "flex", justifyContent: "space-between", mt: 1 }}>

                    <Typography variant="h6" fontWeight={700}>
                        {t("common.total")}
                    </Typography>

                    <Typography variant="h6" fontWeight={700}>
                        {formatCurrency(booking.estimatedPrice)}
                    </Typography>

                </Box>

                <TextField 
                    label={t("invoice.notes")}
                    value={notes}
                    onChange={(e) => setNotes(e.target.value)}
                    multiline
                    rows={3}
                    fullWidth
                    sx={{ mt: 3 }}
                    placeholder={t("invoice.optionalInvoiceNotes")}
                />

            </DialogContent>

            <DialogActions>

                <Button onClick={handleClose} disabled={loading}>
                    {t("common.cancel")}
                </Button>

                <Button variant="contained" onClick={handleGenerate} disabled={loading}>
                    {loading ? t("invoice.generating") : t("invoice.generateInvoice")}
                </Button>

            </DialogActions>

        </Dialog>

    );
    
};

export default CreateInvoiceDialog;