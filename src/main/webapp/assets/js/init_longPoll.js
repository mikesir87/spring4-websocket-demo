
function LongPoller(notificationCenter, log) {
	this.notificationCenter = notificationCenter;
	this.log = log;
};

LongPoller.prototype.sendPoll = function() {
	var poller = this;
	this.log.append("<li>Sending long poll</li>");
	$.ajax({
		url: "longPoll?t=" + (new Date()).getTime(),
		dataType: "json",
		success: function(data) {
			poller.onMessageReceived(data);
		},
		error: function() {
			poller.sendPoll();
		}
	});
}

LongPoller.prototype.onMessageReceived = function(responseData) {
	this.log.append("<li>Received long poll response</li>");
	this.sendPoll();
	this.notificationCenter.handleMessage(responseData);
}


$(function() {
	var log = $("#log ul");
	
	var notificationCenter = new NotificationCenter();
	notificationCenter.addEventHandler(new NameSearchedEventHandler());
	notificationCenter.addEventHandler(new PostResponseEventHandler());

	var longPoller = new LongPoller(notificationCenter, log);
	longPoller.sendPoll();
	
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
