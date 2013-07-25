var ws;
var transport = 'websocket';
var setupTransport;

$(function() {
	
	var notificationCenter = new NotificationCenter();
	notificationCenter.addEventHandler(new NameSearchedEventHandler());
	notificationCenter.addEventHandler(new PostResponseEventHandler());
	
	var log = $("#log ul");

	var url = window.location.pathname.replace("sockjs.html", "sockjs/connector");
	
	function setupWs() {
		ws = new SockJS(url, undefined, {protocols_whitelist: [transport]});
		
		ws.onopen = function() {
			log.append("<li>SockJS initiated using [" + this.protocol + "]</li>");
		};
		ws.onmessage = function(evt) {
			var data = $.parseJSON(evt.data);
			notificationCenter.handleMessage(data); 
		};
		ws.onclose = function() {
			log.append("<li>SockJS connection closed</li>");
		};
	}
	
	setupTransport = function(newTransport) {
		transport = newTransport;
		if (ws != null && ws.readyState == 1) {
			ws.close();
		}
		setupWs();
	};
	
	setupWs();

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
