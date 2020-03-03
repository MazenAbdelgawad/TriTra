package iti.intake40.tritra.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.List;

import iti.intake40.tritra.R;
import iti.intake40.tritra.home.RecyclerAdapter;
import iti.intake40.tritra.model.TripModel;

public class HomeActivity extends AppCompatActivity implements HomeContract.ViewInterface , CardMenuInterface{

    HomeContract.PresenterInterface presenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        presenter = new HomePresenter(this);
        presenter.getTrips();

    }

    @Override
    public void displayMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayTrips(List<TripModel> tripsList) {
        RecyclerAdapter adapter = new RecyclerAdapter(tripsList,this);
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

    @Override
    public void onPopupMenuClick(View view, final int pos) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.card_menu_edit:
                        Toast.makeText(HomeActivity.this, "Menu EDIT"+pos, Toast.LENGTH_SHORT).show();
                        presenter.editTrip(pos);
                        return true;
                    case R.id.card_menu_delete:
                        Toast.makeText(HomeActivity.this, "Menu DELETE"+pos, Toast.LENGTH_SHORT).show();
                        presenter.deleteTrip(pos);
                        return true;
                    default:
                        Toast.makeText(HomeActivity.this, "Menu Error", Toast.LENGTH_SHORT).show();
                        return false;
                }
            }
        });
        popup.show();
    }
}
