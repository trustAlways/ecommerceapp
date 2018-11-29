package com.medassi.ecommerce.user.Activity.Model.for_map_stokistshow;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class For_Map_XStokistshowbean {
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("stockiest_id")
    @Expose
    private List<String> stockiestId = null;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_description")
    @Expose
    private String productDescription;
    @SerializedName("product_price")
    @Expose
    private List<String> productPrice = null;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("let")
    @Expose
    private List<String> let = null;
    @SerializedName("long")
    @Expose
    private List<String> _long = null;
    @SerializedName("available_qty")
    @Expose
    private List<String> availableQty = null;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<String> getStockiestId() {
        return stockiestId;
    }

    public void setStockiestId(List<String> stockiestId) {
        this.stockiestId = stockiestId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public List<String> getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(List<String> productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public List<String> getLet() {
        return let;
    }

    public void setLet(List<String> let) {
        this.let = let;
    }

    public List<String> getLong() {
        return _long;
    }

    public void setLong(List<String> _long) {
        this._long = _long;
    }

    public List<String> getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(List<String> availableQty) {
        this.availableQty = availableQty;
    }
}
