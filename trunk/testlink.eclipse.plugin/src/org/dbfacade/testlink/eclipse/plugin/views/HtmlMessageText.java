package org.dbfacade.testlink.eclipse.plugin.views;

public interface HtmlMessageText {
	public static final String OPEN_HTML_DOC="<html><body><p>";
	public static final String CLOSE_HTML_DOC="</p></body></html>";

	public void setHtmlText(String text);
	public String getHtmlText();
}
