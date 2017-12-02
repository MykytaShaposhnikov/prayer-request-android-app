package com.example.android.prosbi;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;

public class PrayerModeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTitle("Prayer Mode");
    setContentView(R.layout.activity_prayer_mode);

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, 2);

    ViewPager viewPager = (ViewPager)
        findViewById(R.id.view_pager_prayer_request);
    viewPager.setAdapter(
        new CustomViewPagerAdapter(
            this, new Settings(this).loadPrayerRequests()));
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.prayer_mode, menu);
    return super.onCreateOptionsMenu(menu);
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        super.onBackPressed();
        break;
      case R.id.item_night_mode:

        break;
    }

    return true;
  }
}
