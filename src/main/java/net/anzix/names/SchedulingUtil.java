/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.names;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import java.util.Calendar;

/**
 * Utility to schedule the update service.
 * 
 * @author elek
 */
public class SchedulingUtil {

    public static void schendule(Context context, boolean start) {

        if (start) {
            //start alarm
            Log.i("names", "alarm started");
            setAlarm(context, true);
        } else {
            Log.i("names", "alarm stopping?");
            //if !exists widget and show.notification==false, stop service
            AppWidgetManager awm = AppWidgetManager.getInstance(context);
            int[] ids = awm.getAppWidgetIds(new ComponentName(context, NameWidgetProvider.class));
            boolean not = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("notification", true);
            if (!not && ids.length == 0) {
                setAlarm(context, false);
                Log.i("names", "alarm stopped");
            }

        }
    }

    protected static PendingIntent makeControlPendingIntent(Context context) {
        Intent active = new Intent(context, UpdateService.class);
        return (PendingIntent.getService(context, 0, active, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    protected static void setAlarm(Context context, boolean start) {
        PendingIntent newPending = makeControlPendingIntent(context);
        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (start) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.AM_PM, Calendar.AM);
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            alarms.setRepeating(AlarmManager.RTC, cal.getTimeInMillis(), 24 * 60 * 60 * 1000, newPending);
        } else {
            alarms.cancel(newPending);

        }
    }
}
