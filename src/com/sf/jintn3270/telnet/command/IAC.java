package com.sf.jintn3270.telnet.command;

import org.jboss.netty.buffer.ChannelBuffer;


public class IAC extends TelnetCommand {
	TelnetCommand subCommand;
	
	public IAC() {
		super((byte)0xff);
		subCommand = null;
	}
	
	public IAC(TelnetCommand tc) {
		super((byte)0xff);
		subCommand = tc;
	}
	
	public int getLength() {
		if (hasSubcommand()) {
			return subCommand.getLength() + 1;
		} else { // if we have no subcommand, we're an escape sequence.
			return 2;
		}
	}
	
	public boolean hasSubcommand() {
		return subCommand != null;
	}
	
	protected void send(ChannelBuffer buf) {
		super.send(buf);
		subCommand.send(buf);
	}
}
