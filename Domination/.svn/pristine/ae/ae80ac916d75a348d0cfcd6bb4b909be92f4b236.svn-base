package net.yura.domination.android;

import static net.yura.domination.android.GCMActivity.SENDER_ID;
import static net.yura.domination.android.GCMActivity.displayMessage;
import net.yura.domination.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

/**
 * @see com.google.android.gcm.demo.app.GCMIntentService
 */
public class GCMIntentService extends GCMBaseIntentService {

    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        displayMessage(context,"Device registered: regId = "+registrationId);
        GCMServerUtilities.register(context, registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        displayMessage(context, "Device unregistered");
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            GCMServerUtilities.unregister(context, registrationId);
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
            displayMessage(context, "Ignoring unregister callback");
        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
	String msg = intent.getExtras().getString("message");
        String message = msg==null?"Received message":msg;
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        String message = "Received deleted messages notification "+total;
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    @Override
    public void onError(Context context, String errorId) {
        displayMessage(context, "Received error: "+errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        displayMessage(context, "Received recoverable error: "+errorId);
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.icon;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(context, net.yura.android.AndroidMeActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        //notification.defaults|= Notification.DEFAULT_SOUND;
        //notification.defaults|= Notification.DEFAULT_LIGHTS;
        //notification.defaults|= Notification.DEFAULT_VIBRATE;
        notification.defaults = Notification.DEFAULT_ALL;
        
        notificationManager.notify(0, notification);
    }

}
