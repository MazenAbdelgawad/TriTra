package iti.intake40.tritra.add_trip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.Arrays;
import java.util.Calendar;

import iti.intake40.tritra.R;
import iti.intake40.tritra.alarm.AlarmReceiver;

public class AddTripActivity extends AppCompatActivity implements AddTripContract.ViewInterface {
   // private String apiKey="AIzaSyCX00aiZAeqt9sbXM-0JGjk4evA54bKS6I"; //me
    //private String apiKey="AIzaSyBHn174_ktTUup-lFD_cO07b2cyx1_zmXE"; //zezo
    private String apiKey="AIzaSyBCXNUjza_-JQWSpFhvMgzpXQqgifH9qak"; //Awatef
    public static final String TAG = "TAG_AUTOSEARCH";

    public static final String TRIP_NAME = "TRIP_NAME";
    public static final String TRIP_START_POINT = "TRIP_START_POINT";
    public static final String TRIP_END_POINT = "TRIP_END_POINT";
    public static final String ALARM_ID = "ALARM_ID";

    TextInputEditText txtTripName;
    Button btnSave;
    Button btnAddNote;
    Button btnDate;
    Button btnTime;
    MaterialTextView txtDate;
    MaterialTextView txtTime;
    Calendar calendar;
    AlarmManager alarmManager;
    int tripYear;
    int tripMonth;
    int tripDay;
    int tripHour;
    int tripMinute;
    int counter = 0 ;
    String tripStartPoint;
    String tripEndPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        calendar = Calendar.getInstance();

        txtTripName = findViewById(R.id.txt_trip_name);
        btnSave = findViewById(R.id.btn_save);
        btnAddNote = findViewById(R.id.btn_add_note);
        btnDate = findViewById(R.id.btn_date);
        btnTime = findViewById(R.id.btn_time);
        txtDate = findViewById(R.id.txt_date);
        txtTime = findViewById(R.id.txt_time);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTripActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                tripYear = year ;
                                tripMonth = monthOfYear;
                                tripDay = dayOfMonth;
                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        },calendar.get(Calendar.YEAR) , calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTripActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                tripHour = hourOfDay;
                                tripMinute = minute;
                                txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlarm();
                Toast.makeText(AddTripActivity.this,"Alarm set on" + tripHour + tripMinute,Toast.LENGTH_LONG).show();
            }
        });


        // Initialize the SDK
        Places.initialize(getApplicationContext(), apiKey);
        // Create a new Places client instance
        PlacesClient placesClient = Places.createClient(this);


        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment acfStartPoint = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_startpoint);

        acfStartPoint.setHint(getString(R.string.select_start_point));
        // Specify the types of place data to return.
        acfStartPoint.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        acfStartPoint.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "S Place: " + place.getName() + ", " + place.getId());
                Log.i(TAG, "S Place ad: " + place.getAddress() + ", " + place.getAddressComponents());
                //Log.i(TAG, "S Place latu: " + place.getLatLng().latitude + ", long: " + place.getLatLng().longitude);
                tripStartPoint = place.getName();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        //////////////////////////////////////////////////////////////////
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment acfEndpoint = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_endpoint);

        acfEndpoint.setHint(getString(R.string.select_end_point));
        // Specify the types of place data to return.
        acfEndpoint.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        acfEndpoint.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                tripEndPoint = place.getName();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void createAlarm(){
        setCalender();
        Intent tripAlarmIntent = configAlarmIntent();
        int tripAlarmPendintgIntentRequestCode = generateId();
        PendingIntent tripAlarmPendingIntent = PendingIntent.getBroadcast(AddTripActivity.this
                ,tripAlarmPendintgIntentRequestCode,
                tripAlarmIntent,
                PendingIntent.FLAG_ONE_SHOT);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //Android kitkat or above
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),tripAlarmPendingIntent);
        }else{
            //Android bel
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),tripAlarmPendingIntent);
        }
    }

    private void setCalender(){
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(tripYear,tripMonth,tripDay,tripHour,tripMinute,0);
    }

    private Intent configAlarmIntent(){
        Intent tripAlarmIntent = new Intent(AddTripActivity.this, AlarmReceiver.class);
        tripAlarmIntent.putExtra(TRIP_NAME,txtTripName.getText().toString());
        tripAlarmIntent.putExtra(TRIP_START_POINT,tripStartPoint);
        tripAlarmIntent.putExtra(TRIP_END_POINT,tripEndPoint);
        return  tripAlarmIntent;
    }

    private int generateId(){
        counter += 1;
        return counter;
    }


}
