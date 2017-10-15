package com.example.android.prosbi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
  private static final int ACTIVITY_PRAYER_REQUEST = 1;
  public static final String KEY_REQUEST_FROM = "from";
  public static final String KEY_REQUEST_WHEN = "when";
  public static final String KEY_REQUEST = "request";

  private List<Map<String, String>> requests;
  private SimpleAdapter listAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    loadPrayerRequestData();
    createListView();
    findViewById(R.id.button_new_request).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startPrayerRequestActivity(
            "",
            DateUtils.formatDateTime(
                MainActivity.this,
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR),
            "");
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case ACTIVITY_PRAYER_REQUEST:
        String from = data.getStringExtra(KEY_REQUEST_FROM);
        String when = data.getStringExtra(KEY_REQUEST_WHEN);
        String request = data.getStringExtra(KEY_REQUEST);
        // FIXME
//        requests.add(0, request);
//        adapter.notifyDataSetChanged();
        break;
    }
  }

  private void createListView() {
    // получаем экземпляр элемента ListView
    final ListView listView = (ListView) findViewById(R.id.list_view_requests);
    // Создаём адаптер ArrayAdapter, чтобы привязать массив к ListView
    listAdapter = new SimpleAdapter(
        this,
        requests,
        R.layout.list_item_prayer_request,
        new String[] {
            KEY_REQUEST_FROM,
            KEY_REQUEST_WHEN,
            KEY_REQUEST
        },
        new int[] {
            R.id.text_view_from,
            R.id.text_view_when,
            R.id.text_view_request
        });
    listView.setAdapter(listAdapter);
//    listView.setOnItemLongClickListener(
//        new AdapterView.OnItemLongClickListener() {
//          public boolean onItemLongClick(
//              AdapterView<?> adapterView, View view, int position, long itemId) {
//            confirmAndDeleteItem(adapterView.getItemAtPosition(position).toString());
//            return true;
//          }
//        }
//    );
  }

  private void configureSearchEditText() {
    final EditText editText = (EditText) findViewById(R.id.edit_text_search);
    editText.setVisibility(View.GONE);

    // Прослушиваем нажатия клавиш
//    editText.setOnKeyListener(new View.OnKeyListener() {
//      public boolean onKey(View v, int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN)
//          if (keyCode == KeyEvent.KEYCODE_ENTER) {
//            requests.add(0, editText.getText().toString());
//            adapter.notifyDataSetChanged();
//            editText.setText("");
//            return true;
//          }
//        return false;
//      }
//    });
  }

  private void confirmAndDeleteItem(final String requestToDelete) {
//    new AlertDialog.Builder(MainActivity.this)
//        .setMessage(getString(R.string.message_alert, adapter.getCount(), requestToDelete))
//        .setNegativeButton(getString(R.string.button_no), null)
//        .setPositiveButton(
//            getString(R.string.button_yes),
//            new DialogInterface.OnClickListener() {
//              public void onClick(DialogInterface dialog, int arg1) {
//                adapter.remove(requestToDelete);
//                adapter.notifyDataSetChanged();
//              }
//            })
//        .show();
  }

  private void startPrayerRequestActivity(String from, String when, String request) {
    Intent intent = new Intent(this, PrayerRequestActivity.class);
    intent.putExtra(KEY_REQUEST_FROM, from);
    intent.putExtra(KEY_REQUEST_WHEN, when);
    intent.putExtra(KEY_REQUEST, request);
    startActivityForResult(intent, ACTIVITY_PRAYER_REQUEST);
  }

  private void loadPrayerRequestData() {
    requests = new ArrayList<>();

    // TODO replace this test code with real loading of the requests from a JSON or a database
    Map<String, String> itemMap = new HashMap<>();
    itemMap.put(KEY_REQUEST_FROM, "Nikita S.");
    itemMap.put(KEY_REQUEST_WHEN, "Last week?");
    itemMap.put(KEY_REQUEST, "This is a sample resuest\nwith several line feeds\nand with some details.");
    requests.add(itemMap);

    itemMap = new HashMap<>();
    itemMap.put(KEY_REQUEST_FROM, "Denys D.");
    itemMap.put(KEY_REQUEST_WHEN, "Right now");
    itemMap.put(KEY_REQUEST, "If you see this then your first SimpleAdaper has started to work");
    requests.add(itemMap);
  }
}
