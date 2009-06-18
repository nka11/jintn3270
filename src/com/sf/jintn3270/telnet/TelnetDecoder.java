package com.sf.jintn3270.telnet;

import com.sf.jintn3270.telnet.options.TelnetOption;
import com.sf.jintn3270.telnet.options.UnsupportedOption;

import com.sf.jintn3270.telnet.actions.TelnetAction;
import com.sf.jintn3270.telnet.actions.DoOption;
import com.sf.jintn3270.telnet.actions.DontOption;
import com.sf.jintn3270.telnet.actions.WillOption;
import com.sf.jintn3270.telnet.actions.WontOption;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

import java.util.HashMap;
import java.util.Formatter;

public class TelnetDecoder extends ReplayingDecoder<TelnetState> {
	byte previous = (byte)0x00;
	byte command = (byte)0x00;
	
	Formatter console = new Formatter(System.out);
	
	static HashMap<Byte, TelnetOption> options = 
			new HashMap<Byte, TelnetOption>();
	
	
	public TelnetDecoder() {
		super(TelnetState.DEFAULT);
	}
	
	public static void registerOption(TelnetOption option) {
		options.put(option.getCode(), option);
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, 
	                        Channel channel, ChannelBuffer buf,
	                        TelnetState state) 
		throws Exception
	{
		console.format("\ndecode: " + state + " ");
		switch(state) {
			case IAC: {
				previous = command;
				// read the next byte, see what it is.
				command = buf.readByte();
				console.format(" subcommand 0x%02x ", command);
				if (command == TelnetState.WILL.toByte()) {
					previous = command;
					command = buf.readByte();
					
					checkpoint(TelnetState.DEFAULT);
					
					TelnetOption option = options.get(command);
					if (option != null) {
						console.format(" Returning DO ");
						return new DoOption(option);
					} else {
						console.format(" Returning DONT ");
						return new DontOption(new UnsupportedOption(command));
					}
				} else if (command == TelnetState.WONT.toByte()) {
					previous = command;
					command = buf.readByte();
					
					checkpoint(TelnetState.DEFAULT);
				} else if (command == TelnetState.DO.toByte()) {
					previous = command;
					command = buf.readByte();
					
					checkpoint(TelnetState.DEFAULT);
					console.format(" Received DO 0x%02x ", command);
					
					TelnetOption option = options.get(command);
					if (option != null) {
						console.format(" Returning WILL ");
						return new WillOption(option);
					} else {
						console.format(" Returning WONT ");
						return new WontOption(new UnsupportedOption(command));
					}
				} else if (command == TelnetState.DONT.toByte()) {
					previous = command;
					command = buf.readByte();
					
					checkpoint(TelnetState.DEFAULT);
				} else if (command == TelnetState.IAC.toByte()) {
					// this is an escape for 255!
					System.out.println("Escaped 255. What to do? I don't know!");
					checkpoint(TelnetState.DEFAULT);
				}
			}
			case DEFAULT: {
				// read a byte, and see what it is.
				previous = command;
				command = buf.readByte();
				
				if (command == TelnetState.IAC.toByte() && previous != command) {
					checkpoint(TelnetState.IAC);
				}
				return null;
			}
			default: {
				throw new Error("Whoopsie.");
			}
		}
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		super.messageReceived(ctx, e);
		
		System.out.println("\n" + e.getMessage());
		if (e.getMessage() instanceof TelnetAction) {
			TelnetAction ta = (TelnetAction)e.getMessage();
			ta.execute(ctx.getPipeline().getChannel());
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		System.out.println("" + e);
	}
}
