package com.sf.jintn3270;

public interface CharacterFactory {
	public TerminalCharacter get(byte code);
	
	public TerminalCharacter get(char c);
}
