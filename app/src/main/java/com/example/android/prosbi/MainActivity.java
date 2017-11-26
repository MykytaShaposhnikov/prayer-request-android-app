package com.example.android.prosbi;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
  public static final String KEY_PRAYER_REQUEST = "prayer_request";
  public static final String KEY_REQUESTER = "requester";
  public static final String KEY_REQUEST_DATE_STRING = "date_string";
  public static final String KEY_REQUEST_DATE = "date";
  public static final String KEY_REQUEST_SUMMARY = "summary";
  private static final int ACTIVITY_PRAYER_REQUEST = 1;
  private static final String KEY_REQUEST_DETAILS = "details";
  private List<PrayerRequest> requests;
  private SimpleAdapter listAdapter;
  private SortingType sortingType = SortingType.BY_REQUESTER;
  private Settings settings;

  public static String requestDateString(Context context, Date requestDate) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault());
    return format.format(requestDate);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    settings = new Settings(this);
    loadPrayerRequestData();
    createListView();
    findViewById(R.id.button_new_request).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            startPrayerRequestActivity(new PrayerRequest("", new Date(), "", ""));
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
                (RadioButton) dialogContent.findViewById(R.id.radio_button_sorting_by_requester);
            break;
          case BY_REQUEST_DATE_ASCENDING:
            selectedRadioButton =
                (RadioButton) dialogContent.findViewById(R.id.radio_button_sorting_by_date_ascending);
            break;
          case BY_REQUEST_DATE_DESCENDING:
            selectedRadioButton =
                (RadioButton) dialogContent.findViewById(R.id.radio_button_sorting_by_date_descending);
            break;
        }
        selectedRadioButton.setChecked(true);
        AlertDialog dialog =
            new AlertDialog.Builder(this)
                .setTitle(R.string.title_sort_by)
                .setIcon(android.R.drawable.ic_menu_sort_by_size)
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
    switch (item.getItemId()) {
      case R.id.item_prayer_mode:
        startPrayerModeActivity(new PrayerRequest());
        break;
    }
    return true;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case ACTIVITY_PRAYER_REQUEST:
          PrayerRequest updatedPrayerRequest =
              new Gson().fromJson(data.getStringExtra(KEY_PRAYER_REQUEST),
                  PrayerRequest.class);
          PrayerRequest samePrayerRequestInList = null;
          for (final PrayerRequest prayerRequest : requests) {
            if (updatedPrayerRequest.getId().equals(prayerRequest.getId())) {
              samePrayerRequestInList = prayerRequest;
              break;
            }
          }
          if (samePrayerRequestInList == null) {
            requests.add(updatedPrayerRequest);
          } else {
            requests.remove(samePrayerRequestInList);
            requests.add(updatedPrayerRequest);
          }
          sortPrayerRequests();
          settings.savePrayerRequests(requests);
          createListView();
          break;
      }
    }
  }

  private void createListView() {
    final ListView listView = (ListView) findViewById(R.id.list_view_requests);
    listAdapter = new SimpleAdapter(
        this,
        toDataMap(requests),
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
            startPrayerRequestActivity(requests.get(position));
          }
        }
    );
    listView.setOnItemLongClickListener(
        new AdapterView.OnItemLongClickListener() {
          public boolean onItemLongClick(
              AdapterView<?> adapterView, View view, int position, long itemId) {
            confirmAndDeleteItem(position);
            return true;
          }
        }
    );
  }

  private List<Map<String, Object>> toDataMap(List<PrayerRequest> requests) {
    List<Map<String, Object>> resultMapList = new ArrayList<>();
    for (PrayerRequest prayerRequest :
        requests) {
      Map<String, Object> itemMap = new HashMap<>();
      itemMap.put(KEY_REQUESTER, prayerRequest.getRequester());
      itemMap.put(KEY_REQUEST_DATE_STRING, requestDateString(this, prayerRequest.getRequestDate()));
      itemMap.put(KEY_REQUEST_SUMMARY, prayerRequest.getRequestSummary());
      itemMap.put(KEY_REQUEST_DETAILS, prayerRequest.getRequestDetails());
      resultMapList.add(itemMap);
    }
    return resultMapList;
  }

  private void confirmAndDeleteItem(final int position) {
    new AlertDialog.Builder(MainActivity.this)
        .setMessage(getString(R.string.message_alert, listAdapter.getCount(),
            requests.get(position).getRequestSummary()))
        .setNegativeButton(getString(R.string.button_no), null)
        .setPositiveButton(
            getString(R.string.button_yes),
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int arg1) {
                settings.removePrayerRequest(position);
                loadPrayerRequestData();
                createListView();
              }
            })
        .show();
  }

  private void startPrayerRequestActivity(PrayerRequest prayerRequest) {
    String serialaized = new Gson().toJson(prayerRequest);
    Intent intent = new Intent(this, PrayerRequestActivity.class);
    intent.putExtra(KEY_PRAYER_REQUEST, serialaized);
    startActivityForResult(intent, ACTIVITY_PRAYER_REQUEST);
  }

  private void startPrayerModeActivity(PrayerRequest prayerRequest) {
    Intent intent = new Intent(MainActivity.this, PrayerModeActivity.class);
    intent.putExtra(KEY_REQUESTER, prayerRequest.getRequester());
    intent.putExtra(KEY_REQUEST_SUMMARY, prayerRequest.getRequestSummary());
    intent.putExtra(KEY_PRAYER_REQUEST, prayerRequest);
    startActivity(intent);
  }

  private void loadPrayerRequestData() {
    requests = settings.loadPrayerRequests();
    sortPrayerRequests();
  }

  private void sortPrayerRequests() {
    Comparator<PrayerRequest> comparator = null;

    switch (sortingType) {
      case BY_REQUESTER:
        comparator = new Comparator<PrayerRequest>() {
          @Override
          public int compare(PrayerRequest left, PrayerRequest right) {
            return left.getRequester().compareToIgnoreCase(
                right.getRequester());
          }
        };
        break;

      case BY_REQUEST_DATE_ASCENDING:
        comparator = new Comparator<PrayerRequest>() {
          @Override
          public int compare(PrayerRequest left, PrayerRequest right) {
            return left.getRequestDate().compareTo(right.getRequestDate());
          }
        };
        break;

      case BY_REQUEST_DATE_DESCENDING:
        comparator = new Comparator<PrayerRequest>() {
          @Override
          public int compare(PrayerRequest left, PrayerRequest right) {
            return -left.getRequestDate().compareTo(right.getRequestDate());
          }
        };
        break;

    }

    Collections.sort(requests, comparator);
    createListView();
  }


  private enum SortingType {
    BY_REQUESTER,
    BY_REQUEST_DATE_ASCENDING,
    BY_REQUEST_DATE_DESCENDING
  }
}
