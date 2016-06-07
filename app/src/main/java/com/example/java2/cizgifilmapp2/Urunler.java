package com.example.java2.cizgifilmapp2;

public class Urunler {

    private String productName,imgUrl;

    public Urunler(String productName, String imgUrl) {
        this.productName = productName;
        this.imgUrl = imgUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
