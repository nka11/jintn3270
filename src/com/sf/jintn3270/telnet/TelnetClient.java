package com.sf.jintn3270.telnet;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;

import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.bootstrap.ClientBootstrap;

/**
 * Class that implements a client. Yay.
 */
public class TelnetClient {
	ClientBootstrap bootstrap;
	Channel channel;
	ChannelFactory factory;
	
	public TelnetClient() {
		channel = null;
		factory = new NioClientSocketChannelFactory(
					Executors.newCachedThreadPool(),
					Executors.newCachedThreadPool());
		bootstrap = new ClientBootstrap(factory);
		
		bootstrap.setPipelineFactory(new TelnetClientPipelineFactory());
		
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
	}
	
	/**
	 * You may want to call this from another thread...
	 */
	public void connect(String host, int port) {
		if (isConnected()) {
			disconnect();
		}
		
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
		
		channel = future.awaitUninterruptibly().getChannel();
		
		if (!future.isSuccess()) {
			channel = null;
			disconnected();
		} else {
			connected();
		}
	}
	
	/**
	 * Disconnects from the host.
	 */
	public void disconnect() {
		channel.close().awaitUninterruptibly();
		channel = null;
		disconnected();
	}
	
	/**
	 * Invoked when we're disconnected
	 */
	protected void disconnected() {
		System.out.println("Disconnected!");
	}
	
	/**
	 * Invoked when we're connected
	 */
	public void connected() {
		System.out.println("Connected!");
	}
	
	/**
	 * Determines if we have an output channel
	 */
	public boolean isConnected() {
		return channel != null;
	}
	
	/**
	 * Oh yeah, baby.
	 */
	public static void main(String[] args) {
		TelnetClient client = new TelnetClient();
		client.connect(args[0], Integer.parseInt(args[1]));
	}
}

