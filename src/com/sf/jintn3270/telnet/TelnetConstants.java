package com.sf.jintn3270.telnet;

public interface TelnetConstants {
	/* Constants as defined by RFC 854 */ 
	
	public static final short IAC = 255;
	
	public static final short DONT = 254;
	public static final short DO = 253;
	public static final short WONT = 252;
	public static final short WILL = 251;
	
	
	public static final short NOP = 241; // No Op!
	public static final short DM = 242; // Data Mark... data stream portion of Sync
	
	public static final short BRK = 243; // Break
	public static final short IP = 244; // Interrupt Process
	public static final short AO = 245; // Abort Output
	public static final short AYT = 246; // Are you there
	public static final short EC = 247; // Erase Character
	public static final short EL = 248; // Erase Line
	public static final short GA = 249; // Go Ahead
	public static final short SB = 250; // Start Subcommand
	public static final short SE = 240; // End Subcommand
}
