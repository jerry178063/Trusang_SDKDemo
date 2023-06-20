package com.zhj.bluetooth.sdkdemo.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;


public class CnWinUtil {
    /**
     * Get version number
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        // Get an instance of packagemanager
        PackageManager packageManager = context.getPackageManager();
        // GetPackageName() is the package name of your current class, 0 represents to get version information
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        Log.e("TAG",packInfo.versionName);
        return version;
    }

    /**
     * Get version number
     *
     * @param context
     * @return
     */
    public static long getVersionCode(Context context) {
        // Get an instance of packagemanager
        PackageManager packageManager = context.getPackageManager();
        // GetPackageName() is the package name of your current class, 0 represents to get version information
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        long versionCode = packInfo.versionCode;
        Log.e("TAG",packInfo.versionName);
        return versionCode;
    }

    /**
     * Get generic
     *
     * @param o
     * @param i
     * @param <T>
     * @return
     */
    public static <T> T getT(Object o, int i) {
        // Get the generic parent type of the current running class, that is, the parameterized type, which has the advanced interface type common to all types to receive!
        Type type = o.getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) type;
        Type[] ts = pt.getActualTypeArguments();
        Class<T> clazz = (Class<T>) ts[i];
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtained by class name
     *
     * @param className
     * @return
     */
    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Convert the obtained IP of int type to String type
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * Sorts the return position based on the string and string collection
     *
     * @param str
     * @param list
     * @return
     */
    public static int matchingList(String str, List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (str.equals(list.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get the current date and time (Y - M - D H: M: S)
     *
     * @param date
     * @return
     */
    public static String getYMDHMSDate(long date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * Get the current time (H: M: S)
     *
     * @param date
     * @return
     */
    public static String getHMSTimes(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
