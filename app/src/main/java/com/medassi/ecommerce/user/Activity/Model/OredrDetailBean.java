package com.medassi.ecommerce.user.Activity.Model;

public class OredrDetailBean {
    String order_id,order_number,order_date,order_status,payment_mode,shipping_full_name,shipping_phone_number,
            shipping_address_one,shipping_address_two,billing_address,sub_total,total,prodct_qty,price,p_id,s_id,order_p_id,
            prdct_name,product_sku,billing_address2,image_url;

    public OredrDetailBean(String order_id, String order_number, String order_date, String order_status,
                           String payment_mode, String total, String prodct_qty, String price, String p_id,
                           String shipping_full_name, String sub_total, String order_p_id, String s_id,
                           String prdct_name, String billing_address, String shipping_address_one,
                           String shipping_address_two, String product_sku, String shipping_phone_number,
                           String shippingPhoneNumber, String image_url) {


        this.order_id = order_id;
        this.order_number = order_number;
        this.order_date = order_date;
        this.order_status = order_status;
        this.payment_mode = payment_mode;
        this.shipping_full_name = shipping_full_name;
        this.shipping_phone_number = shipping_phone_number;
        this.shipping_address_one = shipping_address_one;
        this.shipping_address_two = shipping_address_two;
        this.billing_address = billing_address;
        this.billing_address2 = shippingPhoneNumber;
        this.sub_total = sub_total;
        this.total = total;
        this.prodct_qty = prodct_qty;
        this.price = price;
        this.p_id = p_id;
        this.s_id = s_id;
        this.order_p_id = order_p_id;
        this.prdct_name = prdct_name;
        this.product_sku = product_sku;
        this.image_url = image_url;

    }

    public String getProduct_sku() {
        return product_sku;
    }

    public void setProduct_sku(String product_sku) {
        this.product_sku = product_sku;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getShipping_full_name() {
        return shipping_full_name;
    }

    public void setShipping_full_name(String shipping_full_name) {
        this.shipping_full_name = shipping_full_name;
    }

    public String getShipping_phone_number() {
        return shipping_phone_number;
    }

    public void setShipping_phone_number(String shipping_phone_number) {
        this.shipping_phone_number = shipping_phone_number;
    }

    public String getBilling_address2() {
        return billing_address2;
    }

    public void setBilling_address2(String billing_address2) {
        this.billing_address2 = billing_address2;
    }

    public String getShipping_address_one() {
        return shipping_address_one;
    }

    public void setShipping_address_one(String shipping_address_one) {
        this.shipping_address_one = shipping_address_one;
    }

    public String getShipping_address_two() {
        return shipping_address_two;
    }

    public void setShipping_address_two(String shipping_address_two) {
        this.shipping_address_two = shipping_address_two;
    }

    public String getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(String billing_address) {
        this.billing_address = billing_address;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getProdct_qty() {
        return prodct_qty;
    }

    public void setProdct_qty(String prodct_qty) {
        this.prodct_qty = prodct_qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getOrder_p_id() {
        return order_p_id;
    }

    public void setOrder_p_id(String order_p_id) {
        this.order_p_id = order_p_id;
    }

    public String getPrdct_name() {
        return prdct_name;
    }

    public void setPrdct_name(String prdct_name) {
        this.prdct_name = prdct_name;
    }



    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
