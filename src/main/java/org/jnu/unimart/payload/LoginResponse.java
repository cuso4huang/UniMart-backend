package org.jnu.unimart.payload;

public class LoginResponse {
    private String token;
    private String tokenType = "Bearer";

    public LoginResponse(String token) {
        this.token = token;
    }

    // Getters and Setters

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }
}