package com.sf.jintn3270.telnet;

public enum TelnetState {
	DEFAULT,
	HANDLE_IAC,
	WILL,
	WONT,
	DO,
	DONT;
}

