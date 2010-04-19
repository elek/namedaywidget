package net.anzix.android;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ListActivity {

    private int NEXT_NAMEDAYS = 1;

    private Namedays namedays;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
       // menu.add(0, 0, 0, "Közelgő napok");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(Namedays.LOGID, WIFI_SERVICE);


        Cursor cursor = getContentResolver().query(Data.CONTENT_URI,
                new String[]{Data._ID, StructuredName.GIVEN_NAME},
                Data.MIMETYPE + "='" + StructuredName.CONTENT_ITEM_TYPE+"'",
                null, null);

//Data.CONTACT_ID + "=?" + " AND "
//                + Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'"

        if (cursor.moveToFirst()){
            Log.i(Namedays.LOGID, cursor.getString(cursor.getColumnIndex(StructuredName.GIVEN_NAME)));
        }
        return true;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        namedays = new Namedays(getResources());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.names);
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        DayOfYear now = DayOfYear.valueOf(Calendar.getInstance());

        int i = 0;
        int selected = 0;
        for (DayOfYear doy : namedays.getKeys()) {
            if (now.equals(doy)) {
                selected = i;
            }
            Map<String, String> item = new HashMap();
            item.put("day", doy.getString());
            item.put("name", namedays.getName(doy));
            i++;
            list.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.name, new String[]{"day", "name"}, new int[]{R.id.day, R.id.name});
        setListAdapter(adapter);
        setSelection(selected);
    }
}
