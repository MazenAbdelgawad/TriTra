package iti.intake40.tritra.alarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String BOOT_COMPLETED =
            "android.intent.action.BOOT_COMPLETED";
    private static final String QUICKBOOT_POWERON =
            "android.intent.action.QUICKBOOT_POWERON";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Reached",Toast.LENGTH_LONG).show();
        String action = intent.getAction();
        if (BOOT_COMPLETED.equals(action) || QUICKBOOT_POWERON.equals(action)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setAlarms(context,intent);
            }
        }else {
            checkIntent(context,intent);
        }
    }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private void setAlarms(Context context,Intent intent){
            Calendar myAlarmDate = Calendar.getInstance();
            myAlarmDate.set(2020, 2, 15, 2 , 36
                    ,0);
            AlarmManager tripAlarmManager =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent tripAlarmIntent = new Intent(context, AlarmActivity.class);
            PendingIntent tripAlarmPendingIntent = PendingIntent.getActivity(context, 0,tripAlarmIntent, 0);
            tripAlarmManager.setExact(AlarmManager.RTC_WAKEUP, myAlarmDate.getTimeInMillis(), tripAlarmPendingIntent);
        }

        private void checkIntent(Context context,Intent intent){
            int notificationId = intent.getIntExtra("NotificationId", 0);
            if (notificationId == 1) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notificationId);
                int actionButtonId = intent.getIntExtra("ActionButtonId", 0);
                if (actionButtonId == 1) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + 48.860294 + "," + 2.338629 + "&daddr=" + 48.858093 + "," + 2.294694));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
                Intent closeIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                context.sendBroadcast(closeIntent);
            }else{
                Intent tripAlarmIntent = new Intent(context, AlarmActivity.class);
                tripAlarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(tripAlarmIntent);
            }

        }
    }
