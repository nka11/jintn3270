package com.sf.jintn3270.awt;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;

import java.awt.AWTEvent;
import java.awt.AWTKeyStroke;
import java.awt.event.KeyEvent;

import com.sf.jintn3270.event.*;
import com.sf.jintn3270.actions.TerminalAction;
import com.sf.jintn3270.TerminalModel;

public class Terminal extends Panel implements TerminalEventListener {
	Image offscreen;
	TerminalRenderer renderer;
	
	TerminalModel model;
	
	TerminalKeyMap keyMap;
	
	public Terminal(TerminalModel tm) {
		this(tm, new DefaultTerminalKeyMap());
	}
	
	public Terminal(TerminalModel tm, TerminalKeyMap km) {
		this(tm, new DefaultTerminalRenderer(), km);
	}
	
	public Terminal(TerminalModel tm, TerminalRenderer rn, TerminalKeyMap km) {
		super();
		enableEvents(AWTEvent.KEY_EVENT_MASK);
		setTerminalRenderer(rn);
		setTerminalModel(tm);
		setKeyMap(km);
	}
	
	
	public void setTerminalModel(TerminalModel tm) {
		if (model != null) {
			model.removeTerminalEventListener(this);
		}
		model = tm;
		if (tm != null) {
			model.addTerminalEventListener(this);
		}
	}
	
	
	public void setTerminalRenderer(TerminalRenderer rend) {
		this.renderer = rend;
	}
	
	
	public void setKeyMap(TerminalKeyMap km) {
		this.keyMap = km;
	}
	
	
	public TerminalRenderer getTerminalRenderer() {
		return this.renderer;
	}
	
	
	public TerminalModel getTerminalModel() {
		return model;
	}
	
	
	public void terminalChanged(TerminalEvent te) {
		if (isVisible()) {
			repaint();
		}
	}
	
	public void invalidate() {
		super.invalidate();
		offscreen = null;
	}
	
	
	public Dimension getPreferredSize() {
		return renderer.getPreferredSize(model);
	}
	
	public Dimension getMinimumSize() {
		return renderer.getMinimumSize(model);
	}
	
	protected void processKeyEvent(KeyEvent e) {
		AWTKeyStroke stroke = AWTKeyStroke.getAWTKeyStrokeForEvent(e);
		if (keyMap != null) {
			TerminalAction action = keyMap.getAction(stroke);
			if (action != null) {
				e.consume();
				try {
					action.doAction(model);
				} catch (Exception ex) {}
			}
		}
		
		if (!e.isConsumed()) {
			TerminalAction action = keyMap.getAction(e, model);
			if (action != null) {
				e.consume();
				try {
					action.doAction(model);
				} catch (Exception ex) {}
			}
		}
		
		if (!e.isConsumed()) {
			keyMap.processKeyEvent(e);
		}
	}
	
	
	public void paint(Graphics g) {
		if (offscreen == null) {
			offscreen = createImage(getSize().width, getSize().height);
		}
		// Set the clipping region to include the entire component.
		Graphics offG = offscreen.getGraphics();
		offG.setClip(0, 0, getSize().width, getSize().height);
		
		// Render this component, then paint the subcomponents.
		renderer.paint(offG, model);
		super.paint(offG);
		
		// Then blit to screen.
		g.drawImage(offscreen, 0, 0, null);
		offG.dispose();
	}
	
	public void update(Graphics g) {
		paint(g);
	}
	
	public void setFont(Font f) {
		super.setFont(f);
		renderer.setFont(f);
	}
}
