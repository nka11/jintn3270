package com.sf.jintn3270.tn3270;

import com.sf.jintn3270.TerminalModel;
import com.sf.jintn3270.telnet.Option;

import com.sf.jintn3270.CursorPosition;
import com.sf.jintn3270.DefaultCharacterFactory;
import com.sf.jintn3270.TerminalCharacter;

import com.sf.jintn3270.telnet.*;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class TerminalModel3278 extends TerminalModel {
	TermType3278 model;
	Option[] opts;
	
	StreamDecoder decoder;
	
	HashMap<Integer, Partition> partitions;
	int activePartition;
	
	/**
	 * Creates a new TerminalModel3278 with the proper default screen size,
	 * required Telnet Options, character factory, and 3270 parser.
	 */
	public TerminalModel3278(TermType3278 type) {
		super(type.rows(), type.cols(), new DefaultCharacterFactory());
		this.model = type;
		
		decoder = new StreamDecoder(this);
		
		Binary binary = new Binary(decoder);
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
	
	
	public void initializeBuffer(int rows, int cols) {
		partitions = new HashMap<Integer, Partition>();
		addPartition(new Partition(getBufferHeight(), getBufferWidth()));
		activePartition = 0;
	}
	
	public void initializeCursor(int rows, int cols) {
		// No-OP. creating a partition intialized the cursor for that partition.
	}
	
	void addPartition(Partition p) {
		partitions.put(p.getPid(), p);
	}
	
	void setActivePartition(int pid) {
		activePartition = pid;
	}
	
	Partition getActivePartition() {
		return partitions.get(activePartition);
	}
	
	/**
	 * Get the CursorPosition of the active partition.
	 */
	public CursorPosition cursor() {
		return getActivePartition().cursor();
	}
	
	/**
	 * Get the buffer for display.
	 */
	public TerminalCharacter[][] buffer() {
		if (partitions.size() == 1 && 
		    activePartition == 0)
		{
			System.out.println("Returning partition 0 buffer");
			return getActivePartition().getVisibleContentBuffer();
		}
		
		System.out.println("Aggregating partition buffers.");
		TerminalCharacter[][] buffer = new TerminalCharacter[getBufferHeight()][getBufferWidth()];
		// Fill the buffer with NULL.
		byte b = 0;
		for (int row = 0; row < buffer.length; row++) {
			for (int col = 0; col < buffer[row].length; col++) {
				buffer[row][col] = characterFactory().get(b);
			}
		}
		
		for (Partition p : partitions.values()) {
			Window w = p.getWindow();
			Viewport v = p.getViewport();
			int row = v.getRow();
			for (int r = w.top(); r < w.bottom(); r++) {
				// Copy from the window source to the viewport source 
				//   up to the smallest size (viewport or window)
				System.arraycopy(p.getBuffer()[r], w.left(),
				                 buffer[row], v.getCol(), 
							  v.getWidth() < w.getWidth() ? v.getWidth() : w.getWidth());
				row++;
			}
		}
		
		return buffer;
	}
	
	
	protected void print(TerminalCharacter ch) {
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
