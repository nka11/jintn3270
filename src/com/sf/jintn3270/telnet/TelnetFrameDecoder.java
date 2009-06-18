package com.sf.jintn3270.telnet;



public class TelnetFrameDecoder extends FrameDecoder {
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) {
		buffer.markReaderIndex();
		
		byte b = buffer.readByte();
		if (IAC.equals(b)) {
			// Read the next byte.
			b = buffer.readByte();
			
			
			
			
			
			
			
			
		}
		
		// If we don't have an IAC, we just do something with what we're sent.
		
		
		
		// When there isn't enough data to figure out what to return, return this.
		buffer.resetReaderIndex();
		return null;
	}
}
