package com.example.android.prosbi;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
  private ArrayAdapter<String> adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    createListView();
  }

  public void createListView() {
    // получаем экземпляр элемента ListView
    final ListView listView = (ListView) findViewById(R.id.listView);
    // Создаём пустой массив для хранения имен
    final ArrayList<String> names = new ArrayList<>();
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

  public void confirmAndDeleteItem(final String requestToDelete) {
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

  public void Edit(View view) {
    final EditText editText = (EditText) findViewById(R.id.editText);
    editText.setVisibility(View.VISIBLE);
  }
}
