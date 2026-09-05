import { useEffect, useState } from "react";
import { deleteCustomer, getCustomers, updateActiveDeactiveStatusChange } from "../../services/CustomerService";
import { Alert, Button, FormControl, InputLabel, MenuItem, Paper, Select, Table, TableBody, TableCell, TableContainer, TableHead, TablePagination, TableRow, TextField, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { Add, Delete, Edit, Search, Visibility } from "@mui/icons-material";
import { useTranslation } from "react-i18next";

const CustomerList = () => {

    const { t } = useTranslation();
    const navigate = useNavigate();

    const [customers, setCustomers] = useState([]);

    const [search, setSearch] = useState("");

    const [status, setStatus] = useState("ALL");

    const [page, setPage] = useState(0);

    const [rowsPerPage, setRowsPerPage] = useState(10);

    const [totalElements, setTotalElements] = useState(0);

    const [loading, setLoading] = useState(false);

    const [error, setError] = useState("");

    const loadCustomers = async () => {

        try {
            
            setLoading(true);
            setError("");

            const params = {
                page,
                size: rowsPerPage,
                sortBy: "fullName",
                direction: "asc"
            };

            if (search.trim()) {
                params.keyword = search.trim();
            }

            if (status !== "ALL") {
                params.active = status === "ACTIVE";
            }

            console.log("Customer API params:", params);

            const response = await getCustomers(params);

            console.log("Customers API response: ", response);

            if (response.success) {

                const data = response.data;

                setCustomers(data.content || []);

                setTotalElements(data.totalElements || 0);

            } else {
                
                setError(response.message || t("customer.fetchError"));
            }
        
        } catch (error) {

            console.error("Failed to fetch customers: ", error);

            setError(error.response?.data?.message || t("customer.fetchError"));

        } finally {

            setLoading(false);
        }
    };

    useEffect(() => {

        loadCustomers();

    }, [page, rowsPerPage, status]);

    const handleSearch = () => {

        if (page !== 0) {

            setPage(0);

        } else {

            loadCustomers();
        }
    };

    const handleStatusChange = (event) => {

        setStatus(event.target.value);
        setPage(0);
    };

    const handleActiveDeactiveStatusChange = async (id, active) => {

        const action = active ? t("common.active") : t("common.deactivate");

        const confirmed = window.confirm(`Are you sure you want to ${action} this customer?`);

        if (!confirmed) return;

        try {

            setError("");

            const response = await updateActiveDeactiveStatusChange(id, active);

            if (response.success) {

                await loadCustomers();

            } else {

                setError(response.message || t("customer.statusUpdateFailed"));

            }
        } catch (error) {

            console.log("Customer status update error: ", error);
            setError(error.response?.data?.message || t("customer.statusUpdateFailed"));
        }
    };

    const handlePageChange = (event, newPage) => {

        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {

        const newRowsPerPage = parseInt(event.target.value, 10);

        setRowsPerPage(newRowsPerPage);

        setPage(0);
    };

    const handleView = (id) => {

        navigate(`/customers/${id}`);
    };

    const handleEdit = (id) => {

        navigate(`/customers/${id}/edit`);
    };

    const handleDelete = async (id) => {

        const confirmed = window.confirm(t("customer.deleteConfirmation"));

        if (!confirmed) return;

        try {

            setError("");

            const response = await deleteCustomer(id);

            console.log("Delete customer response: ", response);

            if (response.success) {

                loadCustomers();

            } else {
                
                setError(response.message || t("customer.failedToDeleteCustomer"))
            }

        } catch (error) {

            console.error("Delete customer error:", error);

            setError(error.response?.data?.message || t("customer.failedToDeleteCustomer"));

        } 
    }

    const handleAddCustomer = () => {

        navigate("/customers/create");
    };

    return (

        <div style={pageStyle}>

            {/* <div style={headerStyle}>

                <Typography variant="h4" fontWeight={600}>{t("customer.title")}</Typography>

                <Button variant="contained" onClick={handleAddCustomer} startIcon={<Add />}>{t("customer.addCustomer")}</Button>

            </div> */}

            {error && (
                    <Alert severity="error" 
                           sx={{ mb: 2 }} 
                           onClose={() => setError("")}
                    >
                        {error}
                    </Alert>
            )}

            <Paper elevation={2} sx={{ p: 2, mb: 3, borderRadius: 2 }}>

                <div style={filterStyle}>

                    <TextField label={t("common.searchCustomer")}
                            placeholder={t("customer.namePhoneOrEmail")}
                            value={search} 
                            onChange={(e) => setSearch(e.target.value)}
                            fullWidth />
                    
                    <FormControl sx={{ minWidth: 180 }}>
                        <InputLabel>{t("common.status")}</InputLabel>

                        <Select 
                            value={status} 
                            label={t("common.status")}
                            onChange={handleStatusChange}
                        >
                            <MenuItem value="ALL">{t("common.all")}</MenuItem>
                            <MenuItem value="ACTIVE">{t("common.active")}</MenuItem>
                            <MenuItem value="INACTIVE">{t("common.inactive")}</MenuItem>
                        </Select>

                    </FormControl>
                        
                        <Button variant="contained" 
                                startIcon={<Search />} 
                                onClick={handleSearch}
                        >
                            {t("common.search")}
                        </Button>
                </div>

            </Paper>

            <Paper elevation={2} sx={{ borderRadius: 2, overflow: "hidden" }}>

                <TableContainer>
                    
                    <Table>
                        
                        <TableHead>

                            <TableRow>
                                <TableCell>{t("commom.all")}</TableCell>
                                <TableCell>{t("customer.fullName")}</TableCell>
                                <TableCell>{t("customer.phoneNumber")}</TableCell>
                                <TableCell>{t("customer.email")}</TableCell>
                                <TableCell>{t("customer.address")}</TableCell>
                                <TableCell>{t("common.status")}</TableCell>
                                <TableCell>{t("common.actions")}</TableCell>
                            </TableRow>

                        </TableHead>

                        <TableBody>

                        {loading ? (

                            <TableRow>

                                <TableCell colSpan={7} align="center">
                                    {t("common.loading")}
                                </TableCell>

                            </TableRow>

                        ) : customers.length === 0 ? (

                                <TableRow>

                                    <TableCell colSpan={7} align="center">
                                        {t("common.noData")}
                                    </TableCell>

                                </TableRow>

                            ) : (

                                customers.map((customer) => (

                                <TableRow key={customer.id} hover>

                                    <TableCell>{customer.id}</TableCell>
                                    <TableCell>{customer.fullName}</TableCell>
                                    <TableCell>{customer.phoneNumber}</TableCell>
                                    <TableCell>{customer.email || "-"}</TableCell>
                                    <TableCell>{customer.address}</TableCell>
                                    <TableCell>
                                        <span style={{ ...statusStyle, backgroundColor: customer.active ? "#2e7d32" : "#757575" }}>
                                            {customer.active ? t("common.active") : t("common.inactive")}
                                        </span>
                                    </TableCell>
                                    <TableCell>
                                        
                                        <div style={{ display: "flex", gap: "8px" }}>

                                            <Button variant="contained" 
                                                    size="small"
                                                    startIcon={<Visibility />} 
                                                    onClick={() => handleView(customer.id)}
                                            >
                                                {t("common.view")}
                                            </Button>

                                            <Button variant="contained" 
                                                    size="small"
                                                    color="primary"
                                                    startIcon={<Edit />} 
                                                    onClick={() => handleEdit(customer.id)}
                                            >
                                                {t("common.edit")}
                                            </Button>

                                            <Button variant="contained" 
                                                    size="small"
                                                    color="error"
                                                    startIcon={<Delete />} 
                                                    onClick={() => handleDelete(customer.id)}
                                            >
                                                {t("common.delete")}
                                            </Button>

                                            <button
                                                variant="contained"
                                                size="small" 
                                                color={ customer.active ? "#757575" : "#2e7d32" }
                                                onClick={() => handleActiveDeactiveStatusChange(customer.id, !customer.active)}
                                            >
                                                {customer.active 
                                                        ? t("common.deactivate")
                                                        : t("common.activate")}
                                            </button>

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
                    onPageChange={handlePageChange}
                    rowsPerPage={rowsPerPage}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                    rowsPerPageOptions={[5, 10, 20]}
                />

            </Paper>

        </div>

    );

}

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
    gridTemplateColumns: "1fr 180px auto",
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

export default CustomerList;