package com.kailaselectrical.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kailaselectrical.common.ApiResponse;
import com.kailaselectrical.dto.response.DashboardResponse;
import com.kailaselectrical.service.DashboardService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Dashboard", description = "Dashboard APIs")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

	private final DashboardService dashboardService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard() {
		System.out.println("Dashboard Controller Called");
		return ResponseEntity.ok(
				ApiResponse.<DashboardResponse>builder()
				.success(true)
				.message("Dashboard fetched successfully")
				.data(dashboardService.getDashboard())
				.build());
	}
}
