import { useTranslation } from "react-i18next";
import { getUser } from "../../utils/auth";
import { useNavigate } from "react-router-dom";
import { Box, Button, Card, CardContent, Divider, Grid, Paper, Typography } from "@mui/material";
import { AddCircleOutlineOutlined, ArrowForward, CalendarMonth, Person, ReceiptLong } from "@mui/icons-material";
import { useEffect, useState } from "react";
import { getMyBookings } from "../../services/BookingService";
import { getProfile } from "../../services/ProfileService";

function CustomerDashboard() {

    const user = getUser();
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [recentBookings, setrecentBookings] = useState([]);
    const [loadingBookings, setLoadingBookings] = useState(false);
    const [profile, setProfile] = useState(null);
    const [loadingProfile, setLoadingProfile] = useState(false);  

    useEffect(() => {
      loadProfile();
      loadRecentBookings();
    }, []);

    const loadRecentBookings = async () => {

      try {

        setLoadingBookings(true);

        const response = await getMyBookings({
          page: 0,
          size: 5,
          sortBy: "bookingDate",
          direction: "desc"
        });

        console.log("Recent Bookings API response:", response);

        if (response.success) {
          setrecentBookings(response.data || []);
        }

      } catch (error) {

        console.error("Failed to load recnet bookings:", error);

      } finally {
         setLoadingBookings(false);

      }

    };

    const loadProfile = async () => {

      try {

        setLoadingProfile(true);

        const response = await getProfile();

        console.log("Profile API response:", response);

        if (response.success) {

          setProfile(response.data);
        }

      } catch (error) {

        console.error("Failed to load profile:", error);

      } finally {

        setLoadingProfile(false);
      }

    };

  return (
    <div style={pageStyle}>
      
      <Box sx={{ mb: 4 }}>

          <Typography variant="h4" fontWeight={600} sx={{ mb: 1 }}>
              {t("dashboard.welcome")}, {user?.fullName || t("customer.customer")}
          </Typography>

          <Typography variant="body1" color="text.secondary">
              {t("dashboard.manageServicesBookingsInvoices")}
          </Typography>

      </Box>

      <Grid container spacing={3} sx={{ mb: 4 }}>

          <Grid size={{ xs: 12, md: 12 }}>

            <Card elevation={2}>

              <CardContent sx={{ p: 3 }}>

                <Typography variant="body2">
                    <strong>{t("customer.email")}:</strong>{user?.email}
                </Typography>

                <Typography variant="body2" sx={{ mt: 1 }}>
                    <strong>{t("dashboard.role")}:</strong>{user?.role}
                </Typography>

                <Typography variant="subtitle1" color="text.secondary">
                    {t("dashboard.accountStatus")}
                </Typography>

                {loadingProfile ? (

                  <Typography variant="body1" sx={{ mt: 2 }}>{t("common.loading")}</Typography>

                ) : (
                  
                  <>
                    
                    <Typography variant="h5" fontWeight={600} sx={{ mt: 1, color: profile?.active ? "success.main" : "error.main" }}>

                      {profile?.active 
                              ? t("common.inactive") 
                              : t("common.active")}

                    </Typography>

                    <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                        
                        {profile?.active
                            ? t("dashboard.inactiveAccountMessage")
                            : t("dashboard.accountActiveMessage")
                        }

                    </Typography>
                 
                  </>
                
                )}

              </CardContent>

            </Card>

          </Grid>

      </Grid>

      <Typography variant="h5" fontWeight={600} sx={{ mb: 2 }}>
          {t("dashboard.quickActions")}
      </Typography>

      <Grid container spacing={3} sx={{ mb: 4 }}>

          <Grid size={{ xs: 12, sm: 6, md: 3 }}>

            <Card elevation={2}
                  sx={{ 
                        height: "100%", 
                        cursor: "pointer", 
                        "&:hover": {boxShadow: 12} 
                      }}
                  onClick={() => navigate("/bookings/create")}>

              <CardContent sx={{ p: 3 }}>

                <AddCircleOutlineOutlined sx={{ fontSize: 40, mb: 1 }}/>

                <Typography variant="h6" fontWeight={600}>
                    {t("dashboard.bookService")}
                </Typography>

                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                    {t("dashboard.createNewBooking")}
                </Typography>

                <Button size="small" endIcon={<ArrowForward />}>
                  {t("dashboard.bookNow")}
                </Button>

              </CardContent>

            </Card>

          </Grid>

          <Grid size={{ xs: 12, sm: 6, md: 3 }}>

            <Card elevation={2}
                  sx={{ 
                        height: "100%", 
                        cursor: "pointer", 
                        "&:hover": {boxShadow: 6} 
                      }}
                  onClick={() => navigate("/bookings")}>

              <CardContent>

                <CalendarMonth sx={{ fontSize: 40, mb: 1 }} />

                <Typography variant="h6" fontWeight={600}>
                    {t("dashboard.myBookings")}
                </Typography>

                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                    {t("dashboard.viewManageBookings")}
                </Typography>

                <Button size="small" endIcon={<ArrowForward />}>
                  {t("dashboard.viewBookings")}
                </Button>

              </CardContent>

            </Card>

          </Grid>

          <Grid size={{ xs: 12, sm: 6, md: 3 }}>

            <Card elevation={2}
                  sx={{ 
                        height: "100%", 
                        cursor: "pointer", 
                        "&:hover": {boxShadow: 6} 
                      }}
                  onClick={() => navigate("/profile")}>

              <CardContent>

                <Person sx={{ fontSize: 40, mb: 1 }} />

                <Typography variant="h6" fontWeight={600}>
                    {t("dashboard.myProfile")}
                </Typography>

                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                    {t("dashboard.viewUpdateProfile")}
                </Typography>

                <Button size="small" endIcon={<ArrowForward />}>
                  {t("dashboard.viewProfile")}
                </Button>

              </CardContent>

            </Card>

          </Grid>

          <Grid size={{ xs: 12, sm: 6, md: 3 }}>

            <Card elevation={2}
                  sx={{ 
                        height: "100%", 
                        cursor: "pointer", 
                        "&:hover": {boxShadow: 6} 
                      }}
                  onClick={() => navigate("/my-invoices")}>

              <CardContent>

                <ReceiptLong sx={{ fontSize: 40, mb: 1 }} />

                <Typography variant="h6" fontWeight={600}>
                    {t("dashboard.myInvoices")}
                </Typography>

                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                    {t("dashboard.viewInvoicesPayments")}
                </Typography>

                <Button size="small" endIcon={<ArrowForward />}>
                  {t("dashboard.viewInvoices")}
                </Button>

              </CardContent>

            </Card>

          </Grid>

      </Grid>

        <Paper elevation={2} sx={{ p: 3, borderRadius: 2 }}>

          <Typography variant="h6" fontWeight={600} sx={{ mb: 1 }}>
              {t("dashboard.recentBookings")}
          </Typography>

          <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
              {t("dashboard.recentBookingsMessage")}
          </Typography>

          {loadingBookings ? (
            <Box>
              <Typography>
                {t("dashboard.loadingRecentBookings")}
              </Typography>
            </Box>
          ) : recentBookings.length === 0 ? (

            <Box sx={{ mt: 3, p: 4, textAlign: "center", backgroundColor: "#f8f9fa", borderRadius: 2 }}>
            
              <CalendarMonth sx={{ fontSize: 50, color: "text.secondary", mb: 1 }} />

              <Typography variant="body2" color="text.secondary">
                {t("dashboard.noRecentBookings")}
              </Typography>

              <Button variant="contained" sx={{ mt: 2 }} onClick={() => navigate("/bookings/create")}>
                {t("dashboard.bookAService")}
              </Button>

            </Box>

          ) : (

            <Box sx={{ mt: 3 }}>

              {recentBookings.map((booking) => (

                <Card key={booking.bookingId} variant="outlined" sx={{ mb: 2 }}>

                  <CardContent>

                    <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
 
                      <Box>

                        <Typography variant="h6" fontWeight={600}>
                            {booking.bookingNumber}
                        </Typography>

                        <Typography variant="body2" color="text.secondary">
                            {t("booking.bookingDate")}: {booking.bookingDate}
                        </Typography>

                        <Typography variant="body2" color="text.secondary">
                            {t("booking.bookingTime")}: {booking.bookingTime}
                        </Typography>

                        <Typography variant="body2" sx={{ mt: 1 }}>
                            {t("common.amount")}: ₹{booking.totalAmount}
                        </Typography>

                      </Box>

                      <Box
                        sx={{
                            px: 2,
                            py: 0.5,
                            borderRadius: 2,
                            backgroundColor:
                                booking.status === "COMPLETED"
                                    ? "success.light"
                                    : booking.status === "CANCELLED"
                                    ? "error.light"
                                    : "warning.light"
                        }}
                      >
                        <Typography variant="body2" fontWeight={600}>
                            {booking.status}
                        </Typography>

                      </Box>

                    </Box>

                  </CardContent>

                </Card>

              ))}

              <Box sx={{ textAlign: "right", mt: 2 }}>

                <Button variant="outlined" onClick={() => navigate("/bookings")}>
                    {t("dashboard.viewAllBookings")}
                </Button>

              </Box>

            </Box>

          )}

        </Paper>
        
    </div>
    
  );

}

const pageStyle = {
    padding: "50px",
    backgroundColor: "#f5f6fa",
    minHeight: "100vh"
};

export default CustomerDashboard;