package com.example.android.prosbi;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Settings {
  private static final String KEY_SERIALIZED_REQUEST_LIST = "request_list";
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
//    saveRequestList(new ArrayList<PrayerRequest>());
//  }

  public void saveRequestList(List<PrayerRequest> list) {
    String s = gson.toJson(list);
        setString(KEY_SERIALIZED_REQUEST_LIST,s);
  }

  public List<PrayerRequest> getRequestList()
  {
    Type listType = new TypeToken<List<PrayerRequest>>(){}.getType();
    return gson.fromJson(sharedPreferences.getString(KEY_SERIALIZED_REQUEST_LIST, ""),
        listType);
  }

  public void addPrayerRequest(PrayerRequest toSave)
  {
    List<PrayerRequest> list=getRequestList();
    list.add(toSave);
    saveRequestList(list);
  }

  public void removePrayerRequest(int position)
  {
    List<PrayerRequest> list=getRequestList();
    list.remove(position);
    saveRequestList(list);
  }

}
