package demo.spring.sockjs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.TextWebSocketHandlerAdapter;

import demo.spring.WebClientNameListener;
import demo.spring.WebClientNameListener.WriteMethod;
import demo.spring.service.NameListeningService;
import demo.spring.service.NameListener;

/**
 * Echo messages by implementing a Spring {@link WebSocketHandler} abstraction.
 */
public class SockJsHandler extends TextWebSocketHandlerAdapter {

	private NameListeningService listeningService;
	private NameListener listener;
	private ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public void afterConnectionEstablished(final WebSocketSession session)
			throws Exception {
		super.afterConnectionEstablished(session);
		listener = new WebClientNameListener(new WriteMethod() {
			public void write(String data) throws IOException {
				try {
					session.sendMessage(new TextMessage(data));
				} catch (IllegalArgumentException e) {
					System.out.println("Have a listener that is expired now");
					listeningService.removeListener(listener);
				}
			}
		});
		listeningService.addListener(listener);
	}
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		Map<String, String> response = new HashMap<String, String>();
		response.put("type", "response");
		response.put("payload", "You don't get a name!");
		
		session.sendMessage(new TextMessage(mapper.writeValueAsString(response)));
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		listeningService.removeListener(listener);
		session.close(CloseStatus.SERVER_ERROR);
	}

	@Autowired
	public void setListeningService(NameListeningService listeningService) {
		this.listeningService = listeningService;
	}
}
