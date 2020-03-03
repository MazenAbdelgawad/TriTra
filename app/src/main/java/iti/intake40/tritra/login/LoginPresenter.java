package iti.intake40.tritra.login;

import android.content.Intent;

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
        viewInterface.showProgress();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    viewInterface.displayMessage("user logined successfully");
                }
                else{
                    viewInterface.displayMessage("username and password doesn't matches");
                }

            }
        });
    }
}
