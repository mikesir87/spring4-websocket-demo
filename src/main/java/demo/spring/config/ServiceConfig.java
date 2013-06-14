package demo.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import demo.spring.service.NameService;
import demo.spring.service.NinjaNameService;

@Configuration
public class ServiceConfig {

	@Bean
	public NameService nameService() {
		return new NinjaNameService();
	}
	
}
