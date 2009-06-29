package com.sf.jintn3270.awt;

import java.awt.AWTKeyStroke;
import java.awt.event.KeyEvent;

import com.sf.jintn3270.actions.TerminalAction;
import com.sf.jintn3270.TerminalModel;

public interface TerminalKeyMap {
	public AWTKeyStroke[] getMappedStrokes();
	
	public TerminalAction getAction(AWTKeyStroke ks);
	
	public TerminalAction getAction(KeyEvent ke, TerminalModel term);
	
	public void processKeyEvent(KeyEvent ke);
}
