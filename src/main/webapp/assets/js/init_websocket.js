
$(function() {
	var log = $("#log ul");
	
	var notificationCenter = new NotificationCenter();
	notificationCenter.addEventHandler(new NameSearchedEventHandler());
	notificationCenter.addEventHandler(new PostResponseEventHandler());

	var basePath = window.location.href;
	basePath = basePath.substring(0, basePath.search(/websocket.html/));
	var base = basePath.replace(/http[s]?\:\/\//, "ws://");
	var url = base + "/ws"; // Has to be in ws://localhost:8080/... format

	var ws = new WebSocket(url);
	ws.onopen = function() {
		log.append("<li>WebSocket opened</li>");
	};
	ws.onmessage = function(evt) {
		var data = $.parseJSON(evt.data);
		notificationCenter.handleMessage(data); 
	};
	ws.onclose = function() {
		log.append("<li>WebSocket closed</li>");
	}

	$("form").submit(function() {
		var firstName = $("input[name='firstName']").val();
		var lastName = $("input[name='lastName']").val();
		$.ajax({
			url: "name",
			data: "firstName=" + firstName + "&lastName=" + lastName,
			type: "POST",
			dataType: "json",
			success: function(data) {
				notificationCenter.handleMessage(data);
			}
		});
		return false;
	});
	
});
