/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.names;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Service to
 *
 * @author elek
 */
@Deprecated
public class NameService extends Service {

    private Namedays namedays;

    int updateno = 0;

    public static final String UPDATE = "update";

    @Override
    public void onStart(Intent intent, int startId) {       
        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
