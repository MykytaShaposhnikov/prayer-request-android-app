package ua.prayerrequests;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class PrayerRequest implements Serializable {
  private final UUID id;
  private String requester;
  private Date requestDate;
  private String requestSummary;
  private String requestDetails;

  public PrayerRequest() {
    id = UUID.randomUUID();
    requestDate = new Date();
  }

  public PrayerRequest(String requester, Date requestDate, String requestSummary,
                       String requestDetails) {
    id = UUID.randomUUID();
    this.requester = requester;
    this.requestDate = requestDate;
    this.requestSummary = requestSummary;
    this.requestDetails = requestDetails;
  }

  public PrayerRequest(PrayerRequest anotherRequest) {
    id = anotherRequest.id;
    requester = anotherRequest.requester;
    requestDate = anotherRequest.requestDate;
    requestSummary = anotherRequest.requestSummary;
    requestDetails = anotherRequest.requestDetails;
  }

  public UUID getId() {
    return id;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PrayerRequest)) return false;
    PrayerRequest request = (PrayerRequest) o;
    if (!requester.equals(request.requester)) return false;
    if (!requestDate.equals(request.requestDate)) return false;
    if (!requestSummary.equals(request.requestSummary)) return false;
    return requestDetails.equals(request.requestDetails);
  }

  @Override
  public int hashCode() {
    int result = requester.hashCode();
    result = 31 * result + requestDate.hashCode();
    result = 31 * result + requestSummary.hashCode();
    result = 31 * result + requestDetails.hashCode();
    return result;
  }
}
