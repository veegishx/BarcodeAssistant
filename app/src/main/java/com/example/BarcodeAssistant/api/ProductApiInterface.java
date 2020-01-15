package com.example.BarcodeAssistant.api;

import com.example.BarcodeAssistant.Model.BarcodeApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductApiInterface {
    @GET("products")
    Call<BarcodeApiResponse> getProductData(
            @Query("barcode") String barcode,
            @Query("key") String key
    );
}
