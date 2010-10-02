package net.anzix.names;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ListActivity {

    private static final int FIND_ID = 1;

    private Namedays namedays;

    private static final int MENU_ABOUT = 1;

    private static final int MENU_PREFERENCES = 2;

    private static final int MENU_REFRESH = 3;

    private String currentCountry;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_PREFERENCES, 0, this.getString(R.string.preferences));
        menu.add(0, MENU_REFRESH, 0, this.getString(R.string.refresh));
        menu.add(0, MENU_ABOUT, 0, this.getString(R.string.about));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ABOUT:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getText(R.string.about_txt)).setCancelable(true);
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            case MENU_REFRESH:
                Intent intent = new Intent(this, UpdateService.class);
                startService(intent);
                return true;
            case MENU_PREFERENCES:
                Intent i2 = new Intent(this, MyPreferencesActivity.class);
                startActivity(i2);
                return true;
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, FIND_ID, 0, getString(R.string.find_in_contacts));

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case FIND_ID:
                DayOfYear doy = new DayOfYear(info.position + 1);
                findTodays(namedays.getName(doy));
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void findTodays(String name) {
        Intent intent = new Intent(this, ContactsActivity.class);
        intent.putExtra("nameday", name);
        Log.d("names", "find " + name);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("names", "resume");
        String c = PreferenceManager.getDefaultSharedPreferences(this).getString("country", "hu");

        if (!c.equals(currentCountry)) {
            reload();
        }
    }

    public void reload() {
        this.currentCountry = PreferenceManager.getDefaultSharedPreferences(this).getString("country", "hu");
        namedays = Namedays.getInstance(PreferenceManager.getDefaultSharedPreferences(this), getResources());
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        DayOfYear now = DayOfYear.valueOf(Calendar.getInstance());
        Log.d("name", "nameday size "+namedays.getKeys().size());
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

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.names);
        reload();
        registerForContextMenu(getListView());

    }
}
