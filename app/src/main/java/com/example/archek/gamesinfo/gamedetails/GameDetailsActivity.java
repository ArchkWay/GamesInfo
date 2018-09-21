package com.example.archek.gamesinfo.gamedetails;

import com.example.archek.gamesinfo.R;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


import com.example.archek.gamesinfo.network.GbObjectResponse;
import com.example.archek.gamesinfo.network.GbSingleObjectResponse;
import com.example.archek.gamesinfo.network.GiantBombService;
import com.example.archek.gamesinfo.network.RestApi;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameDetailsActivity extends AppCompatActivity{

    private static final String EXTRA_GAME_NAME = "EXTRA_GAME_NAME";
    private static final String EXTRA_GAME_DECK = "EXTRA_GAME_DECK";
    private static final String EXTRA_GAME_GUID = "EXTRA_GAME_GUID";
    private static final String EXTRA_GAME_PICTURE_URL = "EXTRA_GAME_PICTURE_URL";


    private GiantBombService service = RestApi.createService( GiantBombService.class );
    private ProgressBar progressBar;
    private TextView tvDescription;
    private ViewGroup vgContent;
    @Nullable private Call<GbSingleObjectResponse> call;

    public static Intent makeIntent(Context context, GbObjectResponse game){
        return new Intent( context,GameDetailsActivity.class)
            .putExtra(GameDetailsActivity.EXTRA_GAME_NAME, game.getName())
            .putExtra(GameDetailsActivity.EXTRA_GAME_DECK, game.getDeck())
            .putExtra(GameDetailsActivity.EXTRA_GAME_GUID, game.getGuid())
            .putExtra(GameDetailsActivity.EXTRA_GAME_DECK, game.getDeck())
            .putExtra(GameDetailsActivity.EXTRA_GAME_PICTURE_URL, game.getImage().getSmallUrl());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_game_details );
        tvDescription = findViewById( R.id.tvDescription );
        progressBar = findViewById( R.id.progressBar );
        vgContent = findViewById( R.id.vgContent );

        Intent intent = getIntent();

        String gameName = intent.getStringExtra( EXTRA_GAME_NAME );
        String gamePicUrl = intent.getStringExtra( EXTRA_GAME_PICTURE_URL );
        String gameDeck = intent.getStringExtra( EXTRA_GAME_DECK );
        String guid = intent.getStringExtra( EXTRA_GAME_GUID );

        ImageView ivPicture =  findViewById( R.id.ivPicture );
        Toolbar toolbar = findViewById( R.id.toolbar );
        TextView tvDeck = findViewById( R.id.tvDeck );

        tvDeck.setText( gameDeck == null ? "No deck" : gameDeck );
        toolbar.setTitle(gameName);
        setSupportActionBar( toolbar );
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        Picasso.get().load(gamePicUrl).into( ivPicture );

        loadGameDetails(guid);
    }
    private void loadGameDetails(String guid){
        showLoading();
        call=service.getGameDetails( guid );

        call.enqueue( new Callback <GbSingleObjectResponse>() {
            @Override
            public void onResponse(Call <GbSingleObjectResponse> call, Response<GbSingleObjectResponse> response) {
                Log.d("33__", "onResponse");
                showContent();
                GbSingleObjectResponse gbResponse = response.body();
                if(gbResponse != null){
                    String description = gbResponse.getResults().getDescription();
                    CharSequence text = description == null ? "No description" : Html.fromHtml( description );
                    tvDescription.setText( text );
                }
            }


            @Override
            public void onFailure(Call <GbSingleObjectResponse> call, Throwable t) {
                if(!call.isCanceled()){
                    showContent();
                    Toast.makeText( GameDetailsActivity.this,R.string.error,Toast.LENGTH_SHORT ).show();
                }
            }
        } );
    }

    private void showLoading(){
        vgContent.setVisibility(View.GONE);
        progressBar.setVisibility( View.VISIBLE );
    }

    private void showContent(){
        vgContent.setVisibility(View.VISIBLE);
        progressBar.setVisibility( View.GONE );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    protected void onDestroy(){
        assert call != null;
        call.cancel();
        super.onDestroy();
    }
}



