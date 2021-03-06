package com.example.parstagram;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.parstagram.fragments.ComposeFragment;
import com.example.parstagram.fragments.ProfileFragment;
import com.example.parstagram.fragments.TimelineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseUser;

public class HomeActivity extends AppCompatActivity {
    TextView tvLogout;
    static final String TAG = "Home Activity";
    private Menu menu;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final FragmentManager fragmentManager = getSupportFragmentManager();

        // define your fragments here
        final Fragment timelineFragment = new TimelineFragment();
        final Fragment composeFragment = new ComposeFragment();
        final Fragment profileFragment = new ProfileFragment();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tvLogout = findViewById(R.id.tvLogout);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                finish();
            }
        });
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.i(TAG, "Menu item selected");
                Fragment fragment = timelineFragment;
                if (item.getItemId() == R.id.menu_timeline){
                    //compose action has been selected
                    //showComposeFragment("");
                    Log.i(TAG, "timeline selected");
                    fragment = timelineFragment;
                }
                if (item.getItemId() == R.id.menu_compose){
                    //invalidateOptionsMenu();
                    //compose action has been selected
                    //showComposeFragment("");
                    Log.i(TAG, "compose selected");
                    fragment = composeFragment;
                    //bottomNavigationView.setVisibility(View.GONE);
                }
                if (item.getItemId() == R.id.menu_profile){
                    //compose action has been selected
                    //showComposeFragment("");
                    Log.i(TAG, "profile selected");
                    fragment = profileFragment;
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("user", ParseUser.getCurrentUser());
                    fragment.setArguments(bundle);
                }
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.tvPlaceholder, fragment);
                transaction.addToBackStack(null).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.menu_timeline);
    }

    public void displayProfileFragment(ParseUser user) {
        Fragment new_fragment = new ProfileFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        new_fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.tvPlaceholder, new_fragment).addToBackStack(null).commit();
    }
}