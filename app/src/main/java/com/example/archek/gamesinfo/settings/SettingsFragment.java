package com.example.archek.geyms.settings;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.archek.geyms.PrefsConst;
import com.example.archek.geyms.R;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static com.example.archek.geyms.PrefsConst.SETTINGS_COMPANIES_AMOUNT;

public class SettingsFragment extends Fragment {

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private Spinner spGamesAmount;
    private Spinner spCompaniesAmount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate( R.layout.fragment_settings, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"CommitPrefEdits"})
    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences( getContext() );
        prefsEditor = prefs.edit();
        ((Toolbar) view.findViewById( R.id.toolbar )).setTitle( R.string.settings );
        spGamesAmount = view.findViewById( R.id.spGamesAmount );
        spCompaniesAmount = view.findViewById( R.id.spCompaniesAmount );
        setOnItemSelectedListeners();
        setSelections();
    }

    private void setSelections() {
        int gamesAmount = prefs.getInt( PrefsConst.SETTINGS_GAME_AMOUNT,PrefsConst.SETTING_DEFAULT_AMOUNT );
        spGamesAmount.setSelection(getAmountIndex( gamesAmount ));
        int companiesAmount = prefs.getInt( PrefsConst.SETTINGS_COMPANIES_AMOUNT,PrefsConst.SETTING_DEFAULT_AMOUNT );
        spCompaniesAmount.setSelection(getAmountIndex( companiesAmount ));
    }

    private int getAmountIndex(int amount) {
        String [] options = getResources().getStringArray(R.array.amount_options);
        List<String > optionsList = Arrays.asList(options);
        return optionsList.indexOf( String .valueOf( amount ) );

    }



    private void setOnItemSelectedListeners(){
        spCompaniesAmount.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                String amountString = parent.getItemAtPosition( position ).toString();
                int amountInt = Integer.parseInt( amountString );
                prefsEditor.putInt( PrefsConst.SETTINGS_COMPANIES_AMOUNT, amountInt ).apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){ }
        });
        spGamesAmount.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                String amountString = parent.getItemAtPosition( position ).toString();
                int amountInt = Integer.parseInt( amountString );
                prefsEditor.putInt( PrefsConst.SETTINGS_GAME_AMOUNT, amountInt ).apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){ }
        });

    }
}
