package com.harsimranksaran.tripplanner.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;

import com.harsimranksaran.tripplanner.PlacesInfo;
import com.harsimranksaran.tripplanner.R;
import com.harsimranksaran.tripplanner.adapters.MakeListAdapter;
import com.harsimranksaran.tripplanner.database.DBHelper;

import java.util.ArrayList;

public class MyListActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<PlacesInfo> arrayList = new ArrayList<>();

    PlacesInfo placesInfo;



    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylist);

        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase msqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.viewData();
        cursor.moveToFirst();
        do {
            placesInfo = new PlacesInfo(cursor.getString(1), cursor.getString(2), cursor.getString(3));
            arrayList.add(placesInfo);

        } while (cursor.moveToNext());

        cursor.close();
        dbHelper.close();
        adapter = new MakeListAdapter(getApplicationContext(), arrayList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        MenuItem item = menu.findItem(R.id.share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.share:
                String name;

                DBHelper dbHelper = new DBHelper(this);
                SQLiteDatabase msqLiteDatabase = dbHelper.getReadableDatabase();
                Cursor cursor = dbHelper.viewData();
                cursor.moveToFirst();
                do {
                    name = (cursor.getString(1));

                } while (cursor.moveToNext());

                cursor.close();
                dbHelper.close();

                Intent sharingIntent = new Intent();
                sharingIntent.setAction(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareText = "Amazing Places to go..... " + name;
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
