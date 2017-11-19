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
  private TextView description;


  public PrayerRequestView(Context context, String requestFrom, final String requestSummary, final String requestFull) {
    super(context);
    View.inflate(context, R.layout.prayer_request_view_layout, this);
    this.requestFrom = requestFrom;
    this.requestSummary = requestSummary;
    this.requestFull = requestFull;
    title = (TextView) findViewById(R.id.text_view_request_from);
    description = (TextView) findViewById(R.id.text_view_request);
    title.setText(requestFrom);
    description.setText(requestSummary);
    description.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        description.setText(requestFull);
      }
    });

  }

  public PrayerRequestView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    View.inflate(context, R.layout.prayer_request_view_layout, this);
  }
}
