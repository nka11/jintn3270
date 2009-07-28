package com.sf.jintn3270.tn3270;

import com.sf.jintn3270.telnet.TelnetClient;

import com.sf.jintn3270.telnet.Binary;
import com.sf.jintn3270.telnet.TerminalType;
import com.sf.jintn3270.telnet.EndOfRecord;

import com.sf.jintn3270.swing.*;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.sf.jintn3270.telnet.*;

public class TestClient extends JFrame {
	TelnetClient client;
	JTerminal term;
	
	public TestClient(TelnetClient tc) {
		super(tc.getHost() + ":" + tc.getPort());
		this.client = tc;
		
		term = new JTerminal(tc.getTerminalModel());
		term.setFont(Font.decode("Lucida Console-12"));
		term.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
		
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
		TelnetClient client = 
			new TelnetClient(args[0], Integer.parseInt(args[1]), 
				Boolean.valueOf(args[2]).booleanValue(), 
				new TerminalModel3278(TerminalModel3278.TermType3278.MODEL2));
		client.start();
		
		TestClient frame = new TestClient(client);
		frame.pack();
		frame.setVisible(true);
	}
}
