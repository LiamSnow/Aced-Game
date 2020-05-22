package server;

public class Player {
	
	private int points, question1, question2, vote;
	private final boolean isAdmin;
	private final String name;
	private String answer1, answer2;
	
	public Player(String name) { this(name, false); }
	public Player(String name, boolean isAdmin) {
		this.name = name;
		this.isAdmin = isAdmin;
		resetAnswers();
		resetQuestions();
	}
	
	public int getPoints() { return points; }
	public int getVote() { return vote; }
	public boolean voted() { return vote != 0; }
	public int getQuestion1() { return question1; }
	public int getQuestion2() { return question2; }
	public boolean isAdmin() { return isAdmin; }
	public String getName() { return name; }
	public String getAnswer1() { return answer1; }
	public String getAnswer2() { return answer2; }
	public boolean answersSubmitted() { return !answer1.equals("") && !answer2.equals(""); }
	
	public void addPoints(int points) { this.points += points; }
	public void setVote(int vote) { this.vote = vote; }
	public void resetVote() { this.vote = 0; }
	public void setQuestion1(int question1) { this.question1 = question1; }
	public void setQuestion2(int question2) { this.question2 = question2; }
	public void resetQuestions() { this.question1 = -1; this.question2 = -1; }
	public void setAnswer1(String answer1) { this.answer1 = answer1; }
	public void setAnswer2(String answer2) { this.answer2 = answer2; }
	public void resetAnswers() { this.answer1 = ""; this.answer2 = ""; }
}
