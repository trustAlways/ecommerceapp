package com.medassi.ecommerce.user.Activity.Model;

public class CartList {

    String name, id,product_id, price, stockiest_id, product_image,qty,
            product_desc,cart_id,product_Sku,available_qty;



    public CartList(String cart_id, String product_id, String stockies_id, String
            product_name, String product_price, String product_image, String product_quantity, String product_desc,
                    String product_sku, String available_qty) {
        this.cart_id = cart_id;
        this.product_id = product_id;
        this.stockiest_id = stockies_id;
        this.name = product_name;
        this.price = product_price;
        this.product_image = product_image;
        this.qty = product_quantity;
        this.product_desc = product_desc;
        this.product_Sku = product_sku;
        this.available_qty = available_qty;
    }

    public String getAvailable_qty() {
        return available_qty;
    }

    public void setAvailable_qty(String available_qty) {
        this.available_qty = available_qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct_Sku() {
        return product_Sku;
    }

    public void setProduct_Sku(String product_Sku) {
        this.product_Sku = product_Sku;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }
}
