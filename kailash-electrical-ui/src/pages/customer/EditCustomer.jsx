import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom"
import { getCustomerById, updateCustomer } from "../../services/CustomerService";
import { Button, Paper, TextField, Typography } from "@mui/material";
import { useTranslation } from "react-i18next";

const EditCustomer = () => {

    const { id } = useParams();

    const navigate = useNavigate();

    const [form, setForm] = useState({
        fullName: "",
        phoneNumber: "",
        email: "",
        address: "",
        active: true
    });

    const [error, setError] = useState("");
    const [loading, setLoading] = useState(true);

    const { t } = useTranslation();

    useEffect(() => {
        loadCustomer();
    }, [id]);

    const loadCustomer = async () => {

        try {

            const response = await getCustomerById(id);

            if (response.success) {
                setForm(response.data);

            } else {

                setError(response.message || t("customer.loadFailed"));
            }

        } catch (error) {

            console.error(error);
            setError(error.response?.data?.message || t("customer.loadFailed"));

        } finally {

            setLoading(false);
        }
    };

    const handleChange = (event) => {

        const { name, value } = event.target;

        setForm((previous) => ({
            ...previous,
            [name]: value
        }));
    };

    const handleSubmit = async (event) => {

        event.preventDefault();

        try {

            const response = await updateCustomer(id, form);

            if (response.success) {

                alert(t("customer.updateSuccess"));
                navigate(`/customers/${id}`);

            } else {

                setError(response.message || t("customer.updateFailed"));
            }
        } catch (error) {

            console.error(error);

            setError(error.response?.data?.message || t("customer.updateFailed"));
        }
    };

    if (loading) {
        return (
            <div style={pageStyle}>
                {t("customer.loadingCustomer")}
            </div>
        );
    }

    return (

        <div style={pageStyle}>

            <Paper elevation={2} style={formContainerStyle}>

                <Typography variant="h4" gutterBottom>{t("customer.editCustomer")}</Typography>

                <form onSubmit={handleSubmit}>

                    <TextField
                        label={t("customer.fullName")}
                        name="fullName"
                        value={form.fullName}
                        onChange={handleChange}
                        fullWidth
                        margin="normal"
                        required
                    />
                    <TextField
                        label={t("customer.phoneNumber")}
                        name="phoneNumber"
                        value={form.phoneNumber}
                        onChange={handleChange}
                        fullWidth
                        margin="normal"
                        required
                    />
                    <TextField
                        label={t("customer.email")}
                        name="email"
                        type="email"
                        value={form.email || ""}
                        onChange={handleChange}
                        fullWidth
                        margin="normal"
                    />
                    <TextField
                        label={t("customer.address")}
                        name="address"
                        value={form.address}
                        onChange={handleChange}
                        fullWidth
                        margin="normal"
                        required
                        rows={3}
                        multiline
                    />

                    {error && (
                        <Typography color="error" sx={{ mt: 2 }}>{error}</Typography>
                    )}

                    <div style={buttonContainerStyle}>

                        <Button variant="outlined" onClick={() => navigate(`/customers/${id}`)}>{t("common.cancel")}</Button>
                        
                        <Button type="submit" variant="contained">{t("common.update")}</Button>

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

const formContainerStyle = {
    maxWidth: "700px",
    margin: "0 auto",
    padding: "30px",
    borderRadius: "10px"
};

const buttonContainerStyle = {
    display: "flex",
    justifyContent: "flex-end",
    gap: "15px",
    marginTop: "25px"
};

export default EditCustomer;