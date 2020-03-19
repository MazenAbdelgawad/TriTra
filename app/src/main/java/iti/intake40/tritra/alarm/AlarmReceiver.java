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

import iti.intake40.tritra.add_trip.AddTripActivity;
import iti.intake40.tritra.floating_head.HeadService;
import iti.intake40.tritra.home.HomeFragment;
import iti.intake40.tritra.model.Database;
import iti.intake40.tritra.model.TripInterface;
import iti.intake40.tritra.model.TripModel;
import iti.intake40.tritra.notes.NoteActivity;

public class AlarmReceiver extends BroadcastReceiver implements TripInterface {

    private static final String BOOT_COMPLETED =
            "android.intent.action.BOOT_COMPLETED";
    private static final String QUICKBOOT_POWERON =
            "android.intent.action.QUICKBOOT_POWERON";

    TripModel trip;
    String userId;
    String tripId;
    Context context;
    Intent intent;
    int notificationId;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        String action = intent.getAction();
        if (BOOT_COMPLETED.equals(action) || QUICKBOOT_POWERON.equals(action)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setAlarms(context, intent);
            }
        } else {
            // getTrip();
            checkIntent(context, intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAlarms(Context context, Intent intent) {
        Calendar myAlarmDate = Calendar.getInstance();
        myAlarmDate.set(2020, 2, 15, 2, 36
                , 0);
        AlarmManager tripAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent tripAlarmIntent = new Intent(context, AlarmActivity.class);
        PendingIntent tripAlarmPendingIntent = PendingIntent.getActivity(context, 0, tripAlarmIntent, 0);
        tripAlarmManager.setExact(AlarmManager.RTC_WAKEUP, myAlarmDate.getTimeInMillis(), tripAlarmPendingIntent);
    }

    private void checkIntent(Context context, Intent intent) {
        int notificationTag = intent.getIntExtra(AlarmActivity.NOTIFICATION_TAG, 0);
        notificationId = intent.getIntExtra(AlarmActivity.NOTIFICATION_ID, 0);
        userId = intent.getStringExtra(HomeFragment.USERID);
        tripId = intent.getStringExtra(AddTripActivity.TRIP_ID);
        if (notificationTag == 1) {
            getTrip();
        } else {
            Intent tripAlarmIntent = configAlarmIntent(context, intent);
            context.startActivity(tripAlarmIntent);
        }
    }

    private Intent configAlarmIntent(Context context, Intent inIntent) {
        Intent tripAlarmIntent = new Intent(context, AlarmActivity.class);
        userId = inIntent.getStringExtra(HomeFragment.USERID);
        tripId = inIntent.getStringExtra(AddTripActivity.TRIP_ID);
        String tripTitle = inIntent.getStringExtra(AddTripActivity.TRIP_NAME);
        String tripStartPoint = inIntent.getStringExtra(AddTripActivity.TRIP_START_POINT);
        String tripEndPoint = inIntent.getStringExtra(AddTripActivity.TRIP_END_POINT);
        tripAlarmIntent.putExtra(HomeFragment.USERID, userId);
        tripAlarmIntent.putExtra(AddTripActivity.TRIP_ID, tripId);
        tripAlarmIntent.putExtra(AddTripActivity.TRIP_NAME, tripTitle);
        tripAlarmIntent.putExtra(AddTripActivity.TRIP_START_POINT, tripStartPoint);
        tripAlarmIntent.putExtra(AddTripActivity.TRIP_END_POINT, tripEndPoint);
        tripAlarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return tripAlarmIntent;
    }

    private void cancelNotification(int notificationId, Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }

    private void performStartAction(Context context) {
        Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + trip.getEndPoint()));
        mapsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mapsIntent);
        if (trip.getType().equals(TripModel.TYPE.ROUND_TRIP) && trip.getStatus().equals(TripModel.STATUS.UPCOMING)) {
            trip.setStatus(TripModel.STATUS.GO);
            Database.getInstance().createRuturnTrip(trip, userId);
        } else {
            trip.setStatus(TripModel.STATUS.DONE);
            Database.getInstance().addTripHistory(trip, userId);
        }
        //Cancel Alarm
        Intent closeIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeIntent);
        Intent serviceIntent = new Intent(context, HeadService.class);
        serviceIntent.putExtra(NoteActivity.TRIP_ID_KEY,tripId);
        context.startService(serviceIntent);

    }

    private void performEndAction(Context context) {
        trip.setStatus(TripModel.STATUS.CANCEL);
        Database.getInstance().addTripHistory(trip, userId);
    }

    public void getTrip() {
        Database.getInstance().getTripForEdit(userId, tripId, this);
    }

    @Override
    public void SetTripForEdit(TripModel trip) {
        if (trip != null) {
            this.trip = trip;
            cancelNotification(notificationId, context);
            int actionButtonId = intent.getIntExtra("ActionButtonId", 0);
            if (actionButtonId == 1) {
                performStartAction(context);
            }else{
                performEndAction(context);
            }
        }
    }
}
