package com.kailaselectrical.util;

import java.util.Set;

public final class CustomerConstants {
	
	private CustomerConstants() {}
	
	public static final String DEFAULT_SORT_FIELD = "fullName";
	
	public static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
			"fullName",
			"phoneNumber",
			"email",
			"createdAt",
			"upatedAt"
	);
}
