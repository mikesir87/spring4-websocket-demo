package demo.spring.service;

import demo.spring.NameLookup;

public interface NameService {

	public static final String NAME_POSTED_TOPIC = "/topic/names.posted";
	
	NameLookup getName(String firstName, String lastName);
	
}
