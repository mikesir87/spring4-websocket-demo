package demo.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.ReplyToUser;
import org.springframework.stereotype.Controller;

import demo.spring.NameLookup;
import demo.spring.NameRequest;
import demo.spring.service.NameService;

@Controller
public class NameController {

	private NameService nameService;
	
	@MessageMapping("/app/getName")
	@ReplyToUser("/queue/names.response")
	public NameLookup getName(NameRequest nameRequest) {
		return nameService.getName(nameRequest.getFirstName(), 
				nameRequest.getLastName());
	}

	@Autowired
	public void setNameService(NameService nameService) {
		this.nameService = nameService;
	}
	
}
