package iti.intake40.tritra.signup;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import iti.intake40.tritra.model.Database;
import iti.intake40.tritra.model.UserModle;


public class SignupPresenter implements SignupContract.PresenterInterface {
SignupContract.ViewInterface viewInterface;
    FirebaseAuth mAuth;

    public SignupPresenter(SignupContract.ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

   @Override
    public void signUpUser(final UserModle user, String password) {
       if(viewInterface.isNetworkAvailable()){
       mAuth = FirebaseAuth.getInstance();
       viewInterface.showProgress();
       mAuth.createUserWithEmailAndPassword(user.getEmail(),password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   viewInterface.displayMessage("user created successfully");
                  // startActivity(new Intent(getApplicationContext(), MainActivity.class));
                   user.setId(mAuth.getCurrentUser().getUid());
                   Database.getInstance().addUser(user);
                   viewInterface.redirectId(user.getId());

               }
               else{
                   viewInterface.displayMessage("user already exist");
                   viewInterface.showProgress();
               }

           }
       });
    }else{
           viewInterface.displayMessage("check network connection");
       }
    }


}
