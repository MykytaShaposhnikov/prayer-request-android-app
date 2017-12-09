package com.example.android.prosbi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import static com.example.android.prosbi.MainActivity.KEY_PRAYER_REQUEST;

public class PrayerRequestActivity extends AppCompatActivity {
  private PrayerRequest initialPrayerRequest;
  private PrayerRequest prayerRequest;
  private EditText editTextRequester;
  private TextView buttonRequestDate;
  private EditText editTextRequestSummary;
  private EditText editTextRequestDetails;
  private MaterialCalendarView calendar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_prayer_request);
    prayerRequest = new Gson().fromJson(getIntent().getStringExtra(KEY_PRAYER_REQUEST),
        PrayerRequest.class);
    initialPrayerRequest = new PrayerRequest(prayerRequest);
    configureRequester();
    configureDate();
    configureRequestSummary();
    configureRequestDetails();
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    findViewById(R.id.date_card_view).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            calendar.setVisibility(calendar.isShown() ? View.GONE : View.VISIBLE);
          }
        });
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.prayer_request, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        break;

      case R.id.item_save:
        if (!isChangedForButtonSave()) {
          super.onBackPressed();
        }
        saveAndFinish();
        break;
    }
    return true;
  }

  private void configureRequester() {
    editTextRequester = (EditText) findViewById(R.id.edit_text_requester);
    editTextRequester.setText(prayerRequest.getRequester());
  }

  private void configureDate() {
    calendar = (MaterialCalendarView) findViewById(R.id.calendar);
    calendar.setDateSelected(prayerRequest.getRequestDate(),true);
    calendar.setCurrentDate(prayerRequest.getRequestDate());
    calendar.setOnDateChangedListener(
        new OnDateSelectedListener() {
          @Override
          public void onDateSelected(@NonNull MaterialCalendarView widget,
                                     @NonNull CalendarDay date, boolean selected) {
            prayerRequest.setRequestDate(date.getCalendar().getTime());
            showDate();
          }
        }
    );
    buttonRequestDate = (TextView) findViewById(R.id.text_view_request_date);
    showDate();
//    buttonRequestDate.setOnClickListener(
//        new View.OnClickListener() {
//          @Override
//          public void onClick(View view) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(prayerRequest.getRequestDate());
//            new DatePickerDialog(
//                PrayerRequestActivity.this,
//                new DatePickerDialog.OnDateSetListener() {
//                  @Override
//                  public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.set(Calendar.YEAR, year);
//                    calendar.set(Calendar.MONTH, month);
//                    calendar.set(Calendar.DAY_OF_MONTH, day);
//                    calendar.set(Calendar.HOUR_OF_DAY, 0);
//                    calendar.set(Calendar.MINUTE, 0);
//                    calendar.set(Calendar.SECOND, 0);
//                    prayerRequest.setRequestDate(calendar.getTime());
//                    showDate();
//                  }
//                },
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)
//            ).show();
//          }
//        }
//    );
  }

  private void showDate() {
    buttonRequestDate.setText(getResources().getString(
        R.string.text_date,
        MainActivity.requestDateString(prayerRequest.getRequestDate())
        )
    );
  }

  private void configureRequestSummary() {
    editTextRequestSummary = (EditText) findViewById(R.id.edit_text_request_summary);
    editTextRequestSummary.setText(prayerRequest.getRequestSummary());
  }

  private void configureRequestDetails() {
    editTextRequestDetails = (EditText) findViewById(R.id.edit_text_request_details);
    editTextRequestDetails.setText(prayerRequest.getRequestDetails());
  }

  public void onBackPressed() {
    if (!isChanged()) {
      super.onBackPressed();
    }
  }

  private void collectFromFields() {
    prayerRequest.setRequester(editTextRequester.getText().toString());
    prayerRequest.setRequestSummary(editTextRequestSummary.getText().toString());
    prayerRequest.setRequestDetails(editTextRequestDetails.getText().toString());
  }

  private void saveAndFinish() {
    Intent result = new Intent();
    collectFromFields();
    result.putExtra(KEY_PRAYER_REQUEST, new Gson().toJson(prayerRequest));
    setResult(RESULT_OK, result);
    super.onBackPressed();
  }

  private boolean isChanged() {
    boolean changed = false;
    collectFromFields();
    if (!prayerRequest.equals(initialPrayerRequest)) {
      changed = true;
      AlertDialog dialogSavePrayerRequest =
          new AlertDialog.Builder(this)
              .setTitle(R.string.title_prayer_request)
              .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  saveAndFinish();
                }
              })
              .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  Intent intent = new Intent(PrayerRequestActivity.this, MainActivity.class);
                  startActivity(intent);
                }
              }).create();
      dialogSavePrayerRequest.show();
    }
    return changed;
  }

  private boolean isChangedForButtonSave() {
    boolean changed = false;
    collectFromFields();
    if (!prayerRequest.equals(initialPrayerRequest)) {
      changed = true;
      saveAndFinish();
    }
    return changed;
  }


}
