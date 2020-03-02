package iti.intake40.tritra.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

import iti.intake40.tritra.R;
import iti.intake40.tritra.home.RecyclerAdapter;
import iti.intake40.tritra.model.TripModel;

public class HomeActivity extends AppCompatActivity implements HomeContract.ViewInterface{

    HomeContract.PresenterInterface presenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        presenter = new HomePresenter(this);
        presenter.getTrips();

    }


    @Override
    public void displayTrips(List<TripModel> tripsList) {
        RecyclerAdapter adapter = new RecyclerAdapter(tripsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.home_recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void displayNoTrips() {
        //TODO: displayNoTrips
    }
}
