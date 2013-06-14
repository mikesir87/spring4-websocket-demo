package demo.spring.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import demo.spring.service.NameService;

@Controller
public class NameController {

	private NameService nameService;
	
	@ResponseBody
	@RequestMapping(value = "/name", method = RequestMethod.POST)
	public Map<String, String> getName(
			@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName) {
		
		Map<String, String> response = new HashMap<String, String>();
		response.put("type", "response");
		response.put("payload", nameService.getName(firstName, lastName));
		return response;
	}

	@Autowired
	public void setNameService(NameService nameService) {
		this.nameService = nameService;
	}
	
}
