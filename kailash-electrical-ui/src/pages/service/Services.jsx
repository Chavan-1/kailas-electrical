import { useEffect, useState } from "react";
import { deleteService, getServices } from "../../services/ServiceService";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { Alert, Button, Chip, CircularProgress, FormControl, InputLabel, MenuItem, Paper, Select, Table, TableBody, TableCell, TableContainer, TableHead, TablePagination, TableRow, TextField, Typography } from "@mui/material";
import { Add, Delete, DeleteOutlineOutlined, Edit, Search, Visibility } from "@mui/icons-material";
import { getRole } from "../../utils/auth";

function Services() {

    const navigate = useNavigate();
    const { t } = useTranslation();

    const [services, setServices] = useState([]);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [search, setSearch] = useState("");
    const [status, setStatus] = useState("ALL");

    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(10);

    const role = getRole();
    const isAdmin = role === "ADMIN";

    const loadServices = async () => {

        try {

            setLoading(true);
            setError("");

            const params = {
                page,
                size: rowsPerPage
            };

            if (search.trim()) { 
                params.keyword = search.trim();
            }

            if (status !== "ALL") {
                params.active = status === "ACTIVE";
            }

            const response = await getServices(params);

            console.log("Services API response: ", response);

            if (response.success) {
                
                setServices(response.data?.content || []);

            } else {

                setError(response.message || t("service.fetchError"));
            }

        } catch (error){

            console.error("Failed to fetch services: ", error);

            setError(error.response?.data?.message || t("service.fetchError"));

        } finally {

            setLoading(false);
        }
    };
    
    useEffect(() => {

        loadServices();

    }, [page, rowsPerPage]);

    const handleSearch = () => {

        setPage(0);
        loadServices();
    };

    const handleStatusChange = (event) => {

        setStatus(event.target.value);
        setPage(0);
    };

    useEffect(() => {

        if (page === 0) loadServices();

    }, [status]);

    const handleDelete = async (id) => {

        if (!isAdmin) return;

        const confirmed = window.confirm(t("service.deleteConfirmation"));

        if (!confirmed) return;

        try {

            const response = await deleteService(id);

            if (response.success) {

                Alert(t("service.deleteSuccess"));

                loadServices();

            } else {

                setError(response.message || t("service.deleteError"));
            }
        } catch (error) {

            console.log(error);
            setError(error.response?.data?.message || t("service.deleteError"));
        }
    };

    const handleChangePage = (event, newPage) => {

        setPage(newPage);
    }; 

    const handleChangeRowsPerPage = (event) => {

        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    return (

        <div style={pageStyle}>

            <div style={headerStyle}>

                <Typography variant="h4" fontWeight={600}>{t("service.services")}</Typography>

                {isAdmin && (
                    <Button variant="contained" startIcon={<Add />} onClick={() => navigate("/services/create")}>
                        {t("service.addService")}
                    </Button>
                )}

            </div>

            
            {error && (
                <Alert severity="error" sx={{ mb: 3 }} onClose={() => setError("")}>
                    {error}
                </Alert>
            )}

            <Paper elevation={2} sx={{ p: 3, mb: 3, borderRadius: 3 }}>

                <div
                    style={{...filterContainerStyle,
                        gridTemplateColumns: isAdmin ? "1fr 180px auto" : "1fr auto"
                    }}>

                    <TextField 
                        label={t("service.search")}
                        placeholder={t("service.searchPlaceholder")}
                        value={search}
                        onChange={(event) => setSearch(event.target.value)}
                        fullWidth
                    />

                    {isAdmin && (

                        <FormControl sx={{ minWidth: 180 }}>

                            <InputLabel>{t("common.status")}</InputLabel>

                            <Select value={status} label={t("common.status")} onChange={handleStatusChange}>
                                <MenuItem value="ALL">{t("common.all")}</MenuItem>
                                <MenuItem value="ACTIVE">{t("common.active")}</MenuItem>
                                <MenuItem value="INACTIVE">{t("common.inactive")}</MenuItem>
                            </Select>

                        </FormControl>

                    )}

                    <Button variant="contained" 
                            startIcon={<Search />} 
                            onClick={handleSearch} 
                            sx={{ height: 56 }}>

                        {t("common.search")}
                    </Button>

                </div>

            </Paper>

            <Paper elevation={2} sx={{ borderRadius: 3, overflow: "hidden" }}>

                
            <TableContainer>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>{t("service.id")}</TableCell>
                            <TableCell>{t("service.serviceName")}</TableCell>
                            <TableCell>{t("service.description")}</TableCell>
                            <TableCell>{t("service.basePrice")}</TableCell>
                            <TableCell>{t("service.duration")}</TableCell>
                            <TableCell>{t("common.status")}</TableCell>
                            <TableCell align="center">{t("common.actions")}</TableCell>
                        </TableRow>
                    </TableHead>

                    <TableBody>
                        {loading ? (

                    <TableRow>
                        <TableCell colSpan={7} align="center" sx={{ py: 6 }}>
                            <CircularProgress />
                            <Typography sx={{ mt: 2 }} color="text.secondary">
                                {t("service.loadingServices")}
                            </Typography> 
                        </TableCell>
                    </TableRow>

                ) : services.length === 0 ? (

                    <TableRow>
                        <TableCell colSpan={7} align="center" sx={{ py: 6 }}>
                            <Typography color="text.secondary">
                                {t("service.noServices")}
                            </Typography>
                        </TableCell>
                    </TableRow>
                ) : (

                    services.map((service) => (
                        <TableRow key={service.id}>
                            <TableCell>{service.id}</TableCell>
                            
                            <TableCell>
                                <Typography fontWeight={600}>{service.serviceName}</Typography>
                            </TableCell>
                            
                            <TableCell>
                                <Typography color="text.secondary" sx={{ maxWidth: 300 }}>{service.description || "-"}</Typography>
                            </TableCell>
                            
                            <TableCell>
                                ₹{Number(service.basePrice).toLocaleString("en-IN")}
                            </TableCell>
                            
                            <TableCell>
                                {service.estimatedDuration}{" "}{t("service.minutes")}
                            </TableCell>
                            
                            <TableCell>
                                <Chip label={
                                            service.active ? t("common.active") : t("common.inactive")
                                            }
                                        color={
                                                service.active ? "success" : "default"
                                            }
                                        size="small"
                                />
                            </TableCell>

                            <TableCell>

                                <div style={actionStyle}>

                                    <Button size="small"
                                            startIcon={<Visibility />}
                                            variant="contained"
                                            onClick={() => navigate(`/services/${service.id}`)}>

                                        {t("common.view")}
                                    </Button>

                                    {isAdmin && (

                                        <Button size="small"
                                                variant="contained"
                                                color="primary"
                                                startIcon={<Edit />}
                                                onClick={() => navigate(`/services/${service.id}/edit`)}>

                                            {t("common.edit")}
                                        </Button>
                                    )}

                                    {isAdmin && (

                                        <Button size="small"
                                                color="error"
                                                variant="contained"
                                                startIcon={<Delete />}
                                                onClick={() => handleDelete(service.id)}>

                                            {t("common.delete")}
                                        </Button>

                                    )}

                                </div>

                            </TableCell>

                        </TableRow>

                        ))
                    )}

                    </TableBody>

                </Table>

            </TableContainer>

            <TablePagination 
                component="div"
                count={-1}
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

const filterContainerStyle = {
    display: "grid",
    gridTemplateColumns: "1fr 180px auto",
    gap: "15px",
    alignItems: "center"
};

const actionStyle = {
    display: "flex",
    gap: "8px",
};

export default Services;