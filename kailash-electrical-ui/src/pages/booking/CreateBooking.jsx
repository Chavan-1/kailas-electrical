import { useEffect, useState } from "react";
import { getServices } from "../../services/ServiceService";
import { createBooking } from "../../services/BookingService";
import { getCustomers } from "../../services/CustomerService";
import { useNavigate } from "react-router-dom";
import { getProfile } from "../../services/ProfileService";
import { useTranslation } from "react-i18next";

function CreateBooking() {

    const navigate = useNavigate();
    const { t } = useTranslation();

    const [role, setRole] = useState("");
    const [customers, setCustomers] = useState([]);
    const [services, setServices] = useState([]);

    const [customerId, setCustomerId] = useState("");
    const [selectedServices, setSelectedServices] = useState([]);

    const [bookingDate, setBookingDate] = useState("");
    const [bookingTime, setBookingTime] = useState("");
    const [remarks, setRemarks] = useState("");

    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);

    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {

        try {

            setLoading(true);
            setError("");

            const profileResponse = await getProfile();

            console.log("Profile response:", profileResponse);

            if (!profileResponse.success) {
                setError(profileResponse.message || t("profile.loadProfileFailed"));
                return;
            }

            const profile = profileResponse.data;

            console.log("Logged-in profile:", profile);

            const userRole = profile.role;

            setRole(userRole);

            const serviceResponse = await getServices();

            console.log("Service API response:", serviceResponse);

            if (!serviceResponse.success) {
                setError(serviceResponse.message || t("booking.failedToLoadServices"));
                return;
            }

            setServices(serviceResponse.data.content || []);

            if (userRole === "ADMIN") {

                const customerResponse = await getCustomers();

                console.log("Customer API response:", customerResponse);

                if (!customerResponse.success) {
                    setError(customerResponse.message || t("booking.failedToLoadCustomers"));
                    return;
                }

                setCustomers(customerResponse.data.content || []);
            }

            if (userRole === "CUSTOMER") {

                const loggedInCustomerId = profile.customerId;

                console.log("Logged-in customer ID:", loggedInCustomerId);

                if (!loggedInCustomerId) {
                    setError(t("booking.customerProfileNotFoundLoggedIn"));
                    return;
                }

                setCustomerId(String(loggedInCustomerId));
            }

        } catch (error) {

            console.error("Failed to load booking data:", error);

            setError(error.response?.data?.message || t("booking.unableToLoadBookingData"));

        } finally {

            setLoading(false);
        }
    };

    const handleServiceChange = (serviceId) => {

        const id = Number(serviceId);

        if (selectedServices.includes(id)) {

            setSelectedServices(selectedServices.filter(
                service => service !== id
            ));

        } else {

            setSelectedServices([...selectedServices, id]);
        }
    };

    const handleSubmit = async (event) => {

        event.preventDefault();

        setError("");
        setSuccess("");

        if (!customerId) {

            setError(role === "ADMIN"
                ? t("booking.selectCustomerRequired")
                : t("booking.customerProfileNotFound")
            );
            return;
        }

        if (selectedServices.length === 0) {

            setError(t("booking.selectService"));
            return;
        }

        if (!bookingDate) {

            setError(t("booking.selectBookingDate"));
            return;
        }

        if (!bookingTime) {

            setError(t("booking.selectBookingTime"));
            return;
        }

        const bookingData = {

            customerId: Number(customerId),
            serviceIds: selectedServices,
            bookingDate: bookingDate,
            bookingTime: bookingTime,
            remarks: remarks
        };

        console.log("Create booking payload:", bookingDate);

        try {

            setSubmitting(true);

            const response = await createBooking(bookingData);

            console.log("Create booking response:", response);

            if (response.success) {

                setSuccess(t("booking.createSuccess"));
                
                const bookingId = response.data?.id;

                if (bookingId) {

                    navigate(`/bookings/${bookingId}`);

                } else {

                    navigate("/bookings");
                }

            } else {

                setError(response.message || t("booking.failedToCreateBooking"));
            }
        } catch (error) {

            console.error("Failed to create booking:", error);

            setError(error.response?.data?.message || t("booking.failedToCreateBooking"));

        } finally {
            
            setSubmitting(false);
        }
    };

    if (loading) {

        return (
            <div style={pageStyle}>
                <h1 style={headingStyle}>t("booking.createBooking")</h1>
                <p>t("booking.loadingCustomersServices")</p>
            </div>
        );
    }

    return (

        <div style={pageStyle}>
            
            <h1 style={headingStyle}>{t("booking.createBooking")}</h1>

            <div style={formContainerStyle}>
                
                {error && (
                    <div style={errorStyle}>{error}</div>
                )}

                {success && (
                    <div style={successStyle}>{success}</div>
                )}

                <form onSubmit={handleSubmit}>
                    
                    {role === "ADMIN" && (

                        <div style={fieldContainerStyle}>

                            <label style={labelStyle}>{t("customer.customer")}</label>

                            <select
                                value={customerId}
                                onChange={(event) =>
                                    setCustomerId(event.target.value)
                                }
                                style={inputStyle}
                            >
                                <option value="">{t("booking.selectCustomer")}</option>

                                {customers
                                    .filter(customer => customer.active)
                                    .map(customer => (
                                        <option
                                            key={customer.id}
                                            value={customer.id}
                                        >
                                            {customer.fullName} - {customer.phoneNumber}
                                        </option>
                                    ))}
                            </select>

                        </div>

                    )}

                    <div style={fieldContainerStyle}>
                        
                        <label style={labelStyle}>{t("common.services")}</label>

                        <div style={serviceContainerStyle}>
                            
                            {services.filter(service => service.active)
                                     .map(service => (
                                        <label key={service.id} style={serviceOptionStyle}>

                                           <input type="checkbox" 
                                                checked={selectedServices.includes(service.id)}
                                                onChange={() => handleServiceChange(service.id)} />

                                            <span>{service.serviceName}</span>
                                            <span style={priceStyle}>₹{service.basePrice}</span>

                                        </label>
                                     ))}
                        </div>
                        
                    </div>
                    
                    <div style={fieldContainerStyle}>
                            
                        <label style={labelStyle}>{t("customer.bookingDate")}</label>

                        <input type="date" 
                               value={bookingDate}
                               onChange={(event) => setBookingDate(event.target.value)} 
                               style={inputStyle}/>

                    </div>

                    <div style={fieldContainerStyle}>
                            
                        <label style={labelStyle}>{t("customer.bookingTime")}</label>

                        <input type="time" 
                               value={bookingTime}
                               onChange={(event) => setBookingTime(event.target.value)} 
                               style={inputStyle}/>

                    </div>

                    <div style={fieldContainerStyle}>
                            
                        <label style={labelStyle}>{t("booking.remarks")}</label>

                        <textarea 
                               value={remarks}
                               onChange={(event) => setRemarks(event.target.value)} 
                               placeholder={t("booking.additionalInformationPlaceholder")}
                               rows={4}
                               style={textareaStyle}
                        />

                    </div>

                    <div style={buttonContainerStyle}>

                        <button
                            type="button"
                            onClick={() => navigate("/bookings")}
                            style={cancelButtonStyle}
                        >
                            {t("common.cancel")}
                        </button>

                        <button type="submit" disabled={submitting} style={buttonStyle}>
                            {submitting ? t("booking.creatingBooking") : t("booking.createBooking")}
                        </button>

                    </div>

                </form>

            </div>

        </div>
    );
}

