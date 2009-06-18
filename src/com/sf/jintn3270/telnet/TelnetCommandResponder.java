package com.sf.jintn3270.telnet;

import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.MessageEvent;

import com.sf.jintn3270.telnet.command.*;

@ChannelPipelineCoverage("one")
public class TelnetCommandResponder extends SimpleChannelHandler {
	public TelnetCommandResponder() {
		super();
	}
	
	
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
		throws Exception
	{
		Object payload = e.getMessage();
		if (payload instanceof TelnetCommand) {
			System.out.println("" + payload);
		} else {
			System.out.println(e.getMessage().toString());
		}
	}
}