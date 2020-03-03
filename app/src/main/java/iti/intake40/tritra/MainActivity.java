package iti.intake40.tritra;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import iti.intake40.tritra.home.HomeActivity;
import iti.intake40.tritra.login.LoginActivity;
import iti.intake40.tritra.signup.SignUp;
import android.view.View;
import android.widget.Button;
import iti.intake40.tritra.login.LoginActivity;
import iti.intake40.tritra.signup.SignUp;


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
        signup=findViewById(R.id.next);
        login=findViewById(R.id.button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(MainActivity.this, SignUp.class);
                startActivity(i);


            }
        });
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
    Button signup,login;
    public void signIn(View view) {
        Intent i=new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }
    ///////
}
