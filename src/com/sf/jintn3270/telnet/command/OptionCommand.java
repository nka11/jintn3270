package com.sf.jintn3270.telnet.command;

import org.jboss.netty.buffer.ChannelBuffer;


public class OptionCommand extends TelnetCommand {
	byte arg;
	boolean noArg;
	
	public OptionCommand(OptionCode opt) {
		super(opt.getCode());
		noArg = true;
	}
	
	public OptionCommand(OptionCode opt, byte arg) {
		super(opt.getCode());
		noArg = false;
		this.arg = arg;
	}
	
	public int getLength() {
		if (noArg) {
			return 1;
		} else {
			return 2;
		}
	}
	
	protected void send(ChannelBuffer buf) {
		super.send(buf);
		if (!noArg) {
			buf.writeByte(arg);
		}
	}
	
	public enum OptionCode {
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
}
