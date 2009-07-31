package com.sf.jintn3270.tn3270;

import com.sf.jintn3270.TerminalModel;
import com.sf.jintn3270.telnet.Option;

import com.sf.jintn3270.CursorPosition;
import com.sf.jintn3270.TerminalCharacter;

import com.sf.jintn3270.telnet.*;
import com.sf.jintn3270.event.TerminalEvent;
import com.sf.jintn3270.tn3270.stream.Command;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * TerminalModel3278 - A TN3270e TerminalModel.
 * 
 * <p>This TerminalModel subclass encapsulates the necessary classes and 
 * and functions required to implement a 3270e compatible terminal. The model
 * expresses serveral Telnet Options as "Required", which really just means
 * that those options are available as dependencies, and that the TelnetClient
 * will be instructed to initiate negotiation.
 * <ul><li>Suppress Go Ahead</li>
 *     <li>Tn3270e</li>
 *     <li>Regime3270</li>
 *     <li>TerminalType</li>
 *     <li>Binary (for receiving the 3270 data stream)</li>
 *     <li>End Of Record</li>
 * </ul>
 * 
 * <p>As per Chapter 2 of the 3270 Data Stream Programmer's Reference,
 * this TerminalModel handles implicit and explicit Partitions. The initial 
 * state of the terminal is a single implicit partition with a PID of 0 (zero).
 * <p>
 * 
 */
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
		super(type.rows(), type.cols(), new EbcdicCharacterFactory());
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
		addPartition(new Partition(this));
		activePartition = 0;
	}
	
	public void initializeBuffer() {
		initializeBuffer(model.rows(), model.cols());
	}
	
	public void initializeCursor(int rows, int cols) {
		// No-OP. creating a partition intializes the cursor for that partition.
	}
	
	
	public void addPartition(Partition p) {
		partitions.put(p.getPid(), p);
	}
	
	public void removePartition(int pid) {
		partitions.remove(pid);
	}
	
	
	public void setActivePartition(int pid) {
		activePartition = pid;
	}
	
	public Partition getActivePartition() {
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
	
	/**
	 * Overrides the default behavior and provides specific behavior for tn3270.
	 */
	protected void print(TerminalCharacter ch) {
		getActivePartition().print(ch);
	}
	
	/**
	 * Prints the given byte
	 */
	public void print(short b) {
		print(characterFactory().get(b));
	}
	
	/**
	 * Prints the given array of bytes, starting at offset, up to length
	 */
	public void print(short[] bytes, int offset, int length) {
		for (int pos = offset; pos < (offset + length); pos++) {
			print(characterFactory().get(bytes[pos]));
		}
	}
	
	/**
	 * Prints the given array of bytes
	 */
	public void print(short[] bytes) {
		print(bytes, 0, bytes.length);
	}
	
	
	/**
	 * Implements printing of Field Starts.
	 */
	public void print(TNFieldCharacter fieldStart) {
		getActivePartition().print(fieldStart);
	}
	
	/**
	 * A write command has finished.
	 */
	public void complete(Command c) {
		fire(TerminalEvent.BUFFER_CHANGED);
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
