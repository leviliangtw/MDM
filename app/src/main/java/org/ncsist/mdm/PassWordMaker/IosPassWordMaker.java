package org.ncsist.mdm.PassWordMaker;

import java.io.PrintStream;
import java.nio.ByteBuffer;

public class IosPassWordMaker {
	public static final String[] UNLOCK_REASON = {"iOS密碼解鎖-1.03以後", "iOS解除安裝-1.03以後", "iOS密碼解鎖-1.02以前", "iOS解除安裝-1.02以前" };
	  static final String codebook = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	  static final int remove_allowed = 888;
	  static final int remove_allowed_mod = 7;
	  static String key = "ZJ3dq";
	  static String input = "1FL0y";

	  // iOS密碼解鎖-1.03以後
	  public static String compute_password(String site_name, String input)
	  {
	    int password_int = decode(input);
	    System.out.println("password_int=" + password_int);
	    int key_int = decode(key);
	    
	    byte[] input_bytes = ByteBuffer.allocate(4).putInt(password_int).array();
	    byte[] key_bytes = ByteBuffer.allocate(4).putInt(key_int).array();
	    byte[] xor_bytes = new byte[4];
	    
	    byte[] name_bytes = ByteBuffer.allocate(4).putInt(1).array();
	    char[] arrayOfChar;
	    int j = (arrayOfChar = site_name.toCharArray()).length;
	    for (int i = 0; i < j; i++)
	    {
	      char c = arrayOfChar[i];
	      ByteBuffer tmp_b = ByteBuffer.wrap(name_bytes);
	      int tmp_int = tmp_b.getInt();
	      name_bytes = ByteBuffer.allocate(4).putInt(c * tmp_int).array();
	    }
	    for (int i = 0; i < 4; i++) {
	      key_bytes[i] = ((byte)(name_bytes[i] ^ key_bytes[i]));
	    }
	    System.out.println("input_bytes=" + byteArrayToHex(input_bytes));
	    for (int i = 0; i < 2; i++) {
	      xor_bytes[i] = ((byte)(input_bytes[(i + 2)] ^ key_bytes[(i + 2)]));
	    }
	    for (int i = 2; i < 4; i++) {
	      xor_bytes[i] = ((byte)(input_bytes[(i - 2)] ^ key_bytes[(i - 2)]));
	    }
	    ByteBuffer bb = ByteBuffer.wrap(xor_bytes);
	    int xor = bb.getInt();
	    if (xor < 0) {
	      xor *= -1;
	    }
	    xor %= 1000000;
	    String return_str = Integer.toString(xor);
	    
	    System.out.println("encoded xor=" + xor);
	    
	    return return_str;
	  }
	  // iOS解除安裝-1.03以後
	  public static String unlock_password_compute(String site_name, String input)
	  {
	    int password_int = decode(input);
	    int key_int = decode(key);
	    
	    byte[] input_bytes = ByteBuffer.allocate(4).putInt(password_int).array();
	    byte[] key_bytes = ByteBuffer.allocate(4).putInt(key_int).array();
	    byte[] xor_bytes = new byte[4];
	    
	    byte[] name_bytes = ByteBuffer.allocate(4).putInt(1).array();
	    char[] arrayOfChar;
	    int j = (arrayOfChar = site_name.toCharArray()).length;
	    for (int i = 0; i < j; i++)
	    {
	      char c = arrayOfChar[i];
	      ByteBuffer tmp_b = ByteBuffer.wrap(name_bytes);
	      int tmp_int = tmp_b.getInt();
	      name_bytes = ByteBuffer.allocate(4).putInt(c * tmp_int).array();
	    }
	    for (int i = 0; i < 4; i++) {
	      key_bytes[i] = ((byte)(name_bytes[i] ^ key_bytes[i]));
	    }
	    for (int i = 0; i < 2; i++) {
	      xor_bytes[i] = ((byte)(input_bytes[(i + 2)] ^ key_bytes[(i + 2)]));
	    }
	    for (int i = 2; i < 4; i++) {
	      xor_bytes[i] = ((byte)(input_bytes[(i - 2)] ^ key_bytes[(i - 2)]));
	    }
	    ByteBuffer bb = ByteBuffer.wrap(xor_bytes);
	    int xor = bb.getInt();
	    if (xor < 0) {
	      xor *= -1;
	    }
	    xor %= 1000000;
	    
	    xor /= 10;
	    xor = xor * 7 + 888;
	    if (xor < 0) {
	      xor *= -1;
	    }
	    String return_str = Integer.toString(xor);
	    
	    return return_str;
	  }

