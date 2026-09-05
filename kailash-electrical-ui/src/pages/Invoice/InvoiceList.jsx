import React, { useEffect, useState } from "react";
import { getInvoices, getMyInvoices } from "../../services/InvoiceService";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { getRole } from "../../utils/auth";

import { Alert, Box, Button, Chip, FormControl, InputLabel, MenuItem, Paper, Select, Table, TableBody, TableCell, TableContainer, TableHead, TablePagination, TableRow, TextField, Typography } from "@mui/material";

import { Search, Visibility } from "@mui/icons-material";


const InvoiceList = () => {

    const navigate = useNavigate();
    const { t } = useTranslation();

    const role = getRole();
    const isAdmin = role === "ADMIN";

    const [invoices, setInvoices] = useState([]);
    const [keyword, setKeyword] = useState("");
    const [paymentStatus, setPaymentStatus] = useState("");

    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const [totalElements, setTotalElements] = useState(0);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");


    useEffect(() => {

        loadInvoices();

    }, [page, rowsPerPage, paymentStatus]);


    const loadInvoices = async () => {

        try {

            setLoading(true);
            setError("");

            if (isAdmin) {

                const response = await getInvoices({
                    keyword,
                    paymentStatus,
                    page,
                    size: rowsPerPage,
                    sortBy: "invoiceDate",
                    direction: "desc"
                });

                console.log("Admin invoices API response:", response);

                if (!response.success) {

                    setError(response.message || t("invoice.loadFailed"));
                    return;
                }

                const data = response.data;

                setInvoices(data.content || []);
                setTotalElements(data.totalElements || 0);

            } else {

                const response = await getMyInvoices();

                console.log("Customer invoices API response:", response);

                if (!response.success) {

                    setError(response.message || t("invoice.loadFailed"));
                    return;
                }

                const data = response.data || [];

                setInvoices(data);
                setTotalElements(data.length);

            }

        } catch (error) {

            console.error("Failed to load invoices:", error);

            setError(error.response?.data?.message || t("invoice.loadFailed"));

        } finally {

            setLoading(false);

        }
    };


    const handleSearch = () => {

        setPage(0);
        loadInvoices();

    };


    const handleClearSearch = () => {

        setKeyword("");
        setPaymentStatus("");
        setPage(0);

        setTimeout(() => {
            loadInvoices();
        }, 0);

    };


    const handleChangePage = (event, newPage) => {

        setPage(newPage);

    };


    const handleChangeRowsPerPage = (event) => {

        setRowsPerPage(parseInt(event.target.value, 10));

        setPage(0);

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
            .replace("_", " ")
            .toLowerCase()
            .replace(/\bw/g, char => char.toUpperCase());

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

        if (!date) return "-";

        return new Date(date).toLocaleDateString("en-IN");

    };


    const handleViewInvoice = (invoiceId) => {

        if (isAdmin) {

            navigate(`/invoices/${invoiceId}`);

        } else {

            navigate(`/my-invoices/${invoiceId}`);

        }

    };


    return (

        <Box sx={{ p: 3 }}>

            <Box
                sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    mb: 3
                }}
            >

                <Box>

                    <Typography variant="h4" fontWeight="600">
                        {t("invoice.invoices")}
                    </Typography>

                    <Typography variant="body2" color="text.secondary">
                        {isAdmin
                            ? t("invoice.manageInvoicesPayments")
                            : t("invoice.information")
                        }
                    </Typography>

                </Box>

            </Box>

            {isAdmin && (

                <Paper sx={{ p: 2, mb: 3 }}>

                    <Box
                        sx={{
                            display: "flex",
                            gap: 2,
                            flexWrap: "wrap",
                            alignItems: "center"
                        }}
                    >

                        <TextField
                            label={t("common.search")}
                            placeholder={t("invoice.invoiceBookingCustomer")}
                            value={keyword}
                            onChange={(event) => setKeyword(event.target.value)}
                            size="small"
                            sx={{ minWidth: 280 }}
                        />


                        <FormControl size="small" sx={{ minWidth: 180 }}>

                            <InputLabel>
                                {t("invoice.paymentStatus")}
                            </InputLabel>

                            <Select
                                value={paymentStatus}
                                label={t("invoice.paymentStatus")}
                                onChange={(event) => setPaymentStatus(event.target.value)}
                            >

                                <MenuItem value="">{t("common.all")}</MenuItem>
                                <MenuItem value="PENDING">{t("invoice.pending")}</MenuItem>
                                <MenuItem value="PARTIALLY_PAID">{t("invoice.partiallyPaid")}</MenuItem>
                                <MenuItem value="PAID">{t("invoice.paid")}</MenuItem>

                            </Select>

                        </FormControl>

                        <Button variant="contained" startIcon={<Search />} onClick={handleSearch}>
                            {t("common.search")}
                        </Button>

                        <Button variant="outlined" onClick={handleClearSearch}>
                            {t("invoice.clear")}
                        </Button>

                    </Box>

                </Paper>

            )}

            {error && (

                <Alert severity="error" sx={{ mb: 2 }}>
                    {error}
                </Alert>

            )}

            <Paper>

                <TableContainer>

                    <Table>

                        <TableHead>

                            <TableRow>

                                <TableCell>{t("invoice.invoiceNumber")}</TableCell>

                                <TableCell>{t("booking.bookingNumber")}</TableCell>

                                {isAdmin && (

                                    <TableCell>{t("customer.customer")}</TableCell>

                                )}


                                <TableCell>{t("invoice.invoiceDate")}</TableCell>
                                <TableCell align="right">{t("customer.amount")}</TableCell>
                                <TableCell>{t("invoice.paymentStatus")}</TableCell>
                                <TableCell align="center">{t("common.action")}</TableCell>

                            </TableRow>

                        </TableHead>

                        <TableBody>

                            {loading ? (

                                <TableRow>

                                    <TableCell colSpan={isAdmin ? 7 : 6} align="center">
                                        {t("invoice.loadingInvoices")}
                                    </TableCell>

                                </TableRow>

                            ) : invoices.length === 0 ? (

                                <TableRow>

                                    <TableCell colSpan={isAdmin ? 7 : 6} align="center">
                                        {t("invoice.noInvoices")}
                                    </TableCell>

                                </TableRow>

                            ) : (

                                invoices.map((invoice) => (

                                    <TableRow key={invoice.id} hover>

                                        <TableCell>{invoice.invoiceNumber}</TableCell>

                                        <TableCell>{invoice.bookingNumber}</TableCell>

                                        {isAdmin && (

                                            <TableCell>{invoice.customerName} </TableCell>

                                        )}


                                        <TableCell>{formatDate(invoice.invoiceDate)}</TableCell>


                                        <TableCell align="right">{formatCurrency(invoice.subtotal)}</TableCell>


                                        <TableCell>

                                            <Chip
                                                label={formatPaymentStatus(invoice.paymentStatus)}
                                                color={getPaymentStatusColor(invoice.paymentStatus)}
                                                size="small"
                                            />

                                        </TableCell>

                                        <TableCell align="center">

                                            <Button
                                                size="small"
                                                variant="outlined"
                                                startIcon={<Visibility />}
                                                onClick={() => handleViewInvoice(invoice.id)}
                                            >
                                                {t("common.view")}
                                            </Button>

                                        </TableCell>

                                    </TableRow>

                                ))

                            )}

                        </TableBody>

                    </Table>

                </TableContainer>

                {isAdmin && (

                    <TablePagination
                        component="div"
                        count={totalElements}
                        page={page}
                        onPageChange={handleChangePage}
                        rowsPerPage={rowsPerPage}
                        onRowsPerPageChange={ handleChangeRowsPerPage}
                        rowsPerPageOptions={[ 5, 10, 25, 50]}
                    />

                )}

            </Paper>

        </Box>

    );

};


export default InvoiceList;