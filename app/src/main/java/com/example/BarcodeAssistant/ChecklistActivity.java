package com.example.BarcodeAssistant;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private int i;
    private String currentBarcode;
    private String currentProductName = "";
    private String currentProductPrice = "";
    private String currentProductBarcode = "";
    private static final String apiKeyParameter="yltm9pm9fic9k6iu625f01agn4k5e2";
    private static final String TAG = ChecklistActivity.class.getSimpleName();
    private NotificationManagerCompat notificationManager;
    private LinearLayout parentLayout;
    private TextView totalPrice;
    private float totalPriceValue = 0;
    List<Products> products = new ArrayList<Products>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        parentLayout = (LinearLayout) findViewById(R.id.linearView);
        totalPrice = (TextView) findViewById(R.id.totalPrice);

        notificationManager = NotificationManagerCompat.from(this);
        ArrayList<String> barcodeList = getIntent().getStringArrayListExtra("code");
        System.out.println("LIST SIZE" + barcodeList.size());
        if (barcodeList.size() > 0) {
            getBarcodeData(barcodeList);
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(ChecklistActivity.this).create();
            alertDialog.setTitle("INFO");
            alertDialog.setMessage("No barcodes scanned.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OKAY",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    public void getBarcodeData(ArrayList<String> barcodeList) {
        for (i = 0; i < barcodeList.size(); i++) {
            currentBarcode = barcodeList.get(i);
            createNotification(currentBarcode);
            System.out.println("BARCODE FOUND: " + barcodeList.get(i));

            // close the activity in case of empty barcode
            if (TextUtils.isEmpty(barcodeList.get(i))) {
                Toast.makeText(getApplicationContext(), "Barcode is empty!", Toast.LENGTH_LONG).show();
                finish();
            }

            ProductApiInterface apiInterface = ProductApiClient.getBarcodeApi().create(ProductApiInterface.class);
            Call<BarcodeApiResponse> call = apiInterface.getProductData(barcodeList.get(i), apiKeyParameter);
            call.enqueue(new Callback<BarcodeApiResponse>() {

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
                            alertDialog.setMessage("Barcode " + currentBarcode + " could not be found!");
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

                            try {
                                int size = products.get(0).getStores().size();
                                currentProductPrice = products.get(0).getStores().get(size - 1).getStore_price();
                            } catch (IndexOutOfBoundsException e) {
                                currentProductPrice = "NOT AVAILABLE";
                            }

                            try {
                                currentProductBarcode = products.get(0).getBarcode_number();
                            } catch (IndexOutOfBoundsException e) {
                                currentProductBarcode = "NOT AVAILABLE";
                            }

                            try {
                                currentProductName = products.get(0).getProduct_name();
                            } catch (IndexOutOfBoundsException e) {
                                currentProductName = "NOT AVAILABLE";
                            }

                            System.out.println("BARCODE: " + currentProductBarcode);
                            System.out.println("PRODUCT NAME: " + currentProductName);
                            System.out.println("PRODUCT PRICE" + currentProductPrice);

                            totalPriceValue = totalPriceValue + Float.parseFloat(currentProductPrice);

                            CheckBox checkBox = new CheckBox(ChecklistActivity.this);
                            checkBox.setId(i);
                            checkBox.setChecked(true);
                            checkBox.setText(currentProductName);

                            LinearLayout.LayoutParams checkParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            checkParams.setMargins(10, 10, 10, 10);
                            checkParams.gravity = Gravity.CENTER;

                            totalPrice.setText(String.valueOf(totalPriceValue));

                            parentLayout.addView(checkBox, checkParams);

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
