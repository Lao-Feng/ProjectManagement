package zr.zrpower.collectionengine.html;

import org.htmlparser.tags.*;
import org.htmlparser.util.NodeList;

public class JavaScriptTag extends ScriptTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JavaScriptTag() {
		super();
		this.setLanguage("javascript");
		this.setType("text/javascript");
		LabelTag lt = new LabelTag();
		lt.setText("</script>");
		this.setEndTag(lt);
	}

	public void addFunction(String function) {
		if (this.getScriptCode().indexOf(function) < 0) {
			this.setScriptCode(this.getScriptCode() + "\n\n" + function + "\n\n");
		}
	}

	public void addToHead(Html html) {
		if (this.getScriptCode().length() > 0) {
			NodeList nlHead = html.searchFor(new HeadTag().getClass(), true);
			HeadTag tagHead = (HeadTag) nlHead.elementAt(0);
			tagHead.getChildren().add(this);
		}
	}

	public void addToBoay(Html html) {
		if (this.getScriptCode().length() > 0) {
			NodeList nlHead = html.searchFor(new BodyTag().getClass(), true);
			BodyTag tagBody = (BodyTag) nlHead.elementAt(0);
			tagBody.getChildren().add(this);
		}
	}
}