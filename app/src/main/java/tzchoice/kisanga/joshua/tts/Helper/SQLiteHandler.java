package tzchoice.kisanga.joshua.tts.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tzchoice.kisanga.joshua.tts.Pojo.InspectionIrregularity;

/**
 * Created by user on 1/28/2017.
 */


public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 6;

    // Database Names
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_ACTIONS = "actions";
    private static final String TABLE_IRREGULARITIES = "irregularities";
    private static final String TABLE_PRODUCT = "products";
    private static final String TABLE_INSPECTION_IRREGULARITIES = "inspection_irregularities";
    private static final String TABLE_UNITS = "units";

    public static final String KEY_NAME = "name";
    // Login Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PIN_CODE = "pin_code";
    public static final String KEY_CHECKPOINT_ID = "checkpoint_id";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_CHECKPOINT = "checkpoint";


    //Irregularities Table Fields
    public static final String KEY_IRREGULARITY = "irregularit";
    public static final String KEY_PRODUCT = "product";
    public static final String KEY_VOLUME = "volume";
    public static final String KEY_VALUE = "value";
    public static final String KEY_ACTION = "action";
    public static final String KEY_ACTION_AMOUNT = "action_amount";
    public static final String KEY_RECEIPT_NO = "receipt_no";



    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT,"
                + KEY_PIN_CODE + " INTEGER," + KEY_CHECKPOINT + " TEXT," + KEY_CHECKPOINT_ID + " INTEGER,"
                + KEY_USER_ID + " INTEGER" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        String CREATE_ACTION_TABLE = "CREATE TABLE " + TABLE_ACTIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT)";
        db.execSQL(CREATE_ACTION_TABLE);

        String CREATE_IRREGULARITIES_TABLE = "CREATE TABLE " + TABLE_IRREGULARITIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT)";
        db.execSQL(CREATE_IRREGULARITIES_TABLE);


        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT)";
        db.execSQL(CREATE_PRODUCT_TABLE);

        String CREATE_UNIT_TABLE = "CREATE TABLE " + TABLE_UNITS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT)";
        db.execSQL(CREATE_UNIT_TABLE);

        //Irregularities
        String CREATE_INSPECTION_IRREGULARITIES_TABLE = "CREATE TABLE " + TABLE_INSPECTION_IRREGULARITIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_IRREGULARITY + " TEXT,"
                + KEY_PRODUCT + " TEXT," + KEY_VOLUME + " TEXT,"
                + KEY_VALUE + " TEXT," + KEY_ACTION + " TEXT,"
                + KEY_ACTION_AMOUNT + " TEXT," + KEY_RECEIPT_NO + " TEXT)";
        db.execSQL(CREATE_INSPECTION_IRREGULARITIES_TABLE);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IRREGULARITIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSPECTION_IRREGULARITIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNITS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String username, int pinCode, int checkpointId, String checkpoint, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_PIN_CODE, pinCode);
        values.put(KEY_CHECKPOINT_ID, checkpointId);
        values.put(KEY_CHECKPOINT, checkpoint);
        values.put(KEY_USER_ID, userId);

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
    }

    public void addInspectionIrregularities(String irregularity,String product, String volume, String value,
                                            String action, String actionAmount, String receiptNo ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IRREGULARITY, irregularity);
        values.put(KEY_PRODUCT, product);
        values.put(KEY_VOLUME, volume);
        values.put(KEY_VALUE, value);
        values.put(KEY_ACTION, action);
        values.put(KEY_ACTION_AMOUNT, actionAmount);
        values.put(KEY_RECEIPT_NO, receiptNo);

        // Inserting Row
        long id = db.insertWithOnConflict(TABLE_INSPECTION_IRREGULARITIES, null, values,SQLiteDatabase.CONFLICT_IGNORE);
        db.close(); // Closing database connection
    }

    public void addAction(int ID, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, ID);
        values.put(KEY_NAME, name);

        // Inserting Row
        long id = db.insertWithOnConflict(TABLE_ACTIONS, null, values,SQLiteDatabase.CONFLICT_IGNORE);
        db.close(); // Closing database connection
    }


    public void addIrregularities(int ID, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, ID);
        values.put(KEY_NAME, name);

        // Inserting Row
        long id = db.insertWithOnConflict(TABLE_IRREGULARITIES, null, values,SQLiteDatabase.CONFLICT_IGNORE);
        db.close(); // Closing database connection
    }


    public void addUnits(int ID, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, ID);
        values.put(KEY_NAME, name);

        // Inserting Row
        long id = db.insertWithOnConflict(TABLE_UNITS, null, values,SQLiteDatabase.CONFLICT_IGNORE);
        db.close(); // Closing database connection
    }



    public void addProducts(int ID, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, ID);
        values.put(KEY_NAME, name);

        // Inserting Row
        long id = db.insertWithOnConflict(TABLE_PRODUCT, null, values,SQLiteDatabase.CONFLICT_IGNORE);
        db.close(); // Closing database connection
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put(KEY_USERNAME, cursor.getString(1));
            user.put(KEY_PIN_CODE, cursor.getString(2));
            user.put(KEY_CHECKPOINT, cursor.getString(3));
            user.put(KEY_CHECKPOINT_ID, cursor.getString(4));
            user.put(KEY_USER_ID, cursor.getString(5));
        }
        cursor.close();
        db.close();
        // return user

        return user;
    }


    public List<String> actions(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_ACTIONS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        List<String> list = new ArrayList();
        if (cursor .moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));

                list.add(name);
                cursor.moveToNext();
            }

    }
        return list;
    }

    public List<String> units(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_UNITS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        List<String> list = new ArrayList();
        if (cursor .moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));

                list.add(name);
                cursor.moveToNext();
            }

    }
        return list;
    }




 public List<String> irregularities(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_IRREGULARITIES;
        Cursor cursor = db.rawQuery(selectQuery, null);

        List<String> list = new ArrayList();
        if (cursor .moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));

                list.add(name);
                cursor.moveToNext();
            }

    }
        return list;
    }

    public List<String> products(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT;
        Cursor cursor = db.rawQuery(selectQuery, null);

        List<String> list = new ArrayList();
        if (cursor .moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));

                list.add(name);
                cursor.moveToNext();
            }

    }
        return list;
    }

   // private ArrayList<InspectionIrregularity> objectList  = getInspectionResults();

    public List<InspectionIrregularity> getInspectionResults(){
        InspectionIrregularity ob;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_INSPECTION_IRREGULARITIES;

        ArrayList<InspectionIrregularity> resultList = new ArrayList<InspectionIrregularity>();

        Cursor c = db.rawQuery(selectQuery, null);

        while (c.moveToNext())
        {
            String Irre = c.getString(c.getColumnIndex(KEY_IRREGULARITY));
            String Prod = c.getString(c.getColumnIndex(KEY_PRODUCT));
            String volu = c.getString(c.getColumnIndex(KEY_VOLUME));
            String valu = c.getString(c.getColumnIndex(KEY_VALUE));
            String act = c.getString(c.getColumnIndex(KEY_ACTION));
            String actAmo = c.getString(c.getColumnIndex(KEY_ACTION_AMOUNT));
            String recei = c.getString(c.getColumnIndex(KEY_RECEIPT_NO));

            try
            {
                ob =  new InspectionIrregularity();
                ob.setIrregularity(Irre);
                ob.setProduct(Prod);
                ob.setValue(volu);
                ob.setValue(valu);
                ob.setAction(act);
                ob.setActionAmount(actAmo);
                ob.setReceiptNo(recei);
                resultList.add(ob);
            }
            catch (Exception e) {

            }

        }
        c.close();

        db.close();
        return resultList;
    }


    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();
    }
    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteActions() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_ACTIONS, null, null);
        db.close();
    } /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteIrregularities() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_IRREGULARITIES, null, null);
        db.close();
    }

    public void deleteInspectionIrregularities() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_INSPECTION_IRREGULARITIES, null, null);
        db.close();
    }

}
