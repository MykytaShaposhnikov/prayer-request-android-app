package com.example.android.prosbi;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static com.example.android.prosbi.MainActivity.KEY_PRAYER_REQUEST;
import static com.example.android.prosbi.MainActivity.KEY_REQUESTER;
import static com.example.android.prosbi.MainActivity.KEY_REQUEST_DATE;
import static com.example.android.prosbi.MainActivity.KEY_REQUEST_DATE_STRING;
import static com.example.android.prosbi.MainActivity.KEY_REQUEST_SUMMARY;
import static com.example.android.prosbi.MainActivity.requestDateString;

public class PrayerModeActivity extends AppCompatActivity {
  private List<PrayerRequest> requests;
  private ViewPager viewPager;
  private Settings settings;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_prayer_mode);
    settings = new Settings(this);
    requests = settings.loadPrayerRequestList();

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, 2);

    viewPager=(ViewPager) findViewById(R.id.view_pager_prayer_request);
    viewPager.setAdapter(new CustomViewPagerAdapter(this,requests));

  }

  private void putPrayerRequestToItemMap(
      final PrayerRequest prayerRequest, Map<String, Object> itemMap) {
    itemMap.put(KEY_PRAYER_REQUEST, prayerRequest);
    itemMap.put(KEY_REQUESTER, prayerRequest.getRequester());
    itemMap.put(KEY_REQUEST_DATE, prayerRequest.getRequestDate());
    itemMap.put(KEY_REQUEST_DATE_STRING, requestDateString(this, prayerRequest.getRequestDate()));
    itemMap.put(KEY_REQUEST_SUMMARY, prayerRequest.getRequestSummary());
  }

}
