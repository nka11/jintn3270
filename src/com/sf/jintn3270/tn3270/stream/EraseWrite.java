package com.sf.jintn3270.tn3270.stream;

import com.sf.jintn3270.tn3270.TerminalModel3278;
import com.sf.jintn3270.tn3270.Window;

/**
 * Erase/Write 3270 Command
 */
public class EraseWrite extends Command {
	public EraseWrite() {
		super((short)0xf5);
	}
	
	/**
	 * Details provided by 3.5.2 of IBM Document number GA23-0059-07, 
	 * 3270 Data Stream Programmer's Reference.
	 */
	public int preform(TerminalModel3278 model, short[] b, int off, int len) {
		int nextByte = off;
		// Read the WCC!
		WriteControlCharacter wcc = null;
		try {
			wcc = new WriteControlCharacter(b[nextByte++]);
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			wcc = null;
		}
		
		if (wcc != null) {
			if (wcc.reset()) {
				// TODO: Reset the inbound reply mode to Field.
				if (model.getActivePartition().isImplicit()) {
					model.initializeBuffer();
					//TODO: Set INOP to ReadModified
				}
			} else if (model.getActivePartition().isImplicit()) {
				model.initializeBuffer();
			} else {
				model.getActivePartition().erase(model);
				
				// TODO: make sure the above sets all extends field and character attributes to 0x00.
				
				// TODO: Erase all field validation attributes.
				
				// Position cursor and auto-scroll.
				model.getActivePartition().cursor().setPosition(0);
				Window w = model.getActivePartition().getWindow();
				if (w.top() > 0 || w.left() > 0) {
					w.scroll(0 - w.top(), 0 - w.left());
				}
			}
			
			if (wcc.restoreKeyboard()) {
				//TODO: acknowledge outstanding read or enter.
			}
			
			nextByte += new Write().preform(model, b, nextByte, len - (nextByte - off));
		}
		
		
		// TODO: Reset Program Check Indication
		
		// TODO: Provide negative trigger reply.
		
		
		// How many bytes did we read?
		return nextByte - off;
	}
}
