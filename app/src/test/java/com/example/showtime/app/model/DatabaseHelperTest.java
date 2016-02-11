package com.example.showtime.app.model;

import com.example.showtime.app.BuildConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
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
    public void createMovieTest() throws SQLException {
        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setTitle("UP");
        movie1.setReleaseDate("29-05-2009");
        movie1.setOverview("Seventy-eight year old Carl Fredricksen travels to Paradise Falls in his home equipped with balloons, inadvertently taking a young stowaway.");
        databaseHelper.createMovie(movie1);
        assertTrue(databaseHelper.getCountOfMovies() == 1);
        Movie movie2;
        movie2 = databaseHelper.getMovie(1);
        assertTrue(movie2.getId() == 1);
        assertTrue(movie2.getTitle().equals("UP"));
        assertTrue(movie2.getReleaseDate().equals("29-05-2009"));
        assertTrue(movie2.getOverview().equals("Seventy-eight year old Carl Fredricksen travels to Paradise Falls in his home equipped with balloons, inadvertently taking a young stowaway."));
    }

    //Test we cannot add twice a movie with the same id to the database
    @Test
    public void createMovieTwiceWithSameIdTest() throws SQLException{
        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setTitle("UP");
        movie1.setReleaseDate("29-05-2009");
        movie1.setOverview("Seventy-eight year old Carl Fredricksen travels to Paradise Falls in his home equipped with balloons, inadvertently taking a young stowaway.");
        databaseHelper.createMovie(movie1);
        Movie movie2 = new Movie();
        movie2.setId(1);
        movie2.setTitle("UP");
        movie2.setReleaseDate("29-05-2009");
        movie2.setOverview("Seventy-eight year old Carl Fredricksen travels to Paradise Falls in his home equipped with balloons, inadvertently taking a young stowaway.");
        databaseHelper.createMovie(movie2);
        assertTrue(databaseHelper.getCountOfMovies() == 1);

    }

    //Test retrieving a specific movie from the database
    @Test
    public void getMovieTest(){
        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setTitle("UP");
        movie1.setReleaseDate("29-05-2009");
        movie1.setOverview("Seventy-eight year old Carl Fredricksen travels to Paradise Falls in his home equipped with balloons, inadvertently taking a young stowaway.");
        Movie movie2 = new Movie();
        movie2.setId(2);
        movie2.setTitle("Toy Story");
        movie2.setReleaseDate("22-11-1995");
        movie2.setOverview("A cowboy doll is profoundly threatened and jealous when a new spaceman figure supplants him as top toy in a boy's room.");
        databaseHelper.createMovie(movie1);
        databaseHelper.createMovie(movie2);
        Movie movie3 = databaseHelper.getMovie(2);
        assertTrue(movie3.getId() == 2);
        assertTrue(movie3.getTitle().equals("Toy Story"));
        assertTrue(movie3.getReleaseDate().equals("22-11-1995"));
        assertTrue(movie3.getOverview().equals("A cowboy doll is profoundly threatened and jealous when a new spaceman figure supplants him as top toy in a boy's room."));

    }

    //Test deleting a movie from the database
    @Test
    public void deleteMovieTest(){
        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setTitle("UP");
        movie1.setReleaseDate("29-05-2009");
        movie1.setOverview("Seventy-eight year old Carl Fredricksen travels to Paradise Falls in his home equipped with balloons, inadvertently taking a young stowaway.");
        databaseHelper.createMovie(movie1);
        assertTrue(databaseHelper.getCountOfMovies() == 1);
        databaseHelper.deleteMovie(1);
        assertTrue(databaseHelper.getCountOfMovies() == 0);
    }

    //Test if a specific movie exists in the database
    @Test
    public void movieExistsTest(){
        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setTitle("UP");
        movie1.setReleaseDate("29-05-2009");
        movie1.setOverview("Seventy-eight year old Carl Fredricksen travels to Paradise Falls in his home equipped with balloons, inadvertently taking a young stowaway.");
        databaseHelper.createMovie(movie1);
        assertTrue(databaseHelper.movieExists(1));
        assertFalse(databaseHelper.movieExists(2));
    }

}

