// play.js

//TODO: header, leaderboard/podium, highlight winner
//TODO Java: kick afk players

var name = getURLParameter("name"), page, requestStartGame = false, submittedAnswer = "", submittedVote = 0;

function loadPage(data) {
	var html = "";
	if (page == "waiting_for_people") {
		html = `
			<div style="font-size: 40px;">Waiting for people to join...</div>
			<div style="font-size: 25px;" class="numpeople-in">NUMPEOPLE people in</div>
		`;
		html = html.replace(/NUMPEOPLE/g, getParameter("numpeople", data));
	}
	else if (page == "waiting_for_people_admin") {
		html = `
			<div style="font-size: 40px;">Waiting for people to join...</div>
			<div style="justify-content: center; display: flex">
				<button style="width: 180px" class="mdc-button mdc-button--raised" data-mdc-auto-init="MDCRipple" onclick="startGame()">
					<div class="mdc-button__ripple"></div>
					<span class="mdc-button__label">Start</span>
				</button>
			</div>
			<div style="font-size: 25px;" class="numpeople-in">NUMPEOPLE people in</div>
		`;
		html = html.replace(/NUMPEOPLE/g, getParameter("numpeople", data));
	}
	else if (page == "question") {
		html = `
			<question><div style="font-weight: 600">Question:</div>QUESTION</question>
			<label class="mdc-text-field mdc-text-field--outlined" data-mdc-auto-init="MDCTextField" id="answer">
				<input type="text" class="mdc-text-field__input" aria-labelledby="my-label-id" maxlength="250">
				<span class="mdc-notched-outline">
					<span class="mdc-notched-outline__leading"></span>
					<span class="mdc-notched-outline__notch">
						<span class="mdc-floating-label" id="my-label-id">Answer</span>
					</span>
					<span class="mdc-notched-outline__trailing"></span>
				</span>
			</label>
			<div style="justify-content: center; display: flex">
				<button style="width: 180px" class="mdc-button mdc-button--raised" data-mdc-auto-init="MDCRipple" onclick="submitAnswer(document.querySelector('#answer').MDCTextField.value)">
					<div class="mdc-button__ripple"></div>
					<span class="mdc-button__label">Submit</span>
				</button>
			</div>
			<time-left>TIME seconds left</time-left>
			<number-submitted>SUBMITTED submitted</number-submitted>
		`;
		html = html.replace(/QUESTION/g, getParameter("question", data));
		html = html.replace(/TIME/g, getParameter("time", data));
		html = html.replace(/SUBMITTED/g, getParameter("submitted", data));
	}
	else if (page == "waiting_for_answers") {
		html = `
			<div style="font-size: 40px;">Waiting for people to answer...</div>
			<time-left>TIME seconds left</time-left>
			<number-submitted>SUBMITTED submitted</number-submitted>
		`;
		html = html.replace(/TIME/g, getParameter("time", data));
		html = html.replace(/SUBMITTED/g, getParameter("submitted", data));
	}
	else if (page == "vote") {
		html = `
			<div style="font-size: 40px;">Pick the best response!</div>
			<question>
				<div style="font-weight: 600">Question:</div>
				QUESTION
			</question>
			<cards>
				<card onclick="submitVote(1)" class="mdc-elevation--z24 mdc-ripple-surface mdc-ripple-surface--accent" data-mdc-auto-init="MDCRipple"v>
					<card-answer>ANSWER1</card-answer>
					<card-creator>CREATOR1</card-creator>
				</card>
				<card onclick="submitVote(2)" class="mdc-elevation--z24 mdc-ripple-surface mdc-ripple-surface--accent" data-mdc-auto-init="MDCRipple">
					<card-answer>ANSWER2</card-answer>
					<card-creator>CREATOR2</card-creator>
				</card>
			</cards>
			<time-left>TIME seconds left</time-left>
			<number-submitted>SUBMITTED submitted</number-submitted>
		`;
		html = html.replace(/QUESTION/g, getParameter("question", data));
		html = html.replace(/TIME/g, getParameter("time", data));
		html = html.replace(/SUBMITTED/g, getParameter("submitted", data));
		html = html.replace(/ANSWER1/g, getParameter("answer1", data));
		html = html.replace(/ANSWER2/g, getParameter("answer2", data));
		html = html.replace(/CREATOR1/g, getParameter("creator1", data));
		html = html.replace(/CREATOR2/g, getParameter("creator2", data));
	}
	else if (page == "view_vote") {
		html = `
			<div style="font-size: 40px;">Waiting on responses...</div>
			<question>
				<div style="font-weight: 600">Question:</div>
				QUESTION
			</question>
			<cards>
				<card class="mdc-elevation--z24">
					<card-answer>ANSWER1</card-answer>
					<card-creator>CREATOR1</card-creator>
				</card>
				<card class="mdc-elevation--z24">
					<card-answer>ANSWER2</card-answer>
					<card-creator>CREATOR2</card-creator>
				</card>
			</cards>
			<time-left>TIME seconds left</time-left>
			<number-submitted>SUBMITTED submitted</number-submitted>
		`;
		html = html.replace(/QUESTION/g, getParameter("question", data));
		html = html.replace(/TIME/g, getParameter("time", data));
		html = html.replace(/SUBMITTED/g, getParameter("submitted", data));
		html = html.replace(/ANSWER1/g, getParameter("answer1", data));
		html = html.replace(/ANSWER2/g, getParameter("answer2", data));
		html = html.replace(/CREATOR1/g, getParameter("creator1", data));
		html = html.replace(/CREATOR2/g, getParameter("creator2", data));
	}
	else if (page == "show_winner") {
		html = `
			<div style="font-size: 40px;">Winner:</div>
			<question>
				<div style="font-weight: 600">Question:</div>
				QUESTION
			</question>
			<cards>
				<card class="mdc-elevation--z24 WINNER1">
					<card-answer>ANSWER1</card-answer>
					<card-creator>CREATOR1</card-creator>
					<card-votes>VOTES1 votes</card-votes>
				</card>
				<card class="mdc-elevation--z24 WINNER2">
					<card-answer>ANSWER2</card-answer>
					<card-creator>CREATOR2</card-creator>
					<card-votes>VOTES2 votes</card-votes>
				</card>
			</cards>
			<time-left>TIME seconds left</time-left>
		`;
		html = html.replace(/QUESTION/g, getParameter("question", data));
		html = html.replace(/TIME/g, getParameter("time", data));
		html = html.replace(/ANSWER1/g, getParameter("answer1", data));
		html = html.replace(/ANSWER2/g, getParameter("answer2", data));
		html = html.replace(/CREATOR1/g, getParameter("creator1", data));
		html = html.replace(/CREATOR2/g, getParameter("creator2", data));
		html = html.replace(/VOTES1/g, getParameter("votes1", data));
		html = html.replace(/VOTES2/g, getParameter("votes2", data));
		try {
			var votes1 = Number(getParameter("votes1", data));
			var votes2 = Number(getParameter("votes2", data));
			if (votes1 != votes2) {
				html = html.replace("WINNER" + (votes1 > votes2 ? 1 : 2), "winner");
			}
		} catch (e) { console.log(e); }
	}
	else if (page == "leaderboard") {
		html = `
			<div style="font-size: 35px">Leaderboard</div>
			<table>
				<tr>
					<th style="min-width: 21px">#</th>
					<th style="width: 100%">Name</th>
					<th style="width: 47px;">Points</th>
				</tr>
				<tr>
					<td style="width: 21px">RK</td>
					<td style="width: 100%; max-width: 100px;">NAME</td>
					<td style="min-width: 47px;">PTTS</td>
				</tr>
			</table>
			<time-left>TIME seconds left</time-left>
		`;
		//html = html.replace(/PLAYERS/g, getParameter("players", data));
		html = html.replace(/TIME/g, getParameter("time", data));
	}
	else if (page == "podium") {
		html = `
			<div style="font-size: 35px">Podium</div>
			<podium>
				<podium-second class="mdc-elevation--z16">
					<podium-text>RANK1NAME</podium-text>
					<podium-tile></podium-tile>
				</podium-second>
				<podium-first class="mdc-elevation--z24">
					<podium-text>RANK2NAME</podium-text>
					<podium-tile></podium-tile>
				</podium-first>
				<podium-third class="mdc-elevation--z8">
					<podium-text>RANK3NAME</podium-text>
					<podium-tile></podium-tile>
				</podium-third>
			</podium>
			<table>
				<tr>
					<th style="min-width: 21px">#</th>
					<th style="width: 100%">Name</th>
					<th style="width: 47px;">Points</th>
				</tr>
				<tr>
					<td style="width: 21px">RK</td>
					<td style="width: 100%; max-width: 100px;">NAME</td>
					<td style="min-width: 47px;">PTTS</td>
				</tr>
			</table>
			<div style="justify-content: center; display: flex; margin-top: 40px;">
				<a href="/" style="width: 180px" class="mdc-button mdc-button--raised" data-mdc-auto-init="MDCRipple" onclick="submitAnswer(document.querySelector('#answer').MDCTextField.value)">
					<div class="mdc-button__ripple"></div>
					<span class="mdc-button__label">Play Again</span>
				</a>
			</div>
		`;
		//html = html.replace(/PLAYERS/g, getParameter("players", data));
		clearInterval(requestDataInterval);
	}
	document.querySelector("play-content").innerHTML = html;
	if (page == "show_winner") {
		try { document.querySelector("card.winner").classList.add("winner-anim"); }
		catch (e) { }
	}
	mdc.autoInit(document.querySelector("play-content"));
}

