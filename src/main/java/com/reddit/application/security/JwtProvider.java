package com.reddit.application.security;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.time.Instant;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.reddit.application.common.CommonUtils;
import com.reddit.application.exception.SpringRedditException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
public class JwtProvider {

	@Autowired
	CommonUtils commonUtils;
	
	private KeyStore keyStore;
	

	@Value("${jwt.expiration.time}")
	private Long jwtExpirationInMillis;

	@PostConstruct
	public void init() {
		try {
			keyStore = KeyStore.getInstance(commonUtils.getProperty("keystore.instance"));
			InputStream resourceAsStream = getClass()
					.getResourceAsStream(commonUtils.getProperty("keystore.file.name"));
			keyStore.load(resourceAsStream, commonUtils.getProperty("keystore.password").toCharArray());

		} catch (Exception e) {
			// TODO: handle exception
			throw new SpringRedditException("Exception occured while loading keystore!");
		}
	}

	public String generateToken(Authentication authentication) {
		org.springframework.security.core.userdetails.User principal = (User) authentication.getPrincipal();
		return Jwts.builder().setSubject(principal.getUsername()).setIssuedAt(Date.from(Instant.now()))
				.signWith(privateKey()).setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
				.compact();
	}

	private PrivateKey privateKey() {
		// TODO Auto-generated method stub
		try {
			return (PrivateKey) keyStore.getKey(commonUtils.getProperty("keystore.alias"),
					commonUtils.getProperty("keystore.password").toCharArray());
		} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			throw new SpringRedditException("Exception occured while retrieving private key from keystore!");
		}

	}

	@SuppressWarnings("deprecation")
	public boolean validateToken(String jwt) {
		Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
		return true;
	}

	private PublicKey getPublicKey() {
		// TODO Auto-generated method stub

		try {
			return keyStore.getCertificate(commonUtils.getProperty("keystore.alias")).getPublicKey();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			throw new SpringRedditException("Exception occured while retriving public key from keystore");
		}
	}

	public String getUsernameFromJwt(String token) {
		Claims claims = Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token).getBody();

		return claims.getSubject();
	}

	public Long getJwtExpirationInMillis() {
		return jwtExpirationInMillis;
	}

	public String generateTokenWithUserName(String userName) {
		// TODO Auto-generated method stub
		return Jwts.builder().setSubject(userName)
				.setIssuedAt(Date.from(Instant.now()))
				.signWith(privateKey())
				.setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
				.compact();
	}

}
