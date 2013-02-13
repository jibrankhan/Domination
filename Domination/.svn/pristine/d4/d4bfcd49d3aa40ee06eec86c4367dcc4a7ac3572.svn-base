package net.yura.domination.android;

import com.google.android.gcm.GCMRegistrar;

import net.yura.domination.mobile.flashgui.DominationMain;
import net.yura.lobby.client.AndroidLobbyClient;
import net.yura.lobby.client.Connection;
import net.yura.lobby.mini.MiniLobbyClient;
import net.yura.mobile.gui.Midlet;
import android.content.Context;

public class GCMServerUtilities implements AndroidLobbyClient {

    public static void register(Context context, String registrationId) {
        Connection con = getLobbyConnection();
        con.addAndroidEventListener(new GCMServerUtilities(context));
        con.androidRegister(registrationId);
    }

    public static void unregister(Context context, String registrationId) {
        Connection con = getLobbyConnection();
        con.addAndroidEventListener(new GCMServerUtilities(context));
        con.androidUnregister(registrationId);
    }

    static Connection getLobbyConnection() {
        MiniLobbyClient lobby = ((DominationMain)Midlet.getMidlet()).adapter.lobby;
        if (lobby!=null) {
            return lobby.mycom;
        }
        return null;
    }

    private Context context;
    public GCMServerUtilities(Context context) {
        this.context = context;
    }
    
    @Override
    public void registerDone() {
        GCMRegistrar.setRegisteredOnServer(context, true);
    }

    @Override
    public void unregisterDone() {
        GCMRegistrar.setRegisteredOnServer(context, false);
    }

}
