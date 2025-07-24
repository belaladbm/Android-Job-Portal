package com.example.group14_projectiteration1_quickcash;


import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.group14_projectiteration1_quickcash.model.Application;
import com.example.group14_projectiteration1_quickcash.view.PaypalActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PaypalActivityTest {

    private Application applicationForTesting(){
        Application newApp = new Application();
        newApp.setJobTitle("Tester");
        newApp.setJobPay(20.35);
        newApp.setEmployeeEmail("testing@gmail.com");
        return newApp;
    }

    @Test
    public void checkIfUIExistsAndDisplayed(){
        Application application = applicationForTesting();
        Intent intent = new Intent(getApplicationContext(), PaypalActivity.class);
        intent.putExtra("application", application);

        try (ActivityScenario<PaypalActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.jobDetailsTV)).check(matches(isDisplayed()));
            onView(withId(R.id.amountTV)).check(matches(isDisplayed()));
            onView(withId(R.id.payNowBtn)).check(matches(isDisplayed()));
            onView(withId(R.id.paymentStatusTV)).check(matches(isDisplayed()));
        }
    }


}
