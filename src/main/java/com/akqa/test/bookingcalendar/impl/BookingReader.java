/*
 * From standard input, get booking records and
 * transform it to DataType that is understandable.
 *
 * @author xixu
 * @see Booking.java
 */

package com.akqa.test.bookingcalendar.impl;
import static java.lang.Integer.parseInt;

import java.util.Arrays;
import java.util.List;

import hirondelle.date4j.DateTime;
import static com.akqa.test.bookingcalendar.Constants.*;

public class BookingReader {
  /*
   * @param first line of input, which is String type
   *        for example: 0900 1730
   * @exception IllegalArgumentException
   *            If an input is empty or null
   * @return officeHour data type
   */
    public Booking.OfficeHour officeHourParse(final String header) {
      // check for exception
      // but be aware that null should be tested firstly
      if (header == null || header.isEmpty())
        throw new IllegalArgumentException("office hour is empty!");

      // TODO detailed format incorrect exception
      if (header.length() != 9)
        throw new IllegalArgumentException("office hour format is incorrect!");

      // read input header as office hour by $DateTime format
      String[] officeHourArray = header.split(" ");
      DateTime officeOpenTime = DateTime.forTimeOnly(parseInt(officeHourArray[0].substring(0, 2)),
                                                     parseInt(officeHourArray[0].substring(2, 4)), null, null);
      DateTime officeCloseTime = DateTime.forTimeOnly(parseInt(officeHourArray[1].substring(0, 2)),
                                                      parseInt(officeHourArray[1].substring(2, 4)), null, null);

      Booking.OfficeHour officeHour = new Booking.OfficeHour(officeOpenTime, officeCloseTime);
      return officeHour;
    }

    /*
     * @param [request submission time, in the format YYYY-MM-DD HH:MM:SS] [ARCH:employee id]
     * @param [meeting start time, in the format YYYY-MM-DD HH:MM] [ARCH:meeting duration in hours]
     *        for example: 2011-03-17 10:17:06 EMP001
     *                     2011-03-21 09:00 2
     * @exception IllegalArgumentException
     *            If an input is empty or null
     * @return Booking.Meeting type
     *
     */
    public Booking.Meeting meetingTimeParse(final String requestInfo, final String meetingInfo) {
      if (requestInfo == null || requestInfo.isEmpty())
        throw new IllegalArgumentException("request info is empty!");
      if (meetingInfo == null || meetingInfo.isEmpty())
        throw new IllegalArgumentException("meeting info is empty!");

      // TODO detailed format incorrect exception
      if (requestInfo.length() != 26)
        throw new IllegalArgumentException("request info format is incorrect!");
      if (meetingInfo.length() != 18)
        throw new IllegalArgumentException("meeting info format is incorrect!");

      String[] requestInfoArray = requestInfo.split(" ");
      String[] meetingInfoArray = meetingInfo.split(" ");

      // formatting request date
      DateTime requestDate = new DateTime(requestInfoArray[0] + " " + requestInfoArray[1]);
      requestDate = new DateTime(requestDate.format(REQUEST_SUBMISSION_FORMAT));
      String employeeID = requestInfoArray[2];

      // formatting meeting time
      DateTime startTime = new DateTime(meetingInfoArray[0] + " " + meetingInfoArray[1] + ":00");
      DateTime meetingStartTime = new DateTime(startTime.format(MEETING_START_TIME_FORMAT));
      DateTime endTime = startTime.plus((Integer)0, (Integer)0, (Integer)0,
                                        (Integer)parseInt(meetingInfoArray[2]),
                                        (Integer)0, (Integer)0, (Integer)0, null);
      DateTime meetingEndTime = new DateTime(endTime.format(MEETING_START_TIME_FORMAT));

      Booking.Meeting meetingTime = new Booking.Meeting(employeeID, requestDate, meetingStartTime, meetingEndTime);
      return meetingTime;
    }

    /*
     * Combining two parse methods above together,
     * to get full valid booking records.
     *
     * @param Input containing office hours and a list of individual requests
     *        $officeHour + $meetingTime
     * @return valid booking requests with office hour constraint
     *         no part of a meeting may fall outside office hours
     */
    public Booking bookingParse(final String input) {
      // split input as Office Hour & Meeting requests
      List<String> inputRows = Arrays.asList(input.split("\n"));
      Booking.OfficeHour officeHour = officeHourParse(inputRows.get(0));
      Booking booking = new Booking(officeHour);

      for (int i = 1; i<inputRows.size(); i += 2) {
        Booking.Meeting meetingTime = meetingTimeParse(inputRows.get(i), inputRows.get(i + 1));
        booking.addMeetingRecord(meetingTime);
      }
      return booking;
    }
}
