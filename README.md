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
📚 Key Concepts Learned

**This project provided hands-on experience with:**

Java

OOP
Classes and interfaces
Enums
Collections
Streams
Optional
Exception handling
Generics
Lambda expressions

Spring Boot

Dependency Injection
REST Controllers
Service Layer
Repository Layer
Configuration
Exception handling
Bean lifecycle

Spring Data JPA

Entity mapping
Relationships
JPA repositories
Derived queries
Pagination
Sorting
Specifications
Criteria API
Entity lifecycle callbacks

Spring Security

JWT
Authentication
Authorization
SecurityContext
UserDetails
Role-based access
CORS
Stateless sessions

Backend Design

DTO pattern
Mapper pattern
Layered architecture
Business rules
Resource ownership
Soft deletion
API design

React

Components
Props
State
Hooks
useEffect
React Router
Protected routes
Role-based UI
API integration
Axios
Material UI

Other

PostgreSQL
Git/GitHub
Railway deployment
Internationalization
PDF generation
Devanagari font embedding
Production debugging

🚀 Deployment

The application is deployed using Railway for backend/database hosting.

The project repository is maintained on GitHub.

Application pages:
Login Page:
<img width="1898" height="962" alt="image" src="https://github.com/user-attachments/assets/387a0dc0-606f-42cd-8e3d-2a8a8594d27c" />

Register Page:
<img width="1553" height="957" alt="image" src="https://github.com/user-attachments/assets/2e8853d6-aae2-4651-afc8-3c5ea0cfe748" />

Forgot Password Page:
<img width="1568" height="973" alt="image" src="https://github.com/user-attachments/assets/4ad23ccc-0559-4703-a443-9ce9a2007de9" />

Customer Dashboard Page:
<img width="1902" height="958" alt="image" src="https://github.com/user-attachments/assets/af1dd998-01a8-48f1-ab79-acb9e3617ad1" />

Customer Dashboard Page:
<img width="1900" height="951" alt="image" src="https://github.com/user-attachments/assets/f752d03b-3ce6-4ddf-a665-bb187e996099" />

Customer- Services Page:
<img width="1902" height="955" alt="image" src="https://github.com/user-attachments/assets/d1494bb1-0a4e-4f4c-a762-5c896af0bf00" />

Customer- Service Details Page:
<img width="1902" height="958" alt="image" src="https://github.com/user-attachments/assets/9da1b628-fa56-46a2-8238-aa5ab7ff7e01" />

Customer- Bookings Page:
<img width="1901" height="960" alt="image" src="https://github.com/user-attachments/assets/15895be4-1cb7-425a-8eb0-8186509793f4" />

Customer- Create Booking Page:
<img width="1891" height="948" alt="image" src="https://github.com/user-attachments/assets/7ca6a5a6-4212-42a7-8830-626719ac4ece" />

Customer- Booking Details Page:
<img width="1906" height="962" alt="image" src="https://github.com/user-attachments/assets/07040a4b-e092-405d-9d3f-759754ffbbc0" />

Customer- Invoices Page:
<img width="1912" height="937" alt="image" src="https://github.com/user-attachments/assets/690dd3ee-5fa4-46e5-a435-7f9e2ec6e595" />

Customer- Invoice Details Page:
<img width="1902" height="930" alt="image" src="https://github.com/user-attachments/assets/29d4c00d-2093-4d97-9087-29528ad0fb4c" />

Admin Dashboard Page:
<img width="1888" height="963" alt="image" src="https://github.com/user-attachments/assets/27cc9e6b-6372-41a3-9b58-56b17dba7423" />

Admin- Customer Page:
<img width="1895" height="952" alt="image" src="https://github.com/user-attachments/assets/ce32a237-704b-4dc4-9fdf-9d807ea2788b" />

Admin- Customer Details Page:
<img width="2490" height="1053" alt="Untitled" src="https://github.com/user-attachments/assets/76f6fd98-ba52-4559-843f-08ca69dac0e0" />

Admin- Customer Details Page(Booking and Invoice History)
<img width="1891" height="948" alt="image" src="https://github.com/user-attachments/assets/c4a28f4a-5192-4a2b-9bc9-2539a5a6b168" />

Admin- Customer Edit Page
<img width="1897" height="935" alt="image" src="https://github.com/user-attachments/assets/cb5847f2-35bd-49e0-953d-dc446c76b0cc" />

Admin- Services Page
<img width="1892" height="941" alt="image" src="https://github.com/user-attachments/assets/8497aab3-3764-4e23-a8a4-c49989d82ca9" />

Admin- Service Details Page
<img width="1896" height="972" alt="image" src="https://github.com/user-attachments/assets/f60d80e7-aa51-4627-9a48-1952af0f69c6" />

Admin- Service Edit Page
<img width="1902" height="930" alt="image" src="https://github.com/user-attachments/assets/3bb5b79c-8435-446c-8408-e3d826b91a85" />

Admin- Bookings Page
<img width="1896" height="950" alt="image" src="https://github.com/user-attachments/assets/cb60fb7b-58e2-468c-b122-b7f7bd578b44" />

Admin- Booking Details Page
<img width="1916" height="933" alt="image" src="https://github.com/user-attachments/assets/ebd47325-a9b9-4045-8da3-ac5a0c524ead" />

Admin- Invoices Page
<img width="1905" height="965" alt="image" src="https://github.com/user-attachments/assets/fdf1154f-86cf-4e6e-9eb7-a3851d3422a9" />

Admin- Invoice Details Page
<img width="2490" height="1053" alt="Untitled" src="https://github.com/user-attachments/assets/979c75e2-a5d8-40e0-8410-3b8ed0efd66f" />

Multilingual Support
<img width="1903" height="950" alt="image" src="https://github.com/user-attachments/assets/c0b18cdc-58d0-473e-8fd9-e37c9421919e" />
<img width="1897" height="950" alt="image" src="https://github.com/user-attachments/assets/2e6db942-2146-4b16-8a92-6c270051ae1c" />


