/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.names;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.widget.RemoteViews;
import java.util.Calendar;
import java.util.Map;

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
        namedays = new Namedays(getResources());
        int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
        RemoteViews remoteView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

        DayOfYear doy = DayOfYear.valueOf(Calendar.getInstance());
        String name = namedays.getName(doy);

        remoteView.setTextViewText(R.id.message1, name.replace(' ', '\n').replace(",", ""));

        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setClass(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
        remoteView.setOnClickPendingIntent(R.id.widget, pendingIntent);

        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(Data.CONTENT_URI, new String[]{Data._ID, StructuredName.GIVEN_NAME, StructuredName.DISPLAY_NAME, ContactsContract.Contacts.LOOKUP_KEY, StructuredName.RAW_CONTACT_ID},
                Data.MIMETYPE + "='" + StructuredName.CONTENT_ITEM_TYPE + "' AND " + ContactsContract.Contacts.IN_VISIBLE_GROUP, null, null);

        Map<Integer, Map<String, String>> result = ContactUtility.findContacts(cursor, name);
        if (result.size() > 0) {
            StringBuilder fullNames = new StringBuilder();
            boolean first = true;
            for (Map<String, String> datas : result.values()) {
                if (!first) {
                    fullNames.append(",");
                }
                fullNames.append(datas.get("name"));
                first = false;
            }


            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
            int icon = R.drawable.namedayb;

            CharSequence tickerText = this.getString(R.string.app_name);
            long when = System.currentTimeMillis();

            Notification notification = new Notification(icon, tickerText, when);


            notification.defaults |= Notification.FLAG_AUTO_CANCEL;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            CharSequence contentTitle = this.getString(R.string.notification);
            CharSequence contentText = fullNames;

            Intent notificationIntent = new Intent(this, ContactsActivity.class);
            notificationIntent.putExtra("nameday", name);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            notification.setLatestEventInfo(getApplicationContext(), contentTitle, contentText, contentIntent);
            mNotificationManager.notify(1, notification);
        }
        cursor.close();

        appWidgetManager.updateAppWidget(appWidgetId, remoteView);
        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
