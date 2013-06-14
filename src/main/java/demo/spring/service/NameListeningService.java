package demo.spring.service;

public interface NameListeningService {

	void addListener(NameListener listener);
	
	void removeListener(NameListener listener);
	
}
