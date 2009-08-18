package com.sf.jintn3270.tn3270;

import com.sf.jintn3270.TerminalModel;
import com.sf.jintn3270.TerminalCharacter;
import com.sf.jintn3270.swing.DefaultTerminalRenderer;
import com.sf.jintn3270.swing.ColorMap;
import com.sf.jintn3270.swing.DefaultColorMap;
import com.sf.jintn3270.tn3270.TNFieldCharacter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class TerminalRenderer3278 extends DefaultTerminalRenderer {
	ColorMap cMap;
	
	TNFieldCharacter currentField;
	
	public TerminalRenderer3278() {
		this(new ColorMap3270());
	}
	
	public TerminalRenderer3278(ColorMap map) {
		super();
		cMap = map;
		currentField = null;
	}
	
	protected Color getDefaultBackground() {
		return cMap.getColor(0xf0);
	}
	
	protected Color getDefaultForeground() {
		return cMap.getColor(0xf7);
	}
	
	
	public void paint(Graphics g, TerminalModel m) {
		currentField = null;
		super.paint(g, m);
	}
	
	protected void paintChar(Graphics2D g2d, TerminalCharacter c, float x, float y) {
		if (c instanceof TNFieldCharacter) {
			currentField = (TNFieldCharacter)c;
		} else {
			TNCharacter tnc = (TNCharacter)c;
			
			boolean fieldProtected = false;
			boolean highIntensity = false;
			
			// Apply the attributes from the current field.
			if (currentField != null) {
				if (currentField.getDisplayMode() == TNFieldCharacter.NONDISPLAY_NO_PEN_DETECTION) {
					return;
				} 
				
				highIntensity = (currentField.getDisplayMode() == TNFieldCharacter.INTENSE_PEN_DETECTION);
				fieldProtected = currentField.isProtected();
			}
			
			Color f = cMap.getColor(tnc.getAttribute(TNCharacter.FOREGROUND_COLOR));;
			Color b = cMap.getColor(tnc.getAttribute(TNCharacter.BACKGROUND_COLOR));;
			
			if (f == null) {
				if (fieldProtected) {
					if (highIntensity) {
						f = Color.WHITE;
					} else {
						f = cMap.getColor(0xf7);
					}
				} else {
					if (highIntensity) {
						f = Color.RED;
					} else {
						f = cMap.getColor(0xf7);
					}
				}
			}
			
			// TODO: Swap F and B if Inverse is on.
			g2d.setColor(f);
			g2d.setBackground(b);
			
			super.paintChar(g2d, c, x, y);
		}
	}
}
