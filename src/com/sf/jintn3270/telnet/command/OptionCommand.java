package com.sf.jintn3270.telnet.command;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.buffer.ChannelBuffer;


public class OptionCommand extends TelnetCommand {
	byte arg;
	boolean noArg;
	OptionCode optionType;
	
	public OptionCommand(OptionCode opt) {
		super(opt.getCode());
		noArg = true;
		this.optionType = opt;
	}
	
	public OptionCommand(OptionCode opt, byte arg) {
		super(opt.getCode());
		noArg = false;
		this.arg = arg;
		this.optionType = opt;
	}
	
	public int getLength() {
		return 2;
	}
	
	protected void send(ChannelBuffer buf) {
		super.send(buf);
		if (!noArg) {
			buf.writeByte(arg);
		}
	}
	
	public void received(Channel channel, ChannelBuffer buf) {
		byte optionCode = buf.readByte();
		
		// TODO: Determine if we know about this option.
		// For now, just deny everything!
		
		if (optionType == OptionCode.WILL) {
			System.out.println("Received WILL: " + optionCode);
			System.out.println("Sending DONT: " + optionCode);
			new IAC(new OptionCommand(OptionCode.DONT, optionCode)).send(buf);
		} else if (optionType == OptionCode.WONT) {
			System.out.println("Received WONT: " + optionCode);
		} else if (optionType == OptionCode.DO) {
			System.out.println("Received DO: " + optionCode);
			System.out.println("Sending WONT: " + optionCode);
			new IAC(new OptionCommand(OptionCode.WONT, optionCode)).send(buf);
		} else if (optionType == OptionCode.DONT) {
			System.out.println("Received DONT: " + optionCode);
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
