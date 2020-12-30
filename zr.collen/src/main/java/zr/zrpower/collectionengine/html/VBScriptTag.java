package zr.zrpower.collectionengine.html;

import org.htmlparser.tags.*;
import org.htmlparser.util.NodeList;

public class VBScriptTag extends ScriptTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VBScriptTag() {
		super();
		this.setLanguage("VBcript");
		this.setType("text/VBScript");
		LabelTag lt = new LabelTag();
		lt.setText("</SCRIPT>");
		this.setEndTag(lt);
	}

	public void addFunction(String function) {
		StringBuffer addBuf = new StringBuffer();
		if (this.getScriptCode().indexOf(function) < 0) {
			addBuf.append(this.getScriptCode()).append("\n\n").append(function).append("\n\n");
			this.setScriptCode(addBuf.toString());
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