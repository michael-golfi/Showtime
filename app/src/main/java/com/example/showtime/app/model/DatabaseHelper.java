package com.example.showtime.app.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.showtime.app.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.DataOutput;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Created by txrdelage on 07/02/16.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "showtime.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 2;

    private Dao<Movie, Integer> movieDao = null;

    private Dao<Query, Integer> queryDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Movie.class);
            TableUtils.createTable(connectionSource, Query.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Movie.class, true);
            TableUtils.dropTable(connectionSource, Query.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our Movie class. It will create it or just give the cached
     * value.
     */
    public Dao<Movie, Integer> getMovieDao() throws SQLException {
        if (movieDao == null) {
            movieDao = getDao(Movie.class);
        }
        return movieDao;
    }

    /**
     * Returns a list of past Queries in order of recency (newest to oldest)
     */
    public List<Query> getQueries() throws SQLException {
        if (queryDao == null) {
            queryDao = getDao(Query.class);
        }

        List<Query> query = queryDao.queryForAll();
        Collections.reverse(query);
        return query;
    }

    public void createQuery(String query) {
        Query q = new Query(query);
        try {
            if (queryDao == null) {
                queryDao = getDao(Query.class);
            }
            queryDao.createIfNotExists(q);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteQueries() {
        try {
            if (queryDao == null) {
                queryDao = getDao(Query.class);
            }

            List<Query> queries = getQueries();
            if (queries.size() > 0) {
                queryDao.delete(queries);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createMovie(Movie movie) {
        try {
            if (movieDao == null) {
                movieDao = getDao(Movie.class);
            }
            movieDao.createIfNotExists(movie);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Movie getMovie(int id) {
        try {
            if (movieDao == null) {
                movieDao = getDao(Movie.class);
            }
            return movieDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteMovie(int id) {
        try {
            if (movieDao == null) {
                movieDao = getDao(Movie.class);
            }
            movieDao.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean movieExists(int id) {
        try {
            if (movieDao == null) {
                movieDao = getDao(Movie.class);
            }
            return movieDao.idExists(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public long getCountOfMovies(){
        try {
            if (movieDao == null) {
                movieDao = getDao(Movie.class);
            }
            return movieDao.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateNotes(int id, String notes){
        try {
            Log.d("Database Helper", "Updating notes:"+notes);
            if (movieDao == null) {
                movieDao = getDao(Movie.class);
            }
            UpdateBuilder<Movie, Integer> updateBuilder = movieDao.updateBuilder();
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("notes" /* column */, notes /* value */);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void close() {
        super.close();
        movieDao = null;
        queryDao = null;
    }
}
