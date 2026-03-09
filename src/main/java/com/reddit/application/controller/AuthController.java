package com.reddit.application.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reddit.application.common.CommonUtils;
import com.reddit.application.dto.AuthenticationResponse;
import com.reddit.application.dto.LoginRequest;
import com.reddit.application.dto.RefreshTokenRequest;
import com.reddit.application.dto.RegisterRequest;
import com.reddit.application.exception.SpringRedditException;
import com.reddit.application.service.AuthService;
import com.reddit.application.service.RefreshTokenService;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
	
	@Autowired
	AuthService authService;
	
	@Autowired
	RefreshTokenService refreshTokenService;
	
	@PostMapping("/signup")
	public ResponseEntity<Object> signUp(@Valid @RequestBody RegisterRequest registerRequest) {
		String response = "";
		log.info("In User SignUp Controller: "+registerRequest);
		try {
			response = authService.signup(registerRequest);
		return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(Exception e) {
			log.error("Exception Occured : "+CommonUtils.getStackTrace(e));
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}
	
	@GetMapping("/accountVerification/{token}")
	public ResponseEntity<Object> verifyAccount(@PathVariable String token){
		
		try {
		authService.verifyToken(token);
		}catch(SpringRedditException se){
			return new ResponseEntity<Object>(se.getMessage(),HttpStatus.OK);
		}
		return new ResponseEntity<Object>("Account activated succesfully",HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public AuthenticationResponse login(@Valid @RequestBody LoginRequest loginRequest) {
		return authService.login(loginRequest);
		
	}
	
	@PostMapping("refresh/token")
	public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
			return authService.refreshToken(refreshTokenRequest);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Object> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
		refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
		return new ResponseEntity<Object>("Refresh token deleted successfully", HttpStatus.OK);
	}
}
