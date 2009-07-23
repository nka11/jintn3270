package com.sf.jintn3270;

public interface CharacterFactory<T extends TerminalCharacter> {
	public T get(short code);
	
	public T get(char c);
}
