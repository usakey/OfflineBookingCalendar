/*
 * This constant class provides a global format as requested
 * In order to avoid constant interface anti-pattern,
 * here a final class with private constructor is used,
 * rather than a constant interface.
 *
 * @author xixu
 */

package com.akqa.test.bookingcalendar;
/*
 * To avoid Constant Interface Anti-pattern
 */
public final class Constants {
  private Constants(){}

  public static final String REQUEST_SUBMISSION_FORMAT = "YYYY-MM-DD hh:mm:ss";

  public static final String MEETING_START_TIME_FORMAT = "YYYY-MM-DD hh:mm";

  public static final String OUTPUT_DAY_FORMAT = "YYYY-MM-DD";

  public static final String OUTPUT_HOUR_FORMAT = "hh:mm";
}
