package com.example.talimshield.utils;

import android.util.Log;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Root command execute করার জন্য utility class
 * মায়ের Messenger কে safe রাখতে
 */
public class RootCommandExecutor {

    private static final String TAG = "RootCommandExecutor";
    private static Process rootProcess = null;

    /**
     * Root shell এ command run করে
     * @param command যে command run করতে হবে
     * @return output result
     */
    public static String executeCommand(String command) {
        try {
            // যদি root process ইতিমধ্যে active থাকে
            if (rootProcess == null) {
                rootProcess = Runtime.getRuntime().exec("su");
            }

            DataOutputStream os = new DataOutputStream(rootProcess.getOutputStream());
            
            // Command execute করি
            os.writeBytes(command + "\n");
            os.flush();
            
            Log.d(TAG, "Command executed: " + command);
            return "সফল";
            
        } catch (Exception e) {
            Log.e(TAG, "Error executing command: " + command, e);
            return "ব্যর্থ: " + e.getMessage();
        }
    }

    /**
     * মেসেঞ্জারের ক্যামেরা block করে
     */
    public static String blockMessengerCamera() {
        return executeCommand("cmd appops set com.facebook.orca CAMERA ignore");
    }

    /**
     * মেসেঞ্জারের মাইক্রোফোন block করে
     */
    public static String blockMessengerMic() {
        return executeCommand("cmd appops set com.facebook.orca RECORD_AUDIO ignore");
    }

    /**
     * মেসেঞ্জারের ক্যামেরা unblock করে
     */
    public static String unblockMessengerCamera() {
        return executeCommand("cmd appops set com.facebook.orca CAMERA allow");
    }

    /**
     * মেসেঞ্জারের মাইক্রোফোন unblock করে
     */
    public static String unblockMessengerMic() {
        return executeCommand("cmd appops set com.facebook.orca RECORD_AUDIO allow");
    }

    /**
     * Root access আছে কিনা check করে
     */
    public static boolean checkRootAccess() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("echo 'root check'\n");
            os.writeBytes("exit\n");
            os.flush();
            
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );
            String line = reader.readLine();
            process.waitFor();
            
            return line != null && line.contains("root check");
        } catch (Exception e) {
            Log.e(TAG, "Root check failed", e);
            return false;
        }
    }

    /**
     * সব root processes close করে
     */
    public static void closeRootShell() {
        try {
            if (rootProcess != null) {
                DataOutputStream os = new DataOutputStream(rootProcess.getOutputStream());
                os.writeBytes("exit\n");
                os.flush();
                rootProcess.waitFor();
                rootProcess = null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error closing root shell", e);
        }
    }
}
