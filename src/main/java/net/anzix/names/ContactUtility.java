/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.names;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author elek
 */
public class ContactUtility {



    public static Map<Integer, Map<String, String>> findContacts(Cursor cursor, String current) {
        Map<Integer, Map<String, String>> result = new HashMap();
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getColumnIndex(StructuredName.GIVEN_NAME) == -1) {
                    continue;
                }
                String givenName = cursor.getString(cursor.getColumnIndex(StructuredName.GIVEN_NAME));
                if (givenName == null) {
                    continue;
                }
                String[] givenNames = givenName.split(" ");
                String[] todays = current.split(",");
                for (String given : givenNames) {
                    for (String tod : todays) {
                        if (given.trim().equals(tod.trim())) {
                            Map<String, String> item = new HashMap();
                            String n = givenName;
                            int idx = cursor.getColumnIndex(StructuredName.DISPLAY_NAME);
                            if (idx > -1) {
                                n = cursor.getString(idx);
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
