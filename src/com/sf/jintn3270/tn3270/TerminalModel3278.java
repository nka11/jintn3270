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
	
	public TerminalModel3278(TermType3278 type) {
		super(type.rows(), type.cols(), new DefaultCharacterFactory());
		this.model = type;
		dataStream = new ByteArrayOutputStream();
		
		opts = new Option[5];
		opts[0] = new Binary(dataStream);
		opts[1] = new TerminalType();
		opts[2] = new SuppressGA();
		opts[3] = new EndOfRecord();
		opts[4] = new Regime3270((EndOfRecord)opts[3], (Binary)opts[0]);
//		opts[5] = new Tn3270e();
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
