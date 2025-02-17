package com.rolebased.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.rolebased.entity.Person;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private String secretKey = null;

	public String generateToken(Person person) {
		Long time = (long) (60000*60);
		return accessToken(person,time);
	}
	
	private String accessToken(Person person,long time) {
		Map<String, Object> claims = new HashMap<>();
		return Jwts.builder().claims().add(claims).subject(person.getEmail()).issuer("Siva")
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + time)).and().signWith(generateKey())
				.compact();
	}
	public  String refreshToken(Person person) {
		Long time = (long) (60000*180);
		return accessToken(person,time);
	}

	private SecretKey generateKey() {
		byte[] decode = Decoders.BASE64.decode(getSecretKey());
		return Keys.hmacShaKeyFor(decode);
	}

	private String getSecretKey() {
		secretKey = "njskjdnfjadflbasjdbhflksbdfgs5df7gs54df58g7s4df786sd5fg78sv96dfr5g4av87sda56gvdf6g6aergfbqruijhstyqghwsa";
		return secretKey;
	}

	public String extractUserName(String jwtToken) {
		return extractClaims(jwtToken, Claims::getSubject);
	}

	private <T> T extractClaims(String jwtToken, Function<Claims, T> claimResolver) {
		Claims claims = extractClaims(jwtToken);
		return claimResolver.apply(claims);
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
