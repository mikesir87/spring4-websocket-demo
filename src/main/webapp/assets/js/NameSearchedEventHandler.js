
function NameSearchedEventHandler() {
	this.log = jQuery("#log ul");
}

NameSearchedEventHandler.prototype = new EventHandler();
NameSearchedEventHandler.prototype.constructor = NameSearchedEventHandler;

NameSearchedEventHandler.prototype.supports = function(eventType) {
	return eventType == "nameSearched";
}

NameSearchedEventHandler.prototype.onMessageReceived = function(payload) {
	this.log.append("<li>" + payload + "</li>");
	this.log.scrollTop( this.log[0].scrollHeight );
}
