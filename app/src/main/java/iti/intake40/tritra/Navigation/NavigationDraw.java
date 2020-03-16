package iti.intake40.tritra.Navigation;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import iti.intake40.tritra.MainActivity;
import iti.intake40.tritra.R;
import iti.intake40.tritra.history.HistoryFragment;
import iti.intake40.tritra.home.HomeFragment;
import iti.intake40.tritra.login.LoginActivity;

public class NavigationDraw extends AppCompatActivity  {
    public static final String EMAil = "EMAIL";
    String email;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Intent intent = getIntent();

         email=intent.getStringExtra(EMAil);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.title);
        navUsername.setText("TriTra");
        View sub_title = navigationView.getHeaderView(0);
        TextView nav_email = sub_title.findViewById(R.id.sub_title);
        nav_email.setText(email);

       /* View headerImg = navigationView.getHeaderView(0);
        ImageView img = headerImg.findViewById(R.id.imageView);
        img.setBackgroundResource(R.drawable.tritra);*/

        HomeFragment home=new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace( R.id.nav_host_fragment,home).commit();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment selectedFregment=null;
                switch (menuItem.getItemId()) {
                    case R.id.nav_share:
                        logout();
                        Toast.makeText(NavigationDraw.this,"Log Out",Toast.LENGTH_LONG).show();
                        break;
                    case  R.id.nav_home:
                        selectedFregment=new HomeFragment();
                        getSupportFragmentManager().beginTransaction().replace( R.id.nav_host_fragment,selectedFregment).commit();
                        break;
                    case R.id.nav_gallery:
                        selectedFregment=new HistoryFragment() ;
                        getSupportFragmentManager().beginTransaction().replace( R.id.nav_host_fragment,selectedFregment).commit();
                        break;
                }
                drawer.closeDrawers();
                return true;

            }
        });
    }
    public void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(NavigationDraw.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}
