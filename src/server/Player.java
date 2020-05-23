package server;

public class Player implements Comparable<Player> {
	
	private int points, question1Ind, question2Ind, vote, rank;
	private String answer1, answer2;
	private final boolean isAdmin;
	private final String name;
	private long lastRequest;
	
	public Player(String name) { this(name, false, -1); }
	public Player(String name, boolean isAdmin, int rank) {
		this.lastRequest = System.currentTimeMillis();
		this.isAdmin = isAdmin;
		this.name = name;
		this.rank = rank;
		resetAnswers();
		resetQuestions();
	}
	
	public int getPoints() { return points; }
	public int getVote() { return vote; }
	public boolean hasVoted() { return vote != 0; }
	public int getQuestion1() { return question1Ind; }
	public int getQuestion2() { return question2Ind; }
	public boolean hasQuestion1() { return question1Ind != -1; }
	public boolean hasQuestion2() { return question2Ind != -1; }
	public boolean isAdmin() { return isAdmin; }
	public String getName() { return name; }
	public String getAnswer1() { return answer1; }
	public String getAnswer2() { return answer2; }
	public boolean answersSubmitted() { return answer1Submited() && answer2Submited(); }
	public boolean answer1Submited() { return !answer1.equals(""); }
	public boolean answer2Submited() { return !answer2.equals(""); }
	public int timeSinceLastRequest() { return (int) (System.currentTimeMillis() - lastRequest); }
	public int getRank() { return rank; }
	public int compareTo(Player player) {
		int points = player.getPoints();
		if (points == this.points) return 0;
		return (points > this.points) ? -1 : 1;
	}
	
	public void addPoints(int points) { this.points += points; }
	public void setVote(int vote) { this.vote = vote; }
	public void resetVote() { this.vote = 0; }
	public void setQuestion1(int question1) { this.question1Ind = question1; }
	public void setQuestion2(int question2) { this.question2Ind = question2; }
	public void resetQuestions() { this.question1Ind = -1; this.question2Ind = -1; }
	public void setAnswer1(String answer1) { this.answer1 = answer1; }
	public void setAnswer2(String answer2) { this.answer2 = answer2; }
	public void resetAnswers() { this.answer1 = ""; this.answer2 = ""; }
	public void updateLastRequest() { this.lastRequest = System.currentTimeMillis(); }
	public void setRank(int rank) { this.rank = rank; }
}
