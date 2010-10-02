/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.names;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilitiy class to process contacts results.
 * 
 * @author elek
 */
public class ContactUtility {

    public static ContactUtility instance = new ContactUtility();

    public ContactUtility() {
    }

    private String getName(Cursor cursor, String type) {
        if (cursor.getColumnIndex(type) != -1) {
            String givenName = cursor.getString(cursor.getColumnIndex(type));
            return givenName;
        }
        return null;
    }

    private void addToList(List<String> names, String givenName) {
        if (givenName != null) {
            for (String st : givenName.replaceAll(",", "").split(" ")) {
                names.add(st);
            }
        }
    }

    public Map<Integer, Map<String, String>> findContacts(Cursor cursor, String current, SharedPreferences preferences) {
        Map<Integer, Map<String, String>> result = new HashMap();
        if (cursor.moveToFirst()) {
            do {
                List<String> names = new ArrayList();

                String givenName = getName(cursor, StructuredName.GIVEN_NAME);
                String displayName = getName(cursor, StructuredName.DISPLAY_NAME);
                String familyName = getName(cursor, StructuredName.FAMILY_NAME);
                if (preferences.getBoolean("find_in_given_name", true)) {
                    addToList(names, givenName);
                }
                if (preferences.getBoolean("find_in_display_name", false)) {
                    addToList(names, displayName);
                }
                if (preferences.getBoolean("find_in_family_name", false)) {
                    addToList(names, familyName);
                }
                String[] todays = current.split(",");
                for (String name : names) {
                    for (String tod : todays) {
                        if (name.trim().equals(tod.trim())) {
                            Map<String, String> item = new HashMap();

                            String n = givenName;
                            if (displayName != null) {
                                n = displayName;
                            }
                            if (n == null && familyName != null) {
                                n = familyName;
                            }
                            item.put("name", n);

                            int id = cursor.getColumnIndex(Data._ID);
                            if (id == -1) {
                                Log.w("names", "No id!!!");
                            } else {
                                item.put("id", "" + cursor.getInt(id));
                            }
                            String lookup = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                            int rowId = cursor.getInt(cursor.getColumnIndex(StructuredName.RAW_CONTACT_ID));
                            Uri uri = ContactsContract.Contacts.getLookupUri(rowId, lookup);
                            item.put("uri", uri.toString());
                            result.put(cursor.getInt(cursor.getColumnIndex(Data._ID)), item);
                        }
                    }
                }
            } while (cursor.moveToNext());
        }
        return result;

    }
}
