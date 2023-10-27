package com.harsimranksaran.tripplanner.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.harsimranksaran.tripplanner.PlacesInfo;
import com.harsimranksaran.tripplanner.R;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.RecyclerViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    ArrayList<PlacesInfo> arrayList = new ArrayList<>();


    public FavoritesAdapter(Context context, ArrayList<PlacesInfo> placesInfoArrayList) {
        this.mContext = context;
        this.arrayList = placesInfoArrayList;
    }



    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        FavoritesAdapter.RecyclerViewHolder recyclerViewHolder;
        View view = null;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_fav, viewGroup, false);
        }

        recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder viewHolder, int i) {
        PlacesInfo placesInfo = arrayList.get(i);
        viewHolder.name.setText(placesInfo.getName());
        viewHolder.address.setText(placesInfo.getAddress());

    }

    public void swapCursor(Cursor newCursor) {

        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {

            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView name, address;


        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.placename);
            address = (TextView) itemView.findViewById(R.id.placeaddress);

        }
    }



}

