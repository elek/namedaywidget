/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.names;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import java.util.Map;

/**
 *
 * @author elek
 */
public class MyPreferencesActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    ListPreference listPref;

    Map<String, Namedays.CountryRecord> countries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);

        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);

        countries = Namedays.getSupportedCountries();
        
        String[] keys = new String[countries.size()];
        String[] values = new String[countries.size()];
        int i = 0;
        for (String key : countries.keySet()) {
            Namedays.CountryRecord rec = countries.get(key);
            keys[i] = key;
            values[i] = rec.name;
            i++;
        }

        listPref = new ListPreference(this);
        listPref.setEntries(values);
        listPref.setEntryValues(keys);
        listPref.setDialogTitle("Choose your country");
        listPref.setKey("country");
        listPref.setTitle("Country");
        listPref.setSummary(countries.get(sp.getString("country", "hu")).name);
        listPref.setDefaultValue("hu");
        root.addPreference(listPref);

        PreferenceCategory inlinePrefCat = new PreferenceCategory(this);
        inlinePrefCat.setTitle("Notification");
        root.addPreference(inlinePrefCat);





        CheckBoxPreference togglePref = new CheckBoxPreference(this);
        togglePref.setKey("notification");
        togglePref.setTitle("Enable notifications");
        togglePref.setSummaryOff("Notifications about contacts are disabled");
        togglePref.setSummaryOn("Notifications about contacts are enabled");
        inlinePrefCat.addPreference(togglePref);

        CheckBoxPreference fn = new CheckBoxPreference(this);
        fn.setKey("find_in_given_name");
        fn.setTitle("Given name");
        fn.setSummaryOff("Given names are ignored");
        fn.setSummaryOn("Search in given names");
        fn.setDefaultValue(true);
        inlinePrefCat.addPreference(fn);

        fn = new CheckBoxPreference(this);

        fn.setKey("find_in_family_name");
        fn.setTitle("Family name");
        fn.setSummaryOff("Family names are ignored");
        fn.setSummaryOn("Search in family names");
        inlinePrefCat.addPreference(fn);

        fn = new CheckBoxPreference(this);
        fn.setKey("find_in_display_name");
        fn.setTitle("Display name");
        fn.setSummaryOff("Display names are ignored");
        fn.setSummaryOn("Search in display names");
        inlinePrefCat.addPreference(fn);



        setPreferenceScreen(root);

    }

    public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
        if (key.equals("notification")) {
            boolean start = sp.getBoolean(key, true);
            Log.i("names", "preference changed " + start);
            Intent intent = new Intent(this, SchedulingService.class);
            intent.putExtra(SchedulingService.START, start);
            startService(intent);
        }
        if (key.equals("country")) {
            Namedays.getInstance(sp, getResources()).reload(getResources(), sp.getString("country", "hu"));
            listPref.setSummary(countries.get(sp.getString("country", "hu")).name);
            Intent intent = new Intent(this, UpdateService.class);
            startService(intent);
        }

    }
}
