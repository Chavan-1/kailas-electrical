package com.kailaselectrical.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kailaselectrical.dto.request.ForgotPasswordRequest;
import com.kailaselectrical.dto.request.ResetPasswordRequest;
import com.kailaselectrical.entity.PasswordResetToken;
import com.kailaselectrical.entity.User;
import com.kailaselectrical.respository.PasswordResetTokenRepository;
import com.kailaselectrical.respository.UserRepository;
import com.kailaselectrical.service.EmailService;
import com.kailaselectrical.service.PasswordResetService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

	private final UserRepository userRepository;
	private final PasswordResetTokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;
	
	@Override
	@Transactional
	public void forgotPassword(ForgotPasswordRequest request) {
		
		User user = userRepository.findByEmail(request.getEmail()).orElse(null);
		
		if (user == null) return;
		
		tokenRepository.deleteByUserId(user.getId());
		
		String rawToken = generateToken();
		String tokenHash = hash(rawToken);
		
		PasswordResetToken resetToken = new PasswordResetToken();
		
		resetToken.setUser(user);
		resetToken.setTokenHash(tokenHash);
		resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));
		resetToken.setUsed(false);
		
		tokenRepository.save(resetToken);
		
		String resetLink = "http://localhost:5173/reset-password?token=" + rawToken;
		
		emailService.sendPasswordResetEmail(
                user.getEmail(),
                user.getFullName(),
                resetLink
        );
	}
	
	@Override
	@Transactional
	public void resetPassword(ResetPasswordRequest request) {
		
		if (!request.getNewPassword().equals(request.getConfirmPassword())) {
			
			throw new IllegalArgumentException("New password and confirm password do not match.");
		}
		
		String tokenHash = hash(request.getToken());
		
		PasswordResetToken resetToken =
				tokenRepository
                        .findByTokenHashAndUsedFalse(tokenHash)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid or expired password reset link."
                                )
                        );
		if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
			
			throw new IllegalArgumentException("Password reset link has expired.");
		}
		
		User user = resetToken.getUser();
		
		if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
			
			throw new IllegalArgumentException("New Password cannot be the same as the current password.");
		}
		
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		
		userRepository.save(user);
		
		resetToken.setUsed(true);
		
		tokenRepository.save(resetToken);
	}
	
	private String generateToken() {
		
		byte[]  bytes = new byte[32];
		
		new SecureRandom().nextBytes(bytes);
		
		return Base64.getUrlEncoder()
				.withoutPadding()
				.encodeToString(bytes);
	}
	
	private String hash(String value) {
		
		try {
			
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			
			byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
			
			return HexFormat.of().formatHex(hash);
			
		} catch (NoSuchAlgorithmException e) {
			
			throw new IllegalStateException("Unable to hash password reset token", e);
		}
	}
	
}
	
	
	
	
