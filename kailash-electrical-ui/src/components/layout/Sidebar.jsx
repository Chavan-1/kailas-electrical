import DashboardIcon from "@mui/icons-material/Dashboard";
import PeopleIcon from "@mui/icons-material/People";
import BuildIcon from "@mui/icons-material/Build";
import EventIcon from "@mui/icons-material/Event";
import ReceiptIcon from "@mui/icons-material/Receipt";

import { useLocation, useNavigate } from "react-router-dom";
import { Drawer, List, ListItemButton, ListItemIcon, ListItemText, Toolbar } from "@mui/material";
import { useTranslation } from "react-i18next";
import { getUser } from "../../utils/auth";

const draweWidth = 240;

function Sidebar() {

    const navigate = useNavigate();
    const location = useLocation();

    const { t } = useTranslation();

    const user = getUser();

    const menuItems = user?.role === "ADMIN" ? [
        {
            text: t("common.dashboard"),
            path: "/dashboard",
            icon: <DashboardIcon />
        },
        {
            text: t("common.customers"),
            path: "/customers",
            icon: <PeopleIcon />
        },
        {
            text: t("common.services"),
            path: "/services",
            icon: <BuildIcon />
        },
        {
            text: t("common.bookings"),
            path: "/bookings",
            icon: <EventIcon />
        },
        {
            text: t("common.invoices"),
            path: "/invoices",
            icon: <ReceiptIcon />
        }
    ] : [
        {
            text: t("common.dashboard"),
            path: "/customer-dashboard",
            icon: <DashboardIcon />
        },
        {
            text: t("common.services"),
            path: "/services",
            icon: <BuildIcon />
        },
        {
            text: t("common.bookings"),
            path: "/bookings",
            icon: <EventIcon />
        },
        {
            text: t("common.invoices"),
            path: "/my-invoices",
            icon: <ReceiptIcon />
        }
    ]

    return (
        <Drawer 
            variant="permanent"
            sx={{
                width: draweWidth,
                flexShrink: 0,
                "& .MuiDrawer-paper": {
                    width: draweWidth,
                    boxSizing: "border-box"
                }
            }}>

                <Toolbar />

                <List>

                    {menuItems.map((item) => (
                        
                        <ListItemButton 
                            key={item.path}
                            selected={location.pathname === item.path}
                            onClick={() => navigate(item.path)}>

                                <ListItemIcon>{item.icon}</ListItemIcon>
                                <ListItemText primary={item.text}/>
                        </ListItemButton>
                    ))}

                </List>
        </Drawer>
    );

}

export default Sidebar;