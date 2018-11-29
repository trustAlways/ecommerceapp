package com.medassi.ecommerce.user.Activity.Model.for_map_stokistshow;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class For_Map_Stokistshowbean {
    @SerializedName("result")
    @Expose
    private List<For_Map_XStokistshowbean> result = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("base_url")
    @Expose
    private String baseUrl;

    public List<For_Map_XStokistshowbean> getResult() {
        return result;
    }

    public void setResult(List<For_Map_XStokistshowbean> result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
