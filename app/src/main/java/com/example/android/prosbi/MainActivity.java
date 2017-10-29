package com.example.android.prosbi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
  private static final int ACTIVITY_PRAYER_REQUEST = 1;
  public static final String KEY_PRAYER_REQUEST = "prayer_request";
  public static final String KEY_REQUESTER = "requester";
  public static final String KEY_REQUEST_DATE = "date";
  public static final String KEY_REQUEST_SUMMARY = "summary";
  private List<Map<String, Object>> requests;
  private SimpleAdapter listAdapter;
  private int indexOrPrayerRequestBeingEdited;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    loadPrayerRequestData();
    createListView();
    findViewById(R.id.button_new_request).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            indexOrPrayerRequestBeingEdited = -1;
            startPrayerRequestActivity(new PrayerRequest());
          }
        });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case ACTIVITY_PRAYER_REQUEST:
        PrayerRequest prayerRequest = (PrayerRequest) data.getSerializableExtra(KEY_PRAYER_REQUEST);
        if (indexOrPrayerRequestBeingEdited >= 0) {
          Map<String, Object> itemMap = requests.get(indexOrPrayerRequestBeingEdited);
          putPrayerRequestToItemMap(prayerRequest, itemMap);
        } else {
          Map<String, Object> itemMap = new HashMap<>();
          putPrayerRequestToItemMap(prayerRequest, itemMap);
          requests.add(itemMap);
        }
        createListView();
        listAdapter.notifyDataSetChanged();
        break;
    }
  }

  private void createListView() {
    final ListView listView = (ListView) findViewById(R.id.list_view_requests);
    listAdapter = new SimpleAdapter(
        this,
        requests,
        R.layout.list_item_prayer_request,
        new String[]{
            KEY_REQUESTER,
            KEY_REQUEST_DATE,
            KEY_REQUEST_SUMMARY
        },
        new int[]{
            R.id.text_view_requester,
            R.id.text_view_request_date,
            R.id.text_view_request_summary
        });
    listView.setAdapter(listAdapter);
    listView.setOnItemClickListener(
        new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            indexOrPrayerRequestBeingEdited = position;
            startPrayerRequestActivity(
                (PrayerRequest) requests.get(position).get(KEY_PRAYER_REQUEST));
          }
        }
    );
    listView.setOnItemLongClickListener(
        new AdapterView.OnItemLongClickListener() {
          public boolean onItemLongClick(
              AdapterView<?> adapterView, View view, int position, long itemId) {
            confirmAndDeleteItem(adapterView.getItemAtPosition(position).toString(), position);
            return true;
          }
        }
    );
  }

  private void configureSearchEditText() {
    final EditText editText = (EditText) findViewById(R.id.edit_text_search);
    editText.setVisibility(View.GONE);
  }

  private void confirmAndDeleteItem(final String requestToDelete, final int position) {
    new AlertDialog.Builder(MainActivity.this)
        .setMessage(getString(R.string.message_alert, listAdapter.getCount(), requestToDelete))
        .setNegativeButton(getString(R.string.button_no), null)
        .setPositiveButton(
            getString(R.string.button_yes),
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int arg1) {
                requests.remove(position);
                listAdapter.notifyDataSetChanged();
              }
            })
        .show();
  }

  private void startPrayerRequestActivity(PrayerRequest prayerRequest) {
    Intent intent = new Intent(this, PrayerRequestActivity.class);
    intent.putExtra(KEY_PRAYER_REQUEST, prayerRequest);
    startActivityForResult(intent, ACTIVITY_PRAYER_REQUEST);
  }

  private void loadPrayerRequestData() {
    requests = new ArrayList<>();

    PrayerRequest prayerRequest =
        new PrayerRequest(
            "Nikita S.",
            new Date(),
            "Sample request1",
            "This is a sample resuest\nwith several line feeds\nand with some details.");
    Map<String, Object> itemMap = new HashMap<>();
    putPrayerRequestToItemMap(prayerRequest, itemMap);
    requests.add(itemMap);

    PrayerRequest prayerRequest2 =
        new PrayerRequest(
            "Denis D.",
            new Date(),
            "Sample request2",
            "This is a sample resuest\nwith several line feeds\nand with some details."
        );
    itemMap = new HashMap<>();
    putPrayerRequestToItemMap(prayerRequest2, itemMap);
    requests.add(itemMap);

    PrayerRequest prayerRequest3 =
        new PrayerRequest(
            "Denis D.",
            new Date(),
            "Sample request3",
            "If you see this then your first SimpleAdaper has started to work"
        );
    itemMap = new HashMap<>();
    putPrayerRequestToItemMap(prayerRequest3, itemMap);
    requests.add(itemMap);
   // alphabetizing(requests);
  }

  private void putPrayerRequestToItemMap(
      final PrayerRequest prayerRequest, Map<String, Object> itemMap) {
    itemMap.put(KEY_PRAYER_REQUEST, prayerRequest);
    itemMap.put(KEY_REQUESTER, prayerRequest.getRequester());
    itemMap.put(KEY_REQUEST_DATE, requestDateString(this, prayerRequest.getRequestDate()));
    itemMap.put(KEY_REQUEST_SUMMARY, prayerRequest.getRequestSummary());
  }

  public static String requestDateString(Context context, Date requestDate) {
    return DateUtils.formatDateTime(context, requestDate.getTime(), DateUtils.FORMAT_SHOW_DATE);
  }

  private void alphabetizing(final List<Map<String, String>> requests) {
    findViewById(R.id.button_alphabetizing).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
           final  Comparator<Map<String, String>> mapComparator = new Comparator<Map<String, String>>() {
              public int compare(Map<String, String> m1, Map<String, String> m2) {
                return m1.get("name").compareTo(m2.get("name"));
              }
            };
            Collections.sort(requests, mapComparator);
          }
        });
  }

}
