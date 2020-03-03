package iti.intake40.tritra.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import iti.intake40.tritra.MainActivity;
import iti.intake40.tritra.R;
import iti.intake40.tritra.signup.SignupPresenter;

public class LoginActivity extends AppCompatActivity implements LoginContract.ViewInterface {
    LoginContract.PresenterInterface presenterInterface;
    ImageView img;
    EditText email,password;
    Button login,google,face;
    TextView signup;
    ProgressBar progressBar;
    @Override
    protected void onStart() {
        super.onStart();
        presenterInterface=new LoginPresenter(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupViews();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isValidate();


            }
        });



    }
    private void setupViews(){
        img=findViewById(R.id.img);
        email=findViewById(R.id.name_txt);
        password=findViewById(R.id.email_txt);
        login=findViewById(R.id.login_btn);
        google=findViewById(R.id.google_btn);
        face=findViewById(R.id.face_btn);
        signup=findViewById(R.id.link_txv);
        progressBar=findViewById(R.id.progressBar);
    }

    public void isValidate() {

        String email_str = email.getText().toString().trim();
        String password_str = password.getText().toString().trim();

        if (TextUtils.isEmpty(email_str)) {
            email.setError("email is required");
            return;
        }
        if (TextUtils.isEmpty(password_str)) {
            password.setError("password is required");
            return;
        }
        if (password_str.length() < 6) {
            password.setError("password must be >= 6 characters");
            password.setText("");
            return;
        }
        presenterInterface.loginUser(email_str, password_str);


    }
    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        if (progressBar.getVisibility() == View.VISIBLE)
            progressBar.setVisibility(View.INVISIBLE);
        else
            progressBar.setVisibility(View.VISIBLE);
    }

}
