package com.sf.jintn3270.telnet;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

import java.util.Formatter;

public class TelnetDecoder extends ReplayingDecoder<TelnetState> {
	byte previous = (byte)0x00;
	byte command = (byte)0x00;
	
	Formatter console = new Formatter(System.out);
	
	public TelnetDecoder() {
		super(TelnetState.DEFAULT);
	}
	
	protected Object decode(ChannelHandlerContext ctx, 
	                        Channel channel, ChannelBuffer buf,
	                        TelnetState state) 
		throws Exception
	{
		switch(state) {
		case HANDLE_IAC:
			previous = command;
			// read the next byte, see what it is.
			command = buf.readByte();
			console.format("Handle IAC 0x%02x ", command);
			switch (command) {
			case (byte)0xfb:
				checkpoint(TelnetState.WILL);
			case (byte)0xfc:
				checkpoint(TelnetState.WONT);
			case (byte)0xfd:
				checkpoint(TelnetState.DO);
			case (byte)0xfe:
				checkpoint(TelnetState.DONT);
			case (byte)0xff: // Another IAC. Two in a row means it's an escape for char 255.
				checkpoint(TelnetState.DEFAULT);
			}
			
		case WILL:
			previous = command;
			command = buf.readByte();
			
			// TODO: Compare this command with what we know to acknowledge.
			console.format("Ignoring WILL 0x%02x ", command);
			checkpoint(TelnetState.DEFAULT);
			// TODO: return Dont(command);
		case WONT:
			previous = command;
			command = buf.readByte();
			// Do I know this thing?
			console.format("Ignoring WONT 0x%02x ", command);
			checkpoint(TelnetState.DEFAULT);
			
		case DO:
			previous = command;
			command = buf.readByte();
			// Do I know this thing?
			console.format("Ignoring DO 0x%02x ", command);
			checkpoint(TelnetState.DEFAULT);
			
		case DONT:
			previous = command;
			command = buf.readByte();
			// Do I know this thing?
			console.format("Ignoring DONT 0x%02x ", command);
			checkpoint(TelnetState.DEFAULT);
			
		case DEFAULT:
			// read a byte, and see what it is.
			previous = command;
			command = buf.readByte();
			console.format("0x%02x ", command);
			
			if (command == (byte)(0xff) && previous != (byte)(0xff)) {
				checkpoint(TelnetState.HANDLE_IAC);
			}
			// TODO: Continue decoding, return objects...
			return command;
			
		default:
			throw new Error("Whoopsie.");
		}
	}
}