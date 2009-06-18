package com.sf.jintn3270.telnet.command;

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
		buf.write(code);
	}
	
	public void send(Channel c) {
		ChannelBuffer buf = Buffers.allocateDirect(getLength());
		
		send(buf);
		
		c.write(buf);
	}
	
	public boolean equals(Object obj) {
		return obj.equals(code);
	}
}
