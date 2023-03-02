package com.zhj.bluetooth.sdkdemo.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;


import com.zhj.bluetooth.sdkdemo.MyAppcation;

import java.lang.reflect.Method;

/**
 * Get auxiliary classes related to the screen
 *
 * @author zhy
 */
public class ScreenUtil {

    private  ScreenUtil() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}
    private static int screenW;
    private static int screenH;
    private static float screenDensity;
    public static void initScreen(Activity mActivity) {
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenW = metric.widthPixels;
        screenH = metric.heightPixels;
        screenDensity = metric.density;
    }

    /**
     * Get screen height
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * Get screen width
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * Get the height of the status bar
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int result = 0;
        //Get the resource ID at the height of the status bar
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Get the screen width and height in px
     * @param context
     * @return
     */
    public static Point getScreenMetrics(Context context){
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);

    }

    /**
     * Get the current screenshot, including the status bar
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * Get the current screenshot without the status bar
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;

    }

    public static void ScreenInfo(Context context) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Log.e("eeeee", "Density is " + displayMetrics.density + " densityDpi is " + displayMetrics.densityDpi + " height: " + displayMetrics.heightPixels + " width: " + displayMetrics.widthPixels);
        Toast.makeText(context, "Density is " + displayMetrics.density + " densityDpi is " + displayMetrics.densityDpi + " height: " + displayMetrics.heightPixels + " width: " + displayMetrics.widthPixels, Toast.LENGTH_LONG).show();
    }

    //Get the height of virtual keys
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * Check if there is a virtual key bar
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * Judge whether the virtual key bar is rewritten
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }

    /**
     * Is it greater than 6 inches
     *
     * @param ctx
     * @return
     */
    public static boolean isOver6Inch(Activity ctx) {
        return getScreenPhysicalSize(ctx) >= 6.0 ? true : false;
    }
    /**
     * Get screen size
     *
     * @param ctx
     * @return
     */
    public static double getScreenPhysicalSize(Activity ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2) + Math.pow(dm.heightPixels, 2));
        return diagonalPixels / (160 * dm.density);
    }

    /**
     * Convert from dp to px (pixel) according to the resolution of the phone
     */
    public static int dp2px(float dpValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Convert the unit of dip (pixel) to px according to the resolution of the phone
     */
    public static int dip2px(float pxValue) {
        return dp2px(pxValue);
    }

    /**
     * Convert from dp to px (pixel) according to the resolution of the phone
     */
    public static int dp2px(float dpValue) {
//        return (int) (dpValue * getScreenDensity() + 0.5f);

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, MyAppcation.getInstance().getResources().getDisplayMetrics());
    }


    // Get a screenshot of the specified activity and save it to a png file
    public static Bitmap takeScreenShot(Activity activity) {

        // View is the view you need to capture
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // Get status bar height
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
//        System.out.println(statusBarHeight);

        // Get screen length and height
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = b1.getHeight();

        String brand = android.os.Build.BRAND;//Get mobile phone brand
        Bitmap b = null;
        //If the screen is larger than 900 * 500, take 900 * 500, otherwise take screenshots according to the screen size
        if (width>500&&height<900){
            width=500;
            height=900;
        }
        if (brand.equals("Meizu")) {
//            b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - 5 * statusBarHeight
//            );
            b = Bitmap.createBitmap(b1, 0, 0, width, height);

        } else {

            // Remove title block
            b = Bitmap.createBitmap(b1, 0, 0, width, height);
//            b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
//                    - statusBarHeight);
        }

        view.destroyDrawingCache();
        return b;
    }

    /**
     * Full screen shot
     */
    public static Bitmap getTotleScreenShot(final ViewGroup viewContainer, View... views) {
        int width = viewContainer.getWidth();
        int h = 0;
        for (int i = 0; i < viewContainer.getChildCount(); i++) {
            h += viewContainer.getChildAt(i).getHeight();
        }
        final Bitmap screenBitmap = Bitmap.createBitmap(width, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(screenBitmap);

        for (View view : views) {
            view.setVisibility(View.VISIBLE);
            view.setDrawingCacheEnabled(true);
            canvas.drawBitmap(view.getDrawingCache(), view.getLeft(), view.getTop(), null);
        }
        return screenBitmap;
    }

    public static int getTextHeight(Paint paint, String text){
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }
}
