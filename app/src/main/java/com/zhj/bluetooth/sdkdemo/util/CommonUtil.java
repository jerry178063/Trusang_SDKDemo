package com.zhj.bluetooth.sdkdemo.util;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.MyAppcation;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Describe toolbar
 */

public class CommonUtil {
    /**
     * Whether it is 24 hours
     *
     * @return
     */
    public static boolean is24Hour() {
//        int timeStyle = (int) SPUtils.get(Constant.TIME_STYLE,0);
        boolean is24;
//        if (timeStyle == 0) {//Follow system
        ContentResolver cv = MyAppcation.getInstance().getContentResolver();
        // Get current system settings
        String time_12_24 = android.provider.Settings.System.getString(cv,
                android.provider.Settings.System.TIME_12_24);
        is24 = "24".equals(time_12_24) ? true : false;
//        }else{
//            is24 = timeStyle == Constants.TIME_MODE_24;
//        }
        return true;
    }

    public static int format24To12(int hour) {
        int h = hour % 12;
        if (hour == 12) {
            h = h == 0 ? 12 : h;
        } else {
            h = h == 0 ? 0 : h;
        }
        return h;
    }

    public static boolean isAM(int hour) {
        return hour < 12;
    }

    /**
     * Change the number of minutes in a day into the corresponding hh: mm format
     *
     * @param mins 00:00 is the first minute, mins=h * 60+m; Range 1~1440
     * @return
     */
    public static String timeFormatter(int mins, boolean is24, String[] amOrPm, boolean isUnit) {
        if (mins >= 0 && mins < 1440) {
            int h = getHourAndMin(mins, is24)[0];
            int min = mins % 60;
            if (is24) {
                return String.format("%1$02d:%2$02d", h == 24 ? 0 : h, min);
            } else {
                String m = "";
                if (isUnit) {
                    if (amOrPm != null) {
                        m = mins <= 12 * 60 ? amOrPm[0] : amOrPm[1];
                    } else {
                        m = mins <= 12 * 60 ? "am" : "pm";
                    }
                }
//                if(m.equals("下午")||m.equals("上午")){
//                    return m+ String.format("%1$02d:%2$02d", h == 24 ? 0 : h, min);
//                }else {
                return String.format("%1$02d:%2$02d", h == 24 ? 0 : h, min) + m;
//                }
            }
        } else if (mins >= 1440) {
            mins -= 1440;
            int h = 0;
            int min = 0;
            if (mins > 0) {
                h = getHourAndMin(mins, is24)[0];
                min = mins % 60;
            }
            if (is24) {
                return String.format("%1$02d:%2$02d", h == 24 ? 0 : h, min);
            } else {
                String m = "";
                if (isUnit) {
                    if (amOrPm != null) {
                        m = mins <= 12 * 60 ? amOrPm[0] : amOrPm[1];
                    } else {
                        m = mins <= 12 * 60 ? "am" : "pm";
                    }
                }

                return String.format("%1$02d:%2$02d", h == 24 ? 0 : h, min) + m;
            }
        }

//        Log.e("Util", "timeFormatter Error : mins is out of range [0 , 1440).");
//        return "--:--";
        return "00:00";
    }

    /**
     * Change the number of minutes in a day into the corresponding hh: mm format
     *
     * @param mins 00:00 is the first minute, mins=h * 60+m; Range 1~1440
     * @return
     */
    public static String timeFormatter(int mins, boolean is24, String[] amOrPm, boolean isUnit, boolean isStart) {
        if (mins >= 0 && mins < 1440) {
            int h = getHourAndMin(mins, is24)[0];
            int min = mins % 60;
            if (!isStart && min != 0) {
                h += 1;
            }
            min = 0;
            if (is24) {
                return String.format("%1$02d:%2$02d", h == 24 ? 0 : h, min);
            } else {
                String m = "";
                if (isUnit) {
                    if (amOrPm != null) {
                        m = mins < 12 * 60 ? amOrPm[0] : amOrPm[1];
                    } else {
                        m = mins < 12 * 60 ? "am" : "pm";
                    }
                }
                return m + String.format("%1$02d:%2$02d", h == 24 ? 0 : h, min);
            }
        } else if (mins >= 1440) {
            mins -= 1440;
            int h = 0;
            int min = 0;
            if (mins > 0) {
                h = getHourAndMin(mins, is24)[0];
                min = mins % 60;
            }
            if (!isStart && min != 0) {
                h += 1;
            }
            min = 0;
            if (is24) {
                return String.format("%1$02d:%2$02d", h == 24 ? 0 : h, min);
            } else {
                String m = "";
                if (isUnit) {
                    if (amOrPm != null) {
                        m = mins < 12 * 60 ? amOrPm[0] : amOrPm[1];
                    } else {
                        m = mins < 12 * 60 ? "am" : "pm";
                    }
                }
                return m + String.format("%1$02d:%2$02d", h == 24 ? 0 : h, min);
            }
        }

//        Log.e("Util", "timeFormatter Error : mins is out of range [0 , 1440).");
//        return "--:--";
        return "00:00";
    }

