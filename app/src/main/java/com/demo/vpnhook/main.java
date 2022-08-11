package com.demo.vpnhook;


import static com.demo.vpnhook.common.readFileString;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.net.NetworkInterface;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class main implements IXposedHookLoadPackage {
    public Context Main_context;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpp) throws Throwable {
        String packageName = lpp.packageName;
        ClassLoader classLoader = lpp.classLoader;
        if (Main_context == null) {
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Main_context = (Context) param.args[0];
                }
            });
        }
        XposedBridge.log("start->" + packageName);

        XposedHelpers.findAndHookMethod(System.class, "getProperty", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String arg1 = String.valueOf(param.args[0]);
                if (arg1.equals("http.proxyHost") || arg1.equals("http.proxyPort")) {
                    param.setResult(null);
                }
            }
        });

        XposedHelpers.findAndHookMethod(NetworkInterface.class, "getName", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String gname = String.valueOf(param.getResult());
                if (gname.equals("tun0")) {
                    param.setResult("rmnet_data0");
                }
            }
        });

        XposedHelpers.findAndHookMethod(ConnectivityManager.class, "getNetworkCapabilities", Network.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);

                    }
                });

        XposedHelpers.findAndHookMethod(NetworkCapabilities.class, "hasTransport", int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(false);
                    }
                });

    }

}
