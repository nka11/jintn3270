package com.sf.jintn3270;

import com.sf.jintn3270.telnet.*;


public class TnClient extends TelnetClient {
	public TnClient(String host, int port, boolean ssl) {
		super(host, port, ssl);
	}
}
