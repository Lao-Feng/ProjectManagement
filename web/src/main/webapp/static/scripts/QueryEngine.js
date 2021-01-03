/************************************************************************************/
/****   表格前台JS类        ***********************************************************/
/****   主要功能：读取指定字段名选中行的值                       ×××××××××××/
 /*************************************************************************************/
//   类名：TableGrid
function TableGrid(fieldName) {
	//私有变量
	var fieldName; //字段名
	fieldName = fieldName;

	/***********************************************/
	/*得到指定的字段名的值                            */
	/***********************************************/
	this.GetCheckFieldValue = GetCheckFieldValue;
	function GetCheckFieldValue() {
		var fieldValue;
		var getrow = document.getElementById("selectrow").value;

		fieldName = fieldName.replace(".", "_");
		fieldValue = mygrid.getCellValue(fieldName, getrow);
		return fieldValue;

	}

}

//   类名：TableGrid1
function TableGrid1(fieldName) {
	//私有变量
	var objTableTou; //表格对象
	var objTableTi;
	var fieldName; //字段名
	//objTableTou=document.getElementById("tableTou");
	objTableTi = document.getElementById("tableTi");
	fieldName = fieldName;

	/***********************************************/
	/*得到指定的字段名的值                            */
	/***********************************************/
	this.GetCheckFieldValue = GetCheckFieldValue;
	function GetCheckFieldValue() {
		var fieldValue;

		var rowSequence = document.getElementById("selectrow").value;

		if (rowSequence == "0") {
			alert("请选择一条操作记录!");
			return null;
		}

		var colSequence = GetColSequence();

		if (rowSequence > -1 && colSequence > -1) {
			fieldValue = objTableTi.rows[rowSequence].cells[colSequence]
					.getAttribute("fieldValue");
		} else {
			alert("不存在指定字段的值，按钮参数设置有误，请检查！");
			fieldValue = null;
		}

		return fieldValue;

	}

	/**********************************************/
	/*得到指定的字段名的列序号                       */
	/**********************************************/

	this.GetColSequence = GetColSequence;
	function GetColSequence() {
		var rowObj = objTableTi.rows[0];
		var cellObj;
		var colSequence = -1;
		var js = 0;
		var js1 = 0;

		for ( var i = 0; i < rowObj.cells.length; i++) {
			cellObj = rowObj.cells[i];
			js1 = cellObj.getAttribute("colspan");
			if (js1 > 1) {
				js = js + (js1 - 1);
			}
			if (cellObj.getAttribute("fieldName") == fieldName) {
				colSequence = i;
				break;
			}
		}
		colSequence = colSequence + js;
		return colSequence;

	}

}

//得到点击行指定字段的值
function getClickColumnValue(fieldName) {
	var tableGrid;
	tableGrid = new TableGrid(fieldName);
	return tableGrid.GetCheckFieldValue()
}
//*******************************************
//***ftl  2016-11-04
//解析按钮的JS中串,并执行
function parseButtonJs(ScriptStr) {
	var beginIndex = -1;
	var endIndex = -1;
	var ScriptStrtmp,oldStr="";
	var value0="";
	//含有动态参数时
	while (ScriptStr.indexOf("[") > -1) {
		beginIndex = ScriptStr.indexOf("[");
		endIndex = ScriptStr.indexOf("]");
		fieldName = ScriptStr.substr(beginIndex + 1, endIndex - beginIndex - 1);
		oldStr=fieldName;
		fieldName=fieldName.replace(".", "_");//替换

		//含有复选框时替换主键ID
		var checkedItems = $('#data').datagrid('getChecked');
	    var names = [];
		$.each(checkedItems, function(index, item){
		   names.push(item[''+fieldName+'']);
		});
		if(names.length>0){
			value0=names.join(",");
		}else{
			message("请选择操作的数据行!");
			return false;
		}
		ScriptStrtmp = ScriptStr;
		ScriptStr = ScriptStr.replace("[" + oldStr + "]", value0);
		if (ScriptStrtmp == ScriptStr){
			break;
		}
    }
	//解数据后，再次比较是否含有要替换的字段，存在则提示
	if(ScriptStr.indexOf("[")<0){
		//不含动态参数时  跳转其他方法js
		if (ScriptStr.indexOf("(") > -1 && ScriptStr.indexOf(")") > -1) {
			eval(ScriptStr);
	    //不含任何参数及js方法，直接本页面打开
		}else if (ScriptStr.indexOf("[") == -1 && ScriptStr.indexOf("]") == -1) {
			window.location.href = ScriptStr;
		}
	}else{
		message("请选择操作的数据行!");
		return false;
	}
}

