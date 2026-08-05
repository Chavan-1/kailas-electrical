package com.kailaselectrical.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kailaselectrical.service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestEmailController {

    private final EmailService emailService;

    @GetMapping("/welcome/{customerId}")
    public String welcome(@PathVariable Long customerId) {
        emailService.sendWelcomeEmail(customerId);
        return "Welcome email sent";
    }

    @GetMapping("/booking/{bookingId}")
    public String booking(@PathVariable Long bookingId) {
        emailService.sendBookingConfirmation(bookingId);
        return "Booking email sent";
    }

    @GetMapping("/invoice/{invoiceId}")
    public String invoice(@PathVariable Long invoiceId) {
        emailService.sendInvoice(invoiceId);
        return "Invoice email sent";
    }
}