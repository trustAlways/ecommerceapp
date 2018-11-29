package com.medassi.ecommerce.user.Activity.Model;

public class Search {
    String product_id,product_name,price,stockiest_id ,product_image , lat,lang,product_desc,available_qty;


    public Search(String name, String id, String available_qty, String price, String
            stockiest_id, String product_image, String longitude, String latitude, String description) {

        this.product_id = id;
        this.product_name = name;
        this.price = price;
        this.stockiest_id = stockiest_id;
        this.product_image = product_image;
        this.lat = latitude;
        this.lang = longitude;
        this.product_desc = description;
        this.available_qty = available_qty;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStockiest_id() {
        return stockiest_id;
    }

    public void setStockiest_id(String stockiest_id) {
        this.stockiest_id = stockiest_id;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getAvailable_qty() {
        return available_qty;
    }

    public void setAvailable_qty(String available_qty) {
        this.available_qty = available_qty;
    }
}
