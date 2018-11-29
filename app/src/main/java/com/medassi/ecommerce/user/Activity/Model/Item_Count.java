package com.medassi.ecommerce.user.Activity.Model;

public class Item_Count {

    String product_id,product_sku,product_name,product_price,product_quantiti,description,distributor_name,
     product_price1,
     stockiest, product_image;

    public Item_Count(String product_id, String product_sku, String product_name, String product_price, String product_quantiti,
                      String description, String distributor_name, String product_price1, String stockiest, String product_image) {
        this.product_id = product_id;
        this.product_sku = product_sku;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_quantiti = product_quantiti;
        this.description = description;
        this.distributor_name = distributor_name;
        this.product_price1 = product_price1;
        this.stockiest = stockiest;
        this.product_image = product_image;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_sku() {
        return product_sku;
    }

    public void setProduct_sku(String product_sku) {
        this.product_sku = product_sku;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_quantiti() {
        return product_quantiti;
    }

    public void setProduct_quantiti(String product_quantiti) {
        this.product_quantiti = product_quantiti;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDistributor_name() {
        return distributor_name;
    }

    public void setDistributor_name(String distributor_name) {
        this.distributor_name = distributor_name;
    }

    public String getProduct_price1() {
        return product_price1;
    }

    public void setProduct_price1(String product_price1) {
        this.product_price1 = product_price1;
    }

    public String getStockiest() {
        return stockiest;
    }

    public void setStockiest(String stockiest) {
        this.stockiest = stockiest;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    /* public Item_Count(String product_id, String product_sku,
                      String product_name, String product_price,
                      String product_quantiti, String description,
                      String distributor_name, String product_price1,
                      String stockiest, String product_image) {


    }*/
}
