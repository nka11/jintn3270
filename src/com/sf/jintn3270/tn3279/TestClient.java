package com.sf.jintn3270.tn3279;

import com.sf.jintn3270.telnet.TelnetClient;

import com.sf.jintn3270.telnet.Binary;
import com.sf.jintn3270.telnet.TerminalType;
import com.sf.jintn3270.telnet.EndOfRecord;

public class TestClient {
	public static void main(String[] args) {
		TelnetClient client = 
			new TelnetClient(args[0], Integer.parseInt(args[1]), 
			Boolean.valueOf(args[2]).booleanValue(), 
			new TerminalModel3279(TerminalModel3279.TermType3279.MODEL4));
		client.addOption(new TerminalType());
		client.start();
	}
}
