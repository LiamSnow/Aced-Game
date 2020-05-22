package server;

import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {
	@SuppressWarnings("unused")
	private static boolean isLocalHost;
	
	public static void main(String[] args) throws IOException {
		int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "5000"));
		if (isLocalHost = port == 5000)
			System.out.println("Hosting locally at localhost:5000");
		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

		createPage(server, "/", "/src/pages/join.html");
		createPage(server, "/play", "/src/pages/play.html");
		createPlayGetData(server, "/play-get-data");
		createSourceFolder(server, "/resources/");
		
		server.start();
	}
	
	public static void createPage(HttpServer server, String url, String path) {
		server.createContext(url, (var t) -> {
			//Read target page (content)
			String content = readFile(System.getProperty("user.dir") + path);
			
			//Read and remove metadata
			String title = Util.readFromTag("title", content);
			String css = Util.joinStringArray(
					"<link href=\"resources/css/", 
					Util.readAllTags("css", content), 
					".css\" type=\"text/css\" rel=\"stylesheet\">\n"
				);
			String javascript = Util.joinStringArray(
					"<script src=\"resources/",
					Util.readAllTags("javascript", content), 
					".js\"></script>\n"
				);
			content = Util.deleteTag("title", content);
			content = Util.deleteAllTags("css", content);
			content = Util.deleteAllTags("javascript", content);
			
			//Read main page
			String page = readFile(System.getProperty("user.dir") + "/src/main.html");
			
			//Merge
			page = page.replace("${content}", content);
			page = page.replaceAll("\\$\\{title\\}", title);
			page = page.replaceAll("\\$\\{css\\}", css);
			page = page.replaceAll("\\$\\{javascript\\}", javascript);
			
			//File header
			t.getResponseHeaders().add("Content-Type", FileHeaders.HTML.type);
			
			byte[] pageBytes = page.getBytes("UTF-8");
			t.sendResponseHeaders(200, pageBytes.length);
			try (OutputStream os = t.getResponseBody()) { os.write(pageBytes); }
		});
	}
	
	public static String readFile(String path) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), "UTF8"));
			String str = "", line;
			while ((line = in.readLine()) != null)
				str += line;
	        in.close();
	        return str;
		} catch (Exception e) { e.printStackTrace(); }
		return "";
	}
	
	public static void createPlayGetData(HttpServer server, String url) {
		server.createContext(url, (var t) -> {
			byte[] response = "?a=b&c=d".getBytes("UTF-8");
			t.sendResponseHeaders(200, response.length);
			try (OutputStream os = t.getResponseBody()) { os.write(response); }
		});
	}
	
	public static enum FileHeaders {
		HTML("text/html"),
		JS("application/javascript"),
		CSS("text/css"),
		PNG("image/png");
		String type; FileHeaders(String type) { this.type = type; }
	}
	
	public static void createSourceFolder(HttpServer server, String url) {
		server.createContext(url, (var t) -> {
			String fileUrl = "/src" + t.getRequestURI().toString();
			t.getResponseHeaders().add("Content-Type", FileHeaders.valueOf(fileUrl.substring(fileUrl.indexOf('.') + 1).toUpperCase()).type);
			
			byte[] bytes = readFile(System.getProperty("user.dir") + fileUrl).getBytes("UTF-8");
			t.sendResponseHeaders(200, bytes.length);
			try (OutputStream os = t.getResponseBody()) { os.write(bytes); }
		});
	}
}