package com.sf.jintn3270.telnet.options;

public class UnsupportedOption extends TelnetOption {
	byte command;
	
	public UnsupportedOption(byte code) {
		command = code;
	}
	
	public byte getCode() {
		return command;
	}
}

