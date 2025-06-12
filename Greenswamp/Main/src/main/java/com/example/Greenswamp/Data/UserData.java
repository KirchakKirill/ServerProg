package com.example.Greenswamp.Data;

import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.*;

public class UserData {


    @Size(min = 3,max = 50,message = "incorrect name")
    private String username;

    @Size(min = 3,max = 50,message = "incorrect displayName")
    private String displayName;


    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 255, message = "incorrect range")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9])[\\S]{8,255}$",
            message = "Пароль должен содержать: 1 заглавную букву, 1 строчную, 1 цифру и 1 спецсимвол (!@#$%^&*)"
    )
    private String password;

    @NotNull
    @NotBlank(message = "Email не может быть пустым")
    @Email(regexp = "^[A-Za-z0-9._%-]+@(mail\\.ru|gmail\\.com|yandex\\.ru)$",message = "incorrect email")
    private String email;

    public  UserData (){
        this.username="";
        this.email = "";
        this.password = "";
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return "@"+displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
