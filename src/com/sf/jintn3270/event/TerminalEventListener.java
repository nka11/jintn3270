package com.sf.jintn3270.event;

import java.util.EventListener;

public interface TerminalEventListener extends EventListener {
	public void terminalChanged(TerminalEvent te);
}