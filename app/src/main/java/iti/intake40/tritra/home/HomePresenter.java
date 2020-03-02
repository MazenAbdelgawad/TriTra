package iti.intake40.tritra.home;

import java.util.ArrayList;
import java.util.List;

import iti.intake40.tritra.model.TripModel;

public class HomePresenter implements HomeContract.PresenterInterface {

    private HomeContract.ViewInterface viewInterface;
    private List<TripModel> tripsList;


    public HomePresenter(HomeContract.ViewInterface viewInterface) {
        this.viewInterface = viewInterface;

        //Dumy data this comr from db
        tripsList = new ArrayList<>();
        for (int i =0; i < 6; i++){
            TripModel trip = new TripModel("Trip ("+i+") ","One Way Trip","ITI - Ismailia",
                    "ITI - Smart Valige","20-02-2020","02:20 PM");
            tripsList.add(trip);
        }
    }


    @Override
    public void getTrips() {
        if(tripsList != null && tripsList.size() > 0){
            viewInterface.displayTrips(tripsList);
        }else {
            viewInterface.displayNoTrips();
        }
    }
}
