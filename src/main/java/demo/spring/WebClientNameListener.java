package demo.spring;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import demo.spring.service.NameListener;

public class WebClientNameListener implements NameListener {

	private ObjectMapper mapper = new ObjectMapper();
	private final WriteMethod writeMethod;

	public interface WriteMethod {
		void write(String data) throws IOException;
	}
	
	public WebClientNameListener(WriteMethod writeMethod) {
		this.writeMethod = writeMethod;
	}

	public void newNameGiven(String generated, String firstName,
			String lastName) {
		Map<String, String> data = new HashMap<String, String>();
		data.put("type", "nameSearched");
		data.put("payload", "Generated " + generated + 
				" for " + firstName + " " + lastName);
		try {
			String jsonPayload = mapper.writeValueAsString(data);
			writeMethod.write(jsonPayload);
		} catch (IOException e) {
			
		}
	}
	
}