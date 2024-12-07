// src/main/java/org/jnu/unimart/payload/JwtResponse.java

package org.jnu.unimart.payload;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private int id;
    private String account;

    public JwtResponse(String accessToken, int id, String account) {
        this.token = accessToken;
        this.id = id;
        this.account = account;
    }

    // Getters

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }
}
