package com.sf.jintn3270.awt;

import java.awt.Dimension;
import java.awt.Graphics;

import com.sf.jintn3270.TerminalModel;

/**
 * Generic Interface to be used by Renderers for TerminalModels.
 */
public interface TerminalRenderer {
	public void paint(Graphics g, TerminalModel m);
	
	public Dimension getPreferredSize(TerminalModel m);
	
	public Dimension getMinimumSize(TerminalModel m);
}
