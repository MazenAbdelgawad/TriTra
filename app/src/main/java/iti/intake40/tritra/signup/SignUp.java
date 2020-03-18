package iti.intake40.tritra.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import iti.intake40.tritra.R;
import iti.intake40.tritra.login.LoginActivity;
import iti.intake40.tritra.model.UserModle;

public class SignUp extends AppCompatActivity implements SignupContract.ViewInterface{
    EditText name,password,confirm_password,email;
    Button signup,face;
    TextView login;
    SignupContract.PresenterInterface presenterInterface;
    ProgressBar progressBar;

    @Override
    protected void onStart() {
        super.onStart();
        presenterInterface=new SignupPresenter(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUpViews();

       /* if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }*/
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isValidate();

            }

        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void setUpViews(){
        name=findViewById(R.id.name_txt);
        email=findViewById(R.id.email);
        password=findViewById(R.id.Password);
        confirm_password=findViewById(R.id.confirm_password_txt);
        signup=findViewById(R.id.signup_btn);
        progressBar=findViewById(R.id.progressBar);
        face=findViewById(R.id.face_btn);
        login=findViewById(R.id.login_btn);

    }


    public void isValidate() {
        String email_str = email.getText().toString().trim();
        String password_str = password.getText().toString().trim();
        String name_str = name.getText().toString().trim();
        String confirm_password_str = confirm_password.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!(email_str.matches(emailPattern))) {
            email.setError("Invalid email address");
            return;

        }

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
        if (!password_str.equals(confirm_password_str)) {
            confirm_password.setError("password doesn't match");
            password.setText("");
            confirm_password.setText("");
            return;

        }
        if (TextUtils.isEmpty(name_str)) {
            name.setError("name is required");
            return;
        }
        UserModle userModle = new UserModle("id",name_str,email_str);
        presenterInterface.signUpUser(userModle, password_str);

    }


    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void redirectId(String s) {
        Intent intent=new Intent(SignUp.this,LoginActivity.class);
        intent.putExtra("userid",s);
        startActivity(intent);
    }

    @Override
    public void showProgress() {
        if (progressBar.getVisibility() == View.VISIBLE)
            progressBar.setVisibility(View.INVISIBLE);
        else
            progressBar.setVisibility(View.VISIBLE);
    }
}

