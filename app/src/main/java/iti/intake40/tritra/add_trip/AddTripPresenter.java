package iti.intake40.tritra.add_trip;

import iti.intake40.tritra.model.Database;
import iti.intake40.tritra.model.TripModel;

public class AddTripPresenter implements AddTripContract.PresenterInterface {
    AddTripContract.ViewInterface viewInterface;

    public AddTripPresenter(AddTripContract.ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    @Override
    public void addTrip(TripModel tripModel,String userId) {
        Database.getInstance().addTrip(tripModel, userId);
    }

}
