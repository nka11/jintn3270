package com.sf.jintn3270.tn3270;

import com.sf.jintn3270.telnet.TelnetClient;

import com.sf.jintn3270.telnet.Binary;
import com.sf.jintn3270.telnet.TerminalType;
import com.sf.jintn3270.telnet.EndOfRecord;

public class TestClient {
	public static void main(String[] args) {
		TelnetClient client = 
			new TelnetClient(args[0], Integer.parseInt(args[1]), 
				Boolean.valueOf(args[2]).booleanValue(), 
				new TerminalModel3278(TerminalModel3278.TermType3278.MODEL4));
		client.start();
	}
}
