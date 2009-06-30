package com.sf.jintn3270.swing;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.sf.jintn3270.actions.TerminalAction;
import com.sf.jintn3270.TerminalModel;

public interface TerminalKeyMap {
	public KeyStroke[] getMappedStrokes();
	
	public TerminalAction getAction(KeyStroke ks);
	
	public TerminalAction getAction(KeyEvent ke, TerminalModel term);
	
	public void processKeyEvent(KeyEvent ke);
}