function parseButtonJs_value(ScriptStr) {
	parseButtonJs(ScriptStr);
}
/**
 * 取出选中字段对应的值，应用于按钮中
 * @param key
 * @param json
 * @return
 */
function getJson(key,json){
	key=key.replace(".", "_");//替换
	var jsonObj=json;
	return jsonObj[''+key+''];
}
//******************************************






//----------付天龙 2016-04-07 新增html样式--------------------

//解析按钮的JS中串,并执行
function parseButtonJsHtml(ScriptStr) {
	var beginIndex = -1;
	var endIndex = -1;
	var ScriptStrtmp;
	var tableGrid;

	while (ScriptStr.indexOf("[") > -1) {
		beginIndex = ScriptStr.indexOf("[");
		endIndex = ScriptStr.indexOf("]");

		fieldName = ScriptStr.substr(beginIndex + 1, endIndex - beginIndex - 1);
		tableGrid = new TableGrid(fieldName);

		if (tableGrid.GetCheckFieldValue() == null
				|| tableGrid.GetCheckFieldValue() == "&nbsp") {
			alert("请选择一条操作记录!");
			return;
		}

		ScriptStrtmp = ScriptStr;
		ScriptStr = ScriptStr.replace("[" + fieldName + "]", tableGrid
				.GetCheckFieldValue());

		if (ScriptStrtmp == ScriptStr)
			break;

	}

	if (ScriptStr.indexOf("(") > -1 && ScriptStr.indexOf(")") > -1) {
		//alert("parseButtonJsHtml=(eval)==="+ScriptStr+"===="+ScriptStr.indexOf(")"));
		eval(ScriptStr);

	} else if (ScriptStr.indexOf("[") == -1 && ScriptStr.indexOf("]") == -1) {
		//alert("parseButtonJsHtml=(location)==="+ScriptStr+"===="+ScriptStr.indexOf("]"));
		self.location = ScriptStr;
	}
}

function parseButtonJsHtml_value(ScriptStr) {

	var beginIndex = -1;
	var endIndex = -1;
	var ScriptStrtmp;
	var tableGrid;

	while (ScriptStr.indexOf("[") > -1) {

		beginIndex = ScriptStr.indexOf("[");

		endIndex = ScriptStr.indexOf("]");

		fieldName = ScriptStr.substr(beginIndex + 1, endIndex - beginIndex - 1);

		tableGrid = new TableGrid1(fieldName);

		if (tableGrid.GetCheckFieldValue() == null)
			return;

		ScriptStrtmp = ScriptStr;

		ScriptStr = ScriptStr.replace("[" + fieldName + "]", tableGrid
				.GetCheckFieldValue());

		if (ScriptStrtmp == ScriptStr)
			break;

	}

	if (ScriptStr.indexOf("(") > -1 && ScriptStr.indexOf(")") > -1) {
		if (ScriptStr.lastIndexOf("'") > 0 && ScriptStr.indexOf(".") > -1) {
			if (ScriptStr.indexOf("?") > -1) {
				ScriptStr = ScriptStr.substring(0, ScriptStr.lastIndexOf("'"))
						+ "&html5=1');";
			} else {
				ScriptStr = ScriptStr.substring(0, ScriptStr.lastIndexOf("'"))
						+ "?html5=1');";
			}
		}
		//alert("parseButtonJsHtml_value=(eval)==="+ScriptStr);
		eval(ScriptStr);

	} else if (ScriptStr.indexOf("[") == -1 && ScriptStr.indexOf("]") == -1) {
		if (ScriptStr.indexOf("?") > -1 && ScriptStr.indexOf(".") > -1) {
			ScriptStr = ScriptStr + "&html5=1";
		} else {
			ScriptStr = ScriptStr + "?html5=1";
		}
		//alert("parseButtonJsHtml_value=(eval)==="+ScriptStr);
		self.location = ScriptStr;
	}

}

