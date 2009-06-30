package com.sf.jintn3270.swing;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.sf.jintn3270.telnet.*;

public class TelnetFrame extends JFrame {
	TelnetClient client;
	JTerminal term;
	
	public TelnetFrame(TelnetClient tc) {
		super(tc.getHost() + ":" + tc.getPort());
		this.client = tc;
		
		term = new JTerminal(tc.getTerminalModel());
		term.setFont(Font.decode("Lucida Console-12"));
		
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
		c.addOption(new SuppressGA());
		c.addOption(new Echo());
		c.addOption(new EndOfRecord());
		c.start();
		
		TelnetFrame tf = new TelnetFrame(c);
		tf.pack();
		tf.setVisible(true);
	}
}

