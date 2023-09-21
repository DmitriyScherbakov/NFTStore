package com.example.nftstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_nft);
    }

    NftPageFragment nftFragment = new NftPageFragment();
    CollectionPageFragment collectionFragment = new CollectionPageFragment();
    ProfilePageFragment profileFragment = new ProfilePageFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.navigation_nft:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, nftFragment).commit();
                return true;

            case R.id.navigation_collection:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, collectionFragment).commit();
                return true;

            case R.id.navigation_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profileFragment).commit();
                return true;
        }
        return false;
    }
}