package iti.intake40.tritra.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginPresenter implements LoginContract.PresenterInterface {
LoginContract.ViewInterface viewInterface;
    public FirebaseAuth mAuth;


    public LoginPresenter(LoginContract.ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    @Override
    public void loginUser(String email, String password) {
        if(viewInterface.isNetworkAvailable()){
        mAuth = FirebaseAuth.getInstance();
        viewInterface.showProgress();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id = mAuth.getCurrentUser().getUid();
                    String email = mAuth.getCurrentUser().getEmail();
                    System.out.println(email);
                    viewInterface.displayMessage("user logined successfully");
                    viewInterface.writeShredPreference(id,email);
                    viewInterface.showProgress();
                    viewInterface.redirectId(email,id);
                }
                else{
                    viewInterface.showProgress();
                    viewInterface.displayMessage("username and password doesn't matches");

                }

            }
        });
    }
    else{
        viewInterface.displayMessage("check network connection");
    }
    }


}
