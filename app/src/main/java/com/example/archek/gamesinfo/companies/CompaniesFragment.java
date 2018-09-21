package com.example.archek.gamesinfo.companies;

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

import com.example.archek.gamesinfo.PrefsConst;
import com.example.archek.gamesinfo.R;
import com.example.archek.gamesinfo.companydetails.CompanyDetailsActivity;
import com.example.archek.gamesinfo.network.GbObjectResponse;
import com.example.archek.gamesinfo.network.GbObjectsListResponse;
import com.example.archek.gamesinfo.network.GiantBombService;
import com.example.archek.gamesinfo.network.RestApi;
import com.example.archek.gamesinfo.search.CompaniesSearchActivity;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompaniesFragment extends Fragment implements Toolbar.OnMenuItemClickListener, CompaniesAdapter.Callback {

    private static final int TOTAL_COMPANIES_AMOUNT = 16855;

    private GiantBombService service = RestApi.createService( GiantBombService.class );
    private Random random = new Random(  );
    private CompaniesAdapter adapter = new CompaniesAdapter( this  );
    private RecyclerView rvCompanies;
    private ProgressBar progressBar;
    @Nullable private Call<GbObjectsListResponse> call;
    private int companiesAmount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_companies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( getContext() );
        companiesAmount = preferences.getInt( PrefsConst.SETTINGS_COMPANIES_AMOUNT, PrefsConst.SETTING_DEFAULT_AMOUNT );
        setupToolbar(view);
        setupRecyclerView(view);
        progressBar = view.findViewById( R.id.progressBar );
        view.findViewById( R.id.fabSearchCo ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getContext(), CompaniesSearchActivity.class ) );
            }
        } );
        loadRandomCompanies();
       }

    @Override
    public void onCompanyClick(GbObjectResponse company){
        Intent intent = CompanyDetailsActivity.makeIntent(getContext(),company);
        startActivity( intent );
    }

    private void loadRandomCompanies(){
        if(call != null && call.isExecuted()){
            return;
        }
        showLoading();
        int offset = random.nextInt(TOTAL_COMPANIES_AMOUNT-companiesAmount+1);
        call =service.getCompanies( companiesAmount, offset );
        Log.d("33_", toString());
        //noinspection ConstantConditions
        call.enqueue( new Callback <GbObjectsListResponse>() {
            @Override
            public void onResponse(Call<GbObjectsListResponse> call, Response <GbObjectsListResponse> response) {
                Log.d("33_", toString());
                showContent();
                CompaniesFragment.this.call = call.clone();
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
    rvCompanies.setVisibility( View.GONE );
    progressBar.setVisibility( View.VISIBLE );
    }

    private void showContent(){
        progressBar.setVisibility( View.GONE );
        rvCompanies.setVisibility(View.VISIBLE);

    }

    private void setupToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.companies);
        toolbar.inflateMenu(R.menu.menu_games);
        toolbar.setOnMenuItemClickListener(this);

    }

    private void setupRecyclerView(View view) {
        rvCompanies = view.findViewById(R.id.rvCompanies);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvCompanies.setLayoutManager(layoutManager);
        rvCompanies.setAdapter(adapter);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            loadRandomCompanies();
            return true;
        }
        return false;
    }
}
