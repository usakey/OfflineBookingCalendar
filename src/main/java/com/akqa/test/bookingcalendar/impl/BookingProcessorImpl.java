/*
 * This is the core implementation of booking request processing.
 * For each booking request:
 *   firstly parse standard info to booking type,
 *   then output valid meeting info.
 *
 * @author xixu
 *
 */

package com.akqa.test.bookingcalendar.impl;

import com.akqa.test.bookingcalendar.BookingProcessor;

public class BookingProcessorImpl
        implements BookingProcessor {

  public String generateCalendar(String input)
          throws IllegalArgumentException {
    if (input == null || input.isEmpty())
      throw new IllegalArgumentException("input is empty, please check!");

    Booking booking = new BookingReader().bookingParse(input);
    return new BookingWriter().bookingWriter(booking);
  }
}
