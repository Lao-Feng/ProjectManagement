package zr.zrpower.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateWork {
	private static final DateFormat SDF1 = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat SDF2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static Date StringToDate(String param) {
		Date result = null;
		if (param == null)
			return null;
		if (param.length() == 10) {
			Date date = null;
			try {
				date = SDF1.parse(param);
				result = new Date(date.getTime());
			} catch (ParseException ex) {
			}
		}
		return result;
	}

	public static Date StringToDateTime(String param) {
		Date result = null;
		if (param == null || param.length() == 0) {
			return null;
		}
		if (param.length() == 10) {
			Date date = null;
			try {
				date = SDF1.parse(param);
				result = new Date(date.getTime());
			} catch (ParseException ex) {
			}
		}
		if (param.length() == 19) {
			Date date = null;
			try {
				date = SDF2.parse(param);
				result = new Date(date.getTime());
			} catch (ParseException ex) {
			}
		}
		return result;
	}

	public static String DateToString(Date date) {
		String returnValue = "";
		if (date != null) {
			returnValue = SDF1.format(date);
		}
		return returnValue;
	}

	public static String DateTimeToString(Date date) {
		String returnValue = "";
		if (date != null) {
			returnValue = SDF2.format(date);
		}
		return returnValue;
	}

	public static Timestamp UtiltdateToSqldate(Date date) {
		Timestamp sqlDate = null;
		if (date != null) {
			sqlDate = new Timestamp(date.getTime());
			sqlDate.setTime(date.getTime());
		}
		return sqlDate;
	}

	public static long compareToday(Date date) {
		Date thisDate = new Date();
		long dateTime = 0x5265c00L;
		return (date.getTime() - thisDate.getTime()) / dateTime;
	}

	public static int compareDate(Date date1, Date date2) {
		long dateTime = 0x5265c00L;
		long dd = (date1.getTime() - date2.getTime()) / dateTime;
		return Integer.parseInt(Long.toString(dd));
	}

	public static int MonthDay(int Year, int Month) {
		int iDay = 0;
		if (Month == 12) {
			iDay = 31;
		} else {
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal1.set(1, Year);
			cal2.set(1, Year);
			cal1.set(2, Month - 1);
			cal2.set(2, Month);
			iDay = cal2.get(6) - cal1.get(6);
		}
		return iDay;
	}

	public static int MonthDay(String Year, String Month) {
		return MonthDay(Integer.parseInt(Year), Integer.parseInt(Month));
	}

	public static int MonthDay(String YearMonth) {
		String sYear = YearMonth.substring(0, YearMonth.indexOf("-"));
		String sMonth = YearMonth.substring(YearMonth.indexOf("-") + 1, YearMonth.length());
		return MonthDay(sYear, sMonth);
	}

	public static boolean isSameWeekDates(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int subYear = cal1.get(1) - cal2.get(1);
		if (0 == subYear) {
			if (cal1.get(3) == cal2.get(3)) {
				return true;
			}
		} else if (1 == subYear && 11 == cal2.get(2)) {
			if (cal1.get(3) == cal2.get(3)) {
				return true;
			}
		} else if (-1 == subYear && 11 == cal1.get(2) && cal1.get(3) == cal2.get(3)) {
			return true;
		}
		return false;
	}

	public static String[] getYearList(int StartYear) {
		Calendar cal1 = Calendar.getInstance();
		int iYearEnd = cal1.get(1);
		int iYear = (iYearEnd - StartYear) + 1;
		String saYear[] = new String[iYear];
		for (int i = 0; i < iYear; i++) {
			saYear[i] = Integer.toString(StartYear);
			StartYear++;
		}
		return saYear;
	}

	public static String getYearMonth() {
		Calendar calendar = Calendar.getInstance();
		String sYear = Integer.toString(calendar.get(1));
		String sMonth = Integer.toString(calendar.get(2) + 1);
		if (sMonth.length() == 1)
			sMonth = "0" + sMonth;
		return sYear + sMonth;
	}

	public static String getThisYear() {
		Calendar calendar = Calendar.getInstance();
		return Integer.toString(calendar.get(1));
	}

	public static String getThisMonth() {
		Calendar calendar = Calendar.getInstance();
		String sMonth = Integer.toString(calendar.get(2) + 1);
		if (sMonth.length() == 1) {
			sMonth = "0" + sMonth;
		}
		return sMonth;
	}

	public static String getThisMonthFirstDay() {
		Calendar calendar = Calendar.getInstance();
		String Year = Integer.toString(calendar.get(1));
		String Month = Integer.toString(calendar.get(2) + 1);
		if (Month.length() == 1) {
			Month = "0" + Month;
		}
		return Year + "-" + Month + "-01";
	}

}