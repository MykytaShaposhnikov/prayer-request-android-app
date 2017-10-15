package com.example.android.prosbi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  private static final int ACTIVITY_PRAYER_REQUEST = 1;
  // Создаём пустой массив для хранения имен
  final ArrayList<String> names = new ArrayList<>();
  private ArrayAdapter<String> adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    createListView();
    findViewById(R.id.startNewActivity).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startPrayerRequestActivity("Nikita", "Today", "Football");
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case ACTIVITY_PRAYER_REQUEST:
        String from = data.getStringExtra(PrayerRequestActivity.KEY_REQUEST_FROM);
        String when = data.getStringExtra(PrayerRequestActivity.KEY_REQUEST_WHEN);
        String request = data.getStringExtra(PrayerRequestActivity.KEY_REQUEST);
        names.add(0, request);
        adapter.notifyDataSetChanged();
        break;
    }
  }

  private void createListView() {
    // получаем экземпляр элемента ListView
    final ListView listView = (ListView) findViewById(R.id.listView);
    // Создаём адаптер ArrayAdapter, чтобы привязать массив к ListView
    adapter = new ArrayAdapter<>(this,
        android.R.layout.simple_list_item_1, names);

    listView.setOnItemLongClickListener(
        new AdapterView.OnItemLongClickListener() {
          public boolean onItemLongClick(
              AdapterView<?> adapterView, View view, int position, long itemId) {
            confirmAndDeleteItem(adapterView.getItemAtPosition(position).toString());
            return true;
          }
        }
    );

    // Привяжем массив через адаптер к ListView
    listView.setAdapter(adapter);
    final EditText editText = (EditText) findViewById(R.id.editText);
    editText.setVisibility(View.INVISIBLE);

    // Прослушиваем нажатия клавиш
    editText.setOnKeyListener(new View.OnKeyListener() {
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
          if (keyCode == KeyEvent.KEYCODE_ENTER) {
            names.add(0, editText.getText().toString());
            adapter.notifyDataSetChanged();
            editText.setText("");
            return true;
          }
        return false;
      }
    });
  }

  private void confirmAndDeleteItem(final String requestToDelete) {
    new AlertDialog.Builder(MainActivity.this)
        .setMessage(getString(R.string.message_alert, adapter.getCount(), requestToDelete))
        .setNegativeButton(getString(R.string.button_no), null)
        .setPositiveButton(
            getString(R.string.button_yes),
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int arg1) {
                adapter.remove(requestToDelete);
                adapter.notifyDataSetChanged();
              }
            })
        .show();
  }

  private void startPrayerRequestActivity(String from, String when, String request) {
    Intent intent = new Intent(this, PrayerRequestActivity.class);
    intent.putExtra(PrayerRequestActivity.KEY_REQUEST_FROM, from);
    intent.putExtra(PrayerRequestActivity.KEY_REQUEST_WHEN, when);
    intent.putExtra(PrayerRequestActivity.KEY_REQUEST, request);
    startActivityForResult(intent, ACTIVITY_PRAYER_REQUEST);
  }
}