    public static int[] getHourAndMin(int mins, boolean is24) {
        int h = mins / 60;
        // 0, 12, 24 are all 12 o'clock, at - 12 in the afternoon
        h = is24 ? h : (h % 12 == 0 ? 12 : h > 12 ? h - 12 : h);
        return new int[]{h, mins % 60};
    }


    /**
     * @param h
     * @param min
     * @param is24
     * @return
     */

    /**
     * @param time 00:00
     * @param is24
     * @return
     */




    /**
     * Judge whether GPS is turned on. If GPS or AGPS is turned on, it is considered to be turned on
     *
     * @param context
     * @return true Indicates open
     */
    public static boolean isOPen(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Through GPS satellite positioning, the positioning level can be accurate to the street (through 24 satellite positioning, positioning is accurate and fast in outdoor and open places)
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // The location determined by WLAN or mobile network (3G/2G) (also known as AGPS, auxiliary GPS positioning. It is mainly used for positioning indoors or in places with dense covers (buildings or dense forests, etc.))
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * Force users to turn on GPS
     *
     * @param context
     */
    public static void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get weeks based on the start day of the week
     * If the start date is Sunday, the return date is 1234
     * If the start date is six, return to six to twelve
     * If the start date is one, return one, two, three, four
     *
     * @param context
     * @param weekStartDay
     * @return
     */

    /**
     * Convert the week sending the alarm clock to the week displayed
     * The week of sending alarm clock is fixed from Monday
     * The displayed week is determined according to the start Sunday
     *
     * @return Returns an array with Monday as the start date
     * @startWeek 0: Saturday, 1: Sunday, 2: Monday
     */
    public static boolean[] alarmToShowAlarm(boolean[] week, int startWeek) {
        boolean[] tempAlarm = new boolean[7];
        if (startWeek == 0) {
            tempAlarm[0] = week[2];
            tempAlarm[1] = week[3];
            tempAlarm[2] = week[4];
            tempAlarm[3] = week[5];
            tempAlarm[4] = week[6];
            tempAlarm[5] = week[0];
            tempAlarm[6] = week[1];
        } else if (startWeek == 1) {
            tempAlarm[0] = week[1];
            tempAlarm[1] = week[2];
            tempAlarm[2] = week[3];
            tempAlarm[3] = week[4];
            tempAlarm[4] = week[5];
            tempAlarm[5] = week[6];
            tempAlarm[6] = week[0];
        } else {
            tempAlarm = Arrays.copyOf(week, week.length);
        }
        return tempAlarm;
    }


    /**
     * @param week
     * @param startWeek 0: Saturday, 1: Sunday, 2: Monday
     * @return Convert the array of Monday as the start date to another one
     */
    public static boolean[] alarmToShowAlarm2(boolean[] week, int startWeek) {
        boolean[] tempAlarm = new boolean[7];
        if (startWeek == 0) {
            tempAlarm[0] = week[5];
            tempAlarm[1] = week[6];
            tempAlarm[2] = week[0];
            tempAlarm[3] = week[1];
            tempAlarm[4] = week[2];
            tempAlarm[5] = week[3];
            tempAlarm[6] = week[4];
        } else if (startWeek == 1) {
            tempAlarm[0] = week[6];
            tempAlarm[1] = week[0];
            tempAlarm[2] = week[1];
            tempAlarm[3] = week[2];
            tempAlarm[4] = week[3];
            tempAlarm[5] = week[4];
            tempAlarm[6] = week[5];
        } else {
            tempAlarm = Arrays.copyOf(week, week.length);
        }
        return tempAlarm;
    }

    /**
     * Whether there is track
     *
     * @param type
     * @return
     */
    public static boolean hasOrbit(int type) {
        //0x01;// walk
        //0x02;// run
        //0x03;// Cycling
        //0x04;// on foot
        int[] types = {1, 2, 3, 4};
        for (int t : types) {
            if (t == type) {
                return true;
            }
        }
        return false;
    }

    public static String noHeartRate(String s) {
        if (TextUtils.isEmpty(s) || s.equals("0")) {
            return "--";
        }
        return s + "";
    }

    public static String noBloodPressure(int systolicPressure, int diastolicPressure) {
        if (systolicPressure == 0 || diastolicPressure == 0) {
            return "--/--";
        }
        return systolicPressure + "/" + diastolicPressure;
    }

    public static String noPace(int speed) {
        if (speed == 0) {
            return "--";
        }
        StringBuffer avgPace = new StringBuffer();
        avgPace.append(speed / 60);
        avgPace.append("'");
        avgPace.append(speed % 60);  //Convert String
        avgPace.append("\"");
        return avgPace.toString();
    }


    public static void adjustTvTextSize(TextView tv, int maxWidth, String text) {
        int avaiWidth = maxWidth - tv.getPaddingLeft() - tv.getPaddingRight() - 10;

        if (avaiWidth <= 0) {
            return;
        }

        TextPaint textPaintClone = new TextPaint(tv.getPaint());
        // note that Paint text size works in px not sp
        float trySize = textPaintClone.getTextSize();

        while (textPaintClone.measureText(text) > avaiWidth) {
            trySize--;
            textPaintClone.setTextSize(trySize);
        }

        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize);
    }

