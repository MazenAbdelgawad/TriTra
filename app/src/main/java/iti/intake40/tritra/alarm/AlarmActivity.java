package iti.intake40.tritra.alarm;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import java.util.Random;

import iti.intake40.tritra.R;
import iti.intake40.tritra.add_trip.AddTripActivity;
import iti.intake40.tritra.floating_head.HeadService;


public class AlarmActivity extends Activity {

    Uri tripNotification;
    Ringtone tripAlarmRingtone;

    final String NOTIFICATION_CHANNEL_ID = "Alarm_Notification_Channel";
    public static final String NOTIFICATION_ID = "NotificationId";
    public static final String NOTIFICATION_TAG = "NotificationTag";
    public static final String ACTION_BUTTON_ID = "ActionButtonId";

    Intent tripIntent;
    String tripAlertTitle;
    String tripAlertInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripIntent = getIntent();
        //Android 8.0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        displayAlert();
    }

    private void displayAlert() {
        playAlarmRingtone();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        configAlertBuilder(alertDialogBuilder);
        AlertDialog tripAlertDialog = alertDialogBuilder.create();
        tripAlertDialog.setCanceledOnTouchOutside(false);
       // tripAlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        tripAlertDialog.show();
    }

    private void playAlarmRingtone() {
        tripNotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        tripAlarmRingtone = RingtoneManager.getRingtone(this.getApplicationContext(), tripNotification);
        tripAlarmRingtone.play();
    }

    private void configAlertBuilder(AlertDialog.Builder alertDialogBuilder) {
        String tripTitle = tripIntent.getStringExtra(AddTripActivity.TRIP_NAME);
        String tripStartPoint = tripIntent.getStringExtra(AddTripActivity.TRIP_START_POINT);
        String tripEndPoint = tripIntent.getStringExtra(AddTripActivity.TRIP_END_POINT);
        tripAlertTitle = "Your trip " + tripTitle + " will begin";
        tripAlertInfo = "From " + tripStartPoint + " To " + tripEndPoint;
        alertDialogBuilder.setMessage(tripAlertTitle+"\n"+tripAlertInfo);
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                tripAlarmRingtone.stop();
                openMaps();
                Intent serviceIntent = new Intent(AlarmActivity.this, HeadService.class);
                startService(serviceIntent);
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("End", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                tripAlarmRingtone.stop();
                finish();
            }
        });
        alertDialogBuilder.setNeutralButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    showNotificationOreoAndAbove();
                }else{
                    showNotificationBelowOreo();
                }
                dialog.cancel();
                tripAlarmRingtone.stop();
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Alarm_Notifiacation_Channel", NotificationManager.IMPORTANCE_LOW);
        notificationChannel.setDescription("Alarm");
        notificationManager.createNotificationChannel(notificationChannel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationOreoAndAbove() {
        //int id = ((int) System.currentTimeMillis() * -1);
        int notificationId = ((int) System.currentTimeMillis() * -1);
        int actionButtonId = new Random().nextInt(1000);

        Intent startIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
        startIntent.putExtra(NOTIFICATION_ID,notificationId);
        startIntent.putExtra(NOTIFICATION_TAG, 1);
        startIntent.putExtra(ACTION_BUTTON_ID, 1);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent startPendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, actionButtonId, startIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent endIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
        endIntent.putExtra(NOTIFICATION_ID, notificationId);
        endIntent.putExtra(NOTIFICATION_TAG, 1);
        endIntent.putExtra(ACTION_BUTTON_ID, 2);
        endIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent endPendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, actionButtonId+1, endIntent, PendingIntent.FLAG_ONE_SHOT);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(AlarmActivity.this)
                .setOngoing(true)
                .setContentTitle(tripAlertTitle)
                .setContentText(tripAlertInfo)
                .setSmallIcon(R.drawable.tritra)
                .setLargeIcon(BitmapFactory.decodeResource(AlarmActivity.this.getResources(),
                        R.drawable.tritra))
                .setChannelId("Alarm_Notification_Channel")
                .addAction(R.mipmap.ic_launcher_round, "Start", startPendingIntent)
                .addAction(R.mipmap.ic_launcher_round, "End", endPendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(notificationId, notification);
    }

    private void showNotificationBelowOreo() {
        int id = (int) System.currentTimeMillis();
        int tripNotifacationID = id;

        Intent startIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
        startIntent.putExtra("NotificationId", tripNotifacationID);
        startIntent.putExtra("NotificationTag", 1);
        startIntent.putExtra("ActionButtonId", 1);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent startPendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, startIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent endIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
        startIntent.putExtra("NotificationId", tripNotifacationID);
        startIntent.putExtra("NotificationTag", 1);
        endIntent.putExtra("ActionButtonId", 2);
        endIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent endPendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 1, endIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                // Set the intent that will fire when the user taps the notification
               // .setContentIntent(pendingIntent)
                .addAction(R.mipmap.ic_launcher_round, "Start", startPendingIntent)
                .addAction(R.mipmap.ic_launcher_round, "End", endPendingIntent)
                .setAutoCancel(true);

//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//        // notificationId is a unique int for each notification that you must define
//        notificationManager.notify(tripNotifacationID, builder.build());

        // NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(tripNotifacationID, builder.build());


    }

    private void openMaps() {
        Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + 48.860294 + "," + 2.338629 + "&daddr=" + 48.858093 + "," + 2.294694));
        mapsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AlarmActivity.this.startActivity(mapsIntent);
    }
}
