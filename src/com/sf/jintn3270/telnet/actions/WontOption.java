package com.sf.jintn3270.telnet.actions;

import com.sf.jintn3270.telnet.options.TelnetOption;
import com.sf.jintn3270.telnet.TelnetState;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;


public class WontOption implements TelnetAction {
	TelnetOption option;
	
	public WontOption(TelnetOption to) {
		option = to;
	}
	
	public void execute(Channel ch) {
		ChannelBuffer buf = ChannelBuffers.directBuffer(3);
		buf.writeByte(TelnetState.IAC.toByte());
		buf.writeByte(TelnetState.DO.toByte());
		buf.writeByte(option.getCode());
		
		ch.write(buf);
	}
}

