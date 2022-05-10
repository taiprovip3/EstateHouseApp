package com.example.estatehouse.entity;

public class Notic {
    private String documentId;
    private String image;
    private String message;
    private int number;
    private String email;

    @Override
    public String toString() {
        return "Notic{" +
                "documentId='" + documentId + '\'' +
                ", image='" + image + '\'' +
                ", message='" + message + '\'' +
                ", number=" + number +
                ", email='" + email + '\'' +
                '}';
    }

    public Notic(String documentId, String image, String message, int number, String email) {
        this.documentId = documentId;
        this.image = image;
        this.message = message;
        this.number = number;
        this.email = email;
    }

    public Notic() {
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
