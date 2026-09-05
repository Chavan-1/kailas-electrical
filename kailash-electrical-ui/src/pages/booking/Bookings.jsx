import { useEffect, useState } from "react"
import { cancelBooking, cancelMyBooking, getBookings, getMyBookings, updateBookingStatus } from "../../services/BookingService";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { getProfile } from "../../services/ProfileService";
import { Add, Cancel, Edit, Search, Visibility } from "@mui/icons-material";
import { Alert, Button, FormControl, InputLabel, MenuItem, Paper, Select, Table, TableBody, TableCell, TableContainer, TableHead, TablePagination, TableRow, TextField, Typography } from "@mui/material";

const Bookings = () => {

    const navigate = useNavigate();
    const { t } = useTranslation();

    const [bookings, setBookings] = useState([]);

    const [search, setSearch] = useState("");
    const [status, setStatus] = useState("ALL");
    const [bookingDate, setBookingDate] = useState("");

    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(10);

    const [totalElements, setTotalElements] = useState(0);

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    const [role, setRole] = useState("");

    const loadProfile = async () => {

        try {

            const response = await getProfile();

            if (response.success) {

                setRole(response.data.role);
            }

        } catch (error) {

            console.error("Failed to fetch profile:", error);

            setError(error.response?.data?.message || t("profile.loadProfileFailed"));
        }
    };

    const loadBookings = async () => {

        try {

            setLoading(true);
            setError("");

            if (role === "CUSTOMER") {

                const response = await getMyBookings();

                console.log("My Bookings API response: ", response);

                if (response.success) {
                    
                    setBookings(response.data || []);
                    setTotalElements((response.data || []).length);

                } else {

                    setError(response.message || t("booking.failedToFetchBooking"));
                }

                return;
            }

            if (role === "ADMIN") {

                const params = {
                    page,
                    size: rowsPerPage,
                    sortBy: "bookingDate",
                    direction: "desc"
                };

                if (search.trim()) {
                    params.keyword = search.trim();
                }

                if (status !== "ALL") {
                    params.status = status;
                }

                if (bookingDate) {
                    params.bookingDate = bookingDate;
                }

                console.log("Booking API params:", params);

                const response = await getBookings(params);

                console.log("Bookings API response:", response);

                if (response.success) {

                    const data = response.data;

                    setBookings(data.content || []);
                    setTotalElements(data.totalElements || 0);

                } else {

                    setError(response.message || t("booking.failedToFetchBooking"));
                }
            }
            
        } catch (error) {

            console.error("Failed to fetch bookings: ", error);

            setError(error.response?.data?.message || t("booking.failedToFetchBooking"));

        } finally {

            setLoading(false);

        }
    };

    useEffect(() => {

        loadProfile();

    }, []);

    useEffect(() => {

        if (role) {

            loadBookings();
        }

    }, [role, page, rowsPerPage]);

    const handleSearch = () => {

        setPage(0);

        loadBookings();
    };

    const handleStatusChange = (event) => {

        setStatus(event.target.value);
        setPage(0);
    };

    const handleDateChange = (event) => {

        setBookingDate(event.target.value);
        setPage(0);
    };

    const handleChangePage = (event, newPage) => {

        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {

        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    const handleView = (id) => {

        navigate(`/bookings/${id}`);
    };

    const handleEdit = (id) => {

        navigate(`/bookings/${id}/edit`);
    };

    const handleCancel = async (id) => {

        const confirmed = window.confirm(t("booking.cancelConfirmation"));

        if (!confirmed) return;

        try {

            setError("");
            setSuccess("");

            const response = await cancelBooking(id);

            console.log("Cancel booking response:", response);

            if (response.success) {

                setSuccess(response.message || t("booking.cancelSuccess"))
                loadBookings();

            } else {

                setError(response.message || t("booking.cancelFailed"));

            } 

        } catch (error) {
                
            console.error("Cancel booking error:", error);
            setError(error.response?.data?.message || t("booking.cancelFailed"))
        }
    };

    const handleMyCancel = async (id) => {

        const confirmed = window.confirm(t("booking.cancelConfirmation"));

        if (!confirmed) return;

        try {

            setError("");
            setSuccess("");

            const response = await cancelMyBooking(id);

            console.log("Customer cancel booking response:", response);

            if (response.success) {

                setSuccess(response.message || t("booking.cancelSuccess"));

                loadBookings();

            } else {

                setError(response.message || t("booking.cancelFailed"));
            }

        } catch (error) {

            console.error("Customer cancel booking error:", error);

            setError(error.response?.data?.message || t("booking.cancelFailed"));
        }
    };

    const handleStatusUpdate = async (id, newStatus) => {

        try {

            setError("");
            setSuccess("");

            const response = await updateBookingStatus(id, newStatus);

            if (response.success) {

                setSuccess(response.message || t("booking.statusUpdateSuccess"))
                loadBookings();

            } else {

                setError(response.message || t("booking.statusUpdateFailed"));

            } 

        } catch (error) {

            console.error("Update booking status error:", error);
            setError(error.response?.data?.message || t("booking.statusUpdateFailed"))
        }
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


    if (role === "CUSTOMER") {

        return (
        
            <div style={pageStyle}>

                <div style={headerStyle}>

                    <Typography variant="h4" fontWeight={600}>
                        {t("booking.myBookings")}
                    </Typography>
                    
                    <Button variant="contained" startIcon={<Add />} onClick={() => navigate("/bookings/create")}>
                        {t("booking.createBooking")}
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

                <Paper elevation={2} sx={{ p: 3, mb: 3, borderRadius: 2 }}>
                    
                    <div style={filterStyle}>

                        <TextField 
                            label={t("booking.searchBooking")}
                            placeholder={t("booking.searchPlaceholder")}
                            value={search}
                            onChange={(event) => setSearch(event.target.value)}
                            fullWidth
                        />

                        <FormControl sx={{ minWidth: 180 }}>

                            <InputLabel>{t("common.status")}</InputLabel>

                            <Select value={status} 
                                    label={t("common.status")}
                                    onChange={handleStatusChange}>

                                <MenuItem value="ALL">{t("common.all")}</MenuItem>
                                <MenuItem value="PENDING">{t("booking.pending")}</MenuItem>
                                <MenuItem value="CONFIRMED">{t("booking.confirmed")}</MenuItem>
                                <MenuItem value="IN_PROGRESS">{t("booking.inProgress")}</MenuItem>
                                <MenuItem value="COMPLETED">{t("booking.completed")}</MenuItem>
                                <MenuItem value="CANCELLED">{t("booking.cancelled")}</MenuItem>

                            </Select>
                            
                        </FormControl>

                        <TextField 
                            label={t("booking.bookingDate")}
                            type="date"
                            value={bookingDate}
                            onChange={handleDateChange}
                            fullWidth
                            slotProps={{inputLabel: {shrink: true,},}}
                        />

                        <Button variant="contained" startIcon={<Search />} onClick={handleSearch} sx={{ height: 56 }}>
                            {t("common.search")}
                        </Button>

                    </div>

                </Paper>

                <Paper elevation={2} sx={{ borderRadius: 2, overflow: "hidden" }}>

                    <TableContainer>

                        <Table>

                            <TableHead>

                                <TableRow>

                                    <TableCell>{t("booking.booking")}</TableCell>
                                    <TableCell>{t("booking.bookingDate")}</TableCell>
                                    <TableCell>{t("booking.bookingTime")}</TableCell>
                                    <TableCell>{t("common.services")}</TableCell>
                                    <TableCell>{t("booking.price")}</TableCell>
                                    <TableCell>{t("booking.status")}</TableCell>
                                    <TableCell>{t("common.actions")}</TableCell>

                                </TableRow>

                            </TableHead>

                            <TableBody>

                                {loading ? (

                                    <TableRow>

                                        <TableCell colSpan={7} align="center">
                                            {t("booking.loadingBookings")}
                                        </TableCell>

                                    </TableRow>

                                ) : bookings.length === 0 ? (

                                    <TableRow>

                                        <TableCell colSpan={7} align="center">
                                                {t("booking.noBookings")}
                                        </TableCell>

                                    </TableRow>

                                ) : (
                                    
                                    bookings.map((booking) => (

                                    <TableRow key={booking.id || booking.bookingId} hover>

                                        <TableCell>{booking.bookingNumber}</TableCell>
                                        <TableCell>{formatDate(booking.bookingDate)}</TableCell>
                                        <TableCell>{formatTime(booking.bookingTime)}</TableCell>
                                        <TableCell>{booking.services?.map(service => service.serviceName).join(", ") || "-"}</TableCell>
                                        <TableCell>₹{Number(booking.totalAmount || booking.estimatedPrice || 0).toLocaleString("en-IN")}</TableCell>                                     
                                        <TableCell>
                                            <span style={{...statusStyle, backgroundColor: getStatusColor(booking.status)}}>{booking.status}</span>
                                        </TableCell>
                                        <TableCell>
                                            
                                            <div style={{ display: "flex", gap: "8px" }}>
                                                
                                                <Button
                                                    size="small"
                                                    variant="contained"
                                                    startIcon={<Visibility />}
                                                    onClick={() => handleView(booking.bookingId || booking.id)}
                                                >
                                                    {t("common.view")}
                                                </Button>

                                                {booking.status !== "CANCELLED" && booking.status !== "COMPLETED" && (
                                                    
                                                    <Button
                                                        size="small"
                                                        variant="contained"
                                                        color="error"
                                                        startIcon={<Cancel />}
                                                        onClick={() => handleMyCancel(booking.bookingId || booking.id)
                                                        }
                                                    >
                                                        {t("common.cancel")}
                                                    </Button>
                                                    
                                                )}
                                            
                                            </div>
                                        
                                        </TableCell>

                                    </TableRow>

                                )))}

                            </TableBody>

                        </Table>

                    </TableContainer>

                </Paper>

            </div>
        );
    };

    return (

        <div style={pageStyle}>

            <div style={headerStyle}>

                <Typography variant="h4" fontWeight={600}>
                    {t("booking.bookings")}
                </Typography>

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

            <Paper elevation={2} sx={{ p: 2, mb: 3, borderRadius: 2 }}>
                
                <div style={filterStyle}>

                    <TextField 
                        label={t("booking.searchBooking")}
                        placeholder={t("booking.searchPlaceholder")}
                        value={search}
                        onChange={(event) => setSearch(event.target.value)}
                        fullWidth
                    />

                    <FormControl sx={{ minWidth: 180 }}>

                        <InputLabel>{t("common.status")}</InputLabel>

                        <Select value={status} 
                                label={t("common.status")}
                                onChange={handleStatusChange}>

                            <MenuItem value="ALL">{t("common.all")}</MenuItem>
                            <MenuItem value="PENDING">{t("booking.pending")}</MenuItem>
                            <MenuItem value="CONFIRMED">{t("booking.confirmed")}</MenuItem>
                            <MenuItem value="IN_PROGRESS">{t("booking.inProgress")}</MenuItem>
                            <MenuItem value="COMPLETED">{t("booking.completed")}</MenuItem>
                            <MenuItem value="CANCELLED">{t("booking.cancelled")}</MenuItem>

                        </Select>
                        
                    </FormControl>

                    <TextField 
                        label={t("booking.bookingDate")}
                        type="date"
                        value={bookingDate}
                        onChange={handleDateChange}
                        slotProps={{inputLabel: {shrink: true,},}}
                    />

                    <Button variant="contained" startIcon={<Search />} onClick={handleSearch}>
                        {t("common.search")}
                    </Button>

                </div>

            </Paper>

            <Paper elevation={2} sx={{ borderRadius: 2, overflow: "hidden" }}>

                <TableContainer>

                    <Table>

                        <TableHead>

                            <TableRow>

                                <TableCell>{t("booking.booking")}#</TableCell>
                                <TableCell>{t("booking.customer")}</TableCell>
                                <TableCell>{t("customer.phone")}</TableCell>
                                <TableCell>{t("invoice.date")}</TableCell>
                                <TableCell>{t("invoice.time")}</TableCell>
                                <TableCell>{t("common.services")}</TableCell>
                                <TableCell>{t("customer.price")}</TableCell>
                                <TableCell>{t("booking.status")}</TableCell>
                                <TableCell>{t("common.actions")}</TableCell>

                            </TableRow>

                        </TableHead>

                        <TableBody>

                            {loading ? (

                                <TableRow>

                                    <TableCell colSpan={9} align="center">
                                        {t("booking.loadingBookings")}
                                    </TableCell>

                                </TableRow>

                            ) : bookings.length === 0 ? (

                                <TableRow>

                                    <TableCell colSpan={9} align="center">
                                            {t("booking.noBookings")}
                                    </TableCell>

                                </TableRow>

                            ) : (
                                
                                bookings.map((booking) => (

                                <TableRow key={booking.id} hover>

                                    <TableCell>{booking.bookingNumber}</TableCell>
                                    
                                    <TableCell>{booking.customerName}</TableCell>
                                    
                                    <TableCell>{booking.phoneNumber}</TableCell>
                                    
                                    <TableCell>{formatDate(booking.bookingDate)}</TableCell>
                                    
                                    <TableCell>{formatTime(booking.bookingTime)}</TableCell>
                                    
                                    <TableCell>{booking.services?.map(service => service.serviceName).join(", ") || "-"}</TableCell>
                                    
                                    <TableCell>₹{Number( booking.estimatedPrice || 0).toLocaleString("en-IN")}</TableCell>                                     
                                    
                                    <TableCell>
                                        <span style={{...statusStyle, backgroundColor: getStatusColor(booking.status)}}>{booking.status}</span>
                                    </TableCell>
                                    
                                    <TableCell>
                                    
                                        <div style={{ display: "flex", gap: "8px" }}>
                                    
                                            <Button
                                                size="small"
                                                variant="contained"
                                                startIcon={<Visibility />}
                                                onClick={() => handleView(booking.id)}
                                            >
                                                {t("common.view")}
                                            </Button>

                                            {booking.status !== "CANCELLED" && booking.status !== "COMPLETED" && (
                                                <Button
                                                    size="small"
                                                    variant="contained"
                                                    startIcon={<Edit />}
                                                    color="primary"
                                                    onClick={() => handleEdit(booking.id)}
                                                >
                                                    {t("common.edit")}
                                                </Button> 
                                            )}

                                            {booking.status !== "CANCELLED" && booking.status !== "COMPLETED" && (

                                                <Button
                                                    size="small"
                                                    variant="contained"
                                                    color="error"
                                                    startIcon={<Cancel />}
                                                    onClick={() => handleCancel(booking.id)}
                                                >
                                                    {t("common.cancel")}
                                                </Button>

                                            )}
                            
                                        </div>
                                    
                                    </TableCell>

                                </TableRow>

                            )))}

                        </TableBody>

                    </Table>

                </TableContainer>

                    <TablePagination 
                        component="div"
                        count={totalElements}
                        page={page}
                        onPageChange={handleChangePage}
                        rowsPerPage={rowsPerPage}
                        onRowsPerPageChange={handleChangeRowsPerPage}
                        rowsPerPageOptions={[5, 10, 20, 50]}
                />

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

const filterStyle = {
    display: "grid",
    gridTemplateColumns: "1fr 180px 180px auto",
    gap: "15px",
    alignItems: "center"
};

const statusStyle = {
    display: "inline-block",
    color: "#fff",
    padding: "5px 12px",
    borderRadius: "15px",
    fontSize: "12px",
    fontWeight: "600"
};



export default Bookings;