package com.sf.jintn3270.telnet;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.bootstrap.ClientBootstrap;

public class TelnetClient {
	ClientBootstrap bootstrap;
	
	public TelnetClient() {
		ChannelFactory factory = new NioClientSocketChannelFactory(
					Executors.newCachedThreadPool(),
					Executors.newCachedThreadPool());
		bootstrap = new ClientBootstrap(factory);
		
		bootstrap.setPipelineFactory(new TelnetClientPipelineFactory());
		
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
	}
	
	public void connect(String host, int port) {
		bootstrap.connect(new InetSocketAddress(host, port));
	}
	
	public static void main(String[] args) {
		TelnetClient client = new TelnetClient();
		client.connect(args[0], Integer.parseInt(args[1]));
	}
}

