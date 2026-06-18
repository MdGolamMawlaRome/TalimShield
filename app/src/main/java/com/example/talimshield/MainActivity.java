package com.example.talimshield;

import android.os.Bundle;
import android.widget.CompoundButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import java.io.DataOutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SwitchCompat camSwitch = findViewById(R.id.camSwitch);
        SwitchCompat micSwitch = findViewById(R.id.micSwitch);

        // ক্যামেরা সুইচের লজিক
        camSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // মেসেঞ্জারের ক্যামেরা ব্যাকএন্ডে ব্লক
                    executeRootCommand("cmd appops set com.facebook.orca CAMERA ignore");
                } else {
                    // ক্যামেরা আবার স্বাভাবিক করা
                    executeRootCommand("cmd appops set com.facebook.orca CAMERA allow");
                }
            }
        });

        // মাইক্রোফোন সুইচের লজিক
        micSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // মেসেঞ্জারের মাইক ব্যাকএন্ডে ব্লক
                    executeRootCommand("cmd appops set com.facebook.orca RECORD_AUDIO ignore");
                } else {
                    // মাইক আবার স্বাভাবিক করা
                    executeRootCommand("cmd appops set com.facebook.orca RECORD_AUDIO allow");
                }
            }
        });
    }

    // রুট কমান্ড রান করার ফাংশন
    private void executeRootCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
