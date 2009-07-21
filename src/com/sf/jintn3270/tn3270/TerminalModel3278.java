package com.sf.jintn3270.tn3270;

import com.sf.jintn3270.TerminalModel;
import com.sf.jintn3270.telnet.Option;

import com.sf.jintn3270.DefaultCharacterFactory;
import com.sf.jintn3270.TerminalCharacter;

import com.sf.jintn3270.telnet.*;

import java.io.ByteArrayOutputStream;

public class TerminalModel3278 extends TerminalModel {
	TermType3278 model;
	Option[] opts;
	
	ByteArrayOutputStream dataStream;
	
	/**
	 * Creates a new TerminalModel3278 with the proper default screen size,
	 * required Telnet Options, character factory, and 3270 parser.
	 */
	public TerminalModel3278(TermType3278 type) {
		super(type.rows(), type.cols(), new DefaultCharacterFactory());
		this.model = type;
		dataStream = new ByteArrayOutputStream();
		
		Binary binary = new Binary(dataStream);
		EndOfRecord eor = new EndOfRecord();
		
		opts = new Option[6];
		opts[0] = new SuppressGA();
		opts[1] = new Tn3270e(eor, binary);
		opts[2] = new Regime3270(eor, binary);
		opts[3] = new TerminalType();
		opts[4] = binary;
		opts[5] = eor;
	}
	
	
	public String[] getModelName() {
		return model.terminalName();
	}
	
	
	public Option[] getRequiredOptions() {
		return opts;
	}
	
	
	public void print(TerminalCharacter ch) {
		super.print(ch);
		System.out.print(ch.getDisplay());
		System.out.flush();
	}
	
	
	/**
	 * Supported TerminalTypes & modes.
	 */
	public enum TermType3278 {
		MODEL2(24, 80, new String[] {"IBM-3278-2-E", "IBM-3278-2"}),
		MODEL3(32, 80, new String[] {"IBM-3278-3-E", "IBM-3278-3"}),
		MODEL4(43, 80, new String[] {"IBM-3278-4-E", "IBM-3278-4"}),
		MODEL5(27, 132, new String[] {"IBM-3278-5-E", "IBM-3278-5"});
		
		private final int rows;
		private final int cols;
		private final String[] termNames;
		
		TermType3278(int rows, int cols, String[] names) {
			this.rows = rows;
			this.cols = cols;
			this.termNames = names;
		}
		
		public int rows() { return rows; }
		public int cols() { return cols; }
		public String[] terminalName() { return termNames; }
	}
}
