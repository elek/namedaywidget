/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.android;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import java.util.Calendar;
import java.util.Date;

/**
 * Service to
 *
 * @author elek
 */
public class NameService extends Service {

    private Namedays namedays;

    int updateno = 0;

    public static final String UPDATE = "update";

    @Override
    public void onStart(Intent intent, int startId) {
        Log.i("NAMEDAYS", "Start service");
        namedays = new Namedays(getResources());
        int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
        RemoteViews remoteView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

        DayOfYear doy = DayOfYear.valueOf(Calendar.getInstance());
        String name = namedays.getName(doy);

        String[] names = name.split(",");
        remoteView.setTextViewText(R.id.message1, name.replace(' ', '\n'));


        appWidgetManager.updateAppWidget(appWidgetId, remoteView);
        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
