
function PostResponseEventHandler() {
	this.name = $("#name h3");
}

PostResponseEventHandler.prototype = new EventHandler();
PostResponseEventHandler.prototype.constructor = PostResponseEventHandler;

PostResponseEventHandler.prototype.supports = function(eventType) {
	return eventType == "response";
}

PostResponseEventHandler.prototype.onMessageReceived = function(payload) {
	this.name.text(payload);
}
