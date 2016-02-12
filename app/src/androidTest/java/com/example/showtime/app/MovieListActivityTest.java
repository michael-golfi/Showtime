package com.example.showtime.app;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class MovieListActivityTest {

    @Rule
    public ActivityTestRule<MovieListActivity> mActivityRule = new ActivityTestRule(MovieListActivity.class);

    @Test
    public void suggestionsOnKeystroke() {
        onView(withId(R.id.action_search)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText("b"));
        onView(withId(android.R.layout.simple_list_item_activated_1)).check(ViewAssertions.doesNotExist());
        onView(withId(R.id.search_src_text)).perform(typeText("a"));
        onView(withId(android.R.layout.simple_list_item_activated_1)).check(ViewAssertions.doesNotExist());
        onView(withId(R.id.search_src_text)).perform(typeText("t")).check(ViewAssertions.matches(isDisplayed()));
    }
}
