package com.yonglilian.common.remark;

public class CollJs {

	public CollJs() {
	}

	public static final String get() {
		StringBuffer sb = new StringBuffer();
		sb.append("var mCodeInput;\n");
		sb.append("function init(){\n");
		sb.append("  try{\n");
		sb.append("     mCodeInput = new ActiveXObject(\"bpipCodeInput.CodeInputX\");\n");
		sb.append("  }catch(e1){\n");
		sb.append("     alert(\"未找到相应的客户端插件，请确定是否安装了最新的客户端插件\");\n");
		sb.append("  }\n");
		sb.append("}\n");
		sb.append("function SetSelectValue(SelectId, DictTable){\n");
		sb.append("   if(mCodeInput){\n");
		sb.append("     var ime = 0;\n");
		sb.append("     if( mCodeInput.SelectDict(DictTable,ime)==1){\n");
		sb.append("        document.getElementById(SelectId).options.length = 0;\n");
		sb.append("        var oOption = document.createElement(\"OPTION\");\n");
		sb.append("        document.getElementById(SelectId).options.add(oOption);\n");
		sb.append("        document.getElementById(SelectId).options[0].innerText = mCodeInput.DictName;\n");
		sb.append("        document.getElementById(SelectId).options[0].value = mCodeInput.DictCode;\n");
		sb.append("        document.getElementById(SelectId).options[0].selected = true;\n");
		sb.append("        event.keyCode=9;\n");
		sb.append("      }\n");
		sb.append("  }else{\n");
		sb.append("     alert(\"规范字典输入控件未能加载，请确定是否已正确安装了客户端插件\");\n");
		sb.append("  }\n");
		sb.append("}\n");
		sb.append("function getSpell(name){\n");
		sb.append("  if(mCodeInput.GetSpell(name)==1){\n");
		sb.append("     return mCodeInput.Spell;\n");
		sb.append("  }else{\n");
		sb.append("    return \"\";\n");
		sb.append("  }\n");
		sb.append("}\n");
		sb.append("function KeyCode32(){\n");
		sb.append("   if(event.keyCode==32){\n");
		sb.append("     if(document.activeElement.id!=\"\" && document.activeElement.code!=null){\n");
		sb.append("        SetSelectValue(document.activeElement.id,document.activeElement.code);\n");
		sb.append("     }\n");
		sb.append("   }\n");
		sb.append("}\n");
		sb.append("init();\n");
		return sb.toString();
	}
}