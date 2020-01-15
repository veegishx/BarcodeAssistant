package com.example.BarcodeAssistant.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BarcodeApiResponse {
    @SerializedName("products")
    @Expose
    private List<Products> products;

    public BarcodeApiResponse() {}

    public BarcodeApiResponse(List<Products> products) {
        this.products = products;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }
}

