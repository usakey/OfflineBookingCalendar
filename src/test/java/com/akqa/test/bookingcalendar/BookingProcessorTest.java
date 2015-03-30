/*
 * An unit-test for TDD.
 * Since input, output and constraints are all well defined,
 * TDD could be a good choice for this development.
 *
 * As requested,
 * INPUT:
 *  0900 1730
    2011-03-17 10:17:06 EMP001
    2011-03-21 09:00 2
    2011-03-16 12:34:56 EMP002
    2011-03-21 09:00 2
    2011-03-16 09:28:23 EMP003
    2011-03-22 14:00 2
    2011-03-17 11:23:45 EMP004
    2011-03-22 16:00 1
    2011-03-15 17:29:12 EMP005
    2011-03-21 16:00 3
 *
 * OUTPUT:
 *  2011-03-21
    09:00 11:00 EMP002
    2011-03-22
    14:00 16:00 EMP003
    16:00 17:00 EMP004
 *
 * CONSTRAINTS:
 *  • No part of a meeting may fall outside office hours.
    • Meetings may not overlap.
    • The booking submission system only allows one submission at a time, so submission times are guaranteed to be unique.
    • Bookings must be processed in the chronological order in which they were submitted.
    • The ordering of booking submissions in the supplied input is not guaranteed.
 *
 * @author xixu
 */

package com.akqa.test.bookingcalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.akqa.test.bookingcalendar.impl.BookingProcessorImpl;

public class BookingProcessorTest {
  /*
   * encapsulate data for unit-test
   */
  private class DataSetForTest {
    static final String INPUT_FIRST_LINE = "0900 1730\n";
    static final String FIRST_BOOKING    = "2011-03-17 10:17:06 EMP001\n" +
                                           "2011-03-21 09:00 2\n";
    static final String SECOND_BOOKING   = "2011-03-16 12:34:56 EMP002\n" +
                                           "2011-03-21 09:00 2\n";
    static final String THIRD_BOOKING    = "2011-03-16 09:28:23 EMP003\n" +
                                           "2011-03-22 14:00 2\n";
    static final String FOURTH_BOOKING   = "2011-03-17 11:23:45 EMP004\n" +
                                           "2011-03-22 16:00 1\n";
    static final String FIFTH_BOOKING    = "2011-03-15 17:29:12 EMP005\n" +
                                           "2011-03-21 16:00 3\n";

    static final String INPUT_BOOKING_LINES = FIRST_BOOKING +
                                              SECOND_BOOKING +
                                              THIRD_BOOKING +
                                              FOURTH_BOOKING +
                                              FIFTH_BOOKING;

    static final String ACTUAL_INPUT = INPUT_FIRST_LINE +
                                       INPUT_BOOKING_LINES;

    static final String EXPECTED_OUTPUT = "2011-03-21\n" +
                                          "09:00 11:00 EMP002\n" +
                                          "2011-03-22\n" +
                                          "14:00 16:00 EMP003\n" +
                                          "16:00 17:00 EMP004\n" +
                                          "";
  }

  BookingProcessorImpl bookingProcessor =  new BookingProcessorImpl();

  @Test(expected = IllegalArgumentException.class)
  public void nullInputDataShouldEndWithException() {
      // when
      String calendar = bookingProcessor.generateCalendar(null);

      // then exception is thrown
  }

  @Test(expected = IllegalArgumentException.class)
  public void emptyInputDataShouldEndWithException() {
      // when
      String calendar = bookingProcessor.generateCalendar("");

      // then exception is thrown
  }

  @Test(expected = IllegalArgumentException.class)
  public void notCorrectlyFormattedInputDataShouldEndWithException() {
      // when
      String firstMeeting = "2011-03-16 09:28:3 EMP003\n" + // length less than required
                            "2011-03-21 9:30 1\n";          // length less than required

      String calendar = bookingProcessor.generateCalendar(DataSetForTest.INPUT_FIRST_LINE + firstMeeting);

      // then exception is thrown
  }

  @Test
  public void shouldGenerateBookingCalendarCorrectlyWithGivenInput() {
      // when
      String calendar = bookingProcessor.generateCalendar(DataSetForTest.ACTUAL_INPUT);

      // then
      assertEquals(DataSetForTest.EXPECTED_OUTPUT, calendar);
  }

  @Test
  public void noPartOfMeetingMayFallOutsideOfficeHours() {
      // given
      String tooEarlyBooking = "2011-03-16 12:34:56 EMP002\n" +
                               "2011-03-21 08:00 2\n";

      String tooLateBooking = "2011-03-16 09:28:23 EMP003\n" +
                              "2011-03-22 18:00 2\n";

      // when
      String calendar = bookingProcessor.generateCalendar(DataSetForTest.INPUT_FIRST_LINE + tooEarlyBooking + tooLateBooking);

      //then
      assertTrue(calendar.isEmpty());
  }

  @Test
  public void meetingsMayNotOverlap() {
      // given
      String firstMeeting = "2011-03-16 09:28:23 EMP003\n" +
                            "2011-03-21 09:30 1\n";

      String secondMeeting = "2011-03-16 09:28:23 EMP003\n" +
                             "2011-03-21 09:00 2\n";

      // when
      String calendar = bookingProcessor.generateCalendar(DataSetForTest.INPUT_FIRST_LINE + firstMeeting + secondMeeting);

      //then
      assertEquals("2011-03-21\n" +
                   "09:00 11:00 EMP003\n", calendar);
  }

  @Test
  public void bookingsMustBeProcessedInSubmitOrder() {
      // given
      String firstMeeting = "2011-03-17 12:34:56 EMP002\n" +
                            "2011-03-21 16:00 1\n";

      String secondMeeting = "2011-03-16 09:28:23 EMP003\n" +
                             "2011-03-21 15:00 2\n";

      // when
      String calendar = bookingProcessor.generateCalendar(DataSetForTest.INPUT_FIRST_LINE + firstMeeting + secondMeeting);

      // then
      assertEquals("2011-03-21\n15:00 17:00 EMP003\n", calendar);
  }

  @Test
  public void orderingOfBookingSubmissionShouldNotHaveEffects() {
      // given
      // shuffle all bookings
      List<String> shuffledBookings = new ArrayList<String>();
      shuffledBookings.add(DataSetForTest.FIRST_BOOKING);
      shuffledBookings.add(DataSetForTest.SECOND_BOOKING);
      shuffledBookings.add(DataSetForTest.THIRD_BOOKING);
      shuffledBookings.add(DataSetForTest.FOURTH_BOOKING);
      shuffledBookings.add(DataSetForTest.FIFTH_BOOKING);
      Collections.shuffle(shuffledBookings);

      StringBuilder inputBookingLines = new StringBuilder();
      for(String list : shuffledBookings)
        inputBookingLines.append(list);

      // when
      String calendar = bookingProcessor.generateCalendar(DataSetForTest.INPUT_FIRST_LINE + inputBookingLines);

      // then
      assertEquals(DataSetForTest.EXPECTED_OUTPUT, calendar);
  }
}