//----------付天龙   结束 ----------------------------------------

//解析按钮的JS中串,并执行
function parseButtonJs_B(ScriptStr, id) {
	var beginIndex = -1;
	var endIndex = -1;
	var ScriptStrtmp;
	var tableGrid;

	while (ScriptStr.indexOf("[") > -1) {

		beginIndex = ScriptStr.indexOf("[");

		endIndex = ScriptStr.indexOf("]");

		fieldName = ScriptStr.substr(beginIndex + 1, endIndex - beginIndex - 1);

		ScriptStrtmp = ScriptStr;

		ScriptStr = ScriptStr.replace("[" + fieldName + "]", id);

		if (ScriptStrtmp == ScriptStr)
			break;

	}

	if (ScriptStr.indexOf("(") > -1 && ScriptStr.indexOf(")") > -1) {
		eval(ScriptStr);

	} else if (ScriptStr.indexOf("[") == -1 && ScriptStr.indexOf("]") == -1) {
		self.location = ScriptStr;
	}

}

function Settrback(TdObj) {
	var objTi;
	objTi = document.getElementById("tableTi");
	for ( var i = 1; i < objTi.rows.length; i++) {
		if (i == TdObj) {
			document.getElementById("selectrow").value = i;
			for ( var j = 0; j < objTi.rows[i].cells.length; j++) {
				objTi.rows[i].cells[j].className = "Window_TbSE";
			}
		} else {
			if (objTi.rows[i].cells[0].className == "Window_TbSE") {
				for ( var j = 0; j < objTi.rows[i].cells.length; j++) {
					if (i % 2 == 0) {
						objTi.rows[i].cells[j].className = "Window_TbS";
					} else {
						objTi.rows[i].cells[j].className = "Window_TbB";
					}
				}
			}
		}
	}

}

//弹出打印窗口
function Openprint(url) {
	var w, h, s;
	var IndexWin = null;

	w = screen.availWidth - 4;
	h = screen.availHeight - 10;
	s = "directories=no,left=0,top=0,location=no,menubar=no,resizable=yes,scrollbars=yes,status=yes,toolbar=no,width="
			+ w + ",height=" + h;
	IndexWin = window.open(url, "", s);
	IndexWin.moveTo(-4, -4);
	IndexWin.focus();
}

//全选/取消
function SelAllChk(name) {
	var mObject = document.all[name];
	var bool = true;
	if (mObject.length > 0) {
		bool = !mObject[mObject.length - 1].checked
	}

	for (i = 0; i < mObject.length; i++) {
		mObject[i].checked = bool;
	}
}

//查询设置
function query_set(id, type) {
	var name = "PopWindow";
	var ShowPopWindow = null;
	var status = "toolbar=no,location=No,resizable=yes,directories=no,status=no,menubar=no,scrollbars=yes,width=300,height=200";
	url = "ZrQueryEngine/Config/ConfigFrm.html?ID=" + id;
	//新增遮盖层窗口
	if (type == '6') {
		OpenLaoadUrl(url, '300', '200');
	} else {
		ShowPopWindow = window.open(url, name, status);
		ShowPopWindow.moveTo(80, 0);
		ShowPopWindow.focus();
	}
}
//弹出没有标题的窗口
function openLaoadUrl(url, withs, heigths) {
	layer.open( {
		type : 2,
		title : false,
		area : [ withs, heigths ],
		shade : 0.2,
		closeBtn : 0,
		skin : 'yourclass',
		shadeClose : true,
		content : url
	});

}
