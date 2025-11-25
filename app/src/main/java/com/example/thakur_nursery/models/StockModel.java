package com.example.thakur_nursery.models;

import java.io.Serializable;

public class StockModel implements Serializable {
    private String name;
    private int stock;
    private String img_url;

    public StockModel() {
    }

    public StockModel(String name, int stock, String img_url) {
        this.name = name;
        this.stock = stock;
        this.img_url = img_url;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getimg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