    private static DecimalFormat df;
    private static DecimalFormat decimalFormat;

    static {
        Locale.setDefault(Locale.CHINA);
        df = new DecimalFormat("#,###");
        decimalFormat = new DecimalFormat("###,###,###,##0.00");
    }

    public static String formatThree(int value) {
        return df.format(value);
    }

    public static String formatThree(float value) {
        return df.format(value);
    }

    public static String formatNumber(int num) {
        String formatNum;
        if (num > 10000) {
            formatNum = "10,000+";
        } else {
            formatNum = df.format(num);
        }
        return formatNum;
    }

    /**
     * Keep two digits and separate the three with ","
     *
     * @param num
     * @return
     */
    public static String formatDistance(float num) {
        return decimalFormat.format(num);
    }

    public static SimpleDateFormat getFormat(String format) {
        return new SimpleDateFormat(format);
    }

    /**
     * Calculate months
     *
     * @return
     */
    private static int calculationDaysOfMonth(int year, int month) {
        int day = 0;
        switch (month) {
            // 31 days
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            // 30 days
            case 4:
            case 6:
            case 9:
            case 11:
                day = 30;
                break;
            // Calculate the number of days in February
            case 2:
                day = year % 100 == 0 ? year % 400 == 0 ? 29 : 28
                        : year % 4 == 0 ? 29 : 28;
                break;
        }

        return day;
    }


    /**
     *    Target time selection list (no units)
     * @return
     */
    public static List<Float> getTimeList() {
        final List<Float> times = new ArrayList<>();
        times.add(5f);
        for (int i = 10; i <= 6000; i += 10) {
            times.add(i*1f);
        }
        return times;
    }

    /**
     *    Target distance selection list (no units)
     * @return
     */
    public static List<Float> getDistanceList() {
        final List<Float> distances = new ArrayList<>();
        distances.add(0.5f);
        for (int i = 1; i <= 100; i++) {
            distances.add(i*1f);
        }
        return distances;
    }
    /**
     *    Target calorie selection list (no units)
     * @return
     */
    public static List<Float> gettCalorieList() {
        final List<Float> distances = new ArrayList<>();
        for (int i = 300; i <= 9000; i+= 300) {
            distances.add(i*1f);
        }
        return distances;
    }
}
