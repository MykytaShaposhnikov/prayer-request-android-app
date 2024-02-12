package ua.prayerrequests;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by User on 011 11.11.17.
 */

public class PrayerRequestView extends LinearLayout {
  int i = 0;
  private String requestFull;
  private TextView title;
  private TextView descriptionSummary;
  private TextView descriptionDetails;
  private TextView more;
  private boolean isNightMode = false;


  public PrayerRequestView(Context context, String requestFrom, final String requestSummary, final String requestFull) {
    super(context);
    View.inflate(context, R.layout.prayer_request_view_layout, this);
    this.requestFull = requestFull;
    title = (TextView) findViewById(R.id.text_view_request_from);
    descriptionSummary = (TextView) findViewById(R.id.text_view_request_summary);
    descriptionDetails = (TextView) findViewById(R.id.text_view_request_details);
    more = (TextView) findViewById(R.id.text_view_more);
    title.setText(requestFrom);
    descriptionSummary.setText(requestSummary);
    more.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (i == 0)
          changingMoreToLess();
        else
          changingLessToMore();
      }
    });
  }

  public PrayerRequestView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    View.inflate(context, R.layout.prayer_request_view_layout, this);
  }

  public void setNightMode(boolean nightMode) {
    isNightMode = nightMode;
    changeMode();
  }

  public void changingMoreToLess() {
    i = 1;
    descriptionDetails.setText(requestFull);
    more.setText(R.string.text_less);
  }

  public void changingLessToMore() {
    i = 0;
    descriptionDetails.setText("");
    more.setText(R.string.text_more);
  }

  private void changeMode() {
    int backgroundColor = isNightMode ?
        getResources().getColor(R.color.colorNightModeBackground) :
        getResources().getColor(R.color.transparent);
    int textColor = isNightMode ?
        getResources().getColor(R.color.colorNightModeText) :
        getResources().getColor(R.color.colorNightModeBackground);

    setBackgroundColor(backgroundColor);
    TextView textViewRequestFrom = (TextView) findViewById(R.id.text_view_request_from);
    textViewRequestFrom.setTextColor(textColor);
    TextView textViewRequestSummary = (TextView) findViewById(R.id.text_view_request_summary);
    textViewRequestSummary.setTextColor(textColor);
    TextView textViewRequestDetails = (TextView) findViewById(R.id.text_view_request_details);
    textViewRequestDetails.setTextColor(textColor);
    TextView textViewMore = (TextView) findViewById(R.id.text_view_more);
    textViewMore.setTextColor(textColor);
  }

}
