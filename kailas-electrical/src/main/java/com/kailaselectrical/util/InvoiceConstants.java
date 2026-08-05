package com.kailaselectrical.util;

import java.util.List;

public class InvoiceConstants {
	
	private InvoiceConstants() {}
	
	public static final String DEFAULT_SORT_FIELD = "invoiceDate";
	
	public static final List<String> ALLOWED_SORT_FIELDS = List.of(
			"invoiceDate",
			"invoiceNumber",
			"totalAmount",
			"paymentStatus"
	);
}
