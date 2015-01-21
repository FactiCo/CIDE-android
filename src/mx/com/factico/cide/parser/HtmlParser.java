package mx.com.factico.cide.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.com.factico.cide.dialogues.Dialogues;
import android.util.Log;

public class HtmlParser {
	private static String TAG_CLASS = HtmlParser.class.getName();
	
	private static final String imageRegex = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
	private static final String videoRegex = "<iframe[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
	
	private static final String startRegex = "<p>";
	private static final String endRegex = "</p>";
	
	public static void parseHtml(String html) {
		String description = html;
		
		int startIndex;
		int endIndex;
		while ((startIndex = description.indexOf(startRegex)) != -1) {
			description = description.substring(startIndex + startRegex.length());
			// Dialogues.Log(TAG_CLASS, "Description: " + description, Log.INFO);
			
			if ((endIndex = description.indexOf(endRegex)) != -1) {
				// Dialogues.Log(TAG_CLASS, "startIndex: " + startIndex + ", endIndex: " + endIndex, Log.INFO);
				
				String item = description.substring(0, endIndex);
				// Dialogues.Log(TAG_CLASS, "Item: " + item, Log.INFO);
				
				String image = null;
				String video = null;
				if ((image = hasImage(item)) != null) {
					Dialogues.Log(TAG_CLASS, "Url image: " + image, Log.INFO);
					
				} else if ((video = hasVideo(item)) != null) {
					Dialogues.Log(TAG_CLASS, "Url video: " + video, Log.INFO);
					
				} else {
					Dialogues.Log(TAG_CLASS, "Url text: " + item, Log.INFO);
				}
			}
		}
	}
	
	private static String hasImage(String item) {
		String content = null;
		Pattern p = Pattern.compile(imageRegex);
		Matcher m = p.matcher(item);
		if (m.find()) {
			content = m.group(1);
		}
		return content;
	}
	
	private static String hasVideo(String item) {
		String content = null;
		Pattern p = Pattern.compile(videoRegex);
		Matcher m = p.matcher(item);
		if (m.find()) {
			content = m.group(1);
		}
		return content;
	}
}
