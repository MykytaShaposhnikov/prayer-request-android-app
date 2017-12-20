package ua.prayerrequests;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CustomViewPagerAdapter extends PagerAdapter {
  private List<PrayerRequest> requests;
  private Context context;
  private boolean isNightMode = false;
  private boolean isChecked;

  public CustomViewPagerAdapter(Context context, List<PrayerRequest> requests) {
    this.requests = requests;
    this.context = context;
  }

  public void setNightMode(boolean nightMode) {
    isNightMode = nightMode;
    notifyDataSetChanged();
  }

  public void setFiltration(boolean checked) {
    isChecked = checked;
    if (isChecked=false)

    notifyDataSetChanged();
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
    view.setNightMode(isNightMode);
    setFiltration(isChecked);
    view.changingLessToMore();
    container.addView(view);
    return view;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }

  @Override
  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }
}
