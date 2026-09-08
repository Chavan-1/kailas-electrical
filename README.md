⚡ Kailas Electrical Services

A full-stack electrical service management application built to manage electrical services, customers, bookings, invoices, and customer profiles through a secure role-based system.

The project was developed using **Spring Boot, Spring Security, JWT, JPA/Hibernate, MySQL, React, Material UI and REST APIs**, with support for **English, Hindi and Marathi**.

📌 Project Overview

Kailas Electrical Services is designed to digitize the workflow of an electrical service business.

The application provides separate functionality for:

- 👨‍💼 Admin
- 👤 Customer

Admins can manage customers, electrical services, bookings and invoices, while customers can browse services, create bookings, manage their profile and access their own invoices.

🏗️ Architecture

The application follows a layered full-stack architecture.

                    ┌─────────────────────┐
                    │     React Frontend  │
                    │   Vite + MUI        │
                    └──────────┬──────────┘
                               │
                         REST APIs / Axios
                               │
                    ┌──────────▼──────────┐
                    │   Spring Boot API   │
                    │                     │
                    │ Controller Layer    │
                    │ Service Layer       │
                    │ Repository Layer    │
                    │ Specification       │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │    MySQL Database   │
                    └─────────────────────┘
