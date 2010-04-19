package net.anzix.android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import java.util.Calendar;
import java.util.Date;

public class NameWidgetProvider extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            setAlarm(context, appWidgetId, 24 * 60 * 60 * 1000);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            setAlarm(context, appWidgetId, -1);
        }
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        context.stopService(new Intent(context, NameService.class));
        super.onDisabled(context);
    }

    public static PendingIntent makeControlPendingIntent(Context context,
            int appWidgetId) {
        Intent active = new Intent(context, NameService.class);
        active.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        Uri data = Uri.withAppendedPath(Uri.parse("nameservice://widget/id/#" + appWidgetId), String.valueOf(appWidgetId));

        active.setData(data);
        return (PendingIntent.getService(context, 0, active, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    public static void setAlarm(Context context, int appWidgetId, int updateRate) {
        PendingIntent newPending = makeControlPendingIntent(context, appWidgetId);
        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (updateRate >= 0) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.AM_PM, Calendar.AM);
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);

            Log.i("namedays", "" + new Date(cal.getTimeInMillis() + updateRate));
            Log.i("namedays", "" + new Date(System.currentTimeMillis()));
            alarms.setRepeating(AlarmManager.RTC, cal.getTimeInMillis(), updateRate, newPending);
        } else {
            alarms.cancel(newPending);

        }
    }
}
