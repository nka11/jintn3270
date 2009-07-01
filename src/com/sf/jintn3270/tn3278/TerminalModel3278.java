package com.sf.jintn3270.tn3278;

import com.sf.jintn3270.TerminalModel;
import com.sf.jintn3270.telnet.Option;

public class TerminalModel3278 extends TerminalModel {
	TermType3278 model;
	Option[] opts;
	
	public TerminalModel3278(TermType3278 type) {
		super(type.rows(), type.cols(), new EbcdicCharacterFactory());
		this.model = type;
		opts = new Option[1];
		opts[0] = new Tn3270e();
	}
	
	
	public String[] getModelName() {
		return model.terminalName();
	}
	
	
	public Option[] getRequiredOptions() {
		return opts;
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
