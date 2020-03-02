package iti.intake40.tritra.home;

import java.util.List;

import iti.intake40.tritra.model.TripModel;

public interface HomeContract {

    interface PresenterInterface {
        public void getTrips();

    }

    interface ViewInterface {
        public void displayTrips(List<TripModel> tripsList);
        public void displayNoTrips();

    }
}
