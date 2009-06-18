package com.sf.jintn3270.telnet;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;


import com.sf.jintn3270.telnet.command.IAC;
import com.sf.jintn3270.telnet.command.TelnetCommand;

import java.util.HashMap;


public class TelnetFrameDecoder extends FrameDecoder {
	private HashMap<Byte, TelnetCommand> knownCommands;
	
	
	public TelnetFrameDecoder(TelnetCommand[] commands) {
		super();
		knownCommands = new HashMap<Byte, TelnetCommand>();
		for (TelnetCommand cmd : commands) {
			knownCommands.put(cmd.getCode(), cmd);
		}
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) {
		buffer.markReaderIndex();
		
		byte b = buffer.readByte();
		TelnetCommand cmd = knownCommands.get(b);
		if (cmd != null) {
			
			if (cmd.getCode() == 0xff) {
				if (buffer.readableBytes() < 1) {
					buffer.resetReaderIndex();
					return null;
				}
				
				System.out.println("IAC!");
				b = buffer.readByte();
				
				TelnetCommand subCommand = knownCommands.get(b);
				if (subCommand != null) {
					if (buffer.readableBytes() < subCommand.getLength()) {
						buffer.resetReaderIndex();
						return null;
					}
					
					buffer.resetReaderIndex();
					return buffer.readBytes(subCommand.getLength() + 1);
				}
				
				buffer.resetReaderIndex();
				return buffer.readBytes(cmd.getLength());
			}
		} else {
			// We haven't gotten a command we recognize. what shall we do?
			System.out.println("" + b + " " );
		}
		
		// When there isn't enough data to figure out what to return, return this.
		buffer.resetReaderIndex();
		return null;
	}
}
