package com.example.talimshield.utils;

import android.util.Log;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Root command execute করার জন্য utility class
 * Proper root permission request সহ
 */
public class RootCommandExecutor {

    private static final String TAG = "RootCommandExecutor";
    private static Process rootProcess = null;
    private static DataOutputStream rootOutputStream = null;

    /**
     * Root shell access পেতে চেষ্টা করে
     */
    private static boolean initRootShell() {
        try {
            // যদি ইতিমধ্যে root shell আছে
            if (rootProcess != null && rootOutputStream != null) {
                return true;
            }

            // Root shell request করি
            rootProcess = Runtime.getRuntime().exec("su");
            rootOutputStream = new DataOutputStream(rootProcess.getOutputStream());
            
            Log.d(TAG, "Root shell initialized successfully");
            return true;

        } catch (IOException e) {
            Log.e(TAG, "Failed to get root access: " + e.getMessage());
            return false;
        }
    }

    /**
     * Root shell এ command execute করে
     * @param command যে command run করতে হবে
     * @return সফল হলে true
     */
    public static boolean executeRootCommand(String command) {
        try {
            // Root shell initialize করি
            if (!initRootShell()) {
                Log.e(TAG, "Could not initialize root shell");
                return false;
            }

            // Command execute করি
            rootOutputStream.writeBytes(command + "\n");
            rootOutputStream.flush();
            
            Log.d(TAG, "Command executed: " + command);
            return true;

        } catch (IOException e) {
            Log.e(TAG, "Error executing command: " + command, e);
            // Shell restart করার চেষ্টা করি
            closeRootShell();
            return false;
        }
    }

    /**
     * মেসেঞ্জারের ক্যামেরা block করে
     */
    public static void blockMessengerCamera() {
        Log.d(TAG, "Blocking Messenger camera");
        executeRootCommand("cmd appops set com.facebook.orca CAMERA ignore");
        executeRootCommand("cmd appops set com.facebook.orca CAMERA 3"); // DENY mode
    }

    /**
     * মেসেঞ্জারের মাইক্রোফোন block করে
     */
    public static void blockMessengerMic() {
        Log.d(TAG, "Blocking Messenger mic");
        executeRootCommand("cmd appops set com.facebook.orca RECORD_AUDIO ignore");
        executeRootCommand("cmd appops set com.facebook.orca RECORD_AUDIO 3"); // DENY mode
    }

    /**
     * মেসেঞ্জারের ক্যামেরা unblock করে
     */
    public static void unblockMessengerCamera() {
        Log.d(TAG, "Unblocking Messenger camera");
        executeRootCommand("cmd appops set com.facebook.orca CAMERA allow");
        executeRootCommand("cmd appops set com.facebook.orca CAMERA 0"); // ALLOW mode
    }

    /**
     * মেসেঞ্জারের মাইক্রোফোন unblock করে
     */
    public static void unblockMessengerMic() {
        Log.d(TAG, "Unblocking Messenger mic");
        executeRootCommand("cmd appops set com.facebook.orca RECORD_AUDIO allow");
        executeRootCommand("cmd appops set com.facebook.orca RECORD_AUDIO 0"); // ALLOW mode
    }

    /**
     * সব root processes close করে
     */
    public static void closeRootShell() {
        try {
            if (rootOutputStream != null) {
                rootOutputStream.writeBytes("exit\n");
                rootOutputStream.flush();
                rootOutputStream.close();
                rootOutputStream = null;
            }

            if (rootProcess != null) {
                rootProcess.waitFor();
                rootProcess.destroy();
                rootProcess = null;
            }

            Log.d(TAG, "Root shell closed");

        } catch (Exception e) {
            Log.e(TAG, "Error closing root shell: " + e.getMessage());
        }
    }
}
