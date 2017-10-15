package com.example.android.prosbi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class PrayerRequestActivity extends AppCompatActivity {

  public static final String KEY_REQUEST_FROM = "from";
  public static final String KEY_REQUEST_WHEN = "when";
  public static final String KEY_REQUEST = "request";

  private EditText editTextFrom;
  private EditText editTextWhen;
  private EditText editTextRequest;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_prayer_request);
    configureFrom();
    configureWhen();
    configureRequest();
  }

  private void configureFrom() {
    editTextFrom = (EditText) findViewById(R.id.edit_text_from);
    editTextFrom.setText(getIntent().getStringExtra(KEY_REQUEST_FROM));
  }

  private void configureWhen() {
    editTextWhen = (EditText) findViewById(R.id.edit_text_when);
    editTextWhen.setText(getIntent().getStringExtra(KEY_REQUEST_WHEN));
  }

  private void configureRequest() {
    editTextRequest = (EditText) findViewById(R.id.edit_text_request);
    editTextRequest.setText(getIntent().getStringExtra(KEY_REQUEST));
  }

  public void onBackPressed() {
    saveToResult();
    super.onBackPressed();
  }

  private void saveToResult() {
    Intent result = new Intent();
    result.putExtra(KEY_REQUEST_FROM, editTextFrom.getText().toString());
    result.putExtra(KEY_REQUEST_WHEN, editTextWhen.getText().toString());
    result.putExtra(KEY_REQUEST, editTextRequest.getText().toString());
    setResult(RESULT_OK, result);
  }

}
