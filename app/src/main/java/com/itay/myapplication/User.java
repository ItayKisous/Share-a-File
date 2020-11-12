package com.itay.myapplication;

public class User {
    private String Email;
    private String Passowrd;
    private String Name;

    public User(String email, String passowrd, String name) {
        Email = email;
        Passowrd = passowrd;
        Name = name;
    }

    public User() {

    }


    public String getEmail() {
        return Email;
    }

    public String getPassowrd() {
        return Passowrd;
    }

    public String getName() {
        return Name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassowrd(String passowrd) {
        Passowrd = passowrd;
    }

    public void setName(String namee) {
        Name = namee;
    }
}
