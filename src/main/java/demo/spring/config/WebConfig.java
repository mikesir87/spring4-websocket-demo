package demo.spring.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.sockjs.SockJsService;
import org.springframework.web.socket.sockjs.support.DefaultSockJsService;
import org.springframework.web.socket.sockjs.support.SockJsHttpRequestHandler;
import org.springframework.web.socket.support.PerConnectionWebSocketHandler;

import demo.spring.sockjs.SockJsHandler;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "demo.spring.controller" })
public class WebConfig extends WebMvcConfigurerAdapter {

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
		
		Map<String, Object> urlMap = new HashMap<String, Object>();
		urlMap.put("/sockjs/connector/**", new SockJsHttpRequestHandler(
				sockJsService, echoWebSocketHandler()));

		SimpleUrlHandlerMapping hm = new SimpleUrlHandlerMapping();
		hm.setOrder(2);
		hm.setUrlMap(urlMap);

		return hm;
	}

	@Bean
	public WebSocketHandler echoWebSocketHandler() {
		return new PerConnectionWebSocketHandler(SockJsHandler.class);
	}

	@Bean
	public ThreadPoolTaskScheduler sockJsTaskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setThreadNamePrefix("SockJS-");
		return taskScheduler;
	}
	
	
}
