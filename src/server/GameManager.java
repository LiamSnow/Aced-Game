package server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GameManager {

	private static List<Game> games; 
	
	public static void init() {
		games = new ArrayList<Game>();
		
		Thread thread = new Thread(() -> {
			while (true) {
				long startTime = System.currentTimeMillis();
				
				for (Game game : games) {
					game.update();
					if (game.wantsToDie())
						games.remove(game);
				}
				
				try { Thread.sleep(1000 - (System.currentTimeMillis() - startTime)); } catch (Exception e) { }
			}
		});
		thread.start();
	}
	
	public static Game getGame(int code) {
		for (Game game : games) {
			if (game.getCode() == code)
				return game;
		}
		return null;
	}

	public static String handleUserUpdate(String query) {
		try {
			query = URLDecoder.decode(query, "UTF-8");
			int code = Util.getParameterInt("code", query);
			String name = Util.getParameter("name", query);
			Game game = getGame(code);
			if (game == null)
				game = new Game(code, name);
			return URLEncoder.encode(game.handleUserUpdate(query, name), "UTF-8");
		} catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		return "?";
	}

}
