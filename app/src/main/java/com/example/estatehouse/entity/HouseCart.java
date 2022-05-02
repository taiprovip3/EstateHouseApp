package com.example.estatehouse.entity;

public class HouseCart {
    private String documentId;
    private String email;
    private double cost;
    private String seller;
    private int bedrooms;
    private int bathrooms;
    private int livingarea;
    private String image;

    public String getDocumentId() {
        return documentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public int getLivingarea() {
        return livingarea;
    }

    public void setLivingarea(int livingarea) {
        this.livingarea = livingarea;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public HouseCart() {
    }

    public HouseCart(String documentId, String email, double cost, String seller, int bedrooms, int bathrooms, int livingarea, String image) {
        this.documentId = documentId;
        this.email = email;
        this.cost = cost;
        this.seller = seller;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.livingarea = livingarea;
        this.image = image;
    }

    @Override
    public String toString() {
        return "HouseCart{" +
                "documentId='" + documentId + '\'' +
                ", email='" + email + '\'' +
                ", cost=" + cost +
                ", seller='" + seller + '\'' +
                ", bedrooms=" + bedrooms +
                ", bathrooms=" + bathrooms +
                ", livingarea=" + livingarea +
                ", image='" + image + '\'' +
                '}';
    }
}
