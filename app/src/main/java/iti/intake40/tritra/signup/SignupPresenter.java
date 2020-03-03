package iti.intake40.tritra.signup;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import iti.intake40.tritra.MainActivity;


public class SignupPresenter implements SignupContract.PresenterInterface {
SignupContract.ViewInterface viewInterface;
    public  FirebaseAuth mAuth;

    public SignupPresenter(SignupContract.ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

   @Override
    public void signUpUser(String email, String password) {
       mAuth = FirebaseAuth.getInstance();
       viewInterface.showProgress();
       mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   viewInterface.displayMessage("user created successfully");
                  // startActivity(new Intent(getApplicationContext(), MainActivity.class));
                   String user_id=mAuth.getCurrentUser().getUid();
                   viewInterface.redirectId(user_id);
               }
               else{
                   viewInterface.displayMessage("user already exist");
               }

           }
       });
    }


}
