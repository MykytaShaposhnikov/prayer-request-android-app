package com.example.android.prosbi;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

  public class CustomViewPagerAdapter extends PagerAdapter {
    private List<PrayerRequest> requests;
    private Context context;

    public CustomViewPagerAdapter(Context context, List<PrayerRequest> requests) {
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
    PrayerRequest request = requests.get(position);
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
