//template found at https://github.com/codepath/android_guides/wiki/Local-Databases-with-SQLiteOpenHelper

package com.example.showtime.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by txrdelage on 05/02/16.
 */
public class AppDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "appDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names

    private static final String TABLE_CALENDAR = "calendar";
    private static final String TABLE_CONTAIN = "contain";

    private static final String TABLE_SEARCH = "search";
    private static final String TABLE_PERFORMS = "performs";
    private static final String TABLE_USER = "user";


    // Search Table Columns
    private static final String KEY_SEARCH_ID = "search_id";
    private static final String KEY_MOVIE_NAME_SEARCH = "movie_name_search";
    private static final String KEY_GENRE = "genre";
    private static final String KEY_DIRECTOR = "director";
    private static final String KEY_LEAD_ACTOR = "lead_actor";


    //Calendar Table Columns
    private static final String KEY_CALENDAR_ID = "calendar_id";
    private static final String KEY_DATE = "date";
    private static final String KEY_MOVIE_NAME_CALENDAR = "movie_name_calendar";
    private static final String KEY_POSTER_IMAGE = "poster_image";

    //Contain Table Columns
    private static final String KEY_CALENDAR_ID_FK = "calendar_id_fk";
    private static final String KEY_USER_ID_CONTAIN_FK = "user_id_contain_fk";


    // Performs Table Columns
    private static final String KEY_USER_ID_PERFORMS_FK = "user_id_performs_fk";
    private static final String KEY_SEARCH_ID_FK = "search_id_fk";


    // User Table Columns
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";


    //Singleton pattern to ensure we only have one instance of the database
    private static AppDatabaseHelper sInstance;

    public static synchronized AppDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private AppDatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Allow foreign keys
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_SEARCH = "CREATE TABLE " + TABLE_SEARCH +
                "(" +
                KEY_SEARCH_ID + " INTEGER PRIMARY KEY," +
                KEY_MOVIE_NAME_SEARCH + "TEXT NOT NULL," +
                KEY_GENRE + " TEXT NOT NULL," +
                KEY_DIRECTOR + "TEXT," +
                KEY_LEAD_ACTOR + "TEXT" +
                ")";

        String CREATE_TABLE_PERFORMS = "CREATE TABLE " + TABLE_PERFORMS +
                "(" +
                KEY_USER_ID_PERFORMS_FK + "INTEGER NOT NULL REFERENCES " + TABLE_USER + "ON DELETE CASCADE," +
                KEY_SEARCH_ID_FK + "INTEGER NOT NULL REFERENCES " + TABLE_SEARCH + "ON DELETE CASCADE," +
                "PRIMARY KEY (" + KEY_USER_ID_PERFORMS_FK + "," + KEY_SEARCH_ID_FK + ")" +
                ")";

        String CREATE_TABLE_CALENDAR = "CREATE TABLE" + TABLE_CALENDAR +
                "(" +
                KEY_CALENDAR_ID + " INTEGER PRIMARY KEY," +
                KEY_DATE + "DATE NOT NULL," +
                KEY_MOVIE_NAME_CALENDAR + " TEXT NOT NULL," +
                KEY_POSTER_IMAGE + "URL," +
                ")";

        String CREATE_TABLE_CONTAIN = "CREATE TABLE " + TABLE_CONTAIN +
                "(" +
                KEY_USER_ID_CONTAIN_FK + "INTEGER NOT NULL REFERENCES " + TABLE_USER + "," +
                KEY_CALENDAR_ID_FK + "INTEGER NOT NULL REFERENCES " + TABLE_CALENDAR + "," +
                "PRIMARY KEY (" + KEY_USER_ID_CONTAIN_FK + "," + KEY_CALENDAR_ID_FK + ")" +
                ")";

        String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," +
                KEY_USERNAME + "TEXT NOT NULL," +
                KEY_PASSWORD + " TEXT NOT NULL," +
                ")";

        db.execSQL(CREATE_TABLE_SEARCH);
        db.execSQL(CREATE_TABLE_PERFORMS);
        db.execSQL(CREATE_TABLE_CALENDAR);
        db.execSQL(CREATE_TABLE_CONTAIN);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH);
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_PERFORMS);
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_CALENDAR);//Henry
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_CONTAIN);//Henry
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_USER);
            onCreate(db);
        }
    }

    //SEARCH METHODS

    //Add a search entry to both Performs and Search Tables
    public void addSearchFromUser(int user_id, Search search) {

        addSearch(search);
        addPerforms(user_id, search.search_id);
    }

    //Insert a search entry in the Search Table
    public void addSearch(Search search) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            //If primary key not specified it is auto incremented
            //values.put(KEY_SEARCH_ID,search.search_id);
            values.put(KEY_MOVIE_NAME_SEARCH, search.movie_name_search);
            values.put(KEY_MOVIE_NAME_SEARCH, search.genre);
            values.put(KEY_DIRECTOR, search.director);
            values.put(KEY_LEAD_ACTOR, search.lead_actor);
            db.insertOrThrow(TABLE_SEARCH, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("tag", "Error trying to add search in Search Table");

        } finally {
            db.endTransaction();
        }

    }

    //Insert a user id and search id entry in the Performs Table
    public void addPerforms(int user_id, int search_id) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_ID_PERFORMS_FK, user_id);
            values.put(KEY_SEARCH_ID_FK, search_id);
            db.insertOrThrow(TABLE_PERFORMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("tag", "Error trying to add user_id, search_id in Table Performs");

        } finally {
            db.endTransaction();
        }
    }

    //Retrieve the list of searches of a specific user
    public List<Search> retrieveAllUserSearches(int user_id) {
        List<Search> searches = new ArrayList<Search>();
        //SELECT search_id FROM Performs
        //WHERE user_id = user_id
        String USER_SEARCHES =
                String.format("SELECT %s FROM %s WHERE %s = %d",
                        KEY_SEARCH_ID_FK,
                        TABLE_PERFORMS,
                        KEY_USER_ID_PERFORMS_FK,
                        user_id);

        //SELECT * FROM Search, USER_SEARCHES
        //WHERE Search.search_id = USER_SEARCHES.search_id
        String SEARCH_SELECT_QUERY =
                String.format("SELECT * FROM %s , %s WHERE %s = %s",
                        TABLE_SEARCH,
                        USER_SEARCHES,
                        KEY_SEARCH_ID,
                        KEY_SEARCH_ID_FK);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SEARCH_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Search newSearch = new Search();
                    newSearch.search_id = cursor.getInt(cursor.getColumnIndex(KEY_SEARCH_ID));
                    newSearch.movie_name_search = cursor.getString(cursor.getColumnIndex(KEY_MOVIE_NAME_SEARCH));
                    newSearch.genre = cursor.getString(cursor.getColumnIndex(KEY_GENRE));
                    newSearch.director = cursor.getString(cursor.getColumnIndex(KEY_DIRECTOR));
                    newSearch.lead_actor = cursor.getString(cursor.getColumnIndex(KEY_LEAD_ACTOR));
                    searches.add(newSearch);
                } while (cursor.moveToNext());

            }

        } catch (Exception e) {
            Log.d("tag", "Error while trying retrieve Searches from a specific user");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return searches;
    }

    //This method could be added in a later sprint to delete the search history
    /*public void removeAllSearchesForUser (int user_id){

    }*/


    //CALENDAR METHODS

    public void addCalendarFromUser(int user_id, Calendar calendar) {
        addDate(calendar);
        addContain(user_id, calendar.calendar_id);
    }

    //Insert a data entry in the calendar Table
    public void addDate(Calendar calendar) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_DATE, calendar.date);
            values.put(KEY_MOVIE_NAME_CALENDAR, calendar.movie_name_calendar);
            values.put(KEY_POSTER_IMAGE, calendar.poster_image);
            db.insertOrThrow(TABLE_CALENDAR, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("tag", "Error trying to add search in Calendar Table");

        } finally {
            db.endTransaction();
        }
    }

    //Retrieve calendar entries (used to restore list of movies)
    public List<Calendar> retrieveAllCalendarEntries() {
        List<Calendar> calendarEntries = new ArrayList<Calendar>();
        SQLiteDatabase db = getReadableDatabase();
        String SELECT_ALL_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_CALENDAR);
        Cursor cursor = db.rawQuery(SELECT_ALL_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Calendar newCalendar = new Calendar();
                    newCalendar.calendar_id = cursor.getInt(cursor.getColumnIndex(KEY_CALENDAR_ID));
                    newCalendar.date = cursor.getString(cursor.getColumnIndex(KEY_DATE));
                    newCalendar.movie_name_calendar = cursor.getString(cursor.getColumnIndex(KEY_MOVIE_NAME_CALENDAR));
                    newCalendar.poster_image = cursor.getString(cursor.getColumnIndex(KEY_POSTER_IMAGE));
                    calendarEntries.add(newCalendar);
                } while (cursor.moveToNext());

            }

        } catch (Exception e) {
            Log.d("tag", "Error while trying retrieve all Calendar entries");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return calendarEntries;
    }

    // select calendar from calendar table
    public Calendar selectcalendar(Calendar calendar_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        Calendar selecteCalendar = new Calendar();
        try {
            String selectUserQuery = String.format("Select * FROM %s WHERE %s = ?",
                    TABLE_CALENDAR, KEY_CALENDAR_ID);
            Cursor cursor = db.rawQuery(selectUserQuery, new String[]{String.valueOf(calendar_id)});
            if (cursor.moveToFirst()) {
                //do {
                selecteCalendar.calendar_id = cursor.getInt(cursor.getColumnIndex(KEY_USER_ID));//This is probably wrong
                selecteCalendar.date = cursor.getString(cursor.getColumnIndex(KEY_DATE));
                selecteCalendar.movie_name_calendar = cursor.getString(cursor.getColumnIndex(KEY_MOVIE_NAME_CALENDAR));
                selecteCalendar.poster_image = cursor.getString(cursor.getColumnIndex(KEY_POSTER_IMAGE));
            }
            //} while (cursor.moveToNext());
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to select a calendar");
        } finally {
            db.endTransaction();
        }
        return selecteCalendar;
    }

    public void deleteCalendar(Calendar calendar_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String deleteUserQuery = String.format("Delete * FROM %s WHERE %s = ?",
                    TABLE_CALENDAR, KEY_CALENDAR_ID);
            Cursor cursor = db.rawQuery(deleteUserQuery, new String[]{String.valueOf(calendar_id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to delete a calendar");
        } finally {
            db.endTransaction();
        }
    }


    //Insert a user id and search id entry in the Contain Table
    public void addContain(int user_id, int calendar_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CALENDAR_ID_FK, calendar_id);
            values.put(KEY_USER_ID_CONTAIN_FK, user_id);
            db.insertOrThrow(TABLE_CONTAIN, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("tag", "Error trying to add user_id, calendar_id in Table Contain");
        }
    }


    //USER
    public void addUser(User user) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            //If primary key not specified it is auto incremented
            //values.put(KEY_USER_ID,user.user_id);
            values.put(KEY_USERNAME, user.username);
            values.put(KEY_PASSWORD, user.password);
            db.insertOrThrow(TABLE_USER, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("tag", "Error trying to add a user to the database");

        } finally {
            db.endTransaction();
        }

    }


    public void deleteUser(int user_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            //return db.delete(TABLE_USER, KEY_USER_ID + "=" + user_id, null) > 0;

//                String DELETE_USER = String.format("SELECT %s FROM %s WHERE %s = user_id",
//                *,TABLE_USER, KEY_USER_ID);
//                db.execSQL(DELETE_USER);

            String deleteUserQuery = String.format("Delete * FROM %s WHERE %s = ?",
                    TABLE_USER, KEY_USER_ID);
            Cursor cursor = db.rawQuery(deleteUserQuery, new String[]{String.valueOf(user_id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to delete a user");
        } finally {
            db.endTransaction();
        }
    }


    public void updateUser(int user_id, String user_password) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String updateUserQuery = String.format("Update %s FROM %s WHERE %s = ?",
                    KEY_PASSWORD, TABLE_USER, KEY_USER_ID);
            Cursor cursor = db.rawQuery(updateUserQuery, new String[]{String.valueOf(user_password), String.valueOf(user_id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to update a user");
        } finally {
            db.endTransaction();
        }
    }


    public User selectUser(int user_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        User requiredUser = new User();
        try {
            String selectUserQuery = String.format("Select * FROM %s WHERE %s = ?",
                    TABLE_USER, KEY_USER_ID);
            Cursor cursor = db.rawQuery(selectUserQuery, new String[]{String.valueOf(user_id)});
            if (cursor.moveToFirst()) {
                //do {
                requiredUser.user_id = cursor.getInt(cursor.getColumnIndex(KEY_USER_ID));
                requiredUser.username = cursor.getString(cursor.getColumnIndex(KEY_USERNAME));
                requiredUser.password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));
            }
            //} while (cursor.moveToNext());
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to select a user");
        } finally {
            db.endTransaction();
        }
        return requiredUser;
    }


}
