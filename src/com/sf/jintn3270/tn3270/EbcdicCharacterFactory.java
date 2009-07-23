package com.sf.jintn3270.tn3270;

import com.sf.jintn3270.CharacterFactory;

public class EbcdicCharacterFactory implements CharacterFactory<TNCharacter> {
	public EbcdicCharacterFactory() {}
	
	public TNCharacter get(short code) {
		return new TNCharacter(code);
	}
	
	public TNCharacter get(char c) {
		return new TNCharacter(c);
	}
}
