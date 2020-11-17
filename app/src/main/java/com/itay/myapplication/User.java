package com.itay.myapplication;

public class User {
    private String Email;
    private String Passowrd;
    private String Name;
    private String FirstName;
    private String Surname;

    public User(String email, String passowrd, String name, String firstName, String surname) {
        Email = email;
        Passowrd = passowrd;
        Name = name;
        FirstName=firstName;
        Surname=surname;
    }

    public User() {

    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassowrd() {
        return Passowrd;
    }

    public void setPassowrd(String passowrd) {
        Passowrd = passowrd;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }
}
