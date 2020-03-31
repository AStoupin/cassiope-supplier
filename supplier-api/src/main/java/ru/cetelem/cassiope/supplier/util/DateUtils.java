package ru.cetelem.cassiope.supplier.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

final public class DateUtils {

	public static String PATTERN_DATE = "dd.MM.yyyy";
	public static String PATTERN_TIME = "HH:mm:ss";
	public static String PATTERN_DATE_TIME = PATTERN_DATE + " " + PATTERN_TIME;

	public static DateTimeFormatter FOMATTER_DATE_TIME = DateTimeFormatter.ofPattern(PATTERN_DATE_TIME);
	public static DateTimeFormatter FOMATTER_DATE = DateTimeFormatter.ofPattern(PATTERN_DATE);
	public static DateTimeFormatter FOMATTER_TIME = DateTimeFormatter.ofPattern(PATTERN_TIME);

	public static String asString(LocalDateTime dateTime) {
		if (dateTime == null) {
			return "";
		} else {
			return dateTime.format(FOMATTER_DATE_TIME);
		}
	}

	public static String asString(LocalDate dateTime) {
		if (dateTime == null) {
			return "";
		} else {
			return dateTime.format(FOMATTER_DATE);
		}
	}

	public static String asString(LocalTime dateTime) {
		if (dateTime == null) {
			return "";
		} else {
			return dateTime.format(FOMATTER_TIME);
		}
	}

	public static java.sql.Date asSqlDate(LocalDateTime dateTime) {
		if (dateTime == null) {
			return null;
		} else {
			return java.sql.Date.valueOf(dateTime.toLocalDate());
		}
	}

	public static Date asDate(LocalDate localDate) {
		if (localDate == null) {
			return null;
		} else {
			return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		}
	}

	public static Date asDate(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		} else {
			return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		}
	}

	public static LocalDate asLocalDate(Date date) {
		if (date == null) {
			return null;
		} else {
			return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		}
	}

	public static LocalDateTime asLocalDateTime(Date date) {
		if (date == null) {
			return null;
		} else {
			return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
		}
	}

	public static LocalDate asLocalDate(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		} else {
			return localDateTime.toLocalDate();
		}
	}

	public static LocalDateTime asLocalDateTime(LocalDate date) {
		if (date == null) {
			return null;
		} else {
//        return date.atStartOfDay();
			return date.atTime(LocalTime.now());
		}
	}

	public static int compare(LocalDateTime date1, LocalDateTime date2) {
		if (date1 == null) {
			if (date2 == null) {
				return 0;
			} else {
				return 1;
			}
		} else if (date2 == null) {
			return -1;
		} else {
			return date1.compareTo(date2);
		}
	}

	public static int compare(LocalDate date1, LocalDate date2) {
		if (date1 == null) {
			if (date2 == null) {
				return 0;
			} else {
				return 1;
			}
		} else if (date2 == null) {
			return -1;
		} else {
			return date1.compareTo(date2);
		}
	}

	public static int compare(LocalTime date1, LocalTime date2) {
		if (date1 == null) {
			if (date2 == null) {
				return 0;
			} else {
				return 1;
			}
		} else if (date2 == null) {
			return -1;
		} else {
			return date1.compareTo(date2);
		}
	}

}
