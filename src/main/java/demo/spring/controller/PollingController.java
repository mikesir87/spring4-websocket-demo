package demo.spring.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import demo.spring.WebClientNameListener;
import demo.spring.WebClientNameListener.WriteMethod;
import demo.spring.service.NameListeningService;
import demo.spring.service.NameListener;

@Controller
public class PollingController {

	private NameListener listener;
	private NameListeningService listeningService;

	@ResponseBody
	@RequestMapping(value = "/longPoll", method = RequestMethod.GET)
	public DeferredResult<String> longPoll() {
		final DeferredResult<String> result = new DeferredResult<String>();

		listener = new WebClientNameListener(new WriteMethod() {
			public void write(String data) throws IOException {
				result.setResult(data);
				listeningService.removeListener(listener);
			}
		});
		
		listeningService.addListener(listener);
		return result;
	}
	
	@Autowired
	public void setListeningService(NameListeningService listeningService) {
		this.listeningService = listeningService;
	}
	
}
