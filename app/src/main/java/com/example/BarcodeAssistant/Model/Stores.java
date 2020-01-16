package com.example.BarcodeAssistant.Model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stores {
    @SerializedName("store_name")
    @Expose
    @Nullable
    private String store_name;

    @SerializedName("store_price")
    @Expose
    @Nullable
    private String store_price;

    public Stores() {}

    public Stores(String store_name, String store_price) {
        this.store_name = store_name;
        this.store_price = store_price;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_price() {
        return store_price;
    }

    public void setStore_price(String store_price) {
        this.store_price = store_price;
    }
}
