package com.sf.jintn3270.awt;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Graphics;

import com.sf.jintn3270.event.*;
import com.sf.jintn3270.TerminalModel;

public class Terminal extends Panel implements TerminalEventListener {
	Image offscreen;
	TerminalRenderer renderer;
	
	TerminalModel model;
	
	
	public Terminal() {
		this(new DefaultTerminalRenderer(), null);
	}
	
	public Terminal(TerminalModel tm) {
		this(new DefaultTerminalRenderer(), tm);
	}
	
	public Terminal(TerminalRenderer rn, TerminalModel tm) {
		super();
		setTerminalRenderer(rn);
		setTerminalModel(tm);
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
}
