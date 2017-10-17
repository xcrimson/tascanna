package name.glonki.teamwork.util;

import java.util.Calendar;

/**
 * Created by Glonki on 15.10.2017.
 */

public class DateUtil {

    private final static long WEEK = 7 * 24 * 60 * 60 * 1000;

    public static String today() {
        return getDateString(Calendar.getInstance());
    }

    public static String oneWeekLater() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + WEEK);
        return getDateString(calendar);
    }

    public static String getDateString(Calendar calendar) {
        return getDateString(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static String getDateString(int year, int month, int day) {
        StringBuilder builder = new StringBuilder();
        builder.append(year);
        builder.append(String.format("%02d", month));
        builder.append(String.format("%02d", day));
        return builder.toString();
    }

    public static int[] getYearMonthDay(String date) {
        return new int[] {Integer.parseInt(date.substring(0,4)),
                Integer.parseInt(date.substring(4,6)) - 1, Integer.parseInt(date.substring(6))};
    }

}
