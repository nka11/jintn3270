package com.sf.jintn3270.telnet.actions;

import org.jboss.netty.channel.Channel;

public interface TelnetAction {
	public void execute(Channel ch);
}

