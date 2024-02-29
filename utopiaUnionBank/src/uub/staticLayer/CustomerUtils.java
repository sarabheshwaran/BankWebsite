package uub.staticLayer;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CustomerUtils {
	
public static void main(String[] args) {
	System.out.println(formatDate(1709185654322L));
}

	public static String formatDate(long millis) {
		
		 Instant instant = Instant.ofEpochMilli(millis);
	     ZonedDateTime time =  ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
	     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MMM/dd - hh:mm:ss");
	     
	    return time.format(formatter);
	}
	
}
