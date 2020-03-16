package iti.intake40.tritra.history;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import iti.intake40.tritra.R;
import iti.intake40.tritra.history_note_dialog.NoteDialogActivity;
import iti.intake40.tritra.home.HomeFragment;
import iti.intake40.tritra.model.TripModel;
import iti.intake40.tritra.notes.NoteActivity;


public class HistoryFragment extends Fragment implements HistoryContract.ViewInterface, NoteInterface{
    HistoryContract.PresenterInterface presenter;
    LinearLayout noTripsLayout;
    RecyclerView recyclerView;
    String userId;

    public HistoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        presenter = new HistoryPresenter(this);

        userId = getActivity().getIntent().getStringExtra(HomeFragment.USERID);
        presenter.getTrips(userId);

        noTripsLayout = root.findViewById(R.id.no_trips_layout);
        recyclerView = root.findViewById(R.id.history_recyclerview);

        return root;
    }

    @Override
    public void displayTrips(List<TripModel> tripsList) {
        HistoryRecyclerAdapter adapter = new HistoryRecyclerAdapter(tripsList, this);
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
    public void displayMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void openNote(String tripId) {
        Intent intent = new Intent(getContext(), NoteDialogActivity.class);
        intent.putExtra(NoteActivity.TRIP_ID_KEY,"-M1pmfUjMRPGHlG5SKow"); //chnge with id
        startActivity(intent);
    }
}
