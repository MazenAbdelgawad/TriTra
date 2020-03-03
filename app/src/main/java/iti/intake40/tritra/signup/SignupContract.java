package iti.intake40.tritra.signup;

public interface SignupContract {

    interface PresenterInterface {
 void signUpUser(String email,String password);


    }

    interface ViewInterface {
        void displayMessage(String message);
        void redirectId(String s);
         void showProgress();
    }
}
