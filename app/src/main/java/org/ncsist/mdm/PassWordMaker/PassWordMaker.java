package org.ncsist.mdm.PassWordMaker;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.jboss.aerogear.security.otp.api.MDMClock;

public class PassWordMaker {
	private static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private Date t0Date = new Date(0L);
	public static final String[] UNLOCK_REASON = { "GPS訊號不佳", "移除裝置管理員", "嘗試移除裝置管理員", "申請解除MDM" };
	public static final String UNLOCK_REASON_UNLOCK = UNLOCK_REASON[0];
	public static final String UNLOCK_REASON_DEVICE_ADMIN_ON_DISABLE = UNLOCK_REASON[1];
	public static final String UNLOCK_REASON_DEVICE_ADMIN_ON_DISABLE_REQUESTED = UNLOCK_REASON[2];
	public static final String UNLOCK_REASON_UNINSTALL = UNLOCK_REASON[3];
	
	public static void main(String[] args) {
		//System.out.println("Hello");
		
		try
	    {
			PassWordMaker me = new PassWordMaker();
			long seconds = me.getDateSeconds();
			System.out.println("get seconds: " + seconds);

			String hint = me.toNBase(seconds);
			System.out.println("seconds to hint: " + hint);
	      
			System.out.println(me.getUnlockPass("XJ4jQx", "���]���:" + UNLOCK_REASON_UNLOCK));
	    }
	    catch (UnsupportedEncodingException e)
	    {
	    	e.printStackTrace();
	    }
	    
	}
	
	public long getDateSeconds(){
	    Date tmp = new Date();
	    tmp.setTime(1000L * (tmp.getTime() / 1000L));
	    
	    long seconds = (tmp.getTime() - this.t0Date.getTime()) / 1000L;
	    if (seconds > 1L) {
	      this.t0Date = tmp;
	    } else {
	      this.t0Date.setTime(this.t0Date.getTime() + 1000L);
	    }
	    return this.t0Date.getTime() / 1000L;
	}
	
	public Date secondsToDate(long seconds){
		return new Date(seconds * 1000L);
	}
	
	public String toNBase(long value){
	    int digitCount = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length();
	    if (value < 0L) {
	      return "error";
	    }
	    if (value == 0L) {
	      return String.valueOf("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(0));
	    }
	    StringBuilder sb = new StringBuilder();
	    while (value > 0L)
	    {
	      int c = (int)(value % digitCount);
	      sb.insert(0, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(c));
	      value = (value - c) / digitCount;
	    }
	    return sb.toString();
	}
	
	public long toLong(String nBase){
	    int digitCount = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length();
	    long value = 0L;
	    long digit = 1L;
	    for (int i = nBase.length() - 1; i >= 0; i--)
	    {
	      value += "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".indexOf(nBase.charAt(i)) * digit;
	      digit *= digitCount;
	    }
	    return value;
	}
	
	public String getUnlockPass(long seconds, String text) throws UnsupportedEncodingException{
		Date date = secondsToDate(seconds);
		MDMClock clk = new MDMClock(date, 1);
		String secret = Base32.encode(text.getBytes("UTF-8"));
		Totp totp = new Totp(secret, clk);
		return totp.now();
	}
	
	public String getUnlockPass(String hint, String text) throws UnsupportedEncodingException{
		return getUnlockPass(toLong(hint), text);
	}
}
