package miage.projetindustriel.firebase;



import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import miage.projetindustriel.R;
import miage.projetindustriel.controller.Login;

/**
 * Created by Asus on 18/01/2017.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        traitementMessage(remoteMessage.getData().get("message"));
    }

    private void traitementMessage(String message) {
        Intent i = new Intent(this,Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Musy")
                .setContentText(message)
                //.setSmallIcon(R.drawable.ic_play_dark)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());
    }

}
