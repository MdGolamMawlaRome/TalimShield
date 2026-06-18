package com.example.talimshield;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import com.example.talimshield.utils.RootCommandExecutor;

/**
 * TalimShield Main Activity
 * মায়ের Messenger protection সিস্টেম
 * 
 * এই app মেসেঞ্জারের ক্যামেরা এবং মাইক ব্লক করে
 * যাতে মা Talim নিতে গিয়ে disturb না হয়
 */
public class MainActivity extends AppCompatActivity {

    // UI Elements
    private SwitchCompat camSwitch;
    private SwitchCompat micSwitch;
    private TextView statusText;
    private TextView rootStatusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI elements link করি
        camSwitch = findViewById(R.id.camSwitch);
        micSwitch = findViewById(R.id.micSwitch);
        statusText = findViewById(R.id.statusText);
        rootStatusText = findViewById(R.id.rootStatusText);

        // Root check করি
        checkRootAccess();

        // ক্যামেরা সুইচের listener
        camSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCameraToggle(isChecked);
            }
        });

        // মাইক্রোফোন সুইচের listener
        micSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleMicToggle(isChecked);
            }
        });
    }

    /**
     * ক্যামেরা সুইচ handle করে
     */
    private void handleCameraToggle(boolean isChecked) {
        if (isChecked) {
            // ব্লক করি
            String result = RootCommandExecutor.blockMessengerCamera();
            updateStatus("📷 ক্যামেরা ব্লক করা হয়েছে");
            showToast("মেসেঞ্জার ক্যামেরা ব্লক হয়েছে");
        } else {
            // আনব্লক করি
            String result = RootCommandExecutor.unblockMessengerCamera();
            updateStatus("📷 ক্যামেরা সক্রিয় করা হয়েছে");
            showToast("মেসেঞ্জার ক্যামেরা সক্রিয় হয়েছে");
        }
    }

    /**
     * মাইক সুইচ handle করে
     */
    private void handleMicToggle(boolean isChecked) {
        if (isChecked) {
            // ব্লক করি
            String result = RootCommandExecutor.blockMessengerMic();
            updateStatus("🎤 মাইক্রোফোন ব্লক করা হয়েছে");
            showToast("মেসেঞ্জার মাইক ব্লক হয়েছে");
        } else {
            // আনব্লক করি
            String result = RootCommandExecutor.unblockMessengerMic();
            updateStatus("🎤 মাইক্রোফোন সক্রিয় করা হয়েছে");
            showToast("মেসেঞ্জার মাইক সক্রিয় হয়েছে");
        }
    }

    /**
     * Root access check করে এবং status দেখায়
     */
    private void checkRootAccess() {
        boolean hasRoot = RootCommandExecutor.checkRootAccess();
        
        if (hasRoot) {
            rootStatusText.setText("✅ Root Access: আছে");
            rootStatusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            camSwitch.setEnabled(true);
            micSwitch.setEnabled(true);
        } else {
            rootStatusText.setText("❌ Root Access: নেই");
            rootStatusText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            camSwitch.setEnabled(false);
            micSwitch.setEnabled(false);
            showToast("Root access প্রয়োজন!");
        }
    }

    /**
     * Status text update করে
     */
    private void updateStatus(String message) {
        statusText.setText(message);
    }

    /**
     * Toast message দেখায়
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Activity destroy এর সময় cleanup করি
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        RootCommandExecutor.closeRootShell();
    }
}
