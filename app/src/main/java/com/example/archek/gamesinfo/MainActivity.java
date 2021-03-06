package com.example.archek.gamesinfo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.archek.gamesinfo.companies.CompaniesFragment;
import com.example.archek.gamesinfo.games.GamesFragment;
import com.example.archek.gamesinfo.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {//from MainActivity load navigation bottom
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            replaceFragment(new GamesFragment());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {//load different fragments in click
        if (bottomNavigationView.getSelectedItemId() != item.getItemId()) {
            Fragment fragment = createFragment( item.getItemId());
            replaceFragment( fragment );
            }
            return true;
        }

        private Fragment createFragment(int navItemId){
            final Fragment fragment;
            switch (navItemId){
            case R.id.nav_games:
                fragment = new GamesFragment();
                break;
            case R.id.nav_companies:
                fragment = new CompaniesFragment();
                break;
            case R.id.nav_favorite://not finished
                fragment = new FavoriteFragment();
                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;
            default:
                fragment = new Fragment();
                break;
        }
        replaceFragment(fragment);
        return fragment;
    }

    private void replaceFragment(Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace( R.id.fragmentContainer, fragment )
                    .commit();
        }
    }
}
