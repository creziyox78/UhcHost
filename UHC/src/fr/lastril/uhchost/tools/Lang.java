package fr.lastril.uhchost.tools;

import java.util.HashMap;

public enum Lang {
	
	FRENCH(0, "Fran�ais", "FR"), ENGLISH(1, "Anglais", "EN"), SPAINISH(2, "Espagnol", "SP");

	private int id;

	private String name;

	private String code;

	private HashMap<String, String> words;

	Lang(int id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.words = new HashMap<>();
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public HashMap<String, String> getWords() {
		return this.words;
	}

	public void setWords(HashMap<String, String> words) {
		this.words = words;
	}
}
