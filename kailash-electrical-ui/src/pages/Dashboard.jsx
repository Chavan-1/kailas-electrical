import { useEffect, useState } from "react";
import { getDashboard } from "../services/DashboardService";
import { useTranslation } from "react-i18next";

function Dashboard() {

    const [dashboard, setDashboard] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const { t } = useTranslation();

    useEffect(() => {
        loadDashboard();
    }, []);

    const loadDashboard = async () => {

        try {

            const response = await getDashboard();

            if (response.success) {

                setDashboard(response.data);

            } else {

                setError(response.message || t("dashboard.failedToLoadDashboard"));
            }

        } catch (error) {

            console.error("Failed to fetch dashboard:", error);
            setError("Unable to load dashboard");

        } finally {

            setLoading(false);
        }
    };

    if (loading) {

        return (
            <div style={pageStyle}>
                <h1>{t("dashboard.title")}</h1>
                <p>{t("dashboard.loading")}</p>
            </div>
        );
    }

    if (error) {

        return (
            <div style={pageStyle}>
                <h1>{t("dashboard.title")}</h1>
                <p style={{ color: "#d32f2f" }}>{error}</p>
            </div>
        );
    }

    if (!dashboard) {

        return (
            <div style={pageStyle}>
                <h1>{t("dashboard.title")}</h1>
                <p>{t("dashboard.boData")}</p>
            </div>
        );
    }

    return (

        <div style={pageStyle}>

            <h1 style={headingStyle}>{t("dashboard.title")}</h1>

            <div style={cardGridStyle}>
                <DashboardCard title={t("dashboard.totalCustomers")} value={dashboard.totalCustomers}/>
                <DashboardCard title={t("dashboard.totalServices")} value={dashboard.totalServices}/>
                <DashboardCard title={t("dashboard.totalBookings")} value={dashboard.totalBookings}/>
            </div>

            <div style={cardGridStyle}>
                <DashboardCard title={t("dashboard.pendingBookings")} value={dashboard.pendingBookings}/>
                <DashboardCard title={t("dashboard.completedBookings")} value={dashboard.completedBookings}/>
                <DashboardCard title={t("dashboard.cancelledBookings")} value={dashboard.cancelledBookings}/>
            </div>

            <div style={cardGridStyle}>
                <DashboardCard title={t("dashboard.todaysBookings")} value={dashboard.todayBookings}/>
                <DashboardCard title={t("dashboard.pendingPayments")} value={dashboard.pendingPayments}/>
                <DashboardCard title={t("dashboard.totalRevenue")} value={formatCurrency(dashboard.totalRevenue)}/>
            </div>

            <div style={revenueCardStyle}>
                <div>
                    <p style={revenueTitleStyle}>{t("dashboard.monthlyRevenue")}</p>
                    <h2 style={revenueValueStyle}>{formatCurrency(dashboard.monthlyRevenue)}</h2>
                </div>
            </div>
        </div>
    );
}


const DashboardCard = ({ title, value }) => {

    return (
        <div style={cardStyle}>
            <p style={cardTitleStyle}>{title}</p>
            <h2 style={cardValueStyle}>{value}</h2>
        </div>
    );
};

const formatCurrency = (value) => {

    if (value === null || value === undefined) {
        return "₹0";
    }

    return new Intl.NumberFormat("en-IN", {
        style: "currency",
        currency: "INR",
        maximumFractionDigits: 2
    }).format(value);
};

const pageStyle = {
    padding: "30px",
    backgroundColor: "#f5f6fa",
    minHeight: "100vh"
};

const headingStyle = {
    marginBottom: "25px",
    color: "#222"
};

const cardGridStyle = {
    display: "grid",
    gridTemplateColumns: "repeat(3, 1fr)",
    gap: "20px",
    marginBottom: "20px"
};

const cardStyle = {
    backgroundColor: "white",
    padding: "25px",
    borderRadius: "10px",
    boxShadow: "0 2px 8px rgba(0,0,0,0.08)"
};

const cardTitleStyle = {
    margin: "0 0 10px 0",
    color: "#666",
    fontSize: "15px"
};

const cardValueStyle = {
    margin: 0,
    fontSize: "28px",
    color: "#222"
};

const revenueCardStyle = {
    backgroundColor: "white",
    padding: "25px",
    borderRadius: "10px",
    boxShadow: "0 2px 8px rgba(0,0,0,0.08)",
    marginTop: "5px"
};

const revenueTitleStyle = {
    margin: 0,
    color: "#666",
    fontSize: "15px"
};

const revenueValueStyle = {
    margin: "10px 0 0 0",
    fontSize: "32px",
    color: "#1976d2"
};

export default Dashboard;