package com.example.android.prosbi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Calendar;
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
  public static final String KEY_REQUEST_DATE_STRING = "date_string";
  public static final String KEY_REQUEST_DATE = "date";
  public static final String KEY_REQUEST_SUMMARY = "summary";
  private List<Map<String, Object>> requests;
  private SimpleAdapter listAdapter;
  private int indexOrPrayerRequestBeingEdited;
  private SortingType sortingType = SortingType.BY_REQUESTER;

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
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.item_sorting:
        final View dialogContent = View.inflate(this, R.layout.sorting_layout, null);
        RadioButton selectedRadioButton = null;
        switch (sortingType) {
          case BY_REQUESTER:
            selectedRadioButton =
                dialogContent.findViewById(R.id.radio_button_sorting_by_requester);
            break;
          case BY_REQUEST_DATE_ASCENDING:
            selectedRadioButton =
                dialogContent.findViewById(R.id.radio_button_sorting_by_date_ascending);
            break;
          case BY_REQUEST_DATE_DESCENDING:
            selectedRadioButton =
                dialogContent.findViewById(R.id.radio_button_sorting_by_date_descending);
            break;
        }
        selectedRadioButton.setChecked(true);
        AlertDialog dialog =
            new AlertDialog.Builder(this)
                .setTitle(R.string.title_sort_by)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                    if (((RadioButton) dialogContent.
                        findViewById(R.id.radio_button_sorting_by_requester)).isChecked()) {
                      sortingType = SortingType.BY_REQUESTER;
                    } else if (((RadioButton) dialogContent.
                        findViewById(R.id.radio_button_sorting_by_date_descending)).isChecked()) {
                      sortingType = SortingType.BY_REQUEST_DATE_DESCENDING;
                    } else if (((RadioButton) dialogContent.
                        findViewById(R.id.radio_button_sorting_by_date_ascending)).isChecked()) {
                      sortingType = SortingType.BY_REQUEST_DATE_ASCENDING;
                    }
                    sortPrayerRequests();
                    listAdapter.notifyDataSetChanged();
                  }
                })
                .setNegativeButton(R.string.button_cancel, null).create();
        dialog.setView(dialogContent);
        dialog.show();
        break;
    }
    return true;
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
            KEY_REQUEST_DATE_STRING,
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

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, 2);
    PrayerRequest prayerRequest =
        new PrayerRequest(
            "Nikita S.",
            calendar.getTime(),
            "Sample request1",
            "This is a sample resuest\nwith several line feeds\nand with some details.");
    Map<String, Object> itemMap = new HashMap<>();
    putPrayerRequestToItemMap(prayerRequest, itemMap);
    requests.add(itemMap);

    calendar.add(Calendar.DAY_OF_YEAR, -1);
    PrayerRequest prayerRequest2 =
        new PrayerRequest(
            "Denis D.",
            calendar.getTime(),
            "Sample request2",
            "This is a sample resuest\nwith several line feeds\nand with some details."
        );
    itemMap = new HashMap<>();
    putPrayerRequestToItemMap(prayerRequest2, itemMap);
    requests.add(itemMap);

    calendar.add(Calendar.DAY_OF_YEAR, +10);
    PrayerRequest prayerRequest3 =
        new PrayerRequest(
            "Andrey D.",
            calendar.getTime(),
            "Sample request2",
            "This is a sample resuest\nwith several line feeds\nand with some details."
        );
    itemMap = new HashMap<>();
    putPrayerRequestToItemMap(prayerRequest3, itemMap);
    requests.add(itemMap);
    PrayerRequest prayerRequest4 =
        new PrayerRequest(
            "Boris K.",
            new Date(),
            "Sample request3",
            "If you see this then your first SimpleAdaper has started to work"
        );
    itemMap = new HashMap<>();
    putPrayerRequestToItemMap(prayerRequest4, itemMap);
    requests.add(itemMap);

    calendar.add(Calendar.DAY_OF_YEAR, -4);
    PrayerRequest prayerRequest5 =
        new PrayerRequest(
            "Никита Ш.",
            calendar.getTime(),
            "Sample request2",
            "This is a sample resuest\nwith several line feeds\nand with some details."
        );
    itemMap = new HashMap<>();
    putPrayerRequestToItemMap(prayerRequest5, itemMap);
    requests.add(itemMap);
    sortPrayerRequests();
  }

  private void sortPrayerRequests() {
    Comparator<Map<String, Object>> comparator = null;

    switch (sortingType) {
      case BY_REQUESTER:
        comparator = new Comparator<Map<String, Object>>() {
          public int compare(Map<String, Object> left, Map<String, Object> right) {
            return ((String) left.get(KEY_REQUESTER)).compareToIgnoreCase(
                ((String) right.get(KEY_REQUESTER)));
          }
        };
        break;

      case BY_REQUEST_DATE_ASCENDING:
        comparator = new Comparator<Map<String, Object>>() {
          public int compare(Map<String, Object> left, Map<String, Object> right) {
            return ((Date) left.get(KEY_REQUEST_DATE)).compareTo(
                ((Date) right.get(KEY_REQUEST_DATE)));
          }
        };
        break;

      case BY_REQUEST_DATE_DESCENDING:
        comparator = new Comparator<Map<String, Object>>() {
          public int compare(Map<String, Object> left, Map<String, Object> right) {
            return -((Date) left.get(KEY_REQUEST_DATE)).compareTo(
                ((Date) right.get(KEY_REQUEST_DATE)));
          }
        };
        break;

    }
    if (comparator != null) {
      Collections.sort(requests, comparator);
    }
  }

  private void putPrayerRequestToItemMap(
      final PrayerRequest prayerRequest, Map<String, Object> itemMap) {
    itemMap.put(KEY_PRAYER_REQUEST, prayerRequest);
    itemMap.put(KEY_REQUESTER, prayerRequest.getRequester());
    itemMap.put(KEY_REQUEST_DATE, prayerRequest.getRequestDate());
    itemMap.put(KEY_REQUEST_DATE_STRING, requestDateString(this, prayerRequest.getRequestDate()));
    itemMap.put(KEY_REQUEST_SUMMARY, prayerRequest.getRequestSummary());
  }

  public static String requestDateString(Context context, Date requestDate) {
    return DateUtils.formatDateTime(context, requestDate.getTime(), DateUtils.FORMAT_SHOW_DATE);
  }


  private enum SortingType {
    BY_REQUESTER,
    BY_REQUEST_DATE_ASCENDING,
    BY_REQUEST_DATE_DESCENDING
  }
}
