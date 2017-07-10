package rathore.todoreminder.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import rathore.todoreminder.R;
import rathore.todoreminder.activity.MainActivity;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm completed", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("ID",intent.getIntExtra("ID",0));
        i.putExtra("position",intent.getIntExtra("position",0));
        i.putExtra("flag",1);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("You got a Reminder");
        builder.setContentText(intent.getStringExtra("msg"));
        builder.setSmallIcon(R.mipmap.reminder);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
//        builder.setOngoing(true); // for nikkma notification


        Notification notification = builder.build();
        notification.vibrate = new long[]{100, 2334, 556, 3453};

        int randomId = (int) (Math.random() * 1000);


        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(randomId, notification);

        try {
            Uri notificationUri =
                    Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                            + "://" + context.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(context, notificationUri);
            r.play();
        }
        catch (Exception e)
        {

        }
    }
}
