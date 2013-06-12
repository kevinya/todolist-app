package fr.kevinya.todolistapp.service;


public interface JsonParser {
	public Object convert(String json);
	public String convert(Object task);
}
