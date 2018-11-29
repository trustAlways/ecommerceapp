package com.medassi.ecommerce.user.Activity.Model;

public class ForLatLongitude {

    String lat;
    String lon;
    String stockiest_id;
    String available_qty;
    String product_id;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    String product_price;
    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getStockiest_id() {
        return stockiest_id;
    }

    public void setStockiest_id(String stockiest_id) {
        this.stockiest_id = stockiest_id;
    }

    public String getAvailable_qty() {
        return available_qty;
    }

    public void setAvailable_qty(String available_qty) {
        this.available_qty = available_qty;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

}
