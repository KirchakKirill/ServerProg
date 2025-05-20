package com.example.Greenswamp.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class Subscribe{
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    public Subscribe(){
        this.email="";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
