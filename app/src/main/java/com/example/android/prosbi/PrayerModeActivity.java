package com.example.android.prosbi;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;

public class PrayerModeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_prayer_mode);

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, 2);

    ViewPager viewPager = (ViewPager)
        findViewById(R.id.view_pager_prayer_request);
    viewPager.setAdapter(
        new CustomViewPagerAdapter(
            this, new Settings(this).loadPrayerRequests()));
  }
}
