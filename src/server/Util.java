package server;

import java.util.ArrayList;
import java.util.List;

public class Util {
	public static String readFromTag(String tag, String str) {
		int start = str.lastIndexOf("<" + tag + ">");
		int end = str.lastIndexOf("</" + tag + ">");
		if (start != -1 && end != -1)
			return str.substring(start + tag.length() + 2, end);
		return "";
	}

	public static String[] readAllTags(String tag, String str) {
		List<String> data = new ArrayList<String>();
		while (str.lastIndexOf("<" + tag + ">") != -1) {
			data.add(readFromTag(tag, str));
			str = str.substring(0, str.lastIndexOf("<" + tag + ">"));
		}
		return stringListToArray(data);
	}

	private static String[] stringListToArray(List<String> list) {
		String[] arr = new String[list.size()];
		for (int i = 0; i < list.size(); i++)
			arr[i] = list.get(i);
		return arr;
	}

	public static String deleteTag(String tag, String str) {
		int start = str.lastIndexOf("<" + tag + ">");
		int end = str.lastIndexOf("</" + tag + ">");
		if (start != -1 && end != -1)
			return str.substring(0, start) + str.substring(end + tag.length() + 3);
		return str;
	}

	public static String deleteAllTags(String tag, String str) {
		while (str.lastIndexOf("<" + tag + ">") != -1)
			str = deleteTag(tag, str);
		return str;
	}

	public static String joinStringArray(String before, String[] strings, String after) {
		String ret = "";
		for (String str : strings)
			ret += before + str + after;
		return ret;
	}
	
	public static String getParameter(String name, String data) {
		try {
			int firstIndex = data.indexOf(name + "=");
			if (firstIndex > -1 && (firstIndex + name.length() + 1) < data.length()) {
				firstIndex += name.length() + 1;
				int lastIndex = data.indexOf("&", firstIndex);
				if (lastIndex == -1)
					lastIndex = data.length() - 1;
				if (lastIndex < data.length() && firstIndex < lastIndex)
					return data.substring(firstIndex, lastIndex).replaceAll("\\=", "");
			}
		} catch (Exception e) { e.printStackTrace(); }
		return "";
	}
	public static int getParameterInt(String name, String data) {
		try {
			return Integer.parseInt(getParameter(name, data));
		} catch (Exception e) { }
		return -1;
	}
	public static boolean getParameterBool(String name, String data) {
		try {
			return Boolean.parseBoolean(getParameter(name, data));
		} catch (Exception e) { }
		return false;
	}

	public static int random(int min, int max) {
		//[min, max]
		return min + (int)(Math.random() * ((max - min) + 1));
	}

	public static int[] randomDiffList(int min, int max, int length) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < length; i++)
			list.add(randomNotIncluding(min, max, list));
		int[] arr = new int[list.size()];
		for (int i = 0; i < arr.length; i++)
			arr[i] = list.get(i);
		return arr;
	}

	public static int randomNotIncluding(int min, int max, List<Integer> list) {
		int tries = 0;
		while (tries < 100) {
			int rand = random(min, max);
			boolean works = true;
			for (int i : list) {
				if (rand == i) works = false;
			}
			if (works) return rand;
			tries++;
		}
		return -1;
	}
}