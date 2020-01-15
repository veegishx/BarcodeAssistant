package com.example.BarcodeAssistant.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Products implements Serializable {
    @SerializedName("barcode_number")
    @Expose
    private String barcode_number;

    @SerializedName("product_name")
    @Expose
    private String product_name;

    @SerializedName("stores")
    @Expose
    private List<Stores> stores;

    public Products() {}

    public Products(String barcode_number, String product_name, List<Stores> stores) {
        this.barcode_number = barcode_number;
        this.product_name = product_name;
        this.stores = stores;
    }

    public String getBarcode_number() {
        return barcode_number;
    }

    public void setBarcode_number(String barcode_number) {
        this.barcode_number = barcode_number;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public List<Stores> getStores() {
        return stores;
    }

    public void setStores(List<Stores> stores) {
        this.stores = stores;
    }
}
