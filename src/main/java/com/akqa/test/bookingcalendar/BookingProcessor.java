/*
 * As abstract method for TDD class,
 * only one entry point is needed for meeting calendar generation.
 *
 * @see BookingProcessorTest
 * @author xixu
 */

package com.akqa.test.bookingcalendar;

public interface BookingProcessor {
  String generateCalendar(final String actualInput)
      throws IllegalArgumentException;
}
