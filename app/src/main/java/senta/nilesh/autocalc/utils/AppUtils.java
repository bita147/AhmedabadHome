package senta.nilesh.autocalc.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import senta.nilesh.autocalc.R;

/**
 * Created by "Nilesh Senta" on 4/6/2016.
 */
public class AppUtils {

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager mngr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mngr.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static String generateTenDigitRandom() {
//        Random r = new Random( System.currentTimeMillis() );
//        return String.valueOf(1000000000 + r.nextInt(2000000000));
        return String.valueOf((long) Math.floor(Math.random() * 9000000000L) + 1000000000L);

    }

    public static void showSnack(Activity context, int resId, boolean error) {
        try {
            Snackbar snackbar = Snackbar.make(context.findViewById(R.id.snackbar_position), resId, Snackbar.LENGTH_LONG);
            if (error) {
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                textView.setMaxLines(5);
            }
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showSnack(Activity context, String resource, boolean error) {
        try {
            Snackbar snackbar = Snackbar.make(context.findViewById(R.id.snackbar_position), resource, Snackbar.LENGTH_LONG);
            if (error) {
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isTodayDate(String millies) {
        try {
            long milli = Long.parseLong(millies);
            Calendar now = Calendar.getInstance();
            Calendar timeToCheck = Calendar.getInstance();
            timeToCheck.setTimeInMillis(milli);
            return (now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR)
                    && now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR));
        } catch (Exception e) {
            return false;
        }
    }
}
