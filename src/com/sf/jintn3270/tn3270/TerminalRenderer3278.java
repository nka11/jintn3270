package com.sf.jintn3270.tn3270;

import com.sf.jintn3270.TerminalCharacter;
import com.sf.jintn3270.swing.DefaultTerminalRenderer;
import com.sf.jintn3270.tn3270.TNFieldCharacter;

import java.awt.Graphics2D;

public class TerminalRenderer3278 extends DefaultTerminalRenderer {
	public TerminalRenderer3278() {
		super();
	}
	
	protected void paintChar(Graphics2D g2d, TerminalCharacter c, float x, float y) {
		if (c instanceof TNFieldCharacter) {
			g2d.drawString("" + (char)0, x, y);
		} else {
			super.paintChar(g2d, c, x, y);
		}
	}
}
