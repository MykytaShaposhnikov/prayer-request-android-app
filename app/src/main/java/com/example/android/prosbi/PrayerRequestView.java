package com.example.android.prosbi;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by User on 011 11.11.17.
 */

public class PrayerRequestView extends LinearLayout {
  private String requestFrom;
  private String requestSummary;
  private String requestFull;
  private TextView title;
  private TextView less;
  private TextView descriptionSummary;
  private TextView descriptionDetails;
  private TextView more;
  int i=0;


  public PrayerRequestView(Context context, String requestFrom, final String requestSummary, final String requestFull) {
    super(context);
    View.inflate(context, R.layout.prayer_request_view_layout, this);
    this.requestFrom = requestFrom;
    this.requestSummary = requestSummary;
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
     if(i==0)
       changingMoreToLess();
        else
       changingLessToMore();
      }
    });
  }

  public void NightMode()
  {

  }

  public void changingMoreToLess()
  { i=1;
    descriptionDetails.setText(requestFull);
    more.setText(R.string.text_less);
  }

  public void changingLessToMore()
  {i=0;
    descriptionDetails.setText("");
    more.setText(R.string.text_more);
  }

  public PrayerRequestView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    View.inflate(context, R.layout.prayer_request_view_layout, this);
  }
}
