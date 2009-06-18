package com.sf.jintn3270.telnet;

import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;


public class TelnetClientPipelineFactory implements ChannelPipelineFactory {
	public ChannelPipeline getPipeline() {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("handler", new TelnetFrameDecoder());
		// TODO: Add decoder & encoder
		return pipeline;
	}
}

