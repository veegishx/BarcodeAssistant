package com.example.BarcodeAssistant;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.BarcodeAssistant.Model.BarcodeApiResponse;
import com.example.BarcodeAssistant.Model.Products;
import com.example.BarcodeAssistant.api.ProductApiClient;
import com.example.BarcodeAssistant.api.ProductApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChecklistActivity extends AppCompatActivity {
    private static final String apiKeyParameter="API KEY HERE";
    private static final String TAG = ChecklistActivity.class.getSimpleName();
    List<Products> products = new ArrayList<Products>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        final String barcode = getIntent().getStringExtra("code");

        System.out.println("BARCODE FOUND: " + barcode);

        // close the activity in case of empty barcode
        if (TextUtils.isEmpty(barcode)) {
            Toast.makeText(getApplicationContext(), "Barcode is empty!", Toast.LENGTH_LONG).show();
            finish();
        }

        ProductApiInterface apiInterface = ProductApiClient.getBarcodeApi().create(ProductApiInterface.class);

        Call<BarcodeApiResponse> call = apiInterface.getProductData(barcode, apiKeyParameter);
        System.out.println("--- URL DEBUG INFO ---");

        System.out.println(call.request().url());

        call.enqueue(new Callback<BarcodeApiResponse>() {
            class TAG {
            }

            @Override
            public void onResponse(Call<BarcodeApiResponse> call, Response<BarcodeApiResponse> response) {
                System.out.println("-------------- API DEBUG INFO --------------");
                System.out.println(response.body().getProducts());
                int statusCode = response.code();
                System.out.println(call.request().url());
                System.out.println(response.code());

                products = response.body().getProducts();

                System.out.println(products.get(0).getProduct_name());
                AlertDialog alertDialog = new AlertDialog.Builder(ChecklistActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Barcode found: " + barcode + "for product" + products.get(0).getProduct_name());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "ERROR",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }

            @Override
            public void onFailure(Call<BarcodeApiResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                AlertDialog alertDialog = new AlertDialog.Builder(ChecklistActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Barcode not found: " + barcode);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "ERROR",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }
}
