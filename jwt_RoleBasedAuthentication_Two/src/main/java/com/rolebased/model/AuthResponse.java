package com.rolebased.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AuthResponse {
	@JsonProperty("User_Name")
	private String userName;
	
	@JsonProperty("access_token")
	private String jwtToken;
	
	@JsonProperty("refresh_token")
	private String refreshToken;
}
