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
	
	private static final String startImageRegex = "<img";
	private static final String endImageRegex = "/>";
	
	public static String renameImageInHtml(String html) {
		String description = "";
		
		int startIndex;
		int endIndex;
		
		String beforeIndex = "";
		String afterIndex = "";
		String inIndex = "";
		String auxDescription = html;
		
		while ((startIndex = auxDescription.indexOf(startImageRegex)) != -1) {
			beforeIndex = auxDescription.substring(0, startIndex);
			Dialogues.Log(TAG_CLASS, "/***EdgarBefore: " + beforeIndex, Log.INFO);
			
			inIndex = auxDescription.substring(startIndex + startRegex.length());
			endIndex = inIndex.indexOf(endImageRegex);
			inIndex = auxDescription.substring(startIndex, startIndex + endIndex + endImageRegex.length());
			Dialogues.Log(TAG_CLASS, "/***EdgarIn: " + inIndex, Log.INFO);
			
			afterIndex = auxDescription.substring(startIndex + endIndex + endImageRegex.length());
			Dialogues.Log(TAG_CLASS, "/***EdgarAfter: " + afterIndex, Log.INFO);
			
			auxDescription = afterIndex;
			
			description += beforeIndex + inIndex;
		}
		
		description += afterIndex;
		
		Dialogues.Log(TAG_CLASS, "" + description, Log.ERROR);
		
		return description;
	}
	
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
