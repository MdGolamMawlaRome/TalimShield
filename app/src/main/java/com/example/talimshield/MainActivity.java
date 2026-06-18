package com.example.talimshield;

import android.os.Bundle;
import android.widget.CompoundButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import com.example.talimshield.utils.RootCommandExecutor;

/**
 * TalimShield - মাইক ও ক্যামেরা
 * শুধু ২টি toggle switch
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Switches
        SwitchCompat camSwitch = findViewById(R.id.camSwitch);
        SwitchCompat micSwitch = findViewById(R.id.micSwitch);

        // Camera toggle
        camSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    RootCommandExecutor.blockMessengerCamera();
                } else {
                    RootCommandExecutor.unblockMessengerCamera();
                }
            }
        });

        // Mic toggle
        micSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    RootCommandExecutor.blockMessengerMic();
                } else {
                    RootCommandExecutor.unblockMessengerMic();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RootCommandExecutor.closeRootShell();
    }
}
