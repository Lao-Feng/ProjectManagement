package zr.zrpower.collectionengine.html;

import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.InputTag;
import org.htmlparser.util.NodeList;
import zr.zrpower.collectionengine.ItemField;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBSet;
import zr.zrpower.common.db.DBType;
import zr.zrpower.common.util.Log;
import zr.zrpower.common.util.SysPreperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PrintControl {
	private InputTag mThis;
	private boolean isRemark = false;
	private int size = -1;
	private String align; // 左，中，右对齐
	Log log; // 日志
	DBEngine dbengine;

	public PrintControl(InputTag inputtag) {
		log = new Log();
		log.SetLogForClass("PrintControl");
		log.SetLogFile("PrintControl.log");
		mThis = inputtag;
		dbengine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbengine.initialize();

		String tmpRemark = mThis.getAttribute("remark");
		align = mThis.getAttribute("align");
		if (align == null) {
			align = "left";
		}
		// ------------------------------------------------------
		if (tmpRemark != null && tmpRemark.equals("remark")) {
			// --------暂时把打印模板中的签名图片取消掉------------------
			isRemark = true;
			// -----------------------------------------------------
		}
		String tmpSize = mThis.getAttribute("size");
		if (tmpSize != null && !tmpSize.equals("")) {
			size = Integer.parseInt(tmpSize);
		}
	}

	public void setItemField(ItemField item) {
		Node n = mThis.getParent();
		int ni = n.getChildren().indexOf(mThis);
		NodeList nlist = new NodeList();
		for (int ii = 0; ii < n.getChildren().size(); ii++) {
			if (ii != ni) {
				nlist.add(n.getChildren().elementAt(ii));
			} else {
				if (isRemark) {
					if (item.getValue() != null && item.getValue().length() == 16) {
						ImageTag img = getIdiograph(item.getValue());
						if (img != null) {
							nlist.add(img);
						} else {
							nlist.add(new TextNode(HTMLEncode(ConvertData(item))));
						}
					} else {

						nlist.add(new TextNode(HTMLEncode(null)));
					}
				} else {
					nlist.add(new TextNode(HTMLEncode(ConvertData(item))));
				}
			}
		}
		n.getChildren().removeAll();
		n.getChildren().add(nlist);
	}

	private ImageTag getIdiograph(String userid) {
		log.WriteLog("getIdiograph", "开始调用");
		ImageTag img = null;
		String urlStr;
		String strSql = "Select USERID From BPIP_USER_PHOTO Where IDIOGRAPH IS NOT NULL And  USERID = '" + userid + "'";
		DBSet ds = dbengine.QuerySQL(strSql);
		if (ds != null && ds.RowCount() > 0) {
			img = new ImageTag();
			urlStr = "/user/showuserphoto?Act=idiograph&userid=" + userid;
			img.setImageURL(urlStr);
			img.setAttribute("width", "80");
			img.setAttribute("height", "30");
			log.WriteLog("img != null");
		}
		log.WriteLog("getIdiograph", "结束调用");
		return img;
	}

	// 转换字符串为HTML文本
	private String HTMLEncode(String s) {
		byte[] bc = null;
		int byteCount = 0;
		int len = 0;
		String tmpStr = "";
		if (s != null && !s.equals("")) {
			bc = s.getBytes();
			byteCount = bc.length;
			if (byteCount < size) { // 可以在打印控件中增加align属性来定义对齐方式
				len = size - byteCount;
				if (align.equalsIgnoreCase("middle") || align.equalsIgnoreCase("center")) {
					for (int i = 0; i < len / 2; i++) {
						tmpStr += " ";
					}
					tmpStr += s;
					for (int i = 0; i < len / 2; i++) {
						tmpStr += " ";
					}
				} else if (align.equalsIgnoreCase("right")) {
					for (int i = 0; i < len; i++) {
						tmpStr += " ";
					}
					tmpStr += s;
				} else { // 左对齐
					tmpStr = s;
					for (int i = 0; i < size - byteCount; i++) {
						tmpStr += " ";
					}
				}
				s = tmpStr;
			}
		} else {
			s = "";
			for (int i = 0; i < size; i++) {
				s += " ";
			}
		}
		s = Replace(s, "<", "&lt;");
		s = Replace(s, ">", "&gt;");
		s = Replace(s, " ", "&nbsp;");
		s = Replace(s, "\"", "&quot;");
		s = s.replace('\r', '^');
		s = Replace(s, "^", "<br>");
		return s;
	}

	// 字符串替换函数
	private String Replace(String strSource, String strFrom, String strTo) {
		java.lang.String strDest = "";
		try {
			int intFromLen = strFrom.length();
			int intPos;
			while ((intPos = strSource.indexOf(strFrom)) != -1) {
				strDest = strDest + strSource.substring(0, intPos);
				strDest = strDest + strTo;
				strSource = strSource.substring(intPos + intFromLen);
			}
			strDest = strDest + strSource;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDest;
	}

	// 将转换为日期或时间的中文打印
	private String ConvertData(ItemField item) {
		if (item.getType().getValue() != DBType.DATE.getValue()
				&& item.getType().getValue() != DBType.DATETIME.getValue()) {
			return item.getShowValue();
		}
		Date date;
		String Year;
		String Month;
		String Day;
		String formatstr;
		
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = fmt.parse(item.getShowValue());
		} catch (ParseException ex) {
			log.WriteLog("parse为yyyy-MM-dd HH:mm:ss的格式时出现异常" + ex.getMessage());
			fmt = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = fmt.parse(item.getShowValue());
			} catch (ParseException ex1) {
				log.WriteLog("parse为yyyy-MM-dd的格式时出现异常" + ex1.getMessage());
				return item.getShowValue();
			}
		}
		/*
		 * SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); try {
		 * date = fmt.parse(item.getValue()); } catch (ParseException ex) {
		 * log.WriteLog("parse为yyyy-MM-dd HH:mm:ss的格式时出现异常" + ex.getMessage()); fmt =
		 * new SimpleDateFormat("yyyy-MM-dd"); try { date = fmt.parse(item.getValue());
		 * } catch (ParseException ex1) { log.WriteLog("parse为yyyy-MM-dd的格式时出现异常" +
		 * ex1.getMessage()); return item.getShowValue(); } }
		 */
		formatstr = mThis.getAttribute("formatstr");
		if (formatstr != null) {
			try {
				fmt = new SimpleDateFormat(formatstr);
				log.WriteLog("formatstr=" + formatstr);
				return fmt.format(date);
			} catch (Exception ex) {
				log.WriteLog("格式化为" + formatstr + "时出现异常:" + ex.getMessage());
				return item.getShowValue();
			}
		}
		String datestyle = mThis.getAttribute("datestyle");
		if (datestyle == null) {
			return item.getShowValue();
		}
		if (!datestyle.equalsIgnoreCase("CN")) {
			return item.getShowValue();
		}
		StringBuffer sb = new StringBuffer();
		Map<String, Object> numList = new HashMap<String, Object>();
		numList.put("0", "O");
		numList.put("1", "一");
		numList.put("2", "二");
		numList.put("3", "三");
		numList.put("4", "四");
		numList.put("5", "五");
		numList.put("6", "六");
		numList.put("7", "七");
		numList.put("8", "八");
		numList.put("9", "九");
		numList.put("10", "十");
		numList.put("11", "十一");
		numList.put("12", "十二");
		numList.put("13", "十三");
		numList.put("14", "十四");
		numList.put("15", "十五");
		numList.put("16", "十六");
		numList.put("17", "十七");
		numList.put("18", "十八");
		numList.put("19", "十九");
		numList.put("20", "二十");
		numList.put("21", "二十一");
		numList.put("22", "二十二");
		numList.put("23", "二十三");
		numList.put("24", "二十四");
		numList.put("25", "二十五");
		numList.put("26", "二十六");
		numList.put("27", "二十七");
		numList.put("28", "二十八");
		numList.put("29", "二十九");
		numList.put("30", "三十");
		numList.put("31", "三十一");

		fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = fmt.parse(item.getShowValue());
		} catch (ParseException ex) {
			log.WriteLog("parse出现异常" + ex.getMessage());
			return item.getShowValue();
		}
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		Year = String.valueOf(calendar.get(Calendar.YEAR));
		sb.append(numList.get(Year.substring(0, 1)));
		sb.append(numList.get(Year.substring(1, 2)));
		sb.append(numList.get(Year.substring(2, 3)));
		sb.append(numList.get(Year.substring(3)));
		sb.append("年");

		Month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		sb.append(numList.get(Month));
		sb.append("月");

		Day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

		sb.append(numList.get(Day));
		sb.append("日");

		log.WriteLog(sb.toString());

		return sb.toString();
	}
}