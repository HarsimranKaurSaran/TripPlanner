package com.harsimranksaran.tripplanner;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.harsimranksaran.tripplanner.database.DBHelper;
import com.harsimranksaran.tripplanner.database.DatabaseContract;

public class PlaceDetails extends AppCompatActivity {

    public static final String TAG = "PlacePhotosActivity";

    TextView placename, placeaddress, placeWebsite, placePhone;
    ImageView placeImage, previmage, nextimage, makelistimg, favimg;
    PlacesInfo placesInfo;

    private SQLiteDatabase db;

    RecyclerView recyclerView;

    String name, website, phone;
    String id;
    String address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        placename = (TextView) findViewById(R.id.placeName);
        placeaddress = (TextView) findViewById(R.id.placeAddress);
        placeImage = (ImageView) findViewById(R.id.placeimage);
        previmage = (ImageView) findViewById(R.id.previmage);
        nextimage = (ImageView) findViewById(R.id.nextimage);
        placeWebsite = (TextView) findViewById(R.id.website);
        placePhone = (TextView) findViewById(R.id.phone);
        makelistimg = (ImageView) findViewById(R.id.makelist);
        favimg = (ImageView) findViewById(R.id.favorite);

        //rating = (TextView) findViewById(R.id.rating);

        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
        id = intent.getStringExtra("id");
        website = intent.getStringExtra("website");
        phone = intent.getStringExtra("phone");

        try {

            placename.setText(name);
            placeaddress.setText(address);
            placeWebsite.setText(website);
            placePhone.setText(phone);


        }catch (Exception e){
            e.printStackTrace();
        }

        favimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper dbHelper = new DBHelper(getApplicationContext());

                SQLiteDatabase msqLiteDatabase;
                ContentValues contentValues;
                msqLiteDatabase = dbHelper.getWritableDatabase();
                contentValues = new ContentValues();
                contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_PLACE_ID, id);
                contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_PLACE_NAME, name);
                contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_PLACE_ADDRESS, address);

                long rowInserted = msqLiteDatabase.insertWithOnConflict(DatabaseContract.DatabaseEntry.TABLE_NAME,null,contentValues, SQLiteDatabase.CONFLICT_REPLACE);

                if(rowInserted != -1) {
                    Toast.makeText(getApplicationContext(), "Successfully added", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Cannot add as favorites", Toast.LENGTH_SHORT).show();
                }

                msqLiteDatabase.close();

            }
        });

        makelistimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DBHelper dbHelper =
                        new DBHelper(getApplicationContext());
                SQLiteDatabase msqLiteDatabase;
                ContentValues contentValues;
                msqLiteDatabase = dbHelper.getWritableDatabase();
                contentValues = new ContentValues();
                contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_P_ID, id);
                contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_P_NAME, name);
                contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_P_ADDRESS, address);

                long rowInserted = msqLiteDatabase.insertWithOnConflict(DatabaseContract.DatabaseEntry.TABLE_NAME_TWO,null,contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                if(rowInserted != -1) {
                    Toast.makeText(getApplicationContext(), "Successfully added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot add to the list", Toast.LENGTH_SHORT).show();
                }
                msqLiteDatabase.close();
            }
        });

    }


}
