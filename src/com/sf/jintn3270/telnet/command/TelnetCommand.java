package com.sf.jintn3270.telnet.command;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

public abstract class TelnetCommand {
	byte code;
	
	protected TelnetCommand(byte code) {
		this.code = code;
	}
	
	
	public byte getCode() {
		return code;
	}
	
	public abstract int getLength();
	
	protected void send(ChannelBuffer buf) {
		buf.writeByte(code);
	}
	
	public void received(ChannelBuffer buf) {
	}
	
	public void send(Channel c) {
		ChannelBuffer buf = ChannelBuffers.directBuffer(getLength());
		
		send(buf);
		
		c.write(buf);
	}
	
	public boolean equals(Object obj) {
		return obj.equals(code);
	}
}
