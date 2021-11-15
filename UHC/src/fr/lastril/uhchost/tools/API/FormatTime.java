package fr.lastril.uhchost.tools.API;

import java.util.Date;

public class FormatTime {
	
	private long sec, min, hour, day, month;
	
	public FormatTime(long seconds) {
		for(long i = 0; i < seconds; i++) {
			sec++;
			if(sec == 60) {
				sec = 0;
				min++;
			}
			if(min == 60) {
				hour++;
				min = 0;
			}
			if(hour == 24) {
				day++;
				hour = 0;
			}
			if(day == 30) {
				month++;
				day = 0;
			}
		}
	}
	
	public FormatTime(Date date) {
		this(new Date(), date);
	}
	
	public FormatTime(Date date, Date date2) {
		if(date == null || date2 == null) {
			sec = 0;
			min = 0;
			hour = 0;
			day = 0;
			return;
		}
		for(long i = 0; i < DateManager.getSecondsBetweenTwoDate(date, date2); i++) {
			sec++;
			if(sec == 60) {
				sec = 0;
				min++;
			}
			if(min == 60) {
				hour++;
				min = 0;
			}
			if(hour == 24) {
				day++;
				hour = 0;
			}
			if(day == 30) {
				month++;
				day = 0;
			}
		}
		
	}
	
	public FormatTime(long min, long sec) {
		this.sec = sec;
		this.min = min;
	}
	
	public String toString() {
		String output = "";
		
		if(month >= 1) { output += format(month) + ":"; }
		if(day >= 1) { output += format(day) + ":"; }
		if(hour >= 1) { output += format(hour) + ":"; }

		output += format(min) + ":";
		output += format(sec);

		return output;
	}
	
	public String toMDString() {
		String output = "";
		
		if(month >= 1) { output += format(month) + " Mois "; }
		if(day >= 1) { output += format(day) + " Jour(s) "; }

		if(min == 0 && sec == 0 && day == 0 && month == 0) {
			return "Jamais";
		}
		
		return output;
	}
	
	public String toMSString() {
		String output = "";
		
		if(min >= 1) { output += format(day) + " Minute(s) "; }
		if(sec >= 1) { output += format(hour) + "Seconde(s) "; }

		if(min == 0 && sec == 0 && day == 0 && month == 0) {
			return "Jamais";
		}
		
		return output;	
	}
	
	public String toMDHString() {
		String output = "";
		
		if(month >= 1) { output += format(month) + " Mois "; }
		if(day >= 1) { output += format(day) + " Jour(s) "; }
		if(hour >= 1) { output += format(hour) + "Heure(s) "; }

		if(min == 0 && sec == 0 && day == 0 && month == 0) {
			return "Jamais";
		}
		
		return output;
	}
	
	public String toFormatString() {
		String output = "";
		
		if(month >= 1) { output += format(month) + " Mois "; }
		if(day >= 1) { output += format(day) + " Jour(s) "; }
		if(hour >= 1) { output += format(hour) + "Heure(s) "; }

		output += format(min) + " Minute(s) et ";
		output += format(sec) + " Seconde(s)";

		if(min == 0 && sec == 0 && day == 0 && month == 0) {
			return "Jamais";
		}
		
		return output;
	}
	
	private String format(long in) {
		String out = String.valueOf(in);
		if(in < 10) {
			out = "0" + String.valueOf(in);
		}
		
		return out;
	}

	public long getHour() {
		return hour;
	}

	public void setHour(long hour) {
		this.hour = hour;
	}

	public long getMin() {
		return min;
	}

	public void setMin(long min) {
		this.min = min;
	}

	public long getSec() {
		return sec;
	}

	public void setSec(long sec) {
		this.sec = sec;
	}

	public long getMonth() {
		return month;
	}

	public void setMonth(long month) {
		this.month = month;
	}

	public long getDay() {
		return day;
	}

	public void setDay(long day) {
		this.day = day;
	}
	
}
