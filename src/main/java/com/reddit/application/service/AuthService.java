package com.reddit.application.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reddit.application.common.CommonUtils;
import com.reddit.application.dto.AuthenticationResponse;
import com.reddit.application.dto.LoginRequest;
import com.reddit.application.dto.RefreshTokenRequest;
import com.reddit.application.dto.RegisterRequest;
import com.reddit.application.exception.SpringRedditException;
import com.reddit.application.model.NotificationEmail;
import com.reddit.application.model.User;
import com.reddit.application.model.VerificationToken;
import com.reddit.application.repository.UserRepository;
import com.reddit.application.repository.VerificationTokenRepository;
import com.reddit.application.security.JwtProvider;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {


	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailService mailService;
	private final MessageSource messageSource;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	private final RefreshTokenService refreshTokenService;
	
	@Transactional
	public String signup(RegisterRequest registerRequest) {
		
		String message = "";
		try {
		User user = new User();
		
		boolean exist = userRepository.existsUserByUserName(registerRequest.getUsername());
		
		if(exist) {
			message = messageSource.getMessage("username.already.exist", null, null);
			log.info("Username Already Exist : "+registerRequest.getUsername());
			return message;
		}
		user.setUserName(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setCreateDate(Instant.now());
		user.setEnabled(false);
		userRepository.save(user);

		String token = generateVerificationToken(user);
		
		mailService.sendMail(new NotificationEmail(messageSource.getMessage("activate.account.msg", null, null),
				user.getEmail(), messageSource.getMessage("activate.email.msg", new Object[] { user.getUserName(),token }, null)));
		message = messageSource.getMessage("user.register.success", null, null);
		}catch(Exception e) {
			log.info("Exception occure during registration : "+CommonUtils.getStackTrace(e));
		}
		return message;
	}

	private String generateVerificationToken(User user) {
		// TODO Auto-generated method stub
		String token = UUID.randomUUID().toString().replace("-", "");
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationTokenRepository.save(verificationToken);
		return token;
	}

	public void verifyToken(String token) {
		// TODO Auto-generated method stub
		Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
		verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token!"));
		fetchUserAndEnable(verificationToken.get());
	}

	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		// TODO Auto-generated method stub
		String username = verificationToken.getUser().getUserName();
		User user = userRepository.findByUserName(username)
				.orElseThrow(() -> new SpringRedditException("User not found with username : " + username));
		user.setEnabled(true);
		userRepository.save(user);
	}

	public AuthenticationResponse login(LoginRequest loginRequest) {
		// TODO Auto-generated method stub
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtProvider.generateToken(authentication);
		return AuthenticationResponse.builder()
				.authenticationToken(token)
				.refreshToken(refreshTokenService.generateRefreshToken().getToken())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				.username(loginRequest.getUsername())
				.build();
	}

	@Transactional(readOnly = true)
	public User getCurrentUser() {
		org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		return userRepository.findByUserName(principal.getUsername())
				.orElseThrow(() -> new SpringRedditException("User name not found - " + principal.getUsername()));
	}

	public AuthenticationResponse refreshToken(@Valid RefreshTokenRequest refreshTokenRequest) {
		// TODO Auto-generated method stub
		refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUserName());
		return AuthenticationResponse.builder()
				.authenticationToken(token)
				.refreshToken(refreshTokenRequest.getRefreshToken())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				.username(refreshTokenRequest.getUserName())
				.build();
	}

}
