// src/main/java/org/jnu/unimart/payload/SignupRequest.java

package org.jnu.unimart.payload;

import jakarta.validation.constraints.NotBlank;

public class SignupRequest {

    @NotBlank
    private String account;

    @NotBlank
    private String username;
    
    @NotBlank
    private String password;

    public @NotBlank String getAccount() {
        return account;
    }

    public void setAccount(@NotBlank String account) {
        this.account = account;
    }

    public @NotBlank String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }
}
