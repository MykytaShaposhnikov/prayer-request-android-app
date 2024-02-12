package ua.prayerrequests;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
  public static final String KEY_PRAYER_REQUEST = "prayer_request";
  public static final String KEY_REQUESTER = "requester";
  public static final String KEY_REQUEST_DATE_STRING = "date_string";
  public static final String KEY_REQUEST_SUMMARY = "summary";
  private static final int ACTIVITY_PRAYER_REQUEST = 1;
  private static final String KEY_REQUEST_DETAILS = "details";
  private List<PrayerRequest> requests;
  private CustomRecycleAdapter listAdapter;
  private SortingType sortingType = SortingType.BY_REQUESTER;
  private Settings settings;

  public static String requestDateString(Date requestDate) {
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
    findViewById(R.id.button_add_request).setOnClickListener(
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
                .setPositiveButton(R.string.button_sort, new DialogInterface.OnClickListener() {
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
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
    final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_requests);
    recyclerView.setLayoutManager(mLayoutManager);
    listAdapter = new CustomRecycleAdapter(requests, this,
        new CustomRecycleAdapter.OnRecyclerItemClickListener() {
          @Override
          public void onItemClick(int position) {
            startPrayerRequestActivity(requests.get(position));
          }

          @Override
          public void onItemLongClick(int position) {

          }
        });
    recyclerView.setAdapter(listAdapter);

    SwipeDismissRecyclerViewTouchListener listener =
        new SwipeDismissRecyclerViewTouchListener.Builder(recyclerView,
            new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
              @Override
              public boolean canDismiss(int position) {
                return true;
              }

              @Override
              public void onDismiss(View view, final int position) {
                settings.removePrayerRequest(position);
                final PrayerRequest deletedRequest;
                deletedRequest = requests.get(position);
                loadPrayerRequestData();
                createListView();
                Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.coordinator_layout), "Moved to completed", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                        requests.add(position, deletedRequest);
                        settings.savePrayerRequests(requests);
                        listAdapter.notifyDataSetChanged();
                        Snackbar snackbar1 = Snackbar.make(findViewById(R.id.coordinator_layout), "Request restored!", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                      }
                    });

                snackbar.show();
              }
            }).setIsVertical(false).setItemClickCallback(
            new SwipeDismissRecyclerViewTouchListener.OnItemClickCallBack() {
              @Override
              public void onClick(int position) {
                startPrayerRequestActivity(requests.get(position));
              }
            }
        ).create();

    recyclerView.setOnTouchListener(listener);
  }

//  private void onCheckBoxClick() {
//CheckBox checkBoxStar;
//    checkBoxStar= (CheckBox) findViewById(R.id.checkBoxStar);
//     final PrayerRequest checkedRequest;
//     final PrayerRequest notCheckedRequest;
//    checkBoxStar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//      @Override
//      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if(isChecked)
//         checkedRequest= requests.get(position);
//        else
//          notCheckedRequest=requests.get(position);
//        }
//      });
//    }

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
    BY_REQUEST_DATE_DESCENDING,
  }
}
