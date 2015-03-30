/*
 * To abstract booking data and meeting data as two nested class
 * Core meeting constraints:
 *   1. No part of a meeting may fall outside office hours.
 *   2. Meetings may not overlap.
 *   3. Bookings must be processed in the chronological order in which they were submitted.
 * are all satisfied here.
 *
 * @author xixu
 */

package com.akqa.test.bookingcalendar.impl;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import hirondelle.date4j.DateTime;

public class Booking {

  private OfficeHour officeHour;
  private Map<DateTime, Meeting> validMeetingRequests = new TreeMap<DateTime, Meeting>();
  /*
   * Encapsulate OfficeHour as nested class
   */
  public static final class OfficeHour {
    private DateTime officeOpenTime;
    private DateTime officeCloseTime;

    public OfficeHour (DateTime officeOpenTime, DateTime officeCloseTime) {
      this.officeOpenTime = officeOpenTime;
      this.officeCloseTime = officeCloseTime;
    }

    public DateTime getOfficeOpenTime() {
      return this.officeOpenTime;
    }

    public DateTime getOfficeCloseTime() {
      return this.officeCloseTime;
    }
  }

  /*
   * Encapsulate Meeting request as nested class
   */
  public static final class Meeting {
    private String employeeID;
    private DateTime requestDate;
    private DateTime requestMeetingStartTime;
    private DateTime requestMeetingEndTime;

    public Meeting(String empID, DateTime reqDate, DateTime reqMeetingStartTime, DateTime reqMeetingEndTime) {
      this.employeeID = empID;
      this.requestDate = reqDate;
      this.requestMeetingStartTime = reqMeetingStartTime;
      this.requestMeetingEndTime = reqMeetingEndTime;
    }

    public String getEmployeeID() {
      return employeeID;
    }

    public DateTime getRequestDate() {
      return requestDate;
    }

    public DateTime getRequestMeetingStartTime() {
      return requestMeetingStartTime;
    }

    public DateTime getRequestMeetingEndTime() {
      return requestMeetingEndTime;
    }

    public boolean isOverlapWith(Meeting that) {
      if ((this.requestMeetingStartTime.compareTo(that.requestMeetingStartTime) <= 0 &&
         this.requestMeetingEndTime.compareTo(that.requestMeetingStartTime) > 0)||
         (that.requestMeetingStartTime.compareTo(this.requestMeetingStartTime) <= 0 &&
         that.requestMeetingEndTime.compareTo(this.requestMeetingStartTime) > 0))
        return true;
      return false;
    }
  }

  /*
   *  constructor to set general office hour
   *  @param $officeHour
   */
  public Booking(OfficeHour officeHour) {
    this.officeHour = officeHour;
  }

  /*
   * To test if submitted request is valid regarding office hour
   * if yes, add it to meeting record by ordered TreeMap
   *
   * @param $Booking.Meeting requested meeting time
   */
  public void addMeetingRecord(Meeting meeting) {
    if (!isBookingOutOfOfficeHour(meeting))
      validMeetingRequests.put(meeting.getRequestDate(), meeting);
    else return;
  }
  // test if meeting time out of office hour
  private boolean isBookingOutOfOfficeHour (Meeting meeting) {
    if ((meeting.getRequestMeetingStartTime().getHour() * 60 + meeting.getRequestMeetingStartTime().getMinute()) <
       (this.officeHour.getOfficeOpenTime().getHour() * 60 + this.officeHour.getOfficeOpenTime().getMinute()) ||
       (meeting.getRequestMeetingEndTime().getHour() * 60 + meeting.getRequestMeetingEndTime().getMinute()) >
       (this.officeHour.getOfficeCloseTime().getHour() * 60 + this.officeHour.getOfficeCloseTime().getMinute()))
      return true;
    return false;
  }

  /*
   * With all valid meeting requests,
   * which means all meetings requested are within office hour.
   * Second constraint of meeting overlap should be satisfied.
   *
   * @return valid meetings in constraint of office hour and overlaps
   *         meetings are guaranteed in chronological order of meeting start time
   */
  public Collection<Meeting> getValidMeeting() {
    Map<DateTime, Meeting> validMeeting = new TreeMap<DateTime, Meeting>();

    for (Meeting meeting : validMeetingRequests.values()) {
      if (!isOverlap(meeting, validMeeting.values()))
        validMeeting.put(meeting.getRequestMeetingStartTime(), meeting);
    }
    return validMeeting.values();
  }

  /*
   *  test if one meeting is overlapping with each one in the meeting collection
   */
  private boolean isOverlap(Meeting that, Collection<Meeting> values) {
    for (Meeting meeting : values) {
      if (meeting.isOverlapWith(that))
        return true;
    }
    return false;
  }
}
