package iti.intake40.tritra.home;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import iti.intake40.tritra.R;
import iti.intake40.tritra.model.Database;
import iti.intake40.tritra.model.TripModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeContract.ViewInterface , CardMenuInterface{

    HomeContract.PresenterInterface presenter;
    LinearLayout noTripsLayout;
    FloatingActionButton fabAddTrip;
    RecyclerView recyclerView;
    String userId;
    public static final String USERID = "USERID";



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_home, container, false);

        presenter = new HomePresenter(this);

        userId = getActivity().getIntent().getStringExtra(USERID);
        presenter.getTrips(userId);

        noTripsLayout = root.findViewById(R.id.no_trips_layout);
        recyclerView = root.findViewById(R.id.home_recyclerview);

        fabAddTrip = root.findViewById(R.id.fab_add_trip);
        fabAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "ADD TRIP UID=" + userId, Toast.LENGTH_SHORT).show();
                TripModel trip = new TripModel();
                Database.getInstance().addTrip(trip, userId);
            }
        });
        return root;
    }

    @Override
    public void displayMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayTrips(List<TripModel> tripsList) {
        RecyclerAdapter adapter = new RecyclerAdapter(tripsList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        noTripsLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void displayNoTrips() {
        //TODO: displayNoTrips
        Toast.makeText(getContext(), "NO TRIP!!!!!!", Toast.LENGTH_SHORT).show();
        noTripsLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPopupMenuClick(View view, final int pos) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.card_menu_edit:
                        Toast.makeText(getContext(), "Menu EDIT"+pos, Toast.LENGTH_SHORT).show();
                        presenter.editTrip(pos);
                        return true;
                    case R.id.card_menu_delete:
                        Toast.makeText(getContext(), "Menu DELETE"+pos, Toast.LENGTH_SHORT).show();
                        presenter.deleteTrip(pos, userId);
                        return true;
                    default:
                        Toast.makeText(getContext(), "Menu Error", Toast.LENGTH_SHORT).show();
                        return false;
                }
            }
        });
        popup.show();
    }

}