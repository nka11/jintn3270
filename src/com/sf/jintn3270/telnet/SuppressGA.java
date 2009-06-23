package com.sf.jintn3270.telnet;

public class SuppressGA extends Option {
	public SuppressGA() {
		super();
	}
	
	public String getName() {
		return "Suppress GA";
	}
	
	public byte getCode() {
		return (byte)3;
	}
}
