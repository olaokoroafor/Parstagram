package com.example.parstagram;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(2*1000);

                    // After 5 seconds redirect to another intent
                    if (ParseUser.getCurrentUser() != null){
                        Intent i = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(i);
                    }
                    else{
                        Intent i=new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(i);
                    }

                    //Remove activity
                    finish();
                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();
    }
}