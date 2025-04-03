package com.example.ServerProgLab34.Data;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ContactData{

    @Size(min = 3,max = 50,message = "incorrect name, length must be less 50  and more 3")
    private String name;
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.edu$",message = "incorrect email, not compare \"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.edu$\" ")
    private String email;
    private String subject;
    @Size(min = 3,max = 50,message = "incorrect message, length must be less 50  and more 3")
    private String msg;


    public ContactData(String name, String email, String subject, String msg) {
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.msg = msg;
    }
    public ContactData() {
        this.name = "";
        this.email = "";
        this.subject = "";
        this.msg = "";
    }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
}
