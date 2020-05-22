// join.js

function join(code, name) {
	location.href = location.origin + "/play?code=" + code + "&name=" + encodeURI(name);
}