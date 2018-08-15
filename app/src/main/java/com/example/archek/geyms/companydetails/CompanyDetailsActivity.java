package com.example.archek.geyms.companydetails;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.archek.geyms.R;
import com.example.archek.geyms.network.GbObjectResponse;
import com.example.archek.geyms.network.GbSingleObjectResponse;
import com.example.archek.geyms.network.GiantBombService;
import com.example.archek.geyms.network.RestApi;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyDetailsActivity extends AppCompatActivity{

    private static final String EXTRA_COMPANY_NAME = "EXTRA_COMPANY_NAME";
    private static final String EXTRA_COMPANY_DECK = "EXTRA_COMPANY_DECK";
    private static final String EXTRA_COMPANY_PICTURE_URL = "EXTRA_COMPANY_PICTURE_URL";


    private GiantBombService service = RestApi.creteService( GiantBombService.class );
    private ProgressBar progressBar;
    private TextView tvDescription;
    private ViewGroup vgContent;
    @Nullable private Call<GbSingleObjectResponse> call;

    public static Intent makeIntent(Context context, GbObjectResponse company){
        return new Intent( context,CompanyDetailsActivity.class)
            .putExtra( CompanyDetailsActivity.EXTRA_COMPANY_NAME, company.getName())
            .putExtra( CompanyDetailsActivity.EXTRA_COMPANY_DECK, company.getDeck())
            .putExtra( CompanyDetailsActivity.EXTRA_COMPANY_DECK, company.getDeck())
            .putExtra( CompanyDetailsActivity.EXTRA_COMPANY_PICTURE_URL, company.getImage().getSmallUrl());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_company_details );
        tvDescription = findViewById( R.id.tvDescription );
        progressBar = findViewById( R.id.progressBar );
        vgContent = findViewById( R.id.vgContent );

        Intent intent = getIntent();

        String companyName = intent.getStringExtra( EXTRA_COMPANY_NAME );
        String companyPicUrl = intent.getStringExtra( EXTRA_COMPANY_PICTURE_URL );
        String companyDeck = intent.getStringExtra( EXTRA_COMPANY_DECK );

        ImageView ivPicture =  findViewById( R.id.ivPicture );
        Toolbar toolbar = findViewById( R.id.toolbar );
        TextView tvDeck = findViewById( R.id.tvDeck );

        tvDeck.setText( companyDeck == null ? "No deck" : companyDeck );
        toolbar.setTitle(companyName);
        setSupportActionBar( toolbar );
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        Picasso.get().load(companyPicUrl).into( ivPicture );

        loadCompanyDetails(companyName);
    }
    private void loadCompanyDetails(String companyName){
        showLoading();
        call=service.getCompanyDetails( companyName );

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
                    Toast.makeText( CompanyDetailsActivity.this,R.string.error,Toast.LENGTH_SHORT ).show();
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



