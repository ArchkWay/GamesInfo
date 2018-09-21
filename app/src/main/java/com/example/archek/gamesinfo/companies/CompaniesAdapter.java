package com.example.archek.gamesinfo.companies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.archek.gamesinfo.R;
import com.example.archek.gamesinfo.network.GbObjectResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.ViewHolder> {

    private List<GbObjectResponse> companies = new ArrayList<>();
    private final Callback callback;

    public CompaniesAdapter(Callback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_company, parent, false);
        final ViewHolder holder = new ViewHolder( itemView );
        itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GbObjectResponse company  = companies.get(holder.getAdapterPosition());
                callback.onCompanyClick( company );
            }
        } );
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("44__","position - " + position);
        GbObjectResponse company = companies.get(position);
        Picasso.get().load(company.getImage().getSmallUrl()).into(holder.ivPicture);
        holder.tvName.setText(company.getName());
        holder.tvDeck.setText(company.getDeck());
        holder.tvCountry.setText(company.getCountry());
        holder.tvCity.setText(company.getCity());
    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    public void addAll(List<GbObjectResponse> companiesToAdd) {
        companies.addAll(companiesToAdd);
        notifyDataSetChanged();
    }

    public void replaceAll(List<GbObjectResponse> companiesToReplace) {
        companies.clear();
        companies.addAll(companiesToReplace);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPicture;
        TextView tvName;
        TextView tvDeck;
        TextView tvCountry;
        TextView tvCity;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPicture = itemView.findViewById(R.id.ivPicture);
            tvName = itemView.findViewById(R.id.tvName);
            tvDeck = itemView.findViewById(R.id.tvDeck);
            tvCountry = itemView.findViewById( R.id.tvCountry );
            tvCity = itemView.findViewById( R.id.tvCity );

        }
    }
    public interface Callback{
        void onCompanyClick(GbObjectResponse company);
    }

}
