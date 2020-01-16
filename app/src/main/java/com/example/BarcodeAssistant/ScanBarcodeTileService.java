package com.example.BarcodeAssistant;

import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.TileService;
import android.util.Log;

import androidx.annotation.RequiresApi;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ScanBarcodeTileService extends TileService {
    public final String LOG_TAG = "ScanBarcodeTileService";
    @Override
    public void onTileAdded() {
        super.onTileAdded();
        Log.d(LOG_TAG, "onTileAdded");
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        Log.d(LOG_TAG, "onTileRemoved");
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Log.d(LOG_TAG, "onStartListening");
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        Log.d(LOG_TAG, "onStopListening");
    }

    @Override
    public void onClick() {
        //Start main activity and close Quick Settings Panel
        startActivityAndCollapse(new Intent(this, ScanActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK));
    }
}
