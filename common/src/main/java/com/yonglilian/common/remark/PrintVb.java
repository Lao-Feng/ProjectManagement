package com.yonglilian.common.remark;

public class PrintVb {

	public PrintVb() {
	}

	public static final String get() {
		StringBuffer sb = new StringBuffer();
		sb.append("dim hkey_root, hkey_path, hkey_key\n");
		sb.append("hkey_root = \"HKEY_CURRENT_USER\"\n");
		sb.append("hkey_path = \"\\Software\\Microsoft\\Internet Explorer\\PageSetup\"\n");
		sb.append("\n");
		sb.append("function pagesetup_null()\n");
		sb.append("  on error resume next\n");
		sb.append("  Set RegWsh = CreateObject(\"WScript.Shell\")\n");
		sb.append("  hkey_key = \"\\header\"\n");
		sb.append("  RegWsh.RegWrite hkey_root + hkey_path + hkey_key, \"\"\n");
		sb.append("  hkey_key = \"\\footer\"\n");
		sb.append("  RegWsh.RegWrite hkey_root + hkey_path + hkey_key, \"\"\n");
		sb.append("  hkey_key = \"\\margin_top\"\n");
		sb.append("  RegWsh.RegWrite hkey_root + hkey_path + hkey_key, \"\"\n");
		sb.append("  hkey_key = \"\\margin_right\"\n");
		sb.append("  RegWsh.RegWrite hkey_root + hkey_path + hkey_key, \"\"\n");
		sb.append("  hkey_key = \"\\margin_bottom\"\n");
		sb.append("  RegWsh.RegWrite hkey_root + hkey_path + hkey_key, \"\"\n");
		sb.append("  hkey_key = \"\\margin_left\"\n");
		sb.append("  RegWsh.RegWrite hkey_root + hkey_path + hkey_key, \"\"\n");
		sb.append("end function\n");
		sb.append("\n");
		sb.append("function pagesetup_default()\n");
		sb.append("  on error resume next\n");
		sb.append("  Set RegWsh = CreateObject(\"WScript.Shell\")\n");
		sb.append("  hkey_key = \"\\header\"\n");
		sb.append("  RegWsh.RegWrite hkey_root + hkey_path + hkey_key,\"&w&b页码，&p/&P\"\n");
		sb.append("  hkey_key = \"\\footer\"\n");
		sb.append("  RegWsh.RegWrite hkey_root + hkey_path + hkey_key, \"&u&b&d\"\n");
		sb.append("  hkey_key = \"\\margin_top\"\n");
		sb.append("  RegWsh.RegWrite hkey_root + hkey_path + hkey_key, \"5\"\n");
		sb.append("  hkey_key = \"\\margin_right\"\n");
		sb.append("  RegWsh.RegWrite hkey_root + hkey_path + hkey_key, \"4\"\n");
		sb.append("  hkey_key = \"\\margin_bottom\"\n");
		sb.append("  RegWsh.RegWrite hkey_root + hkey_path + hkey_key, \"3\"\n");
		sb.append("  hkey_key = \"\\margin_left\"\n");
		sb.append("  RegWsh.RegWrite hkey_root + hkey_path + hkey_key, \"2\"\n");
		sb.append("end function\n");
		sb.append("\n");
		sb.append("function printsetup()\n");
		sb.append("  if document.body.scrollheight < 1123 then\n");
		sb.append("    pagesetup_null\n");
		sb.append("  else\n");
		sb.append("    on error resume next\n");
		sb.append("    Set RegWsh = CreateObject(\"WScript.Shell\")\n");
		sb.append("    hkey_key = \"\\header\"\n");
		sb.append("    RegWsh.RegWrite hkey_root + hkey_path + hkey_key, \"\"\n");
		sb.append("    hkey_key = \"\\footer\"\n");
		sb.append("    RegWsh.RegWrite hkey_root + hkey_path + hkey_key, \"\"\n");
		sb.append("    hkey_key = \"\\margin_top\"\n");
		sb.append("    RegWsh.RegWrite hkey_root + hkey_path + hkey_key, \"0\"\n");
		sb.append("    hkey_key = \"\\margin_right\"\n");
		sb.append("    RegWsh.RegWrite hkey_root + hkey_path + hkey_key, \"0\"\n");
		sb.append("    hkey_key = \"\\margin_bottom\"\n");
		sb.append("    RegWsh.RegWrite hkey_root + hkey_path + hkey_key, \"0.8\"\n");
		sb.append("    hkey_key = \"\\margin_left\"\n");
		sb.append("    RegWsh.RegWrite hkey_root + hkey_path + hkey_key, \"0\"\n");
		sb.append("  end if\n");
		sb.append("end function\n");
		sb.append("\n");
		sb.append("function ini(pgval, mode)\n");
		sb.append("  Set xmldoc = createobject(\"Microsoft.XMLDOM\")\n");
		sb.append("  xmldoc.async = False\n");
		sb.append("  a = xmldoc.LoadXML(XML)\n");
		sb.append("  if a = false then\n");
		sb.append("     msgbox \"LoadXML Error！\"\n");
		sb.append("  end if\n");
		sb.append("  Set xmlele = xmldoc.documentElement\n");
		sb.append("  For Index = 0To xmlele.childNodes.length - 1\n");
		sb.append("     id = xmlele.childNodes.Item(Index).Attributes.Item(0).Text\n");
		sb.append("      x = xmlele.childNodes.Item(Index).Attributes.Item(1).Text\n");
		sb.append("      y = xmlele.childNodes.Item(Index).Attributes.Item(2).Text\n");
		sb.append("     page = xmlele.childNodes.Item(Index).Attributes.Item(4).Text\n");
		sb.append("     imglink = \"showcachet.do?type=show&id=\" + id\n");
		sb.append("     if page = pgval then\n");
		sb.append("        if mode = \"0\" then\n");
		sb.append(
				"               document.write \"<div id='divid\" & Index & \"' onmouseup='updatexml(\" & Index & \",\" & id &\",thisx(this),thisy(this))'  onmouseover=\" & chr(34) & \"MM_dragLayer('divid\" & Index &  \"','',0,0,0,0,true,false,-1,-1,-1,-1,false,false,0,'',false,'')\" &  chr(34) & \"  style='background-color:transparent;position:absolute;filter:alpha(opacity=85);cursor:default;left:\" &  x & \"px;top:\" & y & \"px;'><img style='cursor:move' Title='按住鼠标拖动可将公章拖动到适合的位置\" src='\" & imglink &\"'></div>\"\n");
		sb.append("        elseif mode = \"1\" then\n");
		sb.append("            imglink = \"showcachet.do?type=print&id=\" + id\n");
		sb.append(
				"            document.write \"<div id='divid\" & Index & \"' style='background-color:transparent;position:absolute;cursor:default;left:\" &  x & \"px;top:\" & y & \"px;'><img src='\" & imglink & \"'></div>\"\n");
		sb.append("        end if\n");
		sb.append("     end if\n");
		sb.append("     if page = \"-1\" then\n");
		sb.append("        imglink = \"showcachet.do?type=show&id=\" + id\n");
		sb.append("        x = cint(x) - (cint(pgval) - 1) * 791 \n");
		sb.append(
				"        document.write \"<div id='divid\" & Index & \"' onkeyup='if(window.event.keyCode==46){delete1(\"& Index & \")}' style='background-color:transparent;position:absolute;filter:alpha(opacity=85);cursor:default;left:\" &x & \"px;top:\" & y & \"px;'><img Title='按Del键删除！' src='\" & imglink & \"'></div>\"\n");
		sb.append("     end if\n");
		sb.append(" Next\n");
		sb.append("end Function\n");
		sb.append("\n");
		sb.append("printsetup\n");
		return sb.toString();
	}
}
