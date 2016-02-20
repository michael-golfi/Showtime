package com.example.showtime.app;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MovieListActivityTest {

    @Rule
    public ActivityTestRule<MovieListActivity> mActivityRule = new ActivityTestRule(
            MovieListActivity.class);

    @Test
    public void suggestionsOnKeystroke() {
        onView(withId(R.id.action_search)).check(matches(isDisplayed()));
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText("b"));
        onView(withText("Bat Hunter - 2006-01-15")).check(doesNotExist());
        onView(withId(R.id.search_src_text)).perform(typeText("a"));
        onView(withText("Bat Hunter - 2006-01-15")).check(doesNotExist());
        onView(withId(R.id.search_src_text)).perform(typeText("t")).check(matches(isDisplayed()));
        onView(withText("Bat Hunter - 2006-01-15")).check(matches(isDisplayed()));
        onView(withId(R.id.search_src_text)).perform(typeText("m"));
        onView(withText("Bat Hunter - 2006-01-15")).check(doesNotExist());
    }
}
