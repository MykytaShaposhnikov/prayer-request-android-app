package com.example.android.prosbi;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import static com.example.android.prosbi.MainActivity.KEY_PRAYER_REQUEST;

public class CustomViewPagerAdapter extends PagerAdapter {
  private List<Map<String, Object>> requests;
  private Context context;

  public CustomViewPagerAdapter(Context context, List<Map<String, Object>> requests) {
    this.requests = requests;
    this.context = context;
  }

  @Override
  public int getCount() {
    return requests.size();
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    Map<String, Object> map = requests.get(position);
    PrayerRequest request = (PrayerRequest) map.get(KEY_PRAYER_REQUEST);
    PrayerRequestView view = new PrayerRequestView(context, request.getRequester(),
        request.getRequestSummary(), request.getRequestDetails());
    container.addView(view);
    return view;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }
}
