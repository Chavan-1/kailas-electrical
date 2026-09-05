import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getCustomerDetails } from "../../services/CustomerService";
import { Box, Button, Paper, Typography } from "@mui/material";
import { useTranslation } from "react-i18next";

const CustomerDetails = () => {

    const { id } = useParams();

    const navigate = useNavigate();

    const [details, setDetails] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const { t } = useTranslation();

    useEffect(() => {

        loadDetails();

    }, [id]);

    const loadDetails = async () => {

        try {

            const response = await getCustomerDetails(id);

            console.log("Customer Details:", response);

            if (response.success) {

                setDetails(response.data);

            } else {

                setError(response.message || t("customer.failedToLoadCustomerDetails"));
            }
        } catch (error) {

            console.error(error);
            setError(error.response?.data?.message || t("customer.failedToLoadCustomerDetails"));

        } finally {

            setLoading(false);
        }
    };

    const formatCurrency = (value) => {

        if (value === null || value === undefined) {
            return "₹0.00";
        }

        return new Intl.NumberFormat("en-IN", {style: "currency", currency: "INR"}).format(value);
    };

    if (loading) {
        return (
            <div style={pageStyle}>
                {t("customer.loadingDetails")}
            </div>
        );
    }

    if (error) {

        return (
            <div style={pageStyle}>
                <p style={{ color: "#d32f2f" }}>{error}</p>
            </div>
        );
    }

    if (!details) {

        <div style={pageStyle}>
            {t("customer.customerNotFound")}
        </div>
    }

    const customer = details.customer;

    return (

        <div style={pageStyle}>
            
            <div style={headerStyle}>
                
                <Typography variant="h4">{t("customer.customerDetails")}</Typography>

                <div style={{ display: "flex", gap: "10px" }}>

                    <Button variant="outlined" onClick={() => navigate("/customers")}>{t("common.back")}</Button>
                    
                    <Button variant="outlined" onClick={() => navigate(`/customers/${id}/edit`)}>{t("common.edit")}</Button>

                </div>
            
            </div>
        
            <Paper elevation={2} style={sectionStyle}>

                <Typography variant="h5" gutterBottom>{t("customer.customerInformation")}</Typography>

                <div style={infoGridStyle}>
                    <InfoItem label={t("invoice.id")} value={customer.id} />
                    <InfoItem label={t("customer.fullName")} value={customer.fullName} />
                    <InfoItem label={t("customer.phone")} value={customer.phoneNumber} />
                    <InfoItem label={t("customer.email")} value={customer.email || "-"} />
                    <InfoItem label={t("customer.address")} value={customer.address} />
                    <InfoItem label={t("common.status")} value={customer.active ? t("common.active") : t("common.inactive") } />
                </div>

            </Paper>

            <Box sx={summaryGridStyle}>
                <SummaryCard title={t("customer.totalBookings")} value={details.totalBookings} />
                <SummaryCard title={t("customer.completed")} value={details.completedBookings} />
                <SummaryCard title={t("customer.cancelled")} value={details.cancelledBookings} />
                <SummaryCard title={t("customer.totalSpent")} value={formatCurrency(details.totalSpent)} />
                <SummaryCard title={t("customer.pendingAmount")} value={formatCurrency(details.pendingAmount)} />
                <SummaryCard title={t("customer.lastBooking")} value={details.lastBookingDate || "-"} />
            </Box>

            <Paper elevation={2} style={sectionStyle}>
                
                <Typography variant="h5" gutterBottom>{t("customer.bookingHistory")}</Typography>

                <div style={tableContainerStyle}>

                    <table style={tableStyle}>
                        
                        <thead>
                            <tr>
                                <th style={thStyle}>{t("customer.bookingNumber")}</th>
                                <th style={thStyle}>{t("customer.bookingDate")}</th>
                                <th style={thStyle}>{t("customer.bookingTime")}</th>
                                <th style={thStyle}>{t("common.status")}</th>
                                <th style={thStyle}>{t("customer.price")}</th>
                            </tr>
                        </thead>

                        <tbody>
                            {details.bookings?.length === 0 ? (
                                <tr>
                                    <td colSpan="5" style={emptyStyle}>{t("customer.noBookings")}</td>
                                </tr>
                            ) : (
                                details.bookings?.map((booking) => (
                                    <tr key={booking.id}>
                                        <td style={tdStyle}>{booking.bookingNumber}</td>
                                        <td style={tdStyle}>{booking.bookingDate}</td>
                                        <td style={tdStyle}>{booking.bookingTime}</td>
                                        <td style={tdStyle}>{booking.status}</td>
                                        <td style={tdStyle}>{formatCurrency(booking.estimatedPrice)}</td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    
                    </table>

                </div>
            
            </Paper>

            <Paper elevation={2} style={sectionStyle}>
                
                <Typography variant="h5" gutterBottom>{t("customer.invoiceHistory")}</Typography>

                <div style={tableContainerStyle}>
                    
                    <table style={tableStyle}>
                            
                            <thead>
                                <tr>
                                    <th style={thStyle}>{t("customer.invoiceNumber")}</th>
                                    <th style={thStyle}>{t("customer.invoiceDate")}</th>
                                    <th style={thStyle}>{t("customer.amount")}</th>
                                    <th style={thStyle}>{t("customer.paymentStatus")}</th>
                                </tr>
                            </thead>

                            <tbody>

                                {details.invoices?.length === 0 ? (
                                    <tr>
                                        <td colSpan="4" style={emptyStyle}>
                                            {t("customer.noInvoices")}
                                        </td>
                                    </tr>
                                ) : (
                                    details.invoices?.map((invoice) => (
                                        <tr key={invoice.id}>
                                            <td style={tdStyle}>{invoice.invoiceNumber}</td>
                                            <td style={tdStyle}>{invoice.invoiceDate}</td>
                                            <td style={tdStyle}>{invoice.totalAmount}</td>
                                            <td style={tdStyle}>{invoice.paymentStatus}</td>
                                        </tr>
                                    ))
                                )}

                            </tbody>
                    </table>

                </div>

            </Paper>

        </div>
    );
};

const InfoItem = ({ label, value }) => (

    <div>
        <Typography variant="body2" color="text.secondary">{label}</Typography>
        <Typography variant="body1" sx={{ fontWeight: 500 }}>{value}</Typography>
    </div>
);

const SummaryCard = ({ title, value }) => (

    <Paper elevation={2} style={summaryGridStyle}>
        <Typography sx={summaryTitleStyle}>{title}</Typography>
        <Typography sx={summaryValueStyle}>{value}</Typography>
    </Paper>
);

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

const sectionStyle = {
    padding: "25px",
    marginBottom: "25px",
    borderRadius: "10px"
};

const infoGridStyle = {
    display: "grid",
    gridTemplateColumns: "repeat(3, 1fr)",
    gap: "25px"
};

const summaryGridStyle = {
    display: "grid",
    gridTemplateColumns: {
        xs: "1fr",
        sm: "repeat(2, 1fr)",
        md: "repeat(3, 1fr)"
    },
    gap: "20px",
    marginBottom: "30px"
};

const summaryCardStyle = {
    padding: "22px",
    minHeight: "110px",
    borderRadius: "10px",

    display: "flex",
    flexDirection: "column",
    justifyContent: "center",
    alignItems: "center",

    textAlign: "center"
};

const summaryTitleStyle = {
    color: "#757575",
    fontSize: "15px",
    marginBottom: "8px"
};

const summaryValueStyle = {
    fontSize: "24px",
    fontWeight: 600,
    color: "#222"
};

const tableContainerStyle = {
    overflowX: "auto"
};

const tableStyle = {
    width: "100%",
    borderCollapse: "collapse"
};

const thStyle = {
    padding: "12px",
    textAlign: "left",
    borderBottom: "1px solid #ddd"
};

const tdStyle = {
    padding: "12px",
    borderBottom: "1px solid #ddd"
};

const emptyStyle = {
    textAlign: "center",
    padding: "25px"
};

export default CustomerDetails;