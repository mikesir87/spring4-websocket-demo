var ws, stompClient;
var transport = 'websocket';
var setupTransport;

$(function() {
	
	var log = $("#log ul");

	var url = (window.location.pathname.indexOf("index.html") == -1) ?
			window.location.pathname + "/name" :
			window.location.pathname.replace("index.html", "name");
	
	function setupWs() {
		ws = new SockJS(url);
		stompClient = Stomp.over(ws);
		stompClient.connect('', '', function(frame) {
			log.append("<li>STOMP connection established</li>");
			stompClient.subscribe('/topic/names.posted.*', function(message) {
				var details = JSON.parse(message.body);
				log.append("<li>" + details.firstName + " " + details.lastName 
						+ " has a " + details.nameType + " name of " 
						+ details.newName + "</li>");
			});
		}, function(error) {
			log.append("<li>STOMP ERROR: " + error + "</li>");
		});
	}
		
	setupWs();

	$("form").submit(function() {
		var request = {
			firstName: $("input[name='firstName']").val(),
			lastName: $("input[name='lastName']").val()
		};
		stompClient.send('/app/getName', {}, JSON.stringify(request));
		return false;
	});
});
