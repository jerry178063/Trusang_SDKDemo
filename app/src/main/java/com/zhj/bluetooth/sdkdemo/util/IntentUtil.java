package com.zhj.bluetooth.sdkdemo.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Page jump intent tool class
 * Created by wzl on 2018/6/17.
 */

public class IntentUtil {

    /**
     * Jump to the specified page
     * author wzl
     * date 2018/6/17 下午5:31
     * @param c
     *         Context object
     * @param cls
     *         Specify the class to jump
     */
    public static void goToActivity(Context c, Class cls) {
        Intent intent = new Intent();
        intent.setClass(c, cls);
        c.startActivity(intent);
    }

    /**
     * Jump to the specified page with bundle data
     * author wzl
     * date 2018/6/17 下午5:33
     */
    public static void goToActivity(Context c, Class cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(c, cls);
        intent.putExtras(bundle);
        c.startActivity(intent);
    }

    /**
     * Jump to the specified page and return
     * author wzl
     * date 2018/6/17 下午5:35
     * @param requstCode
     *         Request code
     */
    public static void goToActivityForResult(Context c, Class cls, int requstCode) {
        Intent intent = new Intent();
        intent.setClass(c, cls);
        ((Activity) c).startActivityForResult(intent, requstCode);
    }

    /**
     * Carry the bundle data to jump to the specified page and return
     */
    public static void goToActivityForResult(Context c, Class cls, Bundle bundle, int requstCode) {
        Intent intent = new Intent();
        intent.setClass(c, cls);
        intent.putExtras(bundle);
        ((Activity) c).startActivityForResult(intent, requstCode);
    }

    /**
     * Jump to the specified page and close the current page
     * author wzl
     * date 2018/6/17 下午5:59
     */
    public static void goToActivityAndFinish(Context c, Class cls) {
        Intent intent = new Intent();
        intent.setClass(c, cls);
        c.startActivity(intent);
        ((Activity) c).finish();
    }

    /**
     * Jump to the specified page and close the current page
     * author wzl
     * date 2018/6/17 下午5:59
     */
    public static void goToActivityAndFinish(Context c, Class cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(c, cls);
        intent.putExtras(bundle);
        c.startActivity(intent);
        ((Activity) c).finish();
    }

    /**
     * Jump to the specified page
     * author wzl
     * date 2018/6/17 下午6:02
     */
    public static void goToActivityAndFinishTop(Context c, Class cls) {
        Intent intent = new Intent();
        intent.setClass(c, cls);
        // FLAG_ACTIVITY_CLEAR_TOP Destroy the target activity and all activities above it, and re-create the target activity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        c.startActivity(intent);
        ((Activity) c).finish();
    }

    /**
     * Jump to the specified page
     * author wzl
     * date 2018/6/17 下午6:02
     */
    public static void goToActivityAndFinishTop(Context c, Class cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(c, cls);
        intent.putExtras(bundle);
        // FLAG_ACTIVITY_CLEAR_TOP Destroy the target activity and all activities above it, and re-create the target activity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        c.startActivity(intent);
        ((Activity) c).finish();
    }

    /**
     * Jump to the specified page
     * author wzl
     * date 2018/6/17 下午6:02
     */
    public static void goToActivityAndFinishSingleTop(Context c, Class clz, Bundle bundle) {
        Intent intent = new Intent(c, clz);
        intent.putExtras(bundle);
        //When the activity is at the top of the task stack, it can be reused, directly onNewIntent
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Activity a = (Activity) c;
        a.startActivity(intent);
        a.finish();
    }

    /**
     * Jump to the specified page
     * author wzl
     * date 2018/6/17 下午6:02
     */
    public static void goToActivityAndFinishSingleTop(Context c, Class clz) {
        Intent intent = new Intent(c, clz);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Activity a = (Activity) c;
        a.startActivity(intent);
        a.finish();
    }


}
