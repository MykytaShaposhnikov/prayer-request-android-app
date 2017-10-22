package com.example.android.prosbi;

import java.io.Serializable;
import java.util.Date;

public class PrayerRequest implements Serializable {
  private String requester;
  private Date requestDate;
  private String requestSummary;
  private String requestDetails;

  public PrayerRequest()
  {
    requestDate = new Date();
  }

  public PrayerRequest(String requester, Date requestDate, String requestSummary,
                       String requestDetails) {
    this.requester = requester;
    this.requestDate = requestDate;
    this.requestSummary = requestSummary;
    this.requestDetails = requestDetails;
  }

  public String getRequester() {
    return requester;
  }

  public void setRequester(String requester) {
    this.requester = requester;
  }

  public Date getRequestDate() {
    return requestDate;
  }

  public void setRequestDate(Date requestDate) {
    this.requestDate = requestDate;
  }

  public String getRequestSummary() {
    return requestSummary;
  }

  public void setRequestSummary(String requestSummary) {
    this.requestSummary = requestSummary;
  }

  public String getRequestDetails() {
    return requestDetails;
  }

  public void setRequestDetails(String requestDetails) {
    this.requestDetails = requestDetails;
  }
}
