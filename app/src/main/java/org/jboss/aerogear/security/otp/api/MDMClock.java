package org.jboss.aerogear.security.otp.api;

import java.util.Date;

public class MDMClock extends Clock{
	private final Date mdmdate;
	private final int mdminterval;
	
	public MDMClock(Date date, int interval){
		this.mdmdate = date;
		this.mdminterval = interval;
	}
	
	public long getCurrentInterval(){
		long currentTimeSeconds = this.mdmdate.getTime() / 1000L;
		return currentTimeSeconds / this.mdminterval;
	}
}
