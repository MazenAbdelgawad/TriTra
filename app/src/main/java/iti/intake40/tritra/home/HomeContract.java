package iti.intake40.tritra.home;

import java.util.List;

import iti.intake40.tritra.model.TripModel;

public interface HomeContract {

    interface PresenterInterface {
        void getTrips();
        void editTrip(int pos);
        void deleteTrip(int pos);

    }

    interface ViewInterface {
        void displayTrips(List<TripModel> tripsList);
        void displayNoTrips();
        void displayMessage(String msg);

    }
}
