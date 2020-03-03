package iti.intake40.tritra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import iti.intake40.tritra.login.LoginActivity;
import iti.intake40.tritra.signup.SignUp;

public class MainActivity extends AppCompatActivity {
Button signup,login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signup=findViewById(R.id.next);
        login=findViewById(R.id.button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(MainActivity.this, SignUp.class);
                startActivity(i);


            }
        });
    }

    public void signIn(View view) {
        Intent i=new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
