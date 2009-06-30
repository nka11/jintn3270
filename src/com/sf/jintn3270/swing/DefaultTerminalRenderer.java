package com.sf.jintn3270.swing;

import java.awt.Color;
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

import javax.swing.JComponent;

import com.sf.jintn3270.TerminalModel;


public class DefaultTerminalRenderer extends JComponent implements TerminalRenderer {
	FontRenderContext fontContext;
	RenderingHints renderHints;
	
	boolean scaleFont;
	boolean stretchFont;
	
	public DefaultTerminalRenderer() {
		super();
		setFont(Font.decode("Monospaced-12"));
		renderHints = new RenderingHints(null);
		renderHints.put(RenderingHints.KEY_FRACTIONALMETRICS,
		                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		renderHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
		                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		scaleFont = true;
		stretchFont = true;
	}
	
	
	/**
	 * Paint the component to the given Graphics context!
	 */
	public void paint(Graphics g, TerminalModel m) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setFont(calculateFont(g2d, m));
		g2d.setRenderingHints(renderHints);
		Rectangle2D charBound = g2d.getFontMetrics().getStringBounds("M", g2d);
		LineMetrics lineMetrics = g2d.getFontMetrics().getLineMetrics("Mq", g2d);
		
		g2d.setBackground(Color.BLACK);
		g2d.setColor(Color.WHITE);
		g2d.clearRect(0, 0, g2d.getClipBounds().width, g2d.getClipBounds().height);
		
		Point2D p = new Point2D.Float();
		for (int line = 0; line < m.getBufferHeight(); line++) {
			// TODO: Use AttributedCharacterIterator to read/render the column.
			for (int col = 0; col < m.getBufferWidth(); col++) {
				p.setLocation(col * (charBound.getWidth() + 1),
				              (lineMetrics.getAscent() * (line + 1)) + 
					(line * (lineMetrics.getDescent() + lineMetrics.getLeading())));
				
				g2d.drawString("" + m.getChar(line, col).getDisplay(), (float)p.getX(), (float)p.getY());
			}
		}
		
		// Draw an underscore cursor
		p.setLocation(m.cursor().column() * (charBound.getWidth() + 1),
				(lineMetrics.getAscent() * (m.cursor().row() + 1)) + 
				(m.cursor().row() * (lineMetrics.getDescent() + lineMetrics.getLeading())));
		Rectangle2D cursorRect = new Rectangle2D.Double(p.getX(), p.getY(), charBound.getWidth(), 2);
		g2d.fill(cursorRect);
	}
	
	/**
	 * Implements TerminalRenderer
	 */
	public Dimension getPreferredSize(TerminalModel m) {
		if (m == null) {
			return new Dimension(200, 150);
		} else {
			return getBufferSize(m, getFont().deriveFont(12.0f));
		}
	}
	
	/**
	 * Implements TerminalRenderer.
	 */
	public Dimension getMinimumSize(TerminalModel m) {
		if (m == null) {
			return new Dimension(200, 150);
		} else {
			return getBufferSize(m, getFont().deriveFont(6.0f));
		}
	}
	
	/**
	 * Calculate the ideal Font based on the current scaling policy, the 
	 * graphics context to render to, and the the TerminalModel to render.
	 *
	 * @param g2d The Graphics object we're about to render into.
	 * @param m The TerminalModel to render.
	 * @return the Font to use when doing the rendering.
	 */
	protected Font calculateFont(Graphics2D g2d, TerminalModel m) {
		// Fall back to the currently set font on this Component.
		Font ret = getFont();
		
		// If we have a clip && we're supposed to modify the font...
		Rectangle bounds = g2d.getClipBounds();
		if (bounds != null && (scaleFont || stretchFont)) {
			// Given the height / width of the buffer, set the font size.
			Rectangle2D.Double idealCharSize = 
					new Rectangle2D.Double(0d, 0d, 
							(bounds.getWidth() / m.getBufferWidth()) - 1,
							(bounds.getHeight() / m.getBufferHeight()));
			// Derive a scaled font instance
			if (scaleFont) {
				Font reference = Font.decode(getFont().getFontName() + "-" + getStyle(getFont()) + "-12").deriveFont(new AffineTransform());
				
				
				Rectangle2D charSize = g2d.getFontMetrics(reference).getStringBounds("M", g2d);
				
				float targetPt = (float)(idealCharSize.getWidth() / charSize.getWidth());
				targetPt = (float)Math.round(targetPt * reference.getSize2D() * 10) / 10.0f;
				ret = reference.deriveFont(targetPt);
			}
			
			// Derive a stretched font instance.
			if (stretchFont) {
				double scaley = idealCharSize.getHeight() / g2d.getFontMetrics(ret).getLineMetrics("Mq", g2d).getHeight();
				AffineTransform scaleTransform = new AffineTransform();
				scaleTransform.setToScale(1.0, scaley);
				ret = ret.deriveFont(scaleTransform);
			}
		}
		return ret;
	}
	
	
	/**
	 * Used to derive fonts by font-name so that we scale from a set 
	 * starting point rather than scaling already scaled fonts.
	 */
	private String getStyle(Font f) {
		if (f.getStyle() == Font.PLAIN) {
			return "plain";
		} else if (f.getStyle() == Font.BOLD) {
			return "bold";
		} else if (f.getStyle() == Font.ITALIC) {
			return "italic";
		} else {
			return "bolditalic";
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