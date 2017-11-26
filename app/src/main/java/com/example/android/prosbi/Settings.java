package com.example.android.prosbi;


import android.content.Context;
import android.content.SharedPreferences;
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

public class Settings {
  private static final String KEY_SERIALIZED_REQUEST_LIST = "request_list";
  private static final String FILE_NAME_PRAYER_REQUESTS = "prayer_requests.json";
  private Context context;
  private SharedPreferences sharedPreferences;
  private Gson gson;

  public Settings(Context context) {
    this.context = context;
    sharedPreferences = context.getSharedPreferences("default", Context.MODE_PRIVATE);
    gson = new Gson();
  }

  private void setBoolean(String key, boolean value) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean(key, value);
    editor.apply();
  }

  private void setString(String key, String value) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(key, value);
    editor.apply();
  }

  public void setInt(String key, int value) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt(key, value);
    editor.apply();
  }

  //  public void clearList(){
//    savePrayerRequestList(new ArrayList<PrayerRequest>());
//  }
  private File getPrayerRequestsFile() {
    return new File(context.getExternalFilesDir(null), FILE_NAME_PRAYER_REQUESTS);
  }

  public void savePrayerRequestList(List<PrayerRequest> prayerRequests) {
    String serializedList = gson.toJson(prayerRequests);
    try {
      FileWriter fileWriter = new FileWriter(getPrayerRequestsFile(), false);
      fileWriter.append(serializedList);
      fileWriter.close();
    } catch (IOException exception) {
      Log.e("Settings", "Failed to save prayer requests to file", exception);
    }
  }

  public List<PrayerRequest> loadPrayerRequestList() {
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

  public void addPrayerRequest(PrayerRequest toSave) {
    List<PrayerRequest> list = loadPrayerRequestList();
    list.add(toSave);
    savePrayerRequestList(list);
  }

  public void removePrayerRequest(int position) {
    List<PrayerRequest> list = loadPrayerRequestList();
    list.remove(position);
    savePrayerRequestList(list);
  }

}
