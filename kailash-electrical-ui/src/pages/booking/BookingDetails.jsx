import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams } from "react-router-dom";
import { getBookingById, getMyBooking, updateBookingStatus } from "../../services/BookingService";
import { getProfile } from "../../services/ProfileService";
import { Alert, Button, Divider, FormControl, MenuItem, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Select } from "@mui/material";
import { ArrowBack, Edit, Receipt, Save } from "@mui/icons-material";
import { generateInvoice, getInvoices } from "../../services/InvoiceService";

const BookingDetails = () => {

    const { id } = useParams();
    const { t } = useTranslation();
    const navigate = useNavigate();

    const [booking, setBooking] = useState(null);
    const [role, setRole] = useState("");
    
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [selectedStatus, setSelectedStatus] = useState("");
    const [updatingStatus, setUpdatingStatus] = useState(false);
    const [statusSuccess, setStatusSuccess] = useState("");
    const [statusError, setStatusError] = useState("");

    const [generatingInvoice, setGeneratingInvoice] = useState(false);
    const [invoiceError, setInvoiceError] = useState("");

    const [existingInvoice, setExistingInvoice] = useState(null);


    const loadBooking = async (userRole) => {

        try {

            setLoading(true);
            setError("");

            let response;

            if (userRole === "CUSTOMER") {

                response = await getMyBooking(id);

            } else {

                response = await getBookingById(id);

            }

            if (response.success) {

                setBooking(response.data);

                if (response.data.status === "COMPLETED") {

                    checkExistingInvoice(response.data);
                }
                setSelectedStatus(response.data.status);
                setStatusSuccess("");
                setStatusError("");

            } else {

                setError(response.message || t("booking.failedToFetchBooking"));
                
            }

        } catch (error) {

            console.error("Failed to fetch booking:", error);
            setError(error.response?.data?.message || t("booking.failedToFetchBooking"));

        } finally {

            setLoading(false);
        }
    };

    useEffect(() => {

        const load = async () => {

            try {

                const profile = await getProfile();

                if (profile.success) {

                    const userRole = profile.data.role;

                    console.log("Logged-in role:", profile.data.role);

                    setRole(userRole);

                    await loadBooking(userRole);
                }
            } catch (error) {

                console.error("Failed to load profile:", error);
                setError(error.response?.data?.message || t("profile.loadProfileFailed"));
                setLoading(false);
            }
        };

        load();

    }, [id]);

    const formatDate = (date) => {

        if (!date) return "-";

        return new Date(`${date}T00:00:00`).toLocaleDateString("en-IN");
    };

    const formatTime = (time) => {

        if (!time) return "-";

        const [hours, minutes] = time.split(":");

        const date = new Date();

        date.setHours(Number(hours), Number(minutes));

        return date.toLocaleTimeString(
            "en-IN", {
                        hour: "2-digit",
                        minute: "2-digit"
                    });
    };

    const getStatusColor = (status) => {

        switch (status) {

            case "PENDING":
                return "#ed6c02";

            case "CONFIRMED":
                return "#1976d2";

            case "IN_PROGRESS":
                return "#7b1fa2";

            case "COMPLETED":
                return "#2e7d32";

            case "CANCELLED":
                return "#757575";

            default:
                return "#757575";
        }
    };

    const handleStatusUpdate = async () => {

        if (!selectedStatus || selectedStatus === booking.status) return;

        try {

            setUpdatingStatus(true);
            setStatusError("");
            setStatusSuccess("");

            const response = await updateBookingStatus(booking.id, selectedStatus);

            if (response.success) {

                setBooking(response.data);
                
                setSelectedStatus(response.data.status);

                setStatusSuccess(response.message || t("booking.statusUpdateSuccess"));

            } else {

                setStatusError(response.message || t("booking.statusUpdateFailed"));

            }

        } catch (error) {

            console.error("Failed to update booking status:", error);

            setStatusError(error.response?.data?.message || t("booking.statusUpdateFailed"));

            setSelectedStatus(booking.status);

        } finally {

            setUpdatingStatus(false);
        }
    };

    const getAllowedStatuses = (status) => {

        switch (status) {

            case "PENDING":
                return ["CONFIRMED", "CANCELLED"];

            case "CONFIRMED":
                return ["IN_PROGRESS", "CANCELLED"];

            case "IN_PROGRESS":
                return ["COMPLETED", "CANCELLED"];

            default:
                return [];
        }
    };

    const formatStatus = (status) => {

        if (!status) return "-";

        return status
            .toLowerCase()
            .replace(/_/g, " ")
            .replace(/\b\w/g, (char) => char.toUpperCase());
    };

    const handleGenerateInvoice = async () => {

        try {

            setGeneratingInvoice(true);
            setInvoiceError("");

            const response = await generateInvoice(booking.id);     

            if (!response.success) {

                setInvoiceError(response.message || t("invoice.generateFailed"));

                return;
            }

            navigate(`/invoices/${response.data.id}`);

        } catch (error) {

            console.error("Generate invoice error:", error);

            setInvoiceError(error.response?.data?.message || ("invoice.generateFailed"));

        } finally {

            setGeneratingInvoice(false);

        }

    };

    const checkExistingInvoice = async (bookingData) => {

        try {

            const response = await getInvoices({
                keyword: bookingData.bookingNumber,
                page: 0,
                size: 1
            });

            if (response.success && response.data?.content?.length > 0) {

                setExistingInvoice(response.data.content[0]);

            } else {

                setExistingInvoice(null);
            }

        } catch (error) {

            console.error("Failed to check existing invoice", error);
            setExistingInvoice(null);
        }
    };

    if (loading) {

        return (

            <div style={pageStyle}>
                <Typography>t("booking.loadingBooking")</Typography>
            </div>
        );

    }

    if (error) {

        return (

            <div style={pageStyle}>
                
                <Alert severity="error">{error}</Alert>

                <Button sx={{ mt: 2 }} startIcon={<ArrowBack />} onClick={() => navigate("/bookings")}>
                    t("booking.backToBookings")
                </Button>
           
            </div>
        );

    }

    if (!booking) return null;

    return (

        <div style={pageStyle}>

            <div style={headerStyle}>

                <div>

                    <Typography variant="h4" fontWeight={600}>{t("booking.bookingDetails")}</Typography>
                    
                    <Typography color="text.secondary" sx={{ mt: 0.5 }}>{booking.bookingNumber}</Typography>

                </div>

                <div style={{ display: "flex", gap: "10px" }}>

                    <Button variant="outlined" startIcon={<ArrowBack />} onClick={() => navigate("/bookings")}>
                        {t("common.back")}
                    </Button>

                    {booking.status !== "COMPLETED" && booking.status !== "CANCELLED" && (

                        <Button variant="contained"
                                startIcon={<Edit />}
                                onClick={() => navigate(`/bookings/${id}/edit`)}
                        >
                            {t("common.edit")}
                        </Button>

                    )}

                    {booking.status === "COMPLETED" && (

                        existingInvoice ? (

                            <Button variant="contained"
                                color="primary"
                                startIcon={<Receipt />}
                                onClick={() => navigate(`/invoices/${existingInvoice.id}`)}
                            >
                                {t("booking.viewInvoice")}
                            </Button>

                        ) : (

                            <Button variant="contained"
                                    color="success"
                                    startIcon={<Receipt />}
                                    onClick={handleGenerateInvoice}
                                    disabled={generatingInvoice}
                            >
                                {generatingInvoice ? t("invoice.generating") : t("invoice.generateInvoice")}
                            </Button>

                    ))}

                </div>

            </div>

            {statusError && (

                <Alert severity="error" sx={{ mb: 2 }} onClose={() => setStatusError("")}>
                    {statusError}
                </Alert>

            )}

            {statusSuccess && (

                <Alert severity="success" sx={{ mb: 2 }} onClose={() => setStatusSuccess("")}>
                    {statusSuccess}
                </Alert>

            )}

            <Paper elevation={2} sx={{ p: 3, mb: 3, borderRadius: 2 }}>

                <Typography variant="h6" fontWeight={600} sx={{ mb: 2 }}>
                    {t("booking.bookingInformation")}
                </Typography>

                <div style={infoGridStyle}>

                    <InfoItem label={t("booking.bookingNumber")}
                              value={booking.bookingNumber}
                    />

                    <InfoItem label={t("booking.bookingDate")}
                              value={formatDate(booking.bookingDate)}
                    />

                    <InfoItem label={t("booking.bookingTime")}
                              value={formatTime(booking.bookingTime)}
                    />

                    <InfoItem label={t("booking.status")}
                              value={
                                <span style={{
                                                ...statusStyle,
                                                backgroundColor: getStatusColor(booking.status)
                                            }}>
                                    {formatStatus(booking.status)}
                                </span>
                              }
                    />

                    {role === "ADMIN" && getAllowedStatuses(booking.status).length > 0 && (

                        <div>

                            <Typography variant="body2" color="text.secondary" sx={{ mb: 0.5 }}>
                                {t("booking.changeStatus")}
                            </Typography>

                            <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>

                                <FormControl size="small" sx={{ minWidth: 160 }}>

                                    <Select value={selectedStatus} 
                                            onChange={(event) => setSelectedStatus(event.target.value)} 
                                            disabled={updatingStatus}>

                                        <MenuItem value={booking.status} disabled>
                                            {formatStatus(booking.status)}
                                        </MenuItem>

                                        {getAllowedStatuses(booking.status).map((status) => (

                                            <MenuItem key={status} value={status}>
                                                {status.replace("_", " ")}
                                            </MenuItem>

                                        ))}

                                    </Select>

                                </FormControl>

                                <Button variant="contained"
                                        size="small"
                                        startIcon={<Save />}
                                        disabled={updatingStatus || selectedStatus === booking.status}
                                        onClick={handleStatusUpdate}
                                >
                                    {updatingStatus ? t("booking.updating") : t("common.update")}
                                </Button>

                            </div>

                        </div>

                    )}

                    {role === "ADMIN" && (

                        <>
                            <InfoItem label={t("booking.customer")}
                              value={booking.customerName}
                            />

                            <InfoItem label={t("customer.phoneNumber")}
                                    value={booking.phoneNumber}
                            />
                        </>

                    )}

                </div>

            </Paper>

            <Paper elevation={2} sx={{ borderRadius: 2, mb: 3 }}>

                <div style={{ padding: "24px" }}>

                    <Typography variant="h6" fontWeight={600}>
                        {t("service.services")}
                    </Typography>

                </div>

                <Divider />

                <TableContainer>

                    <Table>

                        <TableHead>

                            <TableRow>

                                <TableCell>{t("booking.service")}</TableCell>

                                <TableCell align="right">{t("booking.price")}</TableCell>

                            </TableRow>

                        </TableHead>

                        <TableBody>

                            {booking.services?.map ((service) => (

                                <TableRow key={service.serviceId}>

                                    <TableCell>{service.serviceName}</TableCell>

                                    <TableCell align="right">
                                            ₹{Number(
                                                service.priceAtBookingTime ||
                                                service.price || 0
                                            ).toLocaleString("en-IN")}
                                    </TableCell>

                                </TableRow>
                            ))}

                        </TableBody>

                    </Table>

                </TableContainer>

                <Divider />

                <div style={{ display: "flex", justifyContent: "flex-end", padding: "20px 24px" }}>

                    <Typography variant="h6" fontWeight={600}>
                            {t("invoice.estimatedPrice")}: ₹
                            {Number(booking.estimatedPrice ||
                                    booking.totalAmount || 0
                                    ).toLocaleString("en-IN")}
                    </Typography>

                </div>

            </Paper>

            <Paper elevation={2} sx={{ borderRadius: 2, p: 3 }}>

                <Typography variant="h6" fontWeight={600} sx={{ mb: 1 }}>
                    {t("booking.remarks")}
                </Typography>

                <Typography color={booking.remarks ? "text.primary" : "text.secondary"}>
                    {booking.remarks || "No remarks"}
                </Typography>

            </Paper>

        </div>

    );

};

const InfoItem = ({label, value}) => {

    return (

        <div>
            <Typography variant="body2" color="text.secondary">{label}</Typography>
            <Typography variant="body1" fontWeight={500} sx={{ mt: 0.5 }}>{value || "-"}</Typography>
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

const infoGridStyle = {
    display: "grid",
    gridTemplateColumns: "repeat(4, 1fr)",
    gap: "25px"
};

const statusStyle = {
    display: "inline-block",
    color: "#fff",
    padding: "5px 12px",
    borderRadius: "15px",
    fontSize: "12px",
    fontWeight: "600"
};

export default BookingDetails;