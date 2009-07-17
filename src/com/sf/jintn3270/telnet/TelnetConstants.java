package com.sf.jintn3270.telnet;

public interface TelnetConstants {
	/* Constants as defined by RFC 854 */ 
	
	public static final byte IAC = (byte)255;
	
	public static final byte DONT = (byte)254;
	public static final byte DO = (byte)253;
	public static final byte WONT = (byte)252;
	public static final byte WILL = (byte)251;
	
	
	public static final byte NOP = (byte)241; // No Op!
	public static final byte DM = (byte)242; // Data Mark... data stream portion of Sync
	
	public static final byte BRK = (byte)243; // Break
	public static final byte IP = (byte)244; // Interrupt Process
	public static final byte AO = (byte)245; // Abort Output
	public static final byte AYT = (byte)246; // Are you there
	public static final byte EC = (byte)247; // Erase Character
	public static final byte EL = (byte)248; // Erase Line
	public static final byte GA = (byte)249; // Go Ahead
	public static final byte SB = (byte)250; // Start Subcommand
	public static final byte SE = (byte)240; // End Subcommand
}
