package com.iskconRavet.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject(); // usually the email or username
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	public boolean isTokenValid(String token, String username) {
		return username.equals(extractUsername(token));
	}
}
