package com.sf.jintn3270.swing;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import java.util.HashMap;

import com.sf.jintn3270.TerminalModel;
import com.sf.jintn3270.actions.*;

public class DefaultTerminalKeyMap implements TerminalKeyMap {
	public HashMap<KeyStroke, TerminalAction> map;
	
	public DefaultTerminalKeyMap() {
		map = new HashMap<KeyStroke, TerminalAction>();
		
		for (int i = 32; i <= 126; i++) {
			mapCharacter((char)i);
		}
		
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), new BackspaceTerminalAction());
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), new EnterTerminalAction());
	}
	
	private void mapCharacter(char c) {
		map.put(KeyStroke.getKeyStroke(c), new TerminalKeyTypedAction(c));
	}
	
	public KeyStroke[] getMappedStrokes() {
		return map.keySet().toArray(new KeyStroke[map.size()]);
	}
	
	public TerminalAction getAction(KeyStroke ks) {
		return map.get(ks);
	}
	
	public TerminalAction getAction(KeyEvent ke, TerminalModel m) {
		return null;
	}
	
	public void processKeyEvent(KeyEvent ke) {
		return;
	}
}
