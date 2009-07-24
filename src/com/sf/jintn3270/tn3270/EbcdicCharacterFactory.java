package com.sf.jintn3270.tn3270;

import com.sf.jintn3270.CharacterFactory;

public class EbcdicCharacterFactory implements CharacterFactory<TNCharacter> {
	TNCharacter[] ebcdic = new TNCharacter[256];
	TNCharacter[] ascii = new TNCharacter[256];
	
	public EbcdicCharacterFactory() {
		for (short i = 0; i < ebcdic.length; i++) {
			ebcdic[i] = new TNCharacter(i);
		}
		
		for (int i = 0; i < ascii.length; i++) {
			ascii[i] = new TNCharacter((char)i);
		}
	}
	
	public TNCharacter get(short code) {
		return ebcdic[code];
	}
	
	public TNCharacter get(char c) {
		return ascii[(int)c];
	}
}
