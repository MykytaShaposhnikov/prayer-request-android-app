package com.example.android.prosbi;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.android.prosbi.MainActivity.KEY_PRAYER_REQUEST;
import static com.example.android.prosbi.MainActivity.KEY_REQUESTER;
import static com.example.android.prosbi.MainActivity.KEY_REQUEST_DATE;
import static com.example.android.prosbi.MainActivity.KEY_REQUEST_DATE_STRING;
import static com.example.android.prosbi.MainActivity.KEY_REQUEST_SUMMARY;
import static com.example.android.prosbi.MainActivity.requestDateString;

public class PrayerModeActivity extends AppCompatActivity {
  private List<Map<String, Object>> requests;
  private ViewPager viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_prayer_mode);
    requests = new ArrayList<>();

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, 2);
    PrayerRequest prayerRequest =
        new PrayerRequest(
            "Nikita S.",
            calendar.getTime(),
            "Sample request1",
            "This is a sample resuest\nwith several line feeds\nand with some details.");
    Map<String, Object> itemMap = new HashMap<>();
    putPrayerRequestToItemMap(prayerRequest, itemMap);
    requests.add(itemMap);

    calendar.add(Calendar.DAY_OF_YEAR, -1);
    PrayerRequest prayerRequest2 =
        new PrayerRequest(
            "Denis D.",
            calendar.getTime(),
            "Sample request2",
            "This is a sample resuest\nwith several line feeds\nand with some details."
        );
    itemMap = new HashMap<>();
    putPrayerRequestToItemMap(prayerRequest2, itemMap);
    requests.add(itemMap);

    calendar.add(Calendar.DAY_OF_YEAR, +10);
    PrayerRequest prayerRequest3 =
        new PrayerRequest(
            "Andrey D.",
            calendar.getTime(),
            "Sample request2",
            "This is a sample resuest\nwith several line feeds\nand with some details."
        );
    itemMap = new HashMap<>();
    putPrayerRequestToItemMap(prayerRequest3, itemMap);
    requests.add(itemMap);
    PrayerRequest prayerRequest4 =
        new PrayerRequest(
            "Boris K.",
            new Date(),
            "Sample request3",
            "If you see this then your first SimpleAdaper has started to work"
        );
    itemMap = new HashMap<>();
    putPrayerRequestToItemMap(prayerRequest4, itemMap);
    requests.add(itemMap);

    calendar.add(Calendar.DAY_OF_YEAR, -4);
    PrayerRequest prayerRequest5 =
        new PrayerRequest(
            "Никита Ш.",
            calendar.getTime(),
            "Sample request2",
            "This is a sample resuest\nwith several line feeds\nand with some details."
        );
    itemMap = new HashMap<>();
    putPrayerRequestToItemMap(prayerRequest5, itemMap);
    requests.add(itemMap);
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
