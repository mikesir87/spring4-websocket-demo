package demo.spring.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.handler.AnnotationMethodMessageHandler;
import org.springframework.messaging.simp.handler.SimpleBrokerMessageHandler;
import org.springframework.messaging.simp.handler.SimpleUserQueueSuffixResolver;
import org.springframework.messaging.simp.handler.UserDestinationMessageHandler;
import org.springframework.messaging.simp.stomp.StompWebSocketHandler;
import org.springframework.messaging.support.channel.ExecutorSubscribableChannel;
import org.springframework.messaging.support.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.support.converter.MessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.socket.sockjs.SockJsService;
import org.springframework.web.socket.sockjs.support.DefaultSockJsService;
import org.springframework.web.socket.sockjs.support.SockJsHttpRequestHandler;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "demo.spring.controller" })
public class WebConfig extends WebMvcConfigurerAdapter {

	private final MessageConverter<?> messageConverter = new MappingJackson2MessageConverter();

	private final SimpleUserQueueSuffixResolver userQueueSuffixResolver = new SimpleUserQueueSuffixResolver();

	@Autowired
	private ServiceConfig serviceConfig;

	// Allow serving HTML files through the default Servlet

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	
	/////////////////// LATER ////////////////////////
	
	@Bean
	public SimpleUrlHandlerMapping handlerMapping() {
		SockJsService sockJsService = new DefaultSockJsService(sockJsTaskScheduler());
		HttpRequestHandler requestHandler = new SockJsHttpRequestHandler(sockJsService, stompWebSocketHandler());

		SimpleUrlHandlerMapping hm = new SimpleUrlHandlerMapping();
		hm.setOrder(-1);
		hm.setUrlMap(Collections.singletonMap("/name/**", requestHandler));
		return hm;
	}

	// WebSocketHandler for STOMP messages

	@Bean
	public StompWebSocketHandler stompWebSocketHandler() {
		StompWebSocketHandler handler = new StompWebSocketHandler(dispatchChannel());
		handler.setUserQueueSuffixResolver(this.userQueueSuffixResolver);
		webSocketHandlerChannel().subscribe(handler);
		return handler;
	}

	@Bean
	public SubscribableChannel dispatchChannel() {
		return new ExecutorSubscribableChannel(asyncExecutor());
	}

	// Channel for sending STOMP messages to connected WebSocket sessions (mostly for internal use)

	@Bean
	public SubscribableChannel webSocketHandlerChannel() {
		return new ExecutorSubscribableChannel(asyncExecutor());
	}

	// Executor for message passing via MessageChannel

	@Bean
	public ThreadPoolTaskExecutor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(4);
		executor.setCorePoolSize(8);
		executor.setThreadNamePrefix("MessageChannel-");
		return executor;
	}


	@Bean
	public ThreadPoolTaskScheduler sockJsTaskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setThreadNamePrefix("SockJS-");
		return taskScheduler;
	}

	// MessagingTemplate (and MessageChannel) to dispatch messages to for further processing
	// All MessageHandler beans above subscribe to this channel

	@Bean
	public SimpMessageSendingOperations dispatchMessagingTemplate() {
		SimpMessagingTemplate template = new SimpMessagingTemplate(dispatchChannel());
		template.setMessageConverter(this.messageConverter);
		return template;
	}

	// MessageHandler for processing messages by delegating to @Controller annotated methods

	@Bean
	public AnnotationMethodMessageHandler annotationMessageHandler() {
		AnnotationMethodMessageHandler handler =
				new AnnotationMethodMessageHandler(dispatchMessagingTemplate(), 
						webSocketHandlerChannel());

		handler.setDestinationPrefixes(Arrays.asList("/app/"));
		handler.setMessageConverter(this.messageConverter);
		dispatchChannel().subscribe(handler);
		return handler;
	}

	// MessageHandler that resolves destinations prefixed with "/user/{user}"
	// See the Javadoc of UserDestinationMessageHandler for details

	@Bean
	public UserDestinationMessageHandler userMessageHandler() {
		UserDestinationMessageHandler handler = new UserDestinationMessageHandler(
				dispatchMessagingTemplate(), this.userQueueSuffixResolver);
		dispatchChannel().subscribe(handler);
		return handler;
	}

	// MessageHandler that acts as a "simple" message broker

	@Bean
	public SimpleBrokerMessageHandler simpleBrokerMessageHandler() {
		SimpleBrokerMessageHandler handler = new SimpleBrokerMessageHandler(webSocketHandlerChannel());
		handler.setDestinationPrefixes(Arrays.asList("/topic/", "/queue/"));
		dispatchChannel().subscribe(handler);
		return handler;
	}
		
}
