package org.web.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateConv {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String d = "11/26/15";
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
		try{
			Date expiryDate = formatter.parse(d);
			System.out.println(expiryDate);
		} catch(Exception e){
			System.out.println("failed : " + e);
		}
	}

}
