package com.example.Greenswamp.Entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @ManyToMany(mappedBy = "authority")
    List<User> users = new ArrayList<>();

    @Column(name = "authority")
    @Enumerated(EnumType.STRING)
    private AuthorityType authorityType;

    public Authority(){
        this.authorityType = AuthorityType.USER;
    }

    public Authority(Long id, List<User> users, AuthorityType authorityType){
        this.authorityType  = authorityType;
        this.id= id;
        this.users = users;
    }

    public Authority(AuthorityType authorityType){
        this.authorityType = authorityType;
    }



    public enum AuthorityType
    {
        USER, ADMIN, MODERATOR
    }

    public AuthorityType getAuthorityType() {
        return authorityType;
    }

    public List<User> getUsers() {
        return users;
    }

    public Long getId() {
        return id;
    }

    public void setAuthorityType(AuthorityType authorityType) {
        this.authorityType = authorityType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
