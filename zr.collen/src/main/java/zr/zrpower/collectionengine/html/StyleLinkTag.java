package zr.zrpower.collectionengine.html;

import org.htmlparser.tags.CompositeTag;

public class StyleLinkTag extends CompositeTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String[] mIds = new String[] { "LINK" };
	protected String linkURL;

	public StyleLinkTag() {
		linkURL = null;
		setAttribute("rel", "stylesheet");
		setAttribute("type", "text/css");
	}

	public String[] getIds() {
		return (mIds);
	}

	public void setUrl(String url) {
		setAttribute("href", url);
	}

	public String getUrl() {
		return getAttribute("href");
	}
}
