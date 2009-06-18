package com.sf.jintn3270.telnet;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;




public class TelnetFrameDecoder extends FrameDecoder {
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) {
		buffer.markReaderIndex();
		
		byte b = buffer.readByte();
			
			
			
			
			
			
			
			
		
		// If we don't have an IAC, we just do something with what we're sent.
		
		
		
		// When there isn't enough data to figure out what to return, return this.
		buffer.resetReaderIndex();
		return null;
	}
}
