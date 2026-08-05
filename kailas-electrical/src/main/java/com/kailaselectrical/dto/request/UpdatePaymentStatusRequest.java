package com.kailaselectrical.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import com.kailaselectrical.enums.PaymentStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePaymentStatusRequest {
	
	@NotNull
	private PaymentStatus paymentStatus;
}
