package com.example.archek.geyms.games;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.archek.geyms.PrefsConst;
import com.example.archek.geyms.gamedetails.GameDetailsActivity;
import com.example.archek.geyms.R;
import com.example.archek.geyms.network.GbObjectResponse;
import com.example.archek.geyms.network.GbObjectsListResponse;
import com.example.archek.geyms.network.GiantBombService;
import com.example.archek.geyms.network.RestApi;
import com.example.archek.geyms.search.GamesSearchActivity;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GamesFragment extends Fragment implements Toolbar.OnMenuItemClickListener, GamesAdapter.Callback {

    private static final int TOTAL_GAMES_COUNT = 63804;

    private GiantBombService service = RestApi.creteService( GiantBombService.class );
    private Random random = new Random(  );
    private GamesAdapter adapter = new GamesAdapter( this  );
    private RecyclerView rvGames;
    private ProgressBar progressBar;
    @Nullable private Call<GbObjectsListResponse> call;
    private int gamesAmount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_games, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( getContext() );
        gamesAmount = preferences.getInt( PrefsConst.SETTINGS_GAME_AMOUNT, PrefsConst.SETTING_DEFAULT_AMOUNT );
        setupToolbar(view);
        setupRecyclerView(view);
        progressBar = view.findViewById( R.id.progressBar );
        view.findViewById( R.id.fabSearch ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getContext(), GamesSearchActivity.class ) );
            }
        } );
        loadRandomGames();
       }
    @Override
    public void onGameClick(GbObjectResponse game){
        Intent intent = GameDetailsActivity.makeIntent(getContext(),game);
        startActivity( intent );
    }

    private void loadRandomGames(){
        if(call != null && call.isExecuted()){
            return;
        }
        showLoading();
        int offset = random.nextInt(TOTAL_GAMES_COUNT-gamesAmount+1);
        call =service.getGames( gamesAmount, offset );
        Log.d("33_", toString());
        //noinspection ConstantConditions
        call.enqueue( new Callback <GbObjectsListResponse>() {
            @Override
            public void onResponse(Call<GbObjectsListResponse> call, Response <GbObjectsListResponse> response) {
                Log.d("33_", toString());
                showContent();
                GamesFragment.this.call = call.clone();
                GbObjectsListResponse gbObjectsListResponse = response.body();
                if(gbObjectsListResponse != null){
                    adapter.replaceAll( gbObjectsListResponse.getResults() );
                }
            }

            @Override
            public void onFailure(Call <GbObjectsListResponse> call, Throwable t) {
                showContent();
                if(call.isCanceled()){
                    Toast.makeText( getContext(), R.string.error, Toast.LENGTH_SHORT ).show();

                }
            }
        } );
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        if (call != null) {
            call.cancel();
        }
    }

    private void showLoading(){
    rvGames.setVisibility( View.GONE );
    progressBar.setVisibility( View.VISIBLE );
    }

    private void showContent(){
        progressBar.setVisibility( View.GONE );
        rvGames.setVisibility(View.VISIBLE);

    }

    private void setupToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.games);
        toolbar.inflateMenu(R.menu.menu_games);
        toolbar.setOnMenuItemClickListener(this);

    }

    private void setupRecyclerView(View view) {
        rvGames = view.findViewById(R.id.rvGames);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvGames.setLayoutManager(layoutManager);
        rvGames.setAdapter(adapter);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            loadRandomGames();
            return true;
        }
        return false;
    }
}
