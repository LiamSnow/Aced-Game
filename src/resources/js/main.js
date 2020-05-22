function isValidGameCode(code) {
	return true;
}

function getURLParameter(name) {
	return getParameter(name, location.search);
}

function getParameter(name, data) {
	name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
	var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
	var results = regex.exec(data);
	return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
}