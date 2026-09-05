package com.kailaselectrical.dto.request;

import com.kailaselectrical.enums.PaymentStatus;

import jakarta.validation.constraints.NotNull;
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
