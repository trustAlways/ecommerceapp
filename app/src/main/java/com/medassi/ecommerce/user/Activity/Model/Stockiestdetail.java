package com.medassi.ecommerce.user.Activity.Model;

public class Stockiestdetail
{
    String Stockiest_id,Retailer_Name,stockiest_distance,stockiest_qty,stockiest_price,stockiest_type;

    public Stockiestdetail(String stockiest_id, String stockiest_retailer_name, String stockiest_distance,
                           String stockiest_product_qty, String stockiest_product_price, String stockiest_type) {
        Stockiest_id = stockiest_id;
        Retailer_Name = stockiest_retailer_name;
        this.stockiest_distance = stockiest_distance;
        this.stockiest_qty = stockiest_product_qty;
        this.stockiest_price = stockiest_product_price;
        this.stockiest_type = stockiest_type;
    }

    public String getStockiest_distance() {
        return stockiest_distance;
    }

    public void setStockiest_distance(String stockiest_distance) {
        this.stockiest_distance = stockiest_distance;
    }

    public String getStockiest_qty() {
        return stockiest_qty;
    }

    public void setStockiest_qty(String stockiest_qty) {
        this.stockiest_qty = stockiest_qty;
    }

    public String getStockiest_price() {
        return stockiest_price;
    }

    public void setStockiest_price(String stockiest_price) {
        this.stockiest_price = stockiest_price;
    }

    public String getStockiest_id() {
        return Stockiest_id;
    }

    public void setStockiest_id(String stockiest_id) {
        Stockiest_id = stockiest_id;
    }

    public String getRetailer_Name() {
        return Retailer_Name;
    }

    public void setRetailer_Name(String retailer_Name) {
        Retailer_Name = retailer_Name;
    }

    public String getStockiest_type() {
        return stockiest_type;
    }

    public void setStockiest_type(String stockiest_type) {
        this.stockiest_type = stockiest_type;
    }
}
