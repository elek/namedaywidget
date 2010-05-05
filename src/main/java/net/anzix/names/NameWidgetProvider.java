package net.anzix.names;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class NameWidgetProvider extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent i = new Intent(context, SchedulingService.class);
        i.putExtra(SchedulingService.START, true);
        context.startService(i);
        context.startService(new Intent(context, UpdateService.class));
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Intent i = new Intent(context, SchedulingService.class);
        i.putExtra(SchedulingService.START, false);
        context.startService(i);
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

   
}
