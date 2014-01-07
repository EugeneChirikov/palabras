package com.mates120.myword.ui;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

import com.mates120.myword.R;
import com.mates120.myword.Word;

public class HtmlPageComposer
{
	private static String top = "<meta http-equiv=\"Content-type\" content=\"text/html;charset=UTF-8\">\n" + 
			"<style>\n" + 
			"    body\n" + 
			"    {\n" + 
			"        background: rgb(255, 255, 255);\n" + 
			"    }\n" + 
			"\n" + 
			"    table\n" + 
			"    {\n" + 
			"        border-spacing:0;\n" + 
			"        border-collapse: collapse;\n" + 
			"        margin-bottom: 3px;\n" + 
			"    }\n" + 
			"\n" + 
			"    k\n" + 
			"    {\n" + 
			"        font-weight: bold;\n" + 
			"        font-size: 130%;\n" + 
			"    }\n" + 
			"\n" + 
			"    #rounded-corner\n" + 
			"    {\n" + 
			"        font-family: \"Lucida Sans Unicode\",\"Lucida Grande\",Sans-Serif;\n" + 
			"        text-align: left;\n" + 
			"        border-collapse: collapse;\n" + 
			"    }\n" + 
			"\n" + 
			"    #rounded-corner th {\n" + 
			"        font-weight: normal;\n" + 
			"        font-size: 15px;\n" + 
			"        color: rgb(0, 51, 153);\n" + 
			"        background: none repeat scroll 0% 0% rgb(185, 201, 254);\n" + 
			"        padding: 3px;\n" + 
			"    }\n" + 
			"\n" + 
			"    #rounded-corner thead th.left-corner\n" + 
			"    {\n" + 
			"        background: url(\"corners/left.png\") no-repeat scroll left -1px rgb(185, 201, 254);\n" + 
			"    }\n" + 
			"\n" + 
			"    #rounded-corner thead th.rounded-q4 \n" + 
			"    {\n" + 
			"        background: url(\"corners/right.png\") no-repeat scroll right -1px rgb(185, 201, 254);\n" + 
			"    }\n" + 
			"\n" + 
			"    #rounded-corner tfoot td.rounded-foot-left\n" + 
			"    {\n" + 
			"        background: url(\"corners/botleft.png\") no-repeat scroll left bottom rgb(232, 237, 255);\n" + 
			"    }\n" + 
			"\n" + 
			"    #rounded-corner tfoot td.rounded-foot-right\n" + 
			"    {\n" + 
			"        background: url(\"corners/botright.png\") no-repeat scroll right bottom rgb(232, 237, 255);\n" + 
			"    }\n" + 
			"\n" + 
			"\n" + 
			"\n" + 
			"    #rounded-corner td {\n" + 
			"        background: none repeat scroll 0% 0% rgb(232, 237, 255);\n" + 
			"        \n" + 
			"        color: rgb(102, 102, 153);\n" + 
			"        padding: 3px;\n" + 
			"        padding-top: 3px;\n" + 
			"        padding-right-value: 3px;\n" + 
			"        padding-bottom: 3px;\n" + 
			"        padding-left-value: 3px;\n" + 
			"        padding-left-ltr-source: physical;\n" + 
			"        padding-left-rtl-source: physical;\n" + 
			"        padding-right-ltr-source: physical;\n" + 
			"        padding-right-rtl-source: physical;\n" + 
			"\n" + 
			"    k {font: italic 75%/125% \"Comic Sans MS\", cursive;}\n" + 
			"}\n" + 
			"</style>\n" + 
			"\n" + 
			"<html>\n" + 
			"    <body>";
	
	private static String bottom = "    </body>\n" + 
							"</html>";
	private Context context;

	public HtmlPageComposer(Context c)
	{
		context = c;
	}
	
	public String makePage(List<Word> words)
	{
		StringBuilder page = new StringBuilder();
		page.append(top);
		for (Word w: words)
		{
			String[] wordAndDefenition = splitStarDictValue(w.getValue());
			String defenition = replaceSomeSymbols(wordAndDefenition[1]);
			String wrappedArticle = wrapArticle(wordAndDefenition[0], w.getDictName(), defenition);
			page.append(wrappedArticle);
		}
		page.append(bottom);
		return page.toString();
	}
	
	public String makeNotFoundPage()
	{
		return context.getResources().getString(R.string.no_such_word_in_db);
	}
	
	private String[] splitStarDictValue(String value)
	{
		String[] splitted = new String[2];
		Pattern regexpPattern = Pattern.compile("(<k>.+</k>)\\n(.*)", Pattern.DOTALL);
		Matcher regexpMatcher = regexpPattern.matcher(value);
		if (!regexpMatcher.find())
		{
			splitted[0] = "Sorry, invalid data";
			splitted[1] = "";
		}
		else
		{
			splitted[0] = regexpMatcher.group(1);
			splitted[1] = regexpMatcher.group(2);
		}
		return splitted;
	}
	
	private String replaceSomeSymbols(String text)
	{
		text = text.replaceAll("((:?\\n)+)", "<br>");
		text = text.replaceAll("tr>", "em>");
		return text;
	}
	
	private String wrapArticle(String word, String dictName, String defenition)
	{
		String wrapped = "<table id=\"rounded-corner\">"
                         + "<thead>"
                         + "<tr>"
                         + "<th scope=\"col\" class=\"left-corner\"></th>"
                         + "<th scope=\"col\" class=\"rounded-q1\">"                    
                         + word + " - " + dictName + "</th>"
                         +"<th scope=\"col\" class=\"rounded-q4\"></th>"
                         + "</tr>"
                         + "</thead>"
                         + "<tfoot>"
                         + "<tr>"
                         + "<td colspan=\"2\" class=\"rounded-foot-left\"></td>"
                         + "<td class=\"rounded-foot-right\"></td>"
                         + "</tr>"
                         + "</tfoot>"
                         + "<tbody>"
                         + "<tr>"
                         + "<td></td>"
                         + "<td>" + defenition + "</td>"
                         + "<td></td>"
                         + "</tr>"
                         + "</tbody>"
                         + "</table>";
		return wrapped;
	}
}
