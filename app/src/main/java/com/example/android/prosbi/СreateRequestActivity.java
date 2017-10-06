package com.example.android.prosbi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class СreateRequestActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_request);
  }

  public void back() {
    TextView request = (TextView) findViewById(R.id.request);
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    //intent.putExtra("request",request.getText().toString());
  }
}
