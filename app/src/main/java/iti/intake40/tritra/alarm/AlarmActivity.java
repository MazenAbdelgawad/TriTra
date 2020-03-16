package iti.intake40.tritra.alarm;

import androidx.annotation.RequiresApi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import iti.intake40.tritra.R;
import iti.intake40.tritra.floating_head.HeadService;

public class AlarmActivity extends Activity {

    Uri tripNotification;
    Ringtone tripAlarmRingtone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
      //  tripAlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        tripAlertDialog.show();
    }

    private void playAlarmRingtone(){
        tripNotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        tripAlarmRingtone = RingtoneManager.getRingtone(this.getApplicationContext(), tripNotification);
        tripAlarmRingtone.play();
    }

    private void configAlertBuilder(AlertDialog.Builder alertDialogBuilder){
        alertDialogBuilder.setMessage("Your Trip Will Begin Be Ready");
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
                    showNotification();
                }
                dialog.cancel();
                tripAlarmRingtone.stop();
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(){
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel("Alarm_Notification_Channel","Alarm_Notifiacation_Channel",NotificationManager.IMPORTANCE_LOW);
        notificationChannel.setDescription("Alarm");
        notificationManager.createNotificationChannel(notificationChannel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNotification() {

        Intent startIntent = new Intent(AlarmActivity.this,AlarmReceiver.class);
        startIntent.putExtra("NotificationId",1);
        startIntent.putExtra("ActionButtonId",1);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent startPendingIntent = PendingIntent.getBroadcast(AlarmActivity.this,0,startIntent,PendingIntent.FLAG_ONE_SHOT);

        Intent endIntent = new Intent(AlarmActivity.this,AlarmReceiver.class);
        endIntent.putExtra("NotificationId",1);
        endIntent.putExtra("ActionButtonId",2);
        endIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent endPendingIntent = PendingIntent.getBroadcast(AlarmActivity.this,1,endIntent,PendingIntent.FLAG_ONE_SHOT);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int tripNotifacationID = 1;
        Notification notification = new Notification.Builder(AlarmActivity.this)
                .setOngoing(true)
                .setContentTitle("Your trip is ready")
                .setContentText("You received new notification")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setChannelId("Alarm_Notification_Channel")
                .addAction(R.mipmap.ic_launcher_round,"Start",startPendingIntent)
                .addAction(R.mipmap.ic_launcher_round,"End",endPendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(tripNotifacationID, notification);
    }

    private void openMaps(){
        Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + 48.860294 + "," + 2.338629 + "&daddr=" + 48.858093 + "," + 2.294694));
        mapsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AlarmActivity.this.startActivity(mapsIntent);
    }
}
