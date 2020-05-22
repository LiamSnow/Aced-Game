package server;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private static enum Stage { 
		WAITING_FOR_PLAYERS, QUESTIONING, VOTING, 
		SHOWING_WINNER, SHOWING_LEADERBOARD, SHOWING_PODIUM
	}
	private static final String[] questions = {
			"This is a really bad question0",
			"This is a really bad question1",
			"This is a really bad question2",
			"This is a really bad question3",
			"This is a really bad question4",
			"This is a really bad question5",
			"This is a really bad question6"
	};
	private static final int QUESTIONING_TIME = 120;
	private static final int VOTING_TIME = 60;
	private static final int SHOWING_WINNER_TIME = 20;
	private static final int SHOWING_LEADERBOARD_TIME = 20;
	private static final int SHOWING_PODIUM_TIME = 20;
	
	private int code, round, time, questioningStage;
	private List<Player> players;
	private boolean wantsToDie;
	private Stage stage;
	
	public Game(int code, String adminPlayerName) {
		this.code = code;
		this.players = new ArrayList<Player>();
		this.wantsToDie = false;
		this.stage = Stage.WAITING_FOR_PLAYERS;
	}
	
	//Update
	public void update() {
		switch (stage) {
			case QUESTIONING:
				time--;
				break;
			case SHOWING_LEADERBOARD:
				time--;
				break;
			case SHOWING_PODIUM:
				time--;
				break;
			case SHOWING_WINNER:
				time--;
				break;
			case VOTING:
				time--;
				break;
			case WAITING_FOR_PLAYERS:
				break;
		}
	}
	
	public void assignQuestions() {
		
	}
	
	public void startQuestioning() {
		this.stage = Stage.QUESTIONING;
		this.time = QUESTIONING_TIME;
		this.questioningStage = 0;
		assignQuestions();
	}
	
	//User HTTP Request
	public String handleUserUpdate(String query, String name) {
		try {
			return "?page=";
		} catch (Exception e) { e.printStackTrace(); }
		return "?";
	}
	
	//Getters/Setters
	public int getCode() { return code; }
	public boolean wantsToDie() { return wantsToDie; }
}
