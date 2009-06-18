package com.sf.jintn3270.telnet;

import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;

import java.util.ArrayList;
import com.sf.jintn3270.telnet.command.*;

public class TelnetClientPipelineFactory implements ChannelPipelineFactory {
	public ChannelPipeline getPipeline() {
		ChannelPipeline pipeline = Channels.pipeline();
		
		ArrayList<TelnetCommand> commands = new ArrayList<TelnetCommand>();
		commands.add(new IAC());
		commands.add(new OptionCommand(OptionCommand.OptionCode.WILL));
		commands.add(new OptionCommand(OptionCommand.OptionCode.WONT));
		commands.add(new OptionCommand(OptionCommand.OptionCode.DO));
		commands.add(new OptionCommand(OptionCommand.OptionCode.DONT));
		
		pipeline.addLast("handler", new TelnetFrameDecoder((TelnetCommand[])commands.toArray(new TelnetCommand[0])));
		return pipeline;
	}
}

