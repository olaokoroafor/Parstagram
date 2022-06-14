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

import com.example.parstagram.fragments.ComposeFragment;
import com.example.parstagram.fragments.ProfileFragment;
import com.example.parstagram.fragments.TimelineFragment;
import com.example.parstagram.models.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

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
                }
                fragmentManager.beginTransaction().replace(R.id.tvPlaceholder, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.menu_timeline);
        queryPosts();
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.USER_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e == null){
                    for (Post post: posts){
                        Log.i(TAG, "Post: "+ post.getDescription() + " ,username: " + post.getUser().getUsername());
                    }
                }
                else{
                    Log.e(TAG, "Issue ocurred with getting post: " + e);
                }
            }
        });

    }
}