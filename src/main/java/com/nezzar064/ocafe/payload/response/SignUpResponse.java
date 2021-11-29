package com.nezzar064.ocafe.payload.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SignUpResponse {
	private String message;

	public SignUpResponse(String message) {
		this.message = message;
	}
}