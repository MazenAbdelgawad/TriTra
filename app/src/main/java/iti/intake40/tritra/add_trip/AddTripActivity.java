package iti.intake40.tritra.add_trip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.Arrays;
import java.util.Calendar;

import iti.intake40.tritra.R;
import iti.intake40.tritra.alarm.AlarmReceiver;
import iti.intake40.tritra.home.HomeFragment;
import iti.intake40.tritra.model.TripModel;

public class AddTripActivity extends AppCompatActivity implements AddTripContract.ViewInterface {
   // private String apiKey="AIzaSyCX00aiZAeqt9sbXM-0JGjk4evA54bKS6I"; //me
    //private String apiKey="AIzaSyBHn174_ktTUup-lFD_cO07b2cyx1_zmXE"; //zezo
    private String apiKey="AIzaSyBCXNUjza_-JQWSpFhvMgzpXQqgifH9qak"; //Awatef
    public static final String TAG = "TAG_AUTOSEARCH";
    TextInputEditText txtTripName;
    Button btnSave;
    Button btnAddNote;
    Button btnDate;
    Button btnTime;
    MaterialTextView txtDate;
    MaterialTextView txtTime;
    Calendar calendar;
    int tripYear;
    int tripMonth;
    int tripDay;
    int tripHour;
    int tripMinute;
    ToggleButton tglBtnTripType;
    Place startPoint;
    Place endPoint;
    NestedScrollView spinerContainerLayout;

    TripModel trip;
    String userId;

    AddTripContract.PresenterInterface presenterInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        presenterInterface = new AddTripPresenter(this);
        userId = getIntent().getStringExtra(HomeFragment.USERID);
        calendar = Calendar.getInstance();

        txtTripName = findViewById(R.id.txt_trip_name);
        btnSave = findViewById(R.id.btn_save);
        btnAddNote = findViewById(R.id.btn_add_note);
        btnDate = findViewById(R.id.btn_date);
        btnTime = findViewById(R.id.btn_time);
        txtDate = findViewById(R.id.txt_date);
        txtTime = findViewById(R.id.txt_time);
        tglBtnTripType = findViewById(R.id.tgl_btn_trip_type);
        spinerContainerLayout = findViewById(R.id.spiner_container);

        trip = new TripModel();

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


        tglBtnTripType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    trip.setType(TripModel.TYPE.ROUND_TRIP);
                else
                    trip.setType(TripModel.TYPE.ONE_DIRECTION);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtTripName.getText())){
                    Snackbar.make(spinerContainerLayout, R.string.name_of_trip, Snackbar.LENGTH_SHORT).show();
                }else if(startPoint == null){
                    Snackbar.make(spinerContainerLayout, R.string.start_ponit_empty, Snackbar.LENGTH_SHORT).show();
                }else if(endPoint == null){
                    Snackbar.make(spinerContainerLayout, R.string.end_ponit_empty, Snackbar.LENGTH_SHORT).show();
                }else if(tripYear==0 || tripMonth==0 || tripDay==0){
                    Snackbar.make(spinerContainerLayout, R.string.date_empty, Snackbar.LENGTH_SHORT).show();
                }else if(tripHour==0 || tripMinute==0) {
                    Snackbar.make(spinerContainerLayout, R.string.time_empty, Snackbar.LENGTH_SHORT).show();
                }else{
                    trip.setName(txtTripName.getText().toString());
                    trip.setStartPoint(startPoint.getName());
                    trip.setEndPoint(endPoint.getName());
                    trip.setDate(tripYear+"-"+tripMonth+"-"+tripDay);
                    trip.setTime(tripHour+":"+tripMinute);
                    trip.setStatus(TripModel.STATUS.UPCOMING);
                    presenterInterface.addTrip(trip,userId);
                    createAlarm();
                    Toast.makeText(AddTripActivity.this,"Alarm set on" + tripHour + tripMinute,Toast.LENGTH_LONG).show();
                }
            }
        });

        //StartPoint
        Places.initialize(getApplicationContext(), apiKey);
        PlacesClient placesClient = Places.createClient(this);
        final AutocompleteSupportFragment acfStartPoint = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_startpoint);
        acfStartPoint.setHint(getString(R.string.select_start_point));
        acfStartPoint.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        acfStartPoint.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                startPoint = place;
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(AddTripActivity.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        //EndPoint
        AutocompleteSupportFragment acfEndpoint = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_endpoint);
        acfEndpoint.setHint(getString(R.string.select_end_point));
        acfEndpoint.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        acfEndpoint.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                endPoint = place;
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(AddTripActivity.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void createAlarm(){
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(tripYear,tripMonth,tripDay,tripHour,tripMinute,0);
        Intent tripAlarmIntent = new Intent(AddTripActivity.this, AlarmReceiver.class);
        PendingIntent tripAlarmPendingIntent = PendingIntent.getBroadcast(AddTripActivity.this,0,tripAlarmIntent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),tripAlarmPendingIntent);
        }else{
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),tripAlarmPendingIntent);
        }
    }


}
