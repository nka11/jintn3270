package com.sf.jintn3270.awt;

import java.awt.AWTKeyStroke;
import java.awt.event.KeyEvent;

import java.util.HashMap;

import com.sf.jintn3270.TerminalModel;
import com.sf.jintn3270.actions.*;

public class DefaultTerminalKeyMap implements TerminalKeyMap {
	public HashMap<AWTKeyStroke, TerminalAction> map;
	
	public DefaultTerminalKeyMap() {
		map = new HashMap<AWTKeyStroke, TerminalAction>();
		
		for (int i = 32; i <= 126; i++) {
			mapCharacter((char)i);
		}
		
		map.put(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_BACK_SPACE, 0), new BackspaceTerminalAction());
		map.put(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0), new EnterTerminalAction());
	}
	
	private void mapCharacter(char c) {
		map.put(AWTKeyStroke.getAWTKeyStroke(c), new TerminalKeyTypedAction(c));
	}
	
	public AWTKeyStroke[] getMappedStrokes() {
		return map.keySet().toArray(new AWTKeyStroke[map.size()]);
	}
	
	public TerminalAction getAction(AWTKeyStroke ks) {
		return map.get(ks);
	}
	
	public TerminalAction getAction(KeyEvent ke, TerminalModel m) {
		return null;
	}
	
	public void processKeyEvent(KeyEvent ke) {
		return;
	}
}
