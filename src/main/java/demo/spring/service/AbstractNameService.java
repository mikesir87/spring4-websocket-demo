package demo.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;

import demo.spring.NameLookup;


public abstract class AbstractNameService implements NameService {

	private MessageSendingOperations<String> messagingTemplate;
	
	public abstract String getNameType();
	
	public final NameLookup getName(String firstName, String lastName) {
		String name = doGetName(firstName, lastName);
		NameLookup lookedUpName = new NameLookup(firstName, lastName, getNameType(), name);
		messagingTemplate.convertAndSend(getTopic(), lookedUpName);
		return lookedUpName;
	}
	
	protected abstract String doGetName(String firstName, String lastName);
	
	private String getTopic() {
		return NameService.NAME_POSTED_TOPIC + "." + getNameType();
	}
	
	@Autowired
	public void setMessagingTemplate(
			 MessageSendingOperations<String> messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}
	
}
