package iti.intake40.tritra.home;

import java.util.List;

import iti.intake40.tritra.model.TripModel;

public interface HomeContract {

    interface PresenterInterface {
        void setTrips(List<TripModel> trips);
        void getTrips(String userId);
        void editTrip(int pos);
        void deleteTrip(int pos,String userId);
        void moveTripToHistory(TripModel tripModel,String userId);
    }

    interface ViewInterface {
        void displayTrips(List<TripModel> tripsList);
        void displayNoTrips();
        void displayMessage(String msg);

    }
}
