package com.medassi.ecommerce.user.Activity.Model;

public class order_history_detail {
   String order_id,order_number,order_date,order_status,payment_mode,shipping_full_name,shipping_phone_number,
    shipping_address_one,shipping_address_two,billing_address,sub_total,total;

    public order_history_detail(String order_id, String order_number, String order_date, String order_status,
                                String payment_mode, String shipping_full_name, String shipping_phone_number,
                                String shipping_address_one, String shipping_address_two, String billing_address,
                                String sub_total, String total) {
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
        this.sub_total = sub_total;
        this.total = total;
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
}
