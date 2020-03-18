package iti.intake40.tritra.add_trip;

import iti.intake40.tritra.model.TripModel;

public interface AddTripContract {

    interface PresenterInterface {
        void addTrip(TripModel tripModel,String userId);
        void getTripForEdit(String userId,String tripId);
        void SetTripForEdit(TripModel tripModel);
        void updateTrip(TripModel tripModel,String userId);
    }

    interface ViewInterface {
        void SetTripForEdit(TripModel tripModel);
    }
}