	  // iOS密碼解鎖-1.02以前
	  public static String compute_password_o(String input)
	  {
	    int password_int = decode(input);
	    System.out.println("password_int=" + password_int);
	    int key_int = decode(key);
	    
	    byte[] input_bytes = ByteBuffer.allocate(4).putInt(password_int).array();
	    byte[] key_bytes = ByteBuffer.allocate(4).putInt(key_int).array();
	    byte[] xor_bytes = new byte[4];
	    
	    System.out.println("input_bytes=" + byteArrayToHex(input_bytes));
	    for (int i = 0; i < 2; i++) {
	      xor_bytes[i] = ((byte)(input_bytes[(i + 2)] ^ key_bytes[(i + 2)]));
	    }
	    for (int i = 2; i < 4; i++) {
	      xor_bytes[i] = ((byte)(input_bytes[(i - 2)] ^ key_bytes[(i - 2)]));
	    }
	    ByteBuffer bb = ByteBuffer.wrap(xor_bytes);
	    int xor = bb.getInt();
	    if (xor < 0) {
	      xor *= -1;
	    }
	    System.out.println("encoded xor=" + encode(xor));
	    return encode(xor);
	  }

	  // iOS解除安裝-1.02以前
	  public static String unlock_password_compute_o(String input)
	  {
	    int password_int = decode(input);
	    int key_int = decode(key);
	    
	    byte[] input_bytes = ByteBuffer.allocate(4).putInt(password_int).array();
	    byte[] key_bytes = ByteBuffer.allocate(4).putInt(key_int).array();
	    byte[] xor_bytes = new byte[4];
	    for (int i = 0; i < 2; i++) {
	      xor_bytes[i] = ((byte)(input_bytes[(i + 2)] ^ key_bytes[(i + 2)]));
	    }
	    for (int i = 2; i < 4; i++) {
	      xor_bytes[i] = ((byte)(input_bytes[(i - 2)] ^ key_bytes[(i - 2)]));
	    }
	    ByteBuffer bb = ByteBuffer.wrap(xor_bytes);
	    int xor = bb.getInt();
	    if (xor < 0) {
	      xor *= -1;
	    }
	    xor /= 10;
	    xor = xor * 7 + 888;
	    if (xor < 0) {
	      xor *= -1;
	    }
	    return encode(xor);
	  }
	  
	  public static int search_index(char c)
	  {
	    int result = -1;
	    int codebook_length = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length();
	    for (int i = 0; i < codebook_length; i++) {
	      if ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(i) == c)
	      {
	        result = i; break;
	      }
	    }
	    return result;
	  }
	  
	  public static String encode(int input)
	  {
	    String result = "";
	    int codebook_length = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length();
	    while (input > 0)
	    {
	      int tmp = input % codebook_length;
	      result = result + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(tmp);
	      input = (input - tmp) / codebook_length;
	    }
	    return result;
	  }
	  
	  public static int decode(String input)
	  {
	    int result = 0;
	    int input_length = input.length();
	    int codebook_length = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length();
	    for (int i = input_length - 1; i >= 0; i--)
	    {
	      int index = search_index(input.charAt(i));
	      result = result * codebook_length + index;
	    }
	    return result;
	  }
	  
	  public static String byteArrayToHex(byte[] a)
	  {
	    StringBuilder sb = new StringBuilder(a.length * 2);
	    byte[] arrayOfByte = a;int j = a.length;
	    for (int i = 0; i < j; i++)
	    {
	      byte b = arrayOfByte[i];
	      sb.append(String.format("%02x", new Object[] { Integer.valueOf(b & 0xFF) }));
	    }
	    return sb.toString();
	  }
}
