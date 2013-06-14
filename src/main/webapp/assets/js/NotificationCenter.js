
function NotificationCenter() {
	this.handlers = Array();
};

NotificationCenter.prototype.addEventHandler = function(eventHandler) {
	this.handlers.push(eventHandler);
}

NotificationCenter.prototype.handleMessage = function(data) {
	for (var i = 0; i < this.handlers.length; i++) {
		var handler = this.handlers[i];
		if (handler.supports(data.type)) {
			handler.onMessageReceived(data.payload);
			return;
		}
	}
	console.log("No handler was found for event type " + data.type);
}
