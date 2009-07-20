package com.sf.jintn3270;

public interface CharacterFactory {
	public TerminalCharacter get(short code);
	
	public TerminalCharacter get(char c);
}