const pageStyle = {
    padding: "30px",
    backgroundColor: "#f5f6fa",
    minHeight: "100vh"
};

const headingStyle = {
    marginBottom: "25px",
    color: "#222"
};

const formContainerStyle = {
    backgroundColor: "white",
    padding: "30px",
    borderRadius: "10px",
    boxShadow: "0 2px 8px rgba(0,0,0,0.08)",
    maxWidth: "800px"
};

const fieldContainerStyle = {
    marginBottom: "22px"
};

const labelStyle = {
    display: "block",
    marginBottom: "8px",
    fontWeight: "600",
    color: "#333"
};

const inputStyle = {
    width: "100%",
    padding: "12px",
    border: "1px solid #ccc",
    borderRadius: "5px",
    fontSize: "15px",
    boxSizing: "border-box"
};

const textareaStyle = {
    width: "100%",
    padding: "12px",
    border: "1px solid #ccc",
    borderRadius: "5px",
    fontSize: "15px",
    resize: "vertical",
    boxSizing: "border-box"
};

const serviceContainerStyle = {
    border: "1px solid #ddd",
    borderRadius: "5px",
    padding: "10px"
};

const serviceOptionStyle = {
    display: "flex",
    alignItems: "center",
    gap: "10px",
    padding: "12px",
    borderBottom: "1px solid #eee",
    cursor: "pointer"
};

const priceStyle = {
    marginLeft: "auto",
    fontWeight: "600"
};

const buttonStyle = {
    backgroundColor: "#1976d2",
    color: "white",
    border: "none",
    padding: "12px 25px",
    borderRadius: "5px",
    fontSize: "16px",
    cursor: "pointer"
};

const errorStyle = {
    backgroundColor: "#ffebee",
    color: "#d32f2f",
    padding: "12px",
    borderRadius: "5px",
    marginBottom: "20px"
};

const successStyle = {
    backgroundColor: "#e8f5e9",
    color: "#2e7d32",
    padding: "12px",
    borderRadius: "5px",
    marginBottom: "20px"
};

const buttonContainerStyle = {
    display: "flex",
    justifyContent: "flex-end",
    gap: "10px",
    marginTop: "25px"
};

const cancelButtonStyle = {
    backgroundColor: "white",
    color: "#1976d2",
    border: "1px solid #1976d2",
    padding: "12px 25px",
    borderRadius: "5px",
    fontSize: "16px",
    cursor: "pointer"
};

export default CreateBooking;