package fr.lastril.uhchost.tools.API;

import java.util.Calendar;
import java.util.Date;

public class DateManager {

	public static Date addTime(int cal, int amount) {
		return addTime(new Date(), cal, amount);
	}

	public static Date addTime(Date date, int cal, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(cal, amount);
		date = calendar.getTime();
		return date;
	}

	public static Date removeTime(int cal, int amount) {
		return removeTime(new Date(), cal, amount);
	}

	public static Date removeTime(Date date, int cal, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(cal, -amount);
		date = calendar.getTime();
		return date;
	}

	public static long getSecondsBetweenTwoDate(Date one, Date second) {
		return (Math.max(one.getTime(), second.getTime()) - Math.min(one.getTime(), second.getTime())) / 1000;
	}

}
