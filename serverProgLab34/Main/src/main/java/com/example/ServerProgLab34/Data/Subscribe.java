package com.example.ServerProgLab34.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Subscribe(
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    String email)
{

}
