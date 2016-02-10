import android.content.Context;
import android.util.Log;

import com.example.showtime.app.BuildConfig;
import com.example.showtime.app.model.DatabaseHelper;
import com.example.showtime.app.model.Movie;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;

import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by txrdelage on 08/02/16.
 */

@Config(constants = BuildConfig.class, sdk = 18) // because Robolectric does not yet support API Level 19
@RunWith(RobolectricGradleTestRunner.class)
public class DatabaseHelperTest {

    private DatabaseHelper databaseHelper = null;


    private DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null)
            //databaseHelper = OpenHelperManager.getHelper(RuntimeEnvironment.application.getApplicationContext(), DatabaseHelper.class);
            databaseHelper = new DatabaseHelper(RuntimeEnvironment.application.getApplicationContext());
        return databaseHelper;
    }

    @BeforeClass
    public static void setup(){
        // To redirect Robolectric to stdout
        System.setProperty("robolectric.logging", "stdout");
    }

    @Before
    public void getDatabaseHelperSetUp(){
        databaseHelper = getDatabaseHelper();
    }


    @After
    public void tearDown(){
        databaseHelper.close();
        databaseHelper = null;
    }

    //Test movie is properly added to the database and can be queried from the database
    @Test
    public void addMovieTest() throws SQLException {
        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setTitle("UP");
        movie1.setReleaseDate("29-05-2009");
        movie1.setOverview("Seventy-eight year old Carl Fredricksen travels to Paradise Falls in his home equipped with balloons, inadvertently taking a young stowaway.");
        //moviesDao.create(movie);
        databaseHelper.createMovie(movie1);
        Dao<Movie, Integer> moviesDao = databaseHelper.getMovieDao();
        assertTrue(moviesDao.countOf() == 1);
        //assertTrue(moviesDao.queryForAll().get(0).equals(movie));
        Movie movie2;
        movie2 = moviesDao.queryForId(1);
        assertTrue(movie2.getId() == 1);
        assertTrue(movie2.getTitle().equals("UP"));
        assertTrue(movie2.getReleaseDate().equals("29-05-2009"));
        assertTrue(movie2.getOverview().equals("Seventy-eight year old Carl Fredricksen travels to Paradise Falls in his home equipped with balloons, inadvertently taking a young stowaway."));
    }

    //Test we cannot add twice a movie with the same id to the database
    @Test(expected=SQLException.class)
    public void addTwiceMovieWithSameIdTest() throws SQLException{
       // databaseHelper = getDatabaseHelper();
        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setTitle("UP");
        movie1.setReleaseDate("29-05-2009");
        movie1.setOverview("Seventy-eight year old Carl Fredricksen travels to Paradise Falls in his home equipped with balloons, inadvertently taking a young stowaway.");
        //moviesDao.create(movie);
        databaseHelper.createMovie(movie1);
        Movie movie2 = new Movie();
        movie2.setId(1);
        movie2.setTitle("UP");
        movie2.setReleaseDate("29-05-2009");
        movie2.setOverview("Seventy-eight year old Carl Fredricksen travels to Paradise Falls in his home equipped with balloons, inadvertently taking a young stowaway.");
        databaseHelper.createMovie(movie2);

    }

    //Test retrieving all movies from the database
    @Test
    public void retrieveAllMoviesFromDatabaseTest(){
        
    }

}

