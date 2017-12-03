package com.example.android.prosbi;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class CustomRecycleAdapter extends RecyclerView.Adapter<CustomRecycleAdapter.ViewHolder> {
  OnRecyclerItemClickListener listener;
  private List<PrayerRequest> requests;
  private Context context;

  public CustomRecycleAdapter(List<PrayerRequest> requests, Context context, OnRecyclerItemClickListener listener) {
    this.requests = requests;
    this.context = context;
    this.listener = listener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    CardView v = (CardView) LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_prayer_request, parent, false);
    ViewHolder vh = new ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    PrayerRequest request = requests.get(position);
    holder.requester.setText(request.getRequester());
    holder.date.setText(MainActivity.requestDateString(request.getRequestDate()));
    holder.requestSummary.setText(request.getRequestSummary());
    holder.cardView.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            listener.onItemClick(position);
          }
        }
    );
  }

  @Override
  public int getItemCount() {
    return requests.size();
  }

  public interface OnRecyclerItemClickListener {
    void onItemClick(int position);

    void onItemLongClick(int position);
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    CardView cardView;
    TextView requester;
    TextView date;
    TextView requestSummary;

    public ViewHolder(CardView v) {
      super(v);
      this.cardView = v;
      requester = (TextView) v.findViewById(R.id.text_view_requester);
      date = (TextView) v.findViewById(R.id.text_view_request_date);
      requestSummary = (TextView) v.findViewById(R.id.text_view_request_summary);
    }
  }
}
