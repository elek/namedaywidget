/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.names;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;

import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.HashMap;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elek
 */
public class ContactsActivity extends ListActivity {

    private String name;

    Cursor cursor;

    List<Map<String, String>> list;

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(list.get(position).get("uri")));
        Log.i("names", intent.getDataString());
        startActivity(intent);
    }

    @Override
    public void onNewIntent(Intent intent) {
        name = (String) intent.getStringExtra("nameday");
        Log.i("names", "onNewINtent" + name);
        refresh(name);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("names","onCreate");
        if (name != null) {
            refresh(name);
        } else if (getIntent().getStringExtra("nameday")!=null){
            refresh(getIntent().getStringExtra("nameday"));
        }

    }

    public void refresh(String current) {
        Map<Integer, Map<String, String>> result = new HashMap();

        ContentResolver resolver = getContentResolver();
        if (cursor != null) {
            stopManagingCursor(cursor);
        }
        cursor = resolver.query(Data.CONTENT_URI, new String[]{Data._ID, StructuredName.GIVEN_NAME, StructuredName.DISPLAY_NAME, ContactsContract.Contacts.LOOKUP_KEY, StructuredName.RAW_CONTACT_ID},
                Data.MIMETYPE + "='" + StructuredName.CONTENT_ITEM_TYPE + "' AND " + ContactsContract.Contacts.IN_VISIBLE_GROUP, null, null);
        startManagingCursor(cursor);
        result = ContactUtility.findContacts(cursor, current);
        list = new ArrayList<Map<String, String>>(result.values());
        setListAdapter(new SimpleAdapter(this, list, R.layout.contact, new String[]{"name"}, new int[]{R.id.name}));

    }
}
