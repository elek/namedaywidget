/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.names;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

/**
 *
 * @author elek
 */
public class MyPreferencesActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);

        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);

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
    }
}
