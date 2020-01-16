package com.example.BarcodeAssistant;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

import static com.example.BarcodeAssistant.App.CHANNEL_1_ID;

public class ChecklistActivity extends AppCompatActivity {
    private static final String apiKeyParameter="yltm9pm9fic9k6iu625f01agn4k5e2";
    private static final String TAG = ChecklistActivity.class.getSimpleName();
    private NotificationManagerCompat notificationManager;
    List<Products> products = new ArrayList<Products>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        notificationManager = NotificationManagerCompat.from(this);
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
            boolean barcodeMissing = false;

            @Override
            public void onResponse(Call<BarcodeApiResponse> call, Response<BarcodeApiResponse> response) {
                System.out.println("-------------- API DEBUG INFO --------------");
                int statusCode = response.code();
                System.out.println("REQUEST URL: " + call.request().url());
                System.out.println("STATUS CODE: " +response.code());
                AlertDialog alertDialog = new AlertDialog.Builder(ChecklistActivity.this).create();
                switch (statusCode) {
                    case 404 :
                        alertDialog.setTitle("ERROR");
                        alertDialog.setMessage("Barcode " + barcode + " could not be found!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OKAY",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        break;
                    case 429 :
                        alertDialog.setTitle("ERROR");
                        alertDialog.setMessage("API LIMIT REACHED! Upgrade your plan to continue.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OKAY",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        break;
                        default:
                            System.out.println(response.body().getProducts());
                            products = response.body().getProducts();
                            System.out.println(products.get(0).getProduct_name());
                            createNotification(barcode);
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Barcode found: " + barcode + "for product" + products.get(0).getProduct_name());
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OKAY",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                            break;

                }
            }

            @Override
            public void onFailure(Call<BarcodeApiResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                AlertDialog alertDialog = new AlertDialog.Builder(ChecklistActivity.this).create();
                alertDialog.setTitle("ERROR");
                alertDialog.setMessage("A connection error occured!");
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

    public void createNotification(String barcode) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_center_focus_weak_blue_24dp)
                .setContentTitle("New Barcode Scanned")
                .setContentText(barcode)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }
}
