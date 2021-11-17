package com.example.gittogether;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SignUpTest {


    @Rule
    public ActivityTestRule<SignUp> mActivityTestRule= new ActivityTestRule<SignUp>(SignUp.class);
    private SignUp mActivity = null;

    Instrumentation.ActivityMonitor monitor=getInstrumentation().addMonitor(MainActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        mActivity=mActivityTestRule.getActivity();
    }

    @Test
    public void signuptest(){
     assertNotNull(mActivity.findViewById(R.id.bt_register));

     onView(withId(R.id.bt_register)).perform(click());
     Activity secondActivity= getInstrumentation().waitForMonitorWithTimeout(monitor,5000);

     assertNotNull(secondActivity);
     secondActivity.finish();
    }


    @After
    public void tearDown() throws Exception {
        mActivity=null;
    }
}