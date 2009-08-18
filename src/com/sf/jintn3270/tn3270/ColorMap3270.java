package com.sf.jintn3270.tn3270;

import com.sf.jintn3270.swing.ColorMap;

import java.awt.Color;

public class ColorMap3270 implements ColorMap {
	public static final Color NEUTRAL_FOREGROUND = Color.decode("#55FFFF");
	public static final Color NEUTRAL_BACKGROUND = Color.BLACK;
	
	public Color getColor(int c) {
		switch (c) {
			case 0xf0:
				return NEUTRAL_BACKGROUND; // Black for displays, White for printer.
			case 0xf1:
				return Color.BLUE;
			case 0xf2:
				return Color.RED;
			case 0xf3:
				return Color.PINK;
			case 0xf4:
				return Color.GREEN;
			case 0xf5: // Turquoise
				return Color.decode("#00FFFF");
			case 0xf6:
				return Color.YELLOW;
			case 0xf7:
				return NEUTRAL_FOREGROUND; // White for Display, Black for Printer.
			case 0xf8:
				return Color.BLACK;
			case 0xf9: // Deep Blue
				return Color.decode("#0000A0");
			case 0xfa:
				return Color.ORANGE;
			case 0xfb: // Purple
				return Color.decode("#800080");
			case 0xfc: // Pale Green
				return Color.decode("#A4FFA4");
			case 0xfd: // Pale Turquoise
				return Color.decode("#A4FFFF");
			case 0xfe: 
				return Color.GRAY;
			case 0xff:
				return Color.WHITE;
		}
		return null;
	}
}