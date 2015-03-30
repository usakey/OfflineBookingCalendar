/*
 * This class provides a method of formatting output valid meetings
 *
 * @author xixu
 */
package com.akqa.test.bookingcalendar.impl;

import hirondelle.date4j.DateTime;

import java.util.Collection;
import static com.akqa.test.bookingcalendar.Constants.*;

public class BookingWriter {
  public String bookingWriter(Booking booking) {
    StringBuilder sb = new StringBuilder();
    // get collection of valid meetings in order
    Collection<Booking.Meeting> validMeetings = booking.getValidMeeting();
    // since for valid meetings of the same day, only one date is printed
    // here we need to decide whether to add the date info at the beginning
    // by checking whether the output day is already in the string.
    for(Booking.Meeting meeting : validMeetings) {
      // if no previous meeting of same day
      if(!sb.toString()
              .contains(new DateTime(meeting.getRequestMeetingStartTime().format(OUTPUT_DAY_FORMAT)).toString())) {
        sb.append(new DateTime(meeting.getRequestMeetingStartTime().format(OUTPUT_DAY_FORMAT)));
        sb.append("\n");
        }
      // meeting start and end time should be always printed
      sb.append(new DateTime(meeting.getRequestMeetingStartTime().format(OUTPUT_HOUR_FORMAT)))
        .append(" ")
        .append(new DateTime(meeting.getRequestMeetingEndTime().format(OUTPUT_HOUR_FORMAT)))
        .append(" ")
        .append(meeting.getEmployeeID())
        .append("\n");
    }
    return sb.toString();
  }
}
