package com.harsimranksaran.tripplanner.database;

import android.provider.BaseColumns;

public class DatabaseContract {

public static final class DatabaseEntry implements BaseColumns{

    public static final String TABLE_NAME = "favorites";
    public static final String COLUMN_PLACE_ID = "id";
    public static final String COLUMN_PLACE_NAME = "name";
    public static final String COLUMN_PLACE_ADDRESS = "address";

    public static final String TABLE_NAME_TWO = "mylist";
    public static final String COLUMN_P_ID = "pid";
    public static final String COLUMN_P_NAME = "pname";
    public static final String COLUMN_P_ADDRESS = "paddress";

}

}
