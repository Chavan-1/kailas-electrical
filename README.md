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
