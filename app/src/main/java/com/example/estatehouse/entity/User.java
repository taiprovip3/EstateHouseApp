package com.example.estatehouse.entity;

public class User {
    private String documentId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String location;
    private String phonenumber;
    private String password;
    private double balance;
    private String avatar;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public User() {
        this.firstName = "un";
        this.lastName = "know";
        this.role = "member";
        this.location = "US";
        this.phonenumber = "";
        this.balance = 0.0;
        this.avatar = "image_6.png";
    }

    public User(String documentId, String email, String firstName, String lastName, String role, String location, String phonenumber, String password, double balance, String avatar) {
        this.documentId = documentId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.location = location;
        this.phonenumber = phonenumber;
        this.password = password;
        this.balance = balance;
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "documentId='" + documentId + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role='" + role + '\'' +
                ", location='" + location + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
