package com.medassi.ecommerce.user.Activity.Model;

public class product {
    String name,id,stockiest_id,lat,lang;

    public product(String name, String id, String stockiest_id, String longitude, String latitude) {
        this.name = name;
        this.id = id;
        this.lang = longitude;
        this.lat = latitude;
        this.stockiest_id = stockiest_id;
    }

    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStockiest_id() {
        return stockiest_id;
    }

    public void setStockiest_id(String stockiest_id) {
        this.stockiest_id = stockiest_id;
    }
}
