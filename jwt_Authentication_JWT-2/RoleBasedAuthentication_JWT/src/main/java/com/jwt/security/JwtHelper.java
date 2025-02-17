package com.jwt.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.jwt.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtHelper {

	private String secretKey = null;

	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		return Jwts.builder().claims().add(claims).subject(user.getFullName()).issuer("Siva")
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 60 * 10 * 1000)).and().signWith(generateKey())
				.compact();
	}

	private SecretKey generateKey() {
		byte[] decode = Decoders.BASE64.decode(getSecretKey());
		return Keys.hmacShaKeyFor(decode);
	}

	public String getSecretKey() {
		return secretKey = "kjdngfkjbsdujqwnkjdyhjsbjfs52er8g5ser54g5ser25g7se3r5g36reg58632rg6strhgawe3rh2sr3gnrgbvunfwejlkwefukjd";
	}

	public String extractUserName(String jwtToken) {
		return extractClaims(jwtToken, Claims::getSubject);
	}

	private <T> T extractClaims(String jwtToken, Function<Claims, T> claimsResolver) {
		Claims claims = extractClaims(jwtToken);
		return claimsResolver.apply(claims);
	}

	private Claims extractClaims(String jwtToken) {
		return Jwts.parser().verifyWith(generateKey()).build().parseSignedClaims(jwtToken).getPayload();
	}

	public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
		final String userName = extractUserName(jwtToken);
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));
	}

	private boolean isTokenExpired(String jwtToken) {
		return extractExpiration(jwtToken).before(new Date());
	}

	private Date extractExpiration(String jwtToken) {
		return extractClaims(jwtToken, Claims::getExpiration);
	}

}