function clearCache() {
	if (page != "waiting_for_people") {
		requestStartGame = false;
	}
	if (page != "question") {
		submittedAnswer = "";
	}
	if (page != "vote" && page != "view_vote") {
		submittedVote = 0;
	}
}

var requestDataHttp = new XMLHttpRequest();
requestDataHttp.addEventListener("load", function () {
	var data = decodeURI(this.responseText);
	//console.log(data);
	var lastPage = page;
	page = getParameter("page", data);
	if (lastPage !== page) {
		loadPage(data);
		clearCache();
	}
	try { document.querySelector("time-left").innerText = getParameter("time", data); } catch (e) { }
	try { document.querySelector("rank").innerText = getParameter("rank", data); } catch (e) { }
	try { document.querySelector("round").innerText = getParameter("round", data); } catch (e) { }
	try { document.querySelector("number-submitted").innerText = getParameter("submitted", data) + " submitted"; } catch (e) { }
	try { document.querySelector("question").innerHTML = '<div style="font-weight: 600">Question:</div>' + getParameter("question", data); } catch (e) { }
	try { document.querySelector(".numpeople-in").innerText = getParameter("numpeople", data) + " people in"; } catch (e) { }
});
function requestData() {
	var additionalData = "&";
	if (requestStartGame == true) additionalData += "start=true&";
	if (submittedAnswer != "") additionalData += "answer=" + submittedAnswer + "&";
	if (submittedVote != 0) additionalData += "vote=" + submittedVote + "&";
	console.log(additionalData);
	requestDataHttp.open("GET", "/play-get-data" + location.search + additionalData);
	requestDataHttp.send();
}
var requestDataInterval = setInterval(requestData, 500);

//User Methods
function startGame() {
	requestStartGame = true;
}
function submitAnswer(answer) {
	submittedAnswer = answer;
	try { document.querySelector('#answer').MDCTextField.value = ""; }
	catch (e) { }
}
function submitVote(num) {
	submittedVote = num;
}