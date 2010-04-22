package net.anzix.names;

import java.util.Calendar;

/**
 *
 * @author elek
 */
public class DayOfYear {

    private int yearOffset = 0;

    private static int[] monthLength = new int[]{0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335};

    private int day;

    public DayOfYear(int day) {
        this.day = day;
    }

    public DayOfYear() {
        day = 1;
    }

    public int getMonth() {
        for (int i = 0; i < 13; i++) {
            if (i == 12 || monthLength[i] >= day) {
                return i;
            }
        }
        return -1;
    }

    public int getDay() {
        for (int i = 0; i < 13; i++) {
            if (i == 12 || monthLength[i] >= day) {
                return day - monthLength[i - 1];
            }
        }
        return -1;
    }

    public String getString() {
        for (int i = 0; i < 13; i++) {
            if (i == 12 || monthLength[i] >= day) {
                return (i) + "." + (day - monthLength[i - 1]);
            }
        }
        return "UNKNOWN";
    }

    public DayOfYear nextDay() {
        int nextDay = day + 1;
        if (nextDay > 366) {
            nextDay = 1;
            yearOffset++;
        }
        return new DayOfYear(nextDay);
    }

    public static DayOfYear valueOf(int month, int day) {
        return new DayOfYear(monthLength[month - 1] + day);
    }

    public static DayOfYear valueOf(Calendar date) {
        return valueOf(date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH));
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DayOfYear other = (DayOfYear) obj;
        if (this.day != other.day) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.day;
        return hash;
    }

    public int getYearOffset() {
        return yearOffset;
    }
}
