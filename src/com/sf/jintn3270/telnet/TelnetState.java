package com.sf.jintn3270.telnet;

public enum TelnetState {
	DEFAULT (0),
	IAC (255),
	WILL (251),
	WONT (252),
	DO (253),
	DONT (254);
	
	int code;
	
	TelnetState(int code) {
		this.code = code;
	}
	
	public byte toByte() {
		return (byte)code;
	}
}

