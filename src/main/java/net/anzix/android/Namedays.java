package net.anzix.android;

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

    public static final String LOGID = "Namedays";

    private List<DayOfYear> keys = new ArrayList();

    private Map<DayOfYear, String> names = new HashMap<DayOfYear, String>();

    public Namedays(Resources resources) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resources.openRawResource(R.raw.nevnap)));
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
}
