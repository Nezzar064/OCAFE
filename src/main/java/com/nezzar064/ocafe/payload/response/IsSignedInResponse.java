package com.nezzar064.ocafe.payload.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class IsSignedInResponse {

    private long id;
    private String username;
    private String email;
    List<String> roles;

    public IsSignedInResponse(long id, String username, String email, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
