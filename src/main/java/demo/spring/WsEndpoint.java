package demo.spring;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.server.endpoint.SpringConfigurator;

import demo.spring.WebClientNameListener.WriteMethod;
import demo.spring.service.NameListeningService;
import demo.spring.service.NameListener;

@ServerEndpoint(value = "/ws", configurator = SpringConfigurator.class)
public class WsEndpoint {

	private NameListeningService listeningService;
	private NameListener listener;
	ObjectMapper mapper = new ObjectMapper();
	
	@OnOpen
	public void onOpen(final Session session) {
		WriteMethod writeMethod = new WriteMethod() {
			public void write(String data) throws IOException {
				session.getAsyncRemote().sendText(data);
			}
		};
		
		listener = new WebClientNameListener(writeMethod);
		listeningService.addListener(listener);
	}
	
	@OnClose
	public void onClose() {
		listeningService.removeListener(listener);
	}
	
	@OnMessage
	public void onMessage(Session session, String message) {
		// Fired when a message is sent by the browser
	}
	
	@Autowired
	public void setListeningService(NameListeningService listeningService) {
		this.listeningService = listeningService;
	}
	
}
