package com.mmg.phonect.common.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mmg.phonect.R;
import com.mmg.phonect.background.polling.PollingManager;

public class AwakeUpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getApplicationContext(), R.string.refresh, Toast.LENGTH_SHORT).show();
        PollingManager.resetAllBackgroundTask(this, true);
        finish();
    }
}
