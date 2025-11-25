package com.example.thakur_nursery.models;

import java.util.List;

public class MyCartModel {
    String currentTime;
    String currentDate;
    String productName;
    String productPrice;
    String totalQuantity;
    int totalPrice;
    String documentId;
    private int quantity;
    private double price; // Price of a single unit

    private List<String> productNames;
    public MyCartModel() {
    }

    public MyCartModel(String currentTime, String currentDate, String productName, String productPrice, String totalQuantity, int totalPrice, String img_url, String product_image, List<String> productNames, int quantity, double price) {
        this.currentTime = currentTime;
        this.currentDate = currentDate;
        this.productName = productName;
        this.productPrice = productPrice;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
//        this.img_url = img_url;
//        this.product_image = product_image;
        this.productNames = productNames;
        this.quantity = quantity;
        this.price = price;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

//    public String getImg_url() {
//        return img_url;
//    }
//
//    public void setImg_url(String img_url) {
//        this.img_url = img_url;
//    }
//
//    public String getProduct_image() {
//        return product_image;
//    }
//
//    public void setProduct_image(String product_image) {
//        this.product_image = product_image;
//

    public int getQuantity() {
        return quantity;
    }

    public List<String> getProductNames() {
        return productNames;
    }
}