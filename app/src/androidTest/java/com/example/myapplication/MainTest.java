package com.example.myapplication;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.annotation.ContentView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)

@LargeTest
public class MainTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void listGoesOverTheFold() {


        Espresso.onView(ViewMatchers.withText("Descubra")).check(matches(isDisplayed())).perform(ViewActions.click());

        try {
            Thread.sleep(2000); // 10000 milissegundos (10 segundos)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(ViewMatchers.withText("Compartilhar")).check(matches(isEnabled()));


        try {
            Thread.sleep(500); // 10000 milissegundos (10 segundos)
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }

        Espresso.onView(ViewMatchers.withText("Compartilhar")).check(matches(isEnabled())).perform(ViewActions.click());

        try {
            Thread.sleep(10500); // 10000 milissegundos (10 segundos)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }


}
