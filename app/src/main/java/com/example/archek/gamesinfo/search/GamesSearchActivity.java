package com.example.archek.gamesinfo.search;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.archek.gamesinfo.R;
import com.example.archek.gamesinfo.gamedetails.GameDetailsActivity;
import com.example.archek.gamesinfo.games.GamesAdapter;
import com.example.archek.gamesinfo.network.GbObjectResponse;
import com.example.archek.gamesinfo.network.GbObjectsListResponse;
import com.example.archek.gamesinfo.network.GiantBombService;
import com.example.archek.gamesinfo.network.RestApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GamesSearchActivity extends AppCompatActivity implements GamesAdapter.Callback, View.OnClickListener {

    private GamesAdapter adapter = new GamesAdapter( this );
    private Handler handler = new Handler(  );
    private ProgressBar progressBar;
    private RecyclerView rvGames;
    private final GiantBombService giantBombService = RestApi.createService( GiantBombService.class );
    @Nullable private Call<GbObjectsListResponse> call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_search_games );
        findViewById( R.id.btnBack ).setOnClickListener( this );
        progressBar = findViewById( R.id.progressBar );
        rvGames = findViewById( R.id.rvGames );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
        rvGames.setAdapter( adapter );
        rvGames.setLayoutManager( layoutManager );

        EditText etSearch = findViewById( R.id.etSearch );
        etSearch.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(final Editable s) {
                handler.removeCallbacksAndMessages( null );
                handler.postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        searchGames(s.toString(),10);
                    }
                },400 );
                }
        } );
    }

    private void searchGames(String query, int limit){
        if(query.isEmpty()){
            return;
        }
        showLoading();
        if (call != null){
            call.cancel();
        }
        call = giantBombService.searchGames( query, limit );
        //noinspection ConstantConditions
        call.enqueue( new Callback <GbObjectsListResponse>() {
            @Override
            public void onResponse(Call <GbObjectsListResponse> call, Response<GbObjectsListResponse> response) {
                showContent();
                if(response.body() != null){
                    //noinspection ConstantConditions
                    adapter.replaceAll(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call <GbObjectsListResponse> call, Throwable t) {
                showContent();
                if(!call.isCanceled()){
                    Toast.makeText( GamesSearchActivity.this,R.string.error,Toast.LENGTH_SHORT ).show();
                }
            }
        } );
    }

    private void showLoading(){
        rvGames.setVisibility( View.GONE );
        progressBar.setVisibility( View.VISIBLE );
    }

    private void showContent(){
        rvGames.setVisibility( View.VISIBLE );
        progressBar.setVisibility( View.GONE );
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.btnBack){
            onBackPressed();
        }
    }

    @Override
    public void onGameClick(GbObjectResponse game) {
        startActivity( GameDetailsActivity.makeIntent( this,game ));
    }

    @Override
    protected void onDestroy(){
        if(call != null){
            call.cancel();
        }
        super.onDestroy();
    }
}













