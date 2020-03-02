package iti.intake40.tritra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import iti.intake40.tritra.home.HomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Mazen

        ///////////

        //Zeyad

        /////////

        //Awatef

        ///////
    }
    ///////////////////////////////
    //Mazen
    public void openHome(View view) {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    ///////////

    //Zeyad

    /////////

    //Awatef

    ///////
}
