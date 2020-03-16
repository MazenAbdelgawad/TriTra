package iti.intake40.tritra.login;

public interface LoginContract {

    interface PresenterInterface {
        void loginUser(String email,String password);
    }

    interface ViewInterface {
        void displayMessage(String message);
        void  showProgress();
        void redirectId(String s);
         void writeShredPreference();
    }
}
