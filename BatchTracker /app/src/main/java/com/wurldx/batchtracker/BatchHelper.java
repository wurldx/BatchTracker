package com.wurldx.batchtracker;

package com.analysis.batchcalendar;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BatchHelper {

    private static final Map<Integer, String> BATCH_MAP = new HashMap<>();
    private static final String[] MONTH_NAMES = {
        "January","February","March","April","May","June",
        "July","August","September","October","November","December"
    };

    static {
        BATCH_MAP.put(1, "A");  BATCH_MAP.put(2, "B");  BATCH_MAP.put(3, "C");
        BATCH_MAP.put(4, "D");  BATCH_MAP.put(5, "E");  BATCH_MAP.put(6, "F");
        BATCH_MAP.put(7, "G");  BATCH_MAP.put(8, "H");  BATCH_MAP.put(9, "I");
        BATCH_MAP.put(10, "J"); BATCH_MAP.put(11, "K"); BATCH_MAP.put(12, "L");
        BATCH_MAP.put(13, "M"); BATCH_MAP.put(14, "N"); BATCH_MAP.put(15, "O");
        BATCH_MAP.put(16, "P"); BATCH_MAP.put(17, "R"); BATCH_MAP.put(18, "S");
        BATCH_MAP.put(19, "T"); BATCH_MAP.put(20, "U"); BATCH_MAP.put(21, "X");
        BATCH_MAP.put(22, "Y"); BATCH_MAP.put(23, "Z"); BATCH_MAP.put(24, "2");
        BATCH_MAP.put(25, "3"); BATCH_MAP.put(26, "4"); BATCH_MAP.put(27, "5");
        BATCH_MAP.put(28, "6"); BATCH_MAP.put(29, "7"); BATCH_MAP.put(30, "8");
        BATCH_MAP.put(31, "9");
    }

    public static String getBatch(int day) {
        return BATCH_MAP.getOrDefault(day, "?");
    }

    public static String getBatchForDate(Calendar cal) {
        return getBatch(cal.get(Calendar.DAY_OF_MONTH));
    }

    public static String formatDateLong(Calendar cal) {
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return getOrdinal(day) + " of " + MONTH_NAMES[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR);
    }

    public static String formatDateShort(Calendar cal) {
        return cal.get(Calendar.DAY_OF_MONTH) + " " +
               MONTH_NAMES[cal.get(Calendar.MONTH)].substring(0, 3) + " " +
               cal.get(Calendar.YEAR);
    }

    public static String getMonthYear(int month, int year) {
        return MONTH_NAMES[month] + " " + year;
    }

    public static String getOrdinal(int i) {
        int j = i % 10, k = i % 100;
        if (j == 1 && k != 11) return i + "st";
        if (j == 2 && k != 12) return i + "nd";
        if (j == 3 && k != 13) return i + "rd";
        return i + "th";
    }

    public static Calendar getPastDate(Calendar base, int monthsBack) {
        Calendar d = (Calendar) base.clone();
        int targetDay = d.get(Calendar.DAY_OF_MONTH);
        d.set(Calendar.DAY_OF_MONTH, 1);
        d.add(Calendar.MONTH, -monthsBack);
        int maxDay = d.getActualMaximum(Calendar.DAY_OF_MONTH);
        d.set(Calendar.DAY_OF_MONTH, Math.min(targetDay, maxDay));
        return d;
    }

    public static int getWeekNumber(Calendar cal) {
        Calendar first = (Calendar) cal.clone();
        first.set(Calendar.MONTH, Calendar.JANUARY);
        first.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        int firstDayOfWeek = first.get(Calendar.DAY_OF_WEEK) - 1;
        return (int) Math.ceil((dayOfYear + firstDayOfWeek) / 7.0);
    }

    public static Calendar getStartOfWeek(Calendar cal) {
        Calendar start = (Calendar) cal.clone();
        int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
        start.add(Calendar.DAY_OF_MONTH, -(dayOfWeek - 1));
        return start;
    }

    public static boolean isToday(Calendar cal) {
        Calendar today = Calendar.getInstance();
        return cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
               cal.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
               cal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
    }
}