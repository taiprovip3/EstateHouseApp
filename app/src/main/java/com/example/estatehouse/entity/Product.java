package com.example.estatehouse.entity;

public class Product {
    private String documentId;
    private String name;
    private double price;
    private String image;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Product(String documentId, String name, double price, String image) {
        this.documentId = documentId;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public Product() {
    }

    @Override
    public String toString() {
        return "Product{" +
                "documentId='" + documentId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                '}';
    }
}
