package com.sf.jintn3270.tn3270;

import com.sf.jintn3270.CharacterFactory;
import com.sf.jintn3270.TerminalCharacter;

public class EbcdicCharacterFactory implements CharacterFactory {
	public EbcdicCharacterFactory() {}
	
	public TerminalCharacter get(short code) {
		return new TNCharacter(code);
	}
	
	public TerminalCharacter get(char c) {
		return new TNCharacter(c);
	}
}
