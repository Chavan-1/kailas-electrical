package com.kailaselectrical.util;

import java.util.Set;

public final class ServiceConstants {
	
	private ServiceConstants() {}
	
	public static final String DEFAULT_SORT_FIELD = "id";
	
	public static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
			"id",
			"basePrice",
			"estimatedDuration",
			"active",
			"createdAt"
	);
}
