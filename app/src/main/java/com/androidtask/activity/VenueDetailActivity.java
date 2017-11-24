package com.androidtask.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;


import com.androidtask.R;
import com.androidtask.fragment.VenueDetailFragment;

import java.util.List;


public class VenueDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_detail);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (int i = 0, l = fragments.size(); i < l; i++) {
                Fragment fragment = fragments.get(i);
                if (fragment instanceof VenueDetailFragment) {
                    return;
                }
            }
        }

        VenueDetailFragment venueDetailFragment = new VenueDetailFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_venue_detail, venueDetailFragment)
                .commit();
    }
}
