package com.sf.jintn3270.awt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.sf.jintn3270.TerminalModel;


public class DefaultTerminalRenderer extends Component implements TerminalRenderer {
	FontRenderContext fontContext;
	RenderingHints renderHints;
	
	public DefaultTerminalRenderer() {
		super();
		setFont(Font.decode("Monospaced-12"));
		renderHints = new RenderingHints(null);
		renderHints.put(RenderingHints.KEY_FRACTIONALMETRICS,
		                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		renderHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
		                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	}
	
	public void paint(Graphics g, TerminalModel m) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setFont(getFont());
		g2d.setRenderingHints(renderHints);
		if (!g2d.getFontRenderContext().equals(fontContext)) {
			fontContext = g2d.getFontRenderContext();
		}
		Rectangle2D charBound = getFont().getStringBounds("M", fontContext);
		LineMetrics lineMetrics = getFont().getLineMetrics("Mq", fontContext);
		
		g2d.setBackground(Color.BLACK);
		g2d.setColor(Color.WHITE);
		g2d.clearRect(0, 0, g2d.getClipBounds().width, g2d.getClipBounds().height);
		
		Point2D.Float p = new Point2D.Float();
		for (int line = 0; line < m.getBufferHeight(); line++) {
			// TODO: Use AttributedCharacterIterator to read/render the column.
			for (int col = 0; col < m.getBufferWidth(); col++) {
				p.setLocation(col * (charBound.getWidth() + 1),
				              (lineMetrics.getAscent() * (line + 1)) + 
					(line * (lineMetrics.getDescent() + lineMetrics.getLeading())));
				g2d.drawString("" + m.getChar(line, col).getDisplay(), (float)p.getX(), (float)p.getY());
			}
		}
	}
	
	public Dimension getPreferredSize(TerminalModel m) {
		if (m == null) {
			return new Dimension(200, 150);
		} else {
			return getBufferSize(m, getFont().deriveFont(12.0f));
		}
	}
	
	public Dimension getMinimumSize(TerminalModel m) {
		if (m == null) {
			return new Dimension(200, 150);
		} else {
			return getBufferSize(m, getFont().deriveFont(6.0f));
		}
	}
	
	
	/** 
	 * Calculate the required size to render given the model and font.
	 * 
	 * @param m The TerminalModel to potentially render
	 * @param f The font to use for rendering.
	 * 
	 * @return The required size to render the contents in the given font.
	 */
	Dimension getBufferSize(TerminalModel m, Font f) {
		Rectangle2D charBound = f.getStringBounds("M", fontContext);
		LineMetrics lineMetrics = f.getLineMetrics("Mq", fontContext);
		
		double x = ((double)m.getBufferWidth() * (charBound.getWidth() + 1));
		// Get the full line height for all rows, (ascent + descent) then add
		// up the leading line space (between lines), which is always one line
		// less than the full number of lines.
		double y = ((double)((m.getBufferHeight() * (lineMetrics.getAscent() + lineMetrics.getDescent())) +
		           ((m.getBufferHeight() - 1) * (lineMetrics.getLeading()))));
		
		Dimension d = new Dimension();
		d.setSize(x, y);
		return d;
	}
	
	/**
	 * Overrides the parent class setFont to set the fontContext to approximate
	 * the proper FontRenderContext until the next paint() invocation.
	 */
	public void setFont(Font f) {
		super.setFont(f);
		fontContext = new FontRenderContext(new AffineTransform(), true, true);
	}
}