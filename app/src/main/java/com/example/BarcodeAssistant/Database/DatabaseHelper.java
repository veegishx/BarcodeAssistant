package com.example.BarcodeAssistant.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "itemSelector";

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PRODUCTS = "products";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // USERS Table - column names
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TOTAL_SCANS = "total_scans";

    // PRODUCT Table - column names
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_PRODUCT_BARCODE = "product_barcode";
    private static final String KEY_PRODUCT_PRICE = "product_price";

    // Table Create Statements
    // USERS table create statement
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME
            + " TEXT," + KEY_PASSWORD + " TEXT," + KEY_TOTAL_SCANS + " INTEGER," + KEY_CREATED_AT
            + " DATETIME" + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PRODUCT_NAME + " TEXT,"
            + KEY_PRODUCT_BARCODE + " TEXT," + KEY_PRODUCT_PRICE + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_PRODUCTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);

        // create new tables
        onCreate(db);
    }


    public long addUser(String user, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",user);
        contentValues.put("password",password);
        long res = db.insert(TABLE_USERS,null,contentValues);
        db.close();
        return  res;
    }

    public boolean checkUser(String username, String password){
        String[] columns = { KEY_ID };
        SQLiteDatabase db = getReadableDatabase();
        String selection = KEY_USERNAME + "=?" + " and " + KEY_PASSWORD + "=?";
        String[] selectionArgs = { username, password };
        Cursor cursor = db.query(TABLE_USERS,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if(count>0)
            return  true;
        else
            return  false;
    }

    public long addProduct(String product_barcode, String product_name, String product_price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("product_barcode", product_barcode);
        contentValues.put("product_name",product_name);
        contentValues.put("product_price",product_price);
        long res = db.insert(TABLE_PRODUCTS,null,contentValues);
        db.close();
        return  res;
    }
}