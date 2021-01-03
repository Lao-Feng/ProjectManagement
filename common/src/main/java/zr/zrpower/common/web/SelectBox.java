package zr.zrpower.common.web;

public class SelectBox {
	public SelectBox() {
		selected = "";
		bEnableNull = false;
	}

	public SelectBox(boolean bNull) {
		selected = "";
		bEnableNull = false;
		bEnableNull = bNull;
	}

	public SelectBox(String strDict[][]) {
		selected = "";
		bEnableNull = false;
		strs = strDict;
	}

	public SelectBox(String strDict[][], boolean bNull) {
		selected = "";
		bEnableNull = false;
		strs = strDict;
		bEnableNull = bNull;
	}

	public SelectBox(String strDict[][], String strSelected) {
		selected = "";
		bEnableNull = false;
		strs = strDict;
		selected = strSelected;
	}

	public SelectBox(String strDict[][], String strSelected, boolean bNull) {
		selected = "";
		bEnableNull = false;
		strs = strDict;
		selected = strSelected;
		bEnableNull = bNull;
	}

	public String show() {
		String strResult = "";
		if (bEnableNull)
			strResult = strResult + "<option value=\"\"></option>\n";
		if (strs != null)
			if (selected.equals("")) {
				for (int i = 0; i < strs.length; i++)
					strResult = strResult + "<option value=\"" + strs[i][0] + "\">" + strs[i][1] + "</option>\n";
			} else {
				for (int i = 0; i < strs.length; i++)
					if (strs[i][0].equals(selected))
						strResult = strResult + "<option value=\"" + strs[i][0] + "\" selected>" + strs[i][1]
								+ "</option>\n";
					else
						strResult = strResult + "<option value=\"" + strs[i][0] + "\">" + strs[i][1] + "</option>\n";
			}
		return strResult;
	}

	public void setDictStringArrary(String strDict[][]) {
		strs = strDict;
	}

	public void setSelected(String strSelected) {
		selected = strSelected;
	}

	public void setEnableNULL(boolean bNull) {
		bEnableNull = bNull;
	}

	private String strs[][];
	private String selected;
	private boolean bEnableNull;
}