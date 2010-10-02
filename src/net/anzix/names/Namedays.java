package net.anzix.names;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elek
 */
public class Namedays {

    private static Namedays instance;

    private Map<String, CountryRecord> countries = new HashMap();

    public static final String LOGID = "Namedays";

    private List<DayOfYear> keys = new ArrayList();

    private Map<DayOfYear, String> names = new HashMap<DayOfYear, String>();

    public static Namedays getInstance(SharedPreferences p, Resources r) {
        if (instance == null) {
            instance = new Namedays();
            instance.init(p, r);
        }
        return instance;
    }

    public static Map<String,CountryRecord> getSupportedCountries(){
        Map<String, CountryRecord> c = new HashMap();
        c.put("hu", new CountryRecord("Hungary", R.raw.nevnap));
        c.put("dk", new CountryRecord("Denmark", R.raw.dk));
        c.put("no", new CountryRecord("Norway", R.raw.no));
        c.put("pl", new CountryRecord("Poland", R.raw.pl));
        c.put("se", new CountryRecord("Swedish", R.raw.se));
        c.put("cz", new CountryRecord("Czech Republic", R.raw.cz));
        c.put("cz", new CountryRecord("Slovakia", R.raw.sk));

        return c;
    }
    public void init(SharedPreferences p, Resources resources) {
        countries = getSupportedCountries();
        reload(resources, p.getString("country", "hu"));
    }

    public void reload(Resources resources, String country) {
        try {
            Log.d("names", "reloading "+country);
            names.clear();
            keys.clear();
            int rawId = countries.get("hu").rawId;
            if (countries.get(country)!=null){
                rawId = countries.get(country).rawId;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(resources.openRawResource(rawId)));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                DayOfYear dof = DayOfYear.valueOf(Integer.valueOf(parts[0].substring(0, 2)), Integer.valueOf(parts[0].substring(2, 4)));
                names.put(dof, parts[1]);
                keys.add(dof);
            }
            reader.close();
        } catch (IOException ex) {
            Log.e("NAMES", "Error on loading names", ex);
        }

    }

    public String getName(DayOfYear dof) {
        return names.get(dof);
    }

    public List<DayOfYear> getKeys() {
        return keys;
    }

    public Map<DayOfYear, String> getNames() {
        return names;
    }

    public void setNames(Map<DayOfYear, String> names) {
        this.names = names;
    }

    public Map<String, CountryRecord> getCountries() {
        return countries;
    }
   
    public static class CountryRecord{
        public String name;
        public int rawId;

        public CountryRecord(String name, int rawId) {
            this.name = name;
            this.rawId = rawId;
        }
        
    }
}
