package com.example.android.prosbi;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class PrayerRequestActivity extends AppCompatActivity {
  private PrayerRequest prayerRequest;
  private EditText editTextRequester;
  private Button buttonRequestDate;
  private EditText editTextRequestSummary;
  private EditText editTextRequestDetails;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_prayer_request);
    prayerRequest = (PrayerRequest)
        getIntent().getSerializableExtra(MainActivity.KEY_PRAYER_REQUEST);
    configureRequester();
    configureDate();
    configureRequestSummary();
    configureRequestDetails();
  }

  private void configureRequester() {
    editTextRequester = (EditText) findViewById(R.id.edit_text_requester);
    editTextRequester.setText(prayerRequest.getRequester());
  }

  private void configureDate() {
    buttonRequestDate = (Button) findViewById(R.id.button_new_request);
    showDate();
    buttonRequestDate.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(prayerRequest.getRequestDate());
            new DatePickerDialog(
                PrayerRequestActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                  @Override
                  public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    prayerRequest.setRequestDate(calendar.getTime());
                    showDate();
                  }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
          }
        }
    );
  }

  private void showDate() {
    buttonRequestDate.setText(MainActivity.requestDateString(this, prayerRequest.getRequestDate()));
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
    saveToResult();
    super.onBackPressed();
  }

  private void saveToResult() {
    Intent result = new Intent();
    prayerRequest.setRequester(editTextRequester.getText().toString());
    prayerRequest.setRequestSummary(editTextRequestSummary.getText().toString());
    prayerRequest.setRequestDetails(editTextRequestDetails.getText().toString());
    result.putExtra(MainActivity.KEY_PRAYER_REQUEST, prayerRequest);
    setResult(RESULT_OK, result);
  }
}
