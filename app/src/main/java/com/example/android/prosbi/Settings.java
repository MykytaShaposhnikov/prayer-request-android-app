package com.example.android.prosbi;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

class Settings {
  private static final String FILE_NAME_PRAYER_REQUESTS = "prayer_requests.json";
  private static final String FILE_NAME_DELETED_PRAYER_REQUESTS = "deleted_prayer_requests.json";
  private Context context;
  private Gson gson;
  private boolean isDeleted=false;

  Settings(Context context) {
    this.context = context;
    gson = new Gson();
  }

  private File getPrayerRequestsFile() {
    return new File(context.getExternalFilesDir(null), FILE_NAME_PRAYER_REQUESTS);
  }

  private File getDeletedPrayerRequestsFile() {
    return new File(context.getExternalFilesDir(null), FILE_NAME_DELETED_PRAYER_REQUESTS);
  }

  void savePrayerRequests(List<PrayerRequest> prayerRequests) {
    String serializedList = gson.toJson(prayerRequests);
    try {
      FileWriter fileWriter = new FileWriter(getPrayerRequestsFile(), false);
      fileWriter.append(serializedList);
      fileWriter.close();
    } catch (IOException exception) {
      Log.e("Settings", "Failed to save prayer requests to file", exception);
    }
  }

  List<PrayerRequest> loadPrayerRequests() {
    List<PrayerRequest> prayerRequests = null;
    try {
      String serializedList = "";
      Reader reader = new FileReader(getPrayerRequestsFile());
      StringBuilder sb = new StringBuilder();
      char[] buffer = new char[4096];
      int len;
      while ((len = reader.read(buffer)) > 0) {
        sb.append(buffer, 0, len);
      }
      reader.close();
      serializedList = sb.toString();
      prayerRequests =
          gson.fromJson(
              serializedList,
              new TypeToken<List<PrayerRequest>>() {
              }.getType());
    } catch (Exception exception) {
      Log.e("Settings", "Failed to load prayer requests from file", exception);
    }
    if (prayerRequests == null) {
      prayerRequests = new ArrayList<>();
    }
    return prayerRequests;
  }

  void removePrayerRequest(int position) {
    List<PrayerRequest> list = loadPrayerRequests();
    list.remove(position);
    savePrayerRequests(list);
  }

  void saveDeletedPrayerRequests(PrayerRequest deletedPrayerRequests) {
    String serializedList = gson.toJson(deletedPrayerRequests);
    try {
      FileWriter fileWriter = new FileWriter(getDeletedPrayerRequestsFile(), false);
      fileWriter.append(serializedList);
      fileWriter.close();
    } catch (IOException exception) {
      Log.e("Settings", "Failed to save prayer requests to file", exception);
    }
  }

}
