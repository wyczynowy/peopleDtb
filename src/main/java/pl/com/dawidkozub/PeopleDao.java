package pl.com.dawidkozub;

import java.util.HashMap;
import java.util.Map;

public enum PeopleDao {
	instance;

	private Map<String, Person> contentProvider = new HashMap<>();

	private PeopleDao() {

		Person person = new Person("1", "Michael", "Jackson");
		contentProvider.put("1", person);
		person = new Person("2", "Michael", "Buble");
		contentProvider.put("2", person);

	}

	public Map<String, Person> getModel() {
		return contentProvider;
	}

}
