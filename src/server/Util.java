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
}
