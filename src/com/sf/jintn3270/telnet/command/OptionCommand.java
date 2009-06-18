package com.sf.jintn3270.telnet.command;

import org.jboss.netty.buffer.ChannelBuffer;


enum OptionCode {
	WILL(251),
	WONT(252),
	DO(253),
	DONT(254);
	
	private int code;
	
	OptionCode(int val) {
		code = val;
	}
	
	public byte getCode() {
		return (byte)code;
	}
}

public class OptionCommand extends TelnetCommand {
	byte arg;
	
	public OptionCommand(OptionCode opt, byte arg) {
		super(opt.getCode());
		this.arg = arg;
	}
	
	public int getLength() {
		return 2;
	}
	
	protected void send(ChannelBuffer buf) {
		super.send(buf);
		buf.writeByte(arg);
	}
}
