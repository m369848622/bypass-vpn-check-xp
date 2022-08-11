package com.demo.vpnhook;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class common {

    public static void writeFileString(File file, String str) {
        if (file.canWrite()) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(str.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("sdfsdfsdfdfg", "No permission to write file.");
        }
    }


    public static String readFileString(File file) {
        String str = "";
        if (file.canRead()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                int length = fileInputStream.available();
                byte[] bytes = new byte[length];
                fileInputStream.read(bytes);
                str = new String(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Log.e("sdfsdfsdfdfg", "No permission to Read file.");
        }
        return str;
    }
}
