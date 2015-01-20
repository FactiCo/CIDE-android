package mx.com.factico.cide;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.com.factico.cide.dialogues.Dialogues;
import mx.com.factico.cide.utils.AssetsUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.Context;
import android.util.Log;

public class HtmlParser {
	private static String TAG_CLASS = HtmlParser.class.getName();
	
	private static final String imageRegex = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
	private static final String videoRegex = "<iframe[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
	
	private static final String[] listStartRegex = {"<p", "<a", "<div", "<img", "<iframe"};
	private static final String[] listEndRegex = {"</p>", "</a>", "</div>", "</img>", "</iframe>"};
	private static final String globalEndRegex = "/>";
	private static final String startRegex = "<p>";
	private static final String endRegex = "</p>";
	
	private static ArrayStack stackStart = new ArrayStack();
	
	public static void parseHtml3(Context context, String html) {
		String description = html;
		
		int indexStartBody = description.indexOf("<body");
		int indexEndBody = description.indexOf("</body>");
		
		if (indexStartBody != -1) {
			int indexMiddle = indexStartBody + description.substring(indexStartBody).indexOf(">") + 1;
			description = description.substring(indexMiddle, indexEndBody);
			
			Dialogues.Log(TAG_CLASS, "/***Body:\n" + description, Log.INFO);
			
			List<String> listElements = findTag(description);
		}
	}
	
	private static List<String> findTag(String description) {
		for (int i = 0; i < listStartRegex.length; i++) {
			String regex = listStartRegex[i];
			int index = -1;
			
			if ((index = description.indexOf(regex)) != -1) {
				description = description.substring(index + regex.length());
				int indexMayorQue = description.indexOf(">");
				
				if (indexMayorQue != -1) {
					description = description.substring(indexMayorQue);
					
					Dialogues.Log(TAG_CLASS, "/***Description:\n" + description, Log.INFO);
					
					stackStart.push(regex);
				} else {
					if (!stackStart.isEmpty()) {
						return findTag(description);
					}
				}
			}
		}
		return null;
	}
	
	
	
	public static void parseHtml2(Context context, String html) {
		InputStream input;
		try {
			input = AssetsUtils.getInputStreamFromAssets(context, "prueba.html");

			Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
			// Document doc = Jsoup.connect(url).get();
			
			for (Element p : doc.select("p")) {
				System.out.println("Primer p: " + p.text());
				for (Element img : p.select("img")) {
					System.out.println("Primer p img: " + img.attr("src"));
				}
				for (Element img : p.select("iframe")) {
					System.out.println("Primer p iframe: " + img.attr("src"));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
