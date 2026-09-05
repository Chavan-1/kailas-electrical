import React, { useEffect, useState } from "react";
import { Alert, Box, Button, Chip, CircularProgress, Divider, MenuItem, Paper, Select, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from "@mui/material";

import { ArrowBack, Download, Payment } from "@mui/icons-material";

import { useNavigate, useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";

import {
  downloadInvoice,
  downloadMyInvoice,
  getInvoiceById,
  getMyInvoiceById,
  updatePaymentStatus
} from "../../services/InvoiceService";

import { getRole } from "../../utils/auth";

const InvoiceDetails = () => {

    const { id } = useParams();
    const navigate = useNavigate();
    const { t } = useTranslation();

    const [invoice, setInvoice] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [paymentStatus, setPaymentStatus] = useState("");
    const [updatingPayment, setUpdatingPayment] = useState(false);

    const [downloading, setDownloading] = useState(false);

    const role = getRole();
    const isAdmin = role === "ADMIN";

    useEffect(() => {

        loadInvoice();

    }, [id]);


    const loadInvoice = async () => {

        try {

            setLoading(true);
            setError("");

            const response = isAdmin ? await getInvoiceById(id) : await getMyInvoiceById(id);

            console.log("Invoice Details API response:", response);

            if (!response.success) {

                setError(response.message || t("invoice.loadFailed"));

                return;
            }

            setInvoice(response.data);
            setPaymentStatus(response.data.paymentStatus);

        } catch (error) {

            console.error("Failed to load invoice:", error);

            setError(error.response?.data?.message || t("invoice.unableToLoad"));

        } finally {

            setLoading(false);

        }
    };


    const handlePaymentStatusChange = async (event) => {

        const newStatus = event.target.value;

        try {

            setUpdatingPayment(true);
            setError("");

            const response = await updatePaymentStatus(
                id,
                newStatus
            );

            console.log("Payment status update response:", response);

            if (!response.success) {

                setError(response.message || t("invoice.failedToUpdatePaymentStatus"));

                return;
            }

            setInvoice(response.data);
            setPaymentStatus(response.data.paymentStatus);

        } catch (error) {

            console.error("Failed to update payment status:", error);

            setError(error.response?.data?.message || t("invoice.unableToUpdatePaymentStatus"));

        } finally {

            setUpdatingPayment(false);

        }
    };


    const handleDownload = async () => {

        try {

            setDownloading(true);
            setError("");

            const blob = isAdmin ? await downloadInvoice(id) : await downloadMyInvoice(id);

            const url = window.URL.createObjectURL(
                new Blob([blob], {
                    type: "application/pdf"
                })
            );

            const link = document.createElement("a");

            link.href = url;
            link.download = `Invoice-${invoice.invoiceNumber}.pdf`;

            document.body.appendChild(link);

            link.click();

            link.remove();

            window.URL.revokeObjectURL(url);

        } catch (error) {

            console.error("Failed to download invoice:", error);

            setError(error.response?.data?.message || t("invoice.unableToDownloadInvoice"));

        } finally {

            setDownloading(false);

        }
    };


    const getPaymentStatusColor = (status) => {

        switch (status) {

            case "PAID":
                return "success";

            case "PARTIALLY_PAID":
                return "warning";

            case "PENDING":
                return "error";

            default:
                return "default";
        }
    };


    const formatPaymentStatus = (status) => {

        if (!status) return "";

        return status
            .replaceAll("_", " ")
            .toLowerCase()
            .replace(/\b\w/g, char =>
                char.toUpperCase()
            );
    };


    const formatCurrency = (amount) => {

        return new Intl.NumberFormat(
            "en-IN",
            {
                style: "currency",
                currency: "INR"
            }
        ).format(amount || 0);

    };


    const formatDate = (date) => {

        if (!date) {
            return "-";
        }

        return new Date(date).toLocaleDateString(
            "en-IN"
        );
    };


    if (loading) {

        return (

            <Box
                sx={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    minHeight: "60vh"
                }}
            >

                <CircularProgress />

            </Box>

        );
    }


    if (error && !invoice) {

        return (

            <Box sx={{ p: 3 }}>

                <Alert severity="error">{error}</Alert>

                <Button
                    sx={{ mt: 2 }}
                    variant="outlined"
                    startIcon={<ArrowBack />}
                    onClick={() => navigate(isAdmin ? "/invoices" : "/my-invoices")}
                >
                    {t("common.back")}
                </Button>

            </Box>

        );
    }


    if (!invoice) return null;


    return (

        <Box sx={{ p: 3 }}>

            <Box
                sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    mb: 3,
                    flexWrap: "wrap",
                    gap: 2
                }}
            >

                <Box>

                    <Typography variant="h4" fontWeight={600}>{t("invoice.details")}</Typography>

                    <Typography color="text.secondary" sx={{ mt: 0.5 }}>{invoice.invoiceNumber}</Typography>

                </Box>


                <Box sx={{display: "flex", gap: 1}}>

                    <Button variant="outlined" startIcon={<ArrowBack />} onClick={() => navigate(isAdmin ? "/invoices" : "/my-invoices")}>
                        {t("common.back")}
                    </Button>


                    <Button
                        variant="contained"
                        startIcon={<Download />}
                        onClick={handleDownload}
                        disabled={downloading}
                    >
                        {downloading
                            ? t("invoice.downloading")
                            : t("invoice.download")}
                    </Button>

                </Box>

            </Box>


            {error && (

                <Alert severity="error" sx={{ mb: 2 }}>
                    {error}
                </Alert>

            )}

            <Paper sx={{ p: 3, mb: 3 }}>

                <Typography variant="h5" textalign="center" sx={{ mb: 3 }}>{t("invoice.information")}</Typography>


                <Box
                    sx={{
                        display: "grid",
                        gridTemplateColumns:
                            "repeat(auto-fit, minmax(180px, 1fr))",
                        gap: 3
                    }}
                >

                    <Box>

                        <Typography variant="body2" color="text.secondary">{t("customer.invoiceNumber")}</Typography>

                        <Typography fontWeight={500}>{invoice.invoiceNumber}</Typography>

                    </Box>

                    <Box>

                        <Typography variant="body2" color="text.secondary">{t("customer.invoiceDate")}</Typography>

                        <Typography fontWeight={500}>{formatDate(invoice.invoiceDate)}</Typography>

                    </Box>

                    <Box>

                        <Typography variant="body2" color="text.secondary">{t("customer.bookingNumber")}</Typography>

                        <Typography fontWeight={500}>{invoice.bookingNumber || "-"}</Typography>

                    </Box>


                    <Box>

                        <Typography variant="body2" color="text.secondary">{t("invoice.paymentStatus")}</Typography>

                        <Chip
                            label={formatPaymentStatus(invoice.paymentStatus)}
                            color={getPaymentStatusColor(invoice.paymentStatus)}
                            size="small"
                            sx={{ mt: 0.5 }}
                        />

                    </Box>

                </Box>

            </Paper>

            <Paper sx={{ p: 3, mb: 3 }}>

                <Typography variant="h5" textalign="center" sx={{ mb: 3 }}>{t("customer.customerInformation")}</Typography>

                <Box
                    sx={{
                        display: "grid",
                        gridTemplateColumns:
                            "repeat(auto-fit, minmax(200px, 1fr))",
                        gap: 3
                    }}
                >

                    <Box>

                        <Typography variant="body2" color="text.secondary">{t("customer.customer")}</Typography>

                        <Typography fontWeight={500}>{invoice.customerName || "-"}</Typography>

                    </Box>

                    <Box>

                        <Typography variant="body2" color="text.secondary">{t("customer.phoneNumber")}</Typography>

                        <Typography fontWeight={500}>{invoice.phoneNumber || "-"}</Typography>

                    </Box>

                    <Box>

                        <Typography variant="body2" color="text.secondary">{t("customer.email")}</Typography>

                        <Typography fontWeight={500}>{invoice.email || "-"}</Typography>

                    </Box>

                </Box>

            </Paper>

            <Paper sx={{ mb: 3 }}>

                <Typography variant="h5" textalign="center" sx={{ py: 2 }}>{t("service.services")}</Typography>

                <Divider />

                <TableContainer>

                    <Table>

                        <TableHead>

                            <TableRow>

                                <TableCell>{t("service.service")}</TableCell>
                                <TableCell align="center">{t("invoice.quantity")}</TableCell>
                                <TableCell align="right">{t("invoice.unitPrice")}</TableCell>
                                <TableCell align="right">{t("common.total")}</TableCell>

                            </TableRow>

                        </TableHead>

                        <TableBody>

                            {invoice.items?.length > 0 ? (

                                invoice.items.map((item, index) => (

                                        <TableRow key={`${item.serviceName}-${index}`}>

                                            <TableCell>{item.serviceName || "-"}</TableCell>

                                            <TableCell align="center">{item.quantity || 1}</TableCell>

                                            <TableCell align="right">{formatCurrency(item.unitPrice)}</TableCell>

                                            <TableCell align="right">{formatCurrency(item.lineTotal)}</TableCell>

                                        </TableRow>

                                    )
                                )

                            ) : (

                                <TableRow>

                                    <TableCell colSpan={4} align="center">{t("invoice.noItemsFound")}</TableCell>

                                </TableRow>

                            )}

                        </TableBody>

                    </Table>

                </TableContainer>

                <Box
                    sx={{ display: "flex", justifyContent: "flex-end", p: 3}}>

                    <Box sx={{ minWidth: 200 }}>

                        <Box sx={{ display: "flex", justifyContent: "space-between"}}>

                            <Typography variant="h6" fontWeight={600}>Total</Typography>

                            <Typography variant="h6" fontWeight={500}> {formatCurrency(invoice.subtotal)}</Typography>

                        </Box>

                        {/* <Divider sx={{ my: 1 }} />

                        <Box sx={{ display: "flex", justifyContent: "space-between" }}>

                            <Typography variant="h6" fontWeight={600}>Total</Typography>

                            <Typography variant="h6" fontWeight={600}>{formatCurrency(invoice.totalAmount)}</Typography>

                        </Box> */}

                    </Box>

                </Box>

            </Paper>

            <Paper sx={{ p: 3, mb: 3 }}>

                <Box
                    sx={{
                        display: "flex",
                        alignItems: "center",
                        gap: 2,
                        flexWrap: "wrap"
                    }}
                >

                    <Payment />

                    <Typography variant="h6" fontWeight={600}>{t("invoice.paymentStatus")}</Typography>

                    
                    {isAdmin ? (

                        <Select
                            size="small"
                            value={paymentStatus}
                            onChange={handlePaymentStatusChange}
                            disabled={updatingPayment}
                        >

                            <MenuItem value="PENDING">{t("invoice.pending")}</MenuItem>
                            <MenuItem value="PARTIALLY_PAID">{t("invoice.partiallyPaid")}</MenuItem>
                            <MenuItem value="PAID">{t("invoice.paid")}</MenuItem>

                        </Select>

                    ) : (
                         <Chip
                            label={formatPaymentStatus(invoice.paymentStatus)}
                            color={getPaymentStatusColor(invoice.paymentStatus)}
                            size="small"
                        />
                    )}

                    {updatingPayment && (
                        <CircularProgress size={22} />
                    )}

                </Box>

            </Paper>

            {invoice.notes && (

                <Paper sx={{ p: 3 }}>

                    <Typography variant="h6" fontWeight={600} sx={{ mb: 1 }}>{t("invoice.notes")}</Typography>

                    <Typography color="text.secondary">{invoice.notes}</Typography>

                </Paper>

            )}

        </Box>
    );
};


export default InvoiceDetails;