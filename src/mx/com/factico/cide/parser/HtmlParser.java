
package mx.com.factico.cide.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.com.factico.cide.dialogues.Dialogues;
import android.util.Log;

public class HtmlParser {
	private static String TAG_CLASS = HtmlParser.class.getName();
	
	private static final String imageRegex = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
	private static final String videoRegex = "<iframe[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
	
	private static final String startParagraphRegex = "<p>";
	private static final String endParagraphRegex = "</p>";
	
	public static final String startImageRegex = "<img";
	public static final String endImageRegex = "/>";
	
	public static final String startVideoRegex = "<iframe";
	public static final String endVideoRegex = "</iframe>";
	
	private static final String widthRegex = "width=\"";
	private static final String heightRegex = "height=\"";
	
	public static String resizeItemInHtml(String html, String startRegex, String endRegex) {
		String description = "";
		
		int startIndex;
		int endIndex;
		
		String beforeIndex = "";
		String afterIndex = "";
		String inIndex = "";
		String auxDescription = html;
		
		if (auxDescription.indexOf(startRegex) != -1) {
			while ((startIndex = auxDescription.indexOf(startRegex)) != -1) {
				beforeIndex = auxDescription.substring(0, startIndex);
				Dialogues.Log(TAG_CLASS, "/***EdBefore: " + beforeIndex, Log.INFO);
				
				inIndex = auxDescription.substring(startIndex + startRegex.length());
				endIndex = inIndex.indexOf(endRegex);
				inIndex = auxDescription.substring(startIndex, startIndex + endIndex + endRegex.length());
				Dialogues.Log(TAG_CLASS, "/***EdIn: " + inIndex, Log.INFO);
				
				inIndex = replaceValueInTag(inIndex, "100%", widthRegex);
				// inIndex = replaceValueInTag(inIndex, "auto", heightRegex);
				
				afterIndex = auxDescription.substring(startIndex + endIndex + endRegex.length());
				Dialogues.Log(TAG_CLASS, "/***EdAfter: " + afterIndex, Log.INFO);
				
				auxDescription = afterIndex;
				
				description += beforeIndex + inIndex;
			}
			
			description += afterIndex;
			
		} else {
			description = html;
		}
		
		Dialogues.Log(TAG_CLASS, "" + description, Log.ERROR);
		
		return description;
	}
	
	private static String replaceValueInTag(String tag, String newValue, String startRegex) {
		//String newBetween;
		String newText = "";
		
		int startIndex = tag.indexOf(startRegex) + startRegex.length();
		
		if (startIndex != -1) {
			String textStart = tag.substring(0, startIndex);
			
			Dialogues.Log(TAG_CLASS, "/***EdBeforeImg: "+ textStart, Log.DEBUG);
			
			String textBetween = tag.substring(startIndex);
			int endIndex = textBetween.indexOf("\"");
			
			if (endIndex != -1) {
				String textValue;
				textValue = textBetween.substring(0, endIndex);
				Dialogues.Log(TAG_CLASS, "/***EdBetweenImg: "+ textValue, Log.DEBUG);
				
				//newBetween = textValue.replace(regex, newValue);
				Dialogues.Log(TAG_CLASS, "/***EdNewBetweenImg: "+ newValue, Log.DEBUG);
				
				String endText = textBetween.substring(endIndex + "\"".length());
				Dialogues.Log(TAG_CLASS, "/***EdEndImg: "+ endText, Log.DEBUG);
				
				newText = textStart + newValue + endText;
			}
		}
		
		return newText;
	}
	
	public static String renameVideoUrlInHtml(String html) {
		String description = "";
		
		String regex = "//www.youtube.com";
		String regexHost1 = "http:";
		String regexHost2 = "https:";
		
		if (html.contains(regexHost1 + regex) || html.contains(regexHost2 + regex)) {
			description = html;
		} else {
			description = html.replaceAll(regex, regexHost2 + regex);
			Dialogues.Log(TAG_CLASS, "/***EdVideo: " + description, Log.DEBUG);
		}
		
		return description;
	}
	
	public static String renameVideoUrlInHtml2(String html) {
		String description = "";
		
		String regex = "//www.youtube.com";
		String regexHost = "http:";
		
		int startIndex;
		int endIndex;
		
		String beforeIndex = "";
		String afterIndex = "";
		String inIndex = "";
		String auxDescription = html;
		
		if (auxDescription.indexOf(regex) != -1) {
			while ((startIndex = auxDescription.indexOf(regex)) != -1) {
				beforeIndex = auxDescription.substring(0, startIndex);
				Dialogues.Log(TAG_CLASS, "/***EdVideoBefore: " + beforeIndex, Log.DEBUG);
				
				String textHost = auxDescription.substring(startIndex - regexHost.length());
				Dialogues.Log(TAG_CLASS, "/***EdVideoTab: " + textHost, Log.DEBUG);
				
				//auxDescription = auxDescription.substring(startIndex + regex.length());
				//Dialogues.Log(TAG_CLASS, "/***EdVideoTab: " + textHost, Log.DEBUG);
				
				if (textHost.equals(regexHost)) {
					description = auxDescription;
				} else {
					description = description.replace(regex, regex);
				}
			}
		} else {
			description = html;
		}
		
		return auxDescription;
	}
	
	public static void parseHtml(String html) {
		String description = html;
		
		int startIndex;
		int endIndex;
		while ((startIndex = description.indexOf(startParagraphRegex)) != -1) {
			description = description.substring(startIndex + startParagraphRegex.length());
			// Dialogues.Log(TAG_CLASS, "Description: " + description, Log.INFO);
			
			if ((endIndex = description.indexOf(endParagraphRegex)) != -1) {
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
