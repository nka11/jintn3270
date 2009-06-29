package com.sf.jintn3270.awt;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.sf.jintn3270.telnet.TelnetClient;

public class TelnetFrame extends Frame {
	TelnetClient client;
	Terminal term;
	
	
	public TelnetFrame(TelnetClient tc) {
		super(tc.getHost() + ":" + tc.getPort());
		this.client = tc;
		term = new Terminal(tc.getTerminalModel());
		
		this.setLayout(new BorderLayout());
		this.add(term);
		
		this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent we) {
					client.disconnect();
					dispose();
				}
		});
	}
	
	
	public static void main(String[] args) {
		TelnetClient c = new TelnetClient(args[0], Integer.parseInt(args[1]), Boolean.valueOf(args[2]).booleanValue());
		c.start();
		
		TelnetFrame tf = new TelnetFrame(c);
		tf.pack();
		tf.setVisible(true);
	}
}

