package com.example.BarcodeAssistant.Model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Product {
    String barcode;
    String productName;
    double productPrice;

    public Product() {}

    public Product(String barcode, String productName, double productPrice) {
        this.barcode = barcode;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public static class BarcodeApiResponse {
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

    public static class Products implements Serializable {
        @SerializedName("barcode_number")
        @Expose
        private String barcode_number;

        @SerializedName("product_name")
        @Expose
        private String product_name;

        @SerializedName("stores")
        @Expose
        @Nullable
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

    public static class Stores {
        @SerializedName("store_name")
        @Expose
        private String store_name;

        @SerializedName("store_price")
        @Expose
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
}
