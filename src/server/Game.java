package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
	private static enum Stage { 
		WAITING_FOR_PLAYERS, QUESTIONING, VOTING, 
		SHOWING_WINNER, SHOWING_LEADERBOARD, SHOWING_PODIUM
	}
	private static final String[] QUESTIONS = {
			"This is a really bad question0",
			"This is a really bad question1",
			"This is a really bad question2",
			"This is a really bad question3",
			"This is a really bad question4",
			"This is a really bad question5",
			"This is a really bad question6",
			"This is a really bad question7"
	};
	private static final int QUESTIONING_TIME = 120;
	private static final int VOTING_TIME = 60;
	private static final int SHOWING_WINNER_TIME = 10;//20;
	private static final int SHOWING_LEADERBOARD_TIME = 5;//20;
	private static final int SHOWING_PODIUM_TIME = 5;//20;
	
	private int code, round, time, votingStage;
	private List<Integer> assignedQuestions;
	private Player creator1, creator2;
	private List<Player> players;
	private boolean wantsToDie;
	private Stage stage;
	
	public Game(int code, String adminPlayerName) {
		this.stage = Stage.WAITING_FOR_PLAYERS;
		this.players = new ArrayList<Player>();
		this.players.add(new Player(adminPlayerName, true, 1));
		assignedQuestions = new ArrayList<Integer>();
		this.wantsToDie = false;
		this.code = code;
	}
	
	//Update
	public void update() {
		switch (stage) {
			case WAITING_FOR_PLAYERS:
				break;
			case QUESTIONING:
				time--;
				boolean allSubmitted = true;
				for (Player player : players) {
					if (!player.answersSubmitted())
						allSubmitted = false;
				}
				if (allSubmitted || time <= 0) {
					startVoting();
					this.votingStage = 0;
				}
				break;
			case VOTING:
				time--;
				boolean allVoted = true;
				for (Player player : players) {
					if (!player.hasVoted() && 
						(creator1 != null && !player.getName().equals(creator1.getName())) && 
						(creator2 != null && !player.getName().equals(creator2.getName()))
						)
						allVoted = false;
				}
				if (allVoted || time <= 0) {
					this.stage = Stage.SHOWING_WINNER;
					this.time = SHOWING_WINNER_TIME;
					int votes1 = 0, votes2 = 0;
					for (Player player : players) {
						if (player.getVote() == 1)
							votes1++;
						else if (player.getVote() == 2)
							votes2++;
					}
					try {
						if (creator1 != null)
							creator1.addPoints(votes1);
						if (creator2 != null)
							creator2.addPoints(votes2);
					} catch (Exception e) { e.printStackTrace(); }
					recalculateRanks();
				}
				break;
			case SHOWING_WINNER:
				time--;
				if (time <= 0) {
					votingStage++;
					if (votingStage > Math.ceil(players.size() / 2.0)) {
						if (round == 3) {
							this.stage = Stage.SHOWING_PODIUM;
							this.time = SHOWING_PODIUM_TIME;
						}
						else {
							this.stage = Stage.SHOWING_LEADERBOARD;
							this.time = SHOWING_LEADERBOARD_TIME;
						}
					}
					else startVoting();
				}
				break;
			case SHOWING_LEADERBOARD:
				time--;
				if (time <= 0) {
					round++;
					startQuestioning();
				}
				break;
			case SHOWING_PODIUM:
				time--;
				if (time <= 0) {
					wantsToDie = true;
				}
				break;
				
			//kick AFK players TODO
		}
	}
	
	public void assignQuestions() {
		assignedQuestions.clear();
		int[] list = Util.randomDiffList(0, QUESTIONS.length-1, (int) (Math.ceil(players.size() / 2.0) * 2));
		for (int i : list)
			assignedQuestions.add(i);
		for (int questionInd = 0; questionInd < Math.ceil(players.size() / 2.0) * 2; questionInd++) {
			boolean isQuestion2 = questionInd > Math.ceil(players.size() / 2.0) - 1;
			assignQuestion(questionInd, isQuestion2);
			assignQuestion(questionInd, isQuestion2);
		}
	}
	
	public void assignQuestion(int questionInd, boolean isQuestion2) {
		int attempts = 0;
		while (attempts < 100) {
			int ind = Util.random(0, players.size() - 1);
			if (isQuestion2 && !players.get(ind).hasQuestion2()) {
				players.get(ind).setQuestion2(questionInd);
				System.out.println("assigned " + questionInd + " to " + players.get(ind).getName());
				return;
			}
			else if (!isQuestion2 && !players.get(ind).hasQuestion1()) {
				players.get(ind).setQuestion1(questionInd);
				System.out.println("assigned " + questionInd + " to " + players.get(ind).getName());
				return;
			}
			attempts++;
		}
	}
	
	public void startVoting() {
		this.time = VOTING_TIME;
		this.creator1 = null;
		this.creator2 = null;
		for (Player player : players) {
			player.resetVote();
			if (player.getQuestion1() == votingStage || player.getQuestion2() == votingStage) {
				if (creator1 == null)
					creator1 = player;
				else creator2 = player;
			}
		}
		this.stage = Stage.VOTING;
	}
	
	public void startQuestioning() {
		this.stage = Stage.QUESTIONING;
		this.time = QUESTIONING_TIME;
		for (Player player : players) {
			player.resetAnswers();
			player.resetQuestions();
			player.resetVote();
		}
		assignQuestions();
	}
	
	public Player getPlayer(String name) {
		for (Player player : players) {
			if (player.getName().equals(name))
				return player;
		}
		return null;
	}
	
	public String getPlayersSendable() {
		String str = "[";
		for (Player player : players) {
			str += player.getName() + ";" + player.getPoints() + ";";
		}
		return str.substring(0, str.length()-1) + "]";
	}
	
	public void recalculateRanks() {
		Collections.sort(players);
		for (int i = 0; i < players.size(); i++) {
			players.get(i).setRank(i + 1);
		}
	}
	
	public void startGame() {
		startQuestioning();
		round = 1;
	}
	
	//User HTTP Request
	public String handleUserUpdate(String query, String name) {
		try {
			Player player = getPlayer(name);
			if (player == null) {
				player = new Player(name);
				players.add(player);
				recalculateRanks();
			}
			
			String str = "?";
			
			//Page
			String page = "";
			if (stage == Stage.WAITING_FOR_PLAYERS) {
				page = player.isAdmin() ? "waiting_for_people_admin" : "waiting_for_people";
			}
			else if (stage == Stage.QUESTIONING) {
				page = player.answersSubmitted() ? "waiting_for_answers" : "question";
			}
			else if (stage == Stage.VOTING) {
				page = (
						player.hasVoted() || 
						(creator1 != null && player.getName().equals(creator1.getName())) || 
						(creator2 != null && player.getName().equals(creator2.getName()))
					) ? "view_vote" : "vote";
			}
			else if (stage == Stage.SHOWING_WINNER) {
				page = "show_winner";
			}
			else if (stage == Stage.SHOWING_LEADERBOARD) {
				page = "leaderboard";
			}
			else if (stage == Stage.SHOWING_PODIUM) {
				page = "podium";
			}
			
			//Other variables
			if (stage == Stage.QUESTIONING) {
				if (!player.answersSubmitted()) {
					try {
						str += "&question=" + assignedQuestions.get(player.answer1Submited() ? player.getQuestion2() : player.getQuestion1());
					} catch (Exception e) { }
				}
				int submitted = 0;
				for (Player p : players)
					if (p.answersSubmitted()) submitted++;
				str += "&submitted=" + submitted;
			}
			if (stage == Stage.VOTING || stage == Stage.SHOWING_WINNER) {
				if (creator1 != null) {
					boolean iq1 = votingStage == creator1.getQuestion1();
					try {
						str += "&question=" + assignedQuestions.get(iq1 ? creator1.getQuestion1() : creator1.getQuestion2());
					} catch (Exception e) { }
					str += "&creator1=" + creator1.getName();
					str += "&answer1=" + (iq1 ? creator1.getAnswer1() : creator1.getAnswer2());
				}
				if (creator2 != null) {
					boolean iq1 = votingStage == creator2.getQuestion1();
					try {
						if (str.indexOf("&question=") == -1)
							str += "&question=" + assignedQuestions.get(iq1 ? creator2.getQuestion1() : creator2.getQuestion2());
					} catch (Exception e) { }
					str += "&creator2=" + creator2.getName();
					str += "&answer2=" + (iq1 ? creator2.getAnswer1() : creator2.getAnswer2());
				}
				if (stage == Stage.SHOWING_WINNER) {
					int votes1 = 0, votes2 = 0;
					for (Player p : players) {
						if (p.getVote() == 1)
							votes1++;
						else if (p.getVote() == 2)
							votes2++;
					}
					str += "&votes1=" + votes1;
					str += "&votes2=" + votes2;
				}
				else {
					int submitted = 0;
					for (Player p : players)
						if (p.hasVoted()) submitted++;
					str += "&submitted=" + submitted;
				}
			}
			if (stage == Stage.SHOWING_LEADERBOARD || stage == Stage.SHOWING_PODIUM) {
				str += "&players=" + getPlayersSendable();
			}
			
			str += "&page=" + page;
			str += "&round=" + round;
			str += "&rank=" + player.getRank();
			str += "&numpeople=" + players.size();
			str += "&time=" + time;
			
			//Handle what they send
			if (Util.getParameterBool("start", query) && stage == Stage.WAITING_FOR_PLAYERS) {
				startGame();
			}
			else if (!Util.getParameter("answer", query).equals("") && stage == Stage.QUESTIONING) {
				if (player.answer1Submited()) {
					if (!Util.getParameter("answer", query).equals(player.getAnswer1()))
						player.setAnswer2(Util.getParameter("answer", query));
				}
				else player.setAnswer1(Util.getParameter("answer", query));
			}
			else if (Util.getParameterInt("vote", query) > 0 && stage == Stage.VOTING) {
				player.setVote(Util.getParameterInt("vote", query));
			}
			
			return str;
		} catch (Exception e) { e.printStackTrace(); }
		return "?";
	}
	
	//Getters/Setters
	public int getCode() { return code; }
	public boolean wantsToDie() { return wantsToDie; }
}