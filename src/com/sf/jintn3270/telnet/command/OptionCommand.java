package com.sf.jintn3270.telnet.command;

enum OptionCode extends Integer {
	WILL(251),
	WONT(252),
	DO(253),
	DONT(254);
}

public OptionCommand<OptionCode T> extends TelnetCommand {
	byte arg;
	
	public OptionCommand<OptionCode T>(byte arg) {
		super(T.byteValue());
		this.arg = arg;
	}
	
	public int getLength() {
		return 2;
	}
	
	protected void send(ChannelBuffer buf) {
		super.send(buf);
		buf.write(arg);
	}
}
