package nl.frankkie.bronyvideos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by FrankkieNL on 25-6-13.
 */
public class Util {

    public static final String PREFS_COUNTRY = "country";

    /**
     * Damn.. i'm lazy
     */
    public static String getPref(Context c, String key, String defaultValue){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(key,defaultValue);
    }

    public static void setPref(Context c, String key, String value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(key,value).commit();
    }

    public static void dismissDialog(Dialog dialog){
        try {
            dialog.dismiss();
        } catch (Exception e){
            //ignore dialog errors
        }
    }

    public static void showAlertDialog(Context c, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(title).setMessage(message).setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
            }
        }).create().show();
    }
}
