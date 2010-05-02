package net.anzix.names;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        startActivity(intent);
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
        registerForContextMenu(getListView());



    }
}
