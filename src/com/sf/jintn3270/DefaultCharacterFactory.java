package com.sf.jintn3270;

public class DefaultCharacterFactory implements CharacterFactory {
	private TerminalCharacter[] chars = new TerminalCharacter[256];
	
	public DefaultCharacterFactory() {
		for (int i = 0; i < chars.length; i++) {
			chars[i] = new DefaultTerminalCharacter(i);
		}
		chars[0] = new DefaultTerminalCharacter(0, ' ');
	}
	
	public TerminalCharacter get(byte code) {
		return get((int)code);
	}
	
	public TerminalCharacter get(char c) {
		return get((byte)c);
	}
	
	public TerminalCharacter get(int code) {
		if (code < 0) {
			code += 256;
		}
		return chars[code];
	}
	
	class DefaultTerminalCharacter extends TerminalCharacter {
		DefaultTerminalCharacter(int code) {
			super((byte)code, (char)code);
		}
		
		DefaultTerminalCharacter(int code, char c) {
			super((byte)code, c);
		}
	}
}
