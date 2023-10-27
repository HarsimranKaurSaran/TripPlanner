package com.harsimranksaran.tripplanner.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.harsimranksaran.tripplanner.PlacesInfo;
import com.harsimranksaran.tripplanner.R;
import com.harsimranksaran.tripplanner.adapters.FavoritesAdapter;
import com.harsimranksaran.tripplanner.database.DBHelper;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<PlacesInfo> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase msqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.viewallData();
        cursor.moveToFirst();
        do {
            PlacesInfo placesInfo = new PlacesInfo(cursor.getString(1), cursor.getString(2), cursor.getString(3));
            arrayList.add(placesInfo);

        } while (cursor.moveToNext());

        cursor.close();
        dbHelper.close();
        adapter = new FavoritesAdapter(getApplicationContext(), arrayList);
        recyclerView.setAdapter(adapter);


    }
}

