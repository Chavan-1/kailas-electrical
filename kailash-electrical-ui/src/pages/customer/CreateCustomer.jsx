import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createCustomer } from "../../services/CustomerService";
import { Button, Paper, TextField, Typography } from "@mui/material";
import { useTranslation } from "react-i18next";

const CreateCustomer = () => {

    const navigate = useNavigate();

    const [form, setForm] = useState({
        fullName: "",
        phoneNumber: "",
        email: "",
        address: ""
    });

    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const { t } = useTranslation();

    const handleChange = (event) => {

        const { name, value } = event.target;
        
        setForm((previous) => ({
            ...previous,
            [name]: value
        }));
    };

    const handleSubmit = async (event) => {

        event.preventDefault();

        setLoading(true);
        setError("");

        try {

            const response = await createCustomer(form);

            if (response.success) {
                alert(t("customer.createSuccess"));

                navigate("/customers");

            } else {
                setError(response.message || t("customer.failedToCreateCustomer"));
            }

        } catch (error){

            console.error("Failed to create customer:", error);
            setError(error.response?.data?.message || t("customer.failedToCreateCustomer"));

        } finally {

            setLoading(false);
        }
    };

    return (

        <div style={pageStyle}>

            <Paper elevation={2} style={formContainerStyle}>

                <Typography variant="h4" gutterBottom>{t("customer.addCustomer")}</Typography>

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
                        inputprops={{ maxLength: 10 }}
                    />
                    <TextField
                        label={t("customer.email")}
                        name="email"
                        type="email"
                        value={form.email}
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

                        <Button variant="outlined" onClick={() => navigate("/customers")}>{t("common.cancel")}</Button>
                        
                        <Button type="submit" variant="contained" disabled={loading}>
                            {loading ? t("common.loading") : t("common.save")}
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

export default CreateCustomer;