package demo.spring.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class NinjaNameService extends AbstractNameService {

	private Map<String, String> sounds;

	public NinjaNameService() {
		sounds = new HashMap<String, String>();
		populateSounds();
	}

	public String doGetName(String firstName, String lastName) {
		StringBuilder sb = new StringBuilder();
		String adjFirstName = firstName.toUpperCase();

		for (int i = 0; i < adjFirstName.length(); i++) {
			sb.append(sounds.get(String.valueOf(adjFirstName.charAt(i))));
		}
		String name = sb.toString();
		String finalName = name.substring(0, 1).toUpperCase() + name.substring(1);
		return finalName;
	}
	
	private void populateSounds() {
		sounds.put("A", "ka");
		sounds.put("B", "tu");
		sounds.put("C", "mi");
		sounds.put("D", "te");
		sounds.put("E", "ku");
		sounds.put("F", "lu");
		sounds.put("G", "ji");
		sounds.put("H", "ri");
		sounds.put("I", "ki");
		sounds.put("J", "zu");
		sounds.put("K", "me");
		sounds.put("L", "ta");
		sounds.put("M", "rin");
		sounds.put("N", "to");
		sounds.put("O", "mo");
		sounds.put("P", "no");
		sounds.put("Q", "ke");
		sounds.put("R", "shi");
		sounds.put("S", "ari");
		sounds.put("T", "chi");
		sounds.put("U", "do");
		sounds.put("V", "ru");
		sounds.put("W", "mei");
		sounds.put("X", "na");
		sounds.put("Y", "fu");
		sounds.put("Z", "zi");
	}
}
