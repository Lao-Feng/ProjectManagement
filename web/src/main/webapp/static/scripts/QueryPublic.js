/**
 * 公共JS方法  ftl  2016-11-4
 * @param TdObj
 * @return
 */

/**
 * 提示信息框(右下角)
 * @param Content  提示内容
 * @return
 */
function AlertMsg(Content){
	message("系统提示：\r\n    "+Content);
}

/**
 * 表格(table)提示信息框(居中，是否操作)
 * @param tableid  表格ID
 * @param tiltle  提示内容
 * @param ajax 回调方法
 * @return
 */
function DioalgMsg(tableid,tiltle,ajax){
	var row = $('#'+tableid).datagrid('getSelected');
	if (row){
		 $.messager.confirm('提示窗口',tiltle,function(r){
			  if (r){
				  eval(ajax);
		      }
		 });
	}
}

/**
 * 获取查询引擎表单复选框勾选的字符串
 * @return
 */
function getCheBox(){
	var checkedItems = $('#data').datagrid('getChecked');
    var names = [];
	$.each(checkedItems, function(index, item){
	      names.push(item.NFZR_SID);
	});
	var chestr=names.join(",");
	return chestr;
}

/**
 * 弹出最大窗体
 * @param strURL
 * @return
 */
function OpenMenuWindow(strURL){
	var title1="";
	var w,h;
	w = document.body.clientWidth - 4;
	h = document.body.clientHeight - 4;
	$(document).click(function (e) {
		title1=($(e.target).text())+"";
		if($(e.target).text()!=""){
			title1=($(e.target).text())+"";
		}
    })
	w = screen.availWidth - 4;
	h = screen.availHeight - 4;
	setTimeout(function(){
		if(title1==""){
			title1="数据操作";
	    }
		$('#win').window({
		    width: w,
		    height: h,
		    top:4* 0.5,
            left:4* 0.5,
		    modal: false,//是否遮盖
		    collapsible:false,
		    href: strURL,
		    title: title1
		});
	},100);
}

//弹出900*700窗体
function OpenDataWindow(url){
	var title1="";
	var w,h;
	$(document).click(function (e) {
		title1=($(e.target).text())+"";
		if($(e.target).text()!=""){
			title1=($(e.target).text())+"";
		}
    })
	w = document.body.clientWidth - 4;
	h = document.body.clientHeight - 4;
    setTimeout(function(){
    	if(title1==""){
			title1="数据操作";
	    }
    	$('#win').window({
    	    width: w,
    	    height: h,
    	    top:4* 0.5,
            left:4* 0.5,
    	    modal: false,//是否遮盖
    	    collapsible:false,
    	    href: url,
    	    title: title1+""
    	});
    },100)
}

/**
 * 选择单位(查询引擎  条件输入)
 * @param itemId
 * @return
 */
function SelUnit(itemId){
$('#win').window({
    width: 300,
    height: 400,
    top:(document.body.clientHeight- 400) * 0.5,    //高居中
    left:(document.body.clientWidth- 300) * 0.5,    //宽居中
    modal: true,
    collapsible:false,
    href: "/unit/actionunit?Act=getunit&id="+itemId,

    title: "选择单位"
});
}


/**
 * 选择用户(查询引擎  条件输入)
 * @param itemId
 * @return
 */
function SelPsn(itemId){
$('#win').window({
    width: 600,
    height: 300,
    left:(document.body.clientWidth- 600) * 0.5,    //高居中
    top:(document.body.clientHeight- 300) * 0.5,//宽居中
    modal: true,
    collapsible:false,
    href: "/user/actionuser?Act=getuser&id="+itemId,
    title: "选择人员"
});
}

//导出excel
function onexcel(url){
	$('#win').window({
	    width: 380,
	    height: 280,
	    top:(document.body.clientHeight- 400) * 0.5,    //高居中
	    left:(document.body.clientWidth- 300) * 0.5,    //宽居中
	    modal: true,
	    collapsible:false,
	    href: url,
	    title: "导出excel"
	});
}

//拼音快速录入
function SetSelectValue(SelectId, DictTable){
var mFileUrl ="/query/openquerycfg?Act=selectdict&DictTable="+DictTable+"&SelectId="+SelectId;
$('#win').window({
width: 382,
height: 312,
top:(document.body.clientHeight- 400) * 0.5, //高居中
left:(document.body.clientWidth- 300) * 0.5, //宽居中
modal: true,
collapsible:false,
href: mFileUrl,
title: '拼音代码快速查询'
});
}


/**
 * 删除提示（操作）
 * @return
 */
function delData(url){

}


/**
 * 数字转中文
 * @param dValue
 * @returns
 */
function chineseNumber(dValue) {
	var maxDec = 2;
	// 验证输入金额数值或数值字符串：
	dValue = dValue.toString().replace(/,/g, "");
	dValue = dValue.replace(/^0+/, ""); // 金额数值转字符、移除逗号、移除前导零
	if (dValue == "") {
		return "零元整";
	} // （错误：金额为空！）
	else if (isNaN(dValue)) {
		return "错误：金额不是合法的数值！";
	}
	var minus = ""; // 负数的符号“-”的大写：“负”字。可自定义字符，如“（负）”。
	var CN_SYMBOL = ""; // 币种名称（如“人民币”，默认空）
	if (dValue.length > 1) {
		if (dValue.indexOf('-') == 0) {
			dValue = dValue.replace("-", "");
			minus = "负";
		} // 处理负数符号“-”
		if (dValue.indexOf('+') == 0) {
			dValue = dValue.replace("+", "");
		} // 处理前导正数符号“+”（无实际意义）
	}
	// 变量定义：
	var vInt = "";
	var vDec = ""; // 字符串：金额的整数部分、小数部分
	var resAIW; // 字符串：要输出的结果
	var parts; // 数组（整数部分.小数部分），length=1时则仅为整数。
	var digits, radices, bigRadices, decimals; // 数组：数字（0~9——零~玖）；基（十进制记数系统中每个数字位的基是10——拾,佰,仟）；大基（万,亿,兆,京,垓,杼,穰,沟,涧,正）；辅币（元以下，角/分/厘/毫/丝）。
	var zeroCount; // 零计数
	var i, p, d; // 循环因子；前一位数字；当前位数字。
	var quotient, modulus; // 整数部分计算用：商数、模数。
	// 金额数值转换为字符，分割整数部分和小数部分：整数、小数分开来搞（小数部分有可能四舍五入后对整数部分有进位）。
	var NoneDecLen = (typeof (maxDec) == "undefined" || maxDec == null || Number(maxDec) < 0 || Number(maxDec) > 5); // 是否未指定有效小数位（true/false）
	parts = dValue.split('.'); // 数组赋值：（整数部分.小数部分），Array的length=1则仅为整数。
	if (parts.length > 1) {
		vInt = parts[0];
		vDec = parts[1]; // 变量赋值：金额的整数部分、小数部分
		if (NoneDecLen) {
			maxDec = vDec.length > 5 ? 5 : vDec.length;
		} // 未指定有效小数位参数值时，自动取实际小数位长但不超5。
		var rDec = Number("0." + vDec);
		rDec *= Math.pow(10, maxDec);
		rDec = Math.round(Math.abs(rDec));
		rDec /= Math.pow(10, maxDec); // 小数四舍五入
		var aIntDec = rDec.toString().split('.');
		if (Number(aIntDec[0]) == 1) {
			vInt = (Number(vInt) + 1).toString();
		} // 小数部分四舍五入后有可能向整数部分的个位进位（值1）
		if (aIntDec.length > 1) {
			vDec = aIntDec[1];
		} else {
			vDec = "";
		}
	} else {
		vInt = dValue;
		vDec = "";
		if (NoneDecLen) {
			maxDec = 0;
		}
	}
	if (vInt.length > 44) {
		return "错误：金额值太大了！整数位长【" + vInt.length.toString() + "】超过了上限——44位/千正/10^43（注：1正=1万涧=1亿亿亿亿亿，10^40）！";
	}
	// 准备各字符数组 Prepare the characters corresponding to the digits:
	digits = new Array("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"); // 零~玖
	radices = new Array("", "拾", "佰", "仟"); // 拾,佰,仟
	bigRadices = new Array("", "万", "亿", "兆", "京", "垓", "杼", "穰", "沟", "涧", "正"); // 万,亿,兆,京,垓,杼,穰,沟,涧,正
	decimals = new Array("角", "分", "厘", "毫", "丝"); // 角/分/厘/毫/丝
	resAIW = ""; // 开始处理
	// 处理整数部分（如果有）
	if (Number(vInt) > 0) {
		zeroCount = 0;
		for (i = 0; i < vInt.length; i++) {
			p = vInt.length - i - 1;
			d = vInt.substr(i, 1);
			quotient = p / 4;
			modulus = p % 4;
			if (d == "0") {
				zeroCount++;
			} else {
				if (zeroCount > 0) {
					resAIW += digits[0];
				}
				zeroCount = 0;
				resAIW += digits[Number(d)] + radices[modulus];
			}
			if (modulus == 0 && zeroCount < 4) {
				resAIW += bigRadices[quotient];
			}
		}
		resAIW += "元";
	}
	// 处理小数部分（如果有）
	for (i = 0; i < vDec.length; i++) {
		d = vDec.substr(i, 1);
		if (d != "0") {
			resAIW += digits[Number(d)] + decimals[i];
		}
	}
	// 处理结果
	if (resAIW == "") {
		resAIW = "零" + "元";
	} // 零元
	if (vDec == "") {
		resAIW += "整";
	} // ...元整
	resAIW = CN_SYMBOL + minus + resAIW; // 人民币/负......元角分/整
	return resAIW;
}

/**
 * 中文转数字
 * @param num
 * @returns
 */
function aNumber(num) {
	var numArray = new Array();
	var unit = "亿万元$";
	for ( var i = 0; i < unit.length; i++) {
		var re = eval("/" + (numArray[i - 1] ? unit.charAt(i - 1) : "") + "(.*)" + unit.charAt(i) + "/");
		if (num.match(re)) {
			numArray[i] = num.match(re)[1].replace(/^拾/, "壹拾");
			numArray[i] = numArray[i].replace(/[零壹贰叁肆伍陆柒捌玖]/g, function($1) {
				return "零壹贰叁肆伍陆柒捌玖".indexOf($1);
			});
			numArray[i] = numArray[i].replace(/[分角拾佰仟]/g, function($1) {
				return "*" + Math.pow(10, "分角 拾佰仟 ".indexOf($1) - 2) + "+"
			}).replace(/^\*|\+$/g, "").replace(/整/, "0");
			numArray[i] = "(" + numArray[i] + ")*" + Math.ceil(Math.pow(10, (2 - i) * 4));
		} else
			numArray[i] = 0;
	}
	return eval(numArray.join("+"));
}
//---------------------------------------------------
/**
 * 作用或功能 ：检测的字符串是否有某些字符
 * @return
 * Boolean true :存在，false :不存在
 */
function CheckChar(vString,vCheckStr)
{
  var vStrLen = 0 ;
  var vCheckLen = 0;
  var CurChar ="";
  var tmpStr;
  var mStr;
  for(var i=0;i<vCheckStr.length;i++)
  {
    tmpStr=vCheckStr.charAt(i);
    for(var j=0;j<vString.length;j++)
    {
      mStr = vString.charAt(j);
      if(tmpStr==mStr)
        return true;
    }
  }
  return false;
}


/*

function trim(strInput)
function left(strString,iLength)
function right(strString,iLength)
function replaceQuotation(strInput)
function time(strD)
function getDayBetweenDates(strDate1,strDate2)
function formatDate(dtDate,strTemplate)
function onlyDate()
function isDateStr(strD)
function CheckDate()
function CheckTime()
function ValidDate(thisObj)
function isNumber(objName)
function CheckNumber()
*/



//====================================字符串控制=====================================

//去除前后空格的字符串
function trim(strInput)
{
    if (strInput != null)
		return strInput.replace(/(^\s*)|(\s*$)/g, "");
	else
		return "";
}

//获得字符串长度  NO
String.prototype.getLength = function()
{
	var iLength = this.length;
	var totalLength = 0;
	var chartmp;
	for (var i=0;i<iLength;i++)
	{
		chartmp = this.charCodeAt(i)

		if (parseInt(chartmp)>127)
			totalLength += 2;
		else
			totalLength += 1;
	}
	return totalLength;
}

//取字符串的左边几位
function left(strString,iLength)
{
	if (typeof strString!= "string")
	{
		return "";
	}

	if (typeof strString == "string" && strString.toUpperCase() == "NULL")
	{
		return "";
	}

	if ((strString == null)||(strString == "<NULL>"))
	{
		return "";
	}

	strString=trim(strString);
	return strString.substring(0,iLength);
}

//取字符串的右边几位
function right(strString,iLength)
{
	if (typeof strString!= "string")
	{
		return "";
	}

	if (typeof strString == "string" && strString.toUpperCase() == "NULL")
	{
		return "";
	}
	if ((strString == null)||(strString == "<NULL>"))
	{
		return "";
	}
	strString=trim(strString);
	return strString.substring(strString.length-iLength,strString.length);
}


function onlyDate()
{
	var src = window.event.srcElement;

	if ( 8 == window.event.keyCode ) return true;

	if (src.value.length > 4 && src.value.indexOf("-") < 1)
		src.value = "";

	if (src.value.length == 4 || src.value.length == 7 )
		src.value = src.value + "-";

	if (src.value.length == 10 && src.maxLength != 10)
		src.value = src.value + " ";

	if (src.value.length == 13)
		src.value = src.value + ":";

}

function CheckDate()
{
	var objSrc = event.srcElement;
	if (objSrc.tagName == "INPUT")
	{
		if(event.keyCode >= 48 && event.keyCode <= 57)
		{
			if(objSrc.value.length == 4 || objSrc.value.length == 7)
			{
				objSrc.value += "-";
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	return false;
}

function CheckTime()
{
	var objSrc = event.srcElement;
	if (objSrc.tagName == "INPUT")
	{
		if(event.keyCode >= 48 && event.keyCode <= 57)
		{
			if(objSrc.value.length == 4 || objSrc.value.length == 7)
			{
				objSrc.value += "-";
			}
			if (objSrc.value.length == 10 && objSrc.maxLength != 10)
			{
				objSrc.value += " ";
			}
			if (objSrc.value.length == 13)
			{
				objSrc.value += ":";
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	return false;
}



function ValidDate(thisObj)
{
  if (!isDateStr(thisObj.value)) {
    if (thisObj.value != ""){
       alert('日期有误');
       thisObj.focus();
    }
  }
}



/************************************************
function:		isNumber(objName)
purpose:		is number
parameters :	objName:object ID
return value :	TRUE---number
			FALSE---no number
*************************************************/
function  isNumber(objName)
{
	var strS;
	var iLoop;
	var iBool=1;
	var strTmp;
	var iD=0;
	var iK=0;

	strS=objName.value;
	for (iLoop=0;iLoop<strS.length;iLoop++)
	{
		strTmp=strS.charAt(iLoop);
		if (!((strTmp=="-")||(strTmp==".")||((strTmp>="0")&&(strTmp<="9"))))
		{
			iBool=0;
			break;
		}
		else
		{
			if(strTmp=="-")
			{
				iK++;
				if (iK>1)
				{
					iBool=0;
					break;
				}
				if(iLoop!=0)
				{
					iBool=0;
					break;
				}
			}
			if(strTmp==".")
			{
				iD++;
				if(iD>1)
				{
					iBool=0;
					break;
				}
			}

		}
	}
	if (iBool==0)
		return false;
	else
		return true;
}

/************************************
Method:		CheckNumber()
purpose:		校验文本输入框中键入的是否为数字字符不含小数点
parameters :
return value :	如果输入为数字返回true; 否则返回false.
************************************/
function CheckNumber()
{
	var objSrc = event.srcElement;
	if (objSrc.tagName == "INPUT")
	{
		if(event.keyCode >= 48 && event.keyCode <= 57)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	return false;
}


/************************************
Method:		CheckNumberPoint()
purpose:	校验文本输入框中键入的是否为数字字符含小数点
parameters :
return value :	如果输入为数字返回true; 否则返回false.
************************************/
function CheckNumberPoint()
{
	var objSrc = event.srcElement;
	if (objSrc.tagName == "INPUT")
	{
		if((event.keyCode >= 48 && event.keyCode <= 57) || event.keyCode == 46)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	return false;
}


function DoRefresh(Head_Url,Menu_Url,Body_Url)
{
  top.window.Head.location.href=Head_Url;
  top.window.Menu.location.href=Menu_Url;
  top.window.Body.location.href=Body_Url;
}

function DoMenu(MenuValue){
  location.href=MenuValue;
  return true;
}

//===============================全选控制===================================
//选择所有
function SelAll(SelectName)
{
  var mObject= document.all[SelectName];
  for (i = 0; i < mObject.options.length; i++)
  {
    mObject.options[i].selected = true;
  }
}

//清除所有
function NullAll(SelectName)
{
  var mObject= document.all[SelectName];
  for (i = 0; i < mObject.options.length; i++)
  {
    mObject.options[i].selected = false;
  }
}

//
function CheckBoxSel(blChecked,SelectName)
{
  if (blChecked){
    SelAll(SelectName);
  }else{
    NullAll(SelectName);
  }
}


//指定录入焦点
function SetFocus(Obj)
{
  Obj.select();
  Obj.focus();
}
//导出excel
function ExportExcel(){
   location.href=document.getElementById("excelurl").value;
}
//测试



/************************************
Purpose:	    检测录入构建的值表达式是否合法
expressionStr:	    表达式字串
inputValue :        输入的值
ReturnValue:        如果检测合法返回true
                    如果非法返回false
************************************/
function CheckInputValueExpression(expressionStr,inputValue)
{
 var consultStr,consultStr1;
 var inputValueTrim=trim(inputValue);
 consultStr=">,>=,<,<=,!=,="; //不能出现在值表达式中
  if(consultStr.indexOf(inputValueTrim)>-1)
  {
    alert("无效的录入!");
    return false;
  }

  consultStr="+,-,*,/,AND,OR,)";//不能出现在第一个

  if(consultStr.indexOf(inputValueTrim)>-1 && trim(expressionStr).length==0)
  {
    alert("无效的录入!");
    return false;
  }

  consultStr="+,-,*,/";  //不能同时出现两个
 if(consultStr.indexOf(right(expressionStr,1))>-1 && consultStr.indexOf(inputValueTrim)>-1)
 {
    alert("无效的录入!");
    return false;
 }

 consultStr="+,-,*,/";  //AND 和 OR 前不能出现的符号

 if(consultStr.indexOf(right(expressionStr,1))>-1 && (inputValueTrim=="AND" || inputValueTrim=="OR" ))
 {
    alert("无效的录入!");
   return false;
 }

consultStr="+,-,*,/,(,)";// 不能在一起的符号
consultStr1="+,-,*,/";
if(consultStr.indexOf(right(expressionStr,1))>-1 && consultStr1.indexOf(inputValueTrim) > -1)
{
  alert("无效的录入!");
  return false;
}

if(right(expressionStr,1)=="(" && inputValueTrim==")")
{
  alert("无效的录入!");
  return false;
}

consultStr="AND,OR";//不能在一起
consultStr1="AND,OR,0,1,2,3,4,5,6,7,8,9";
if(consultStr.indexOf(trim(right(expressionStr,4)))>-1 && consultStr1.indexOf(inputValueTrim)>-1)
{
  alert("无效的录入!");
  return false;
}

  return true;
}


/************************************
Purpose:	    检测录入构建的条件表达式是否合法
expressionStr:	    表达式字串
inputValue :        输入的值
ReturnValue:        如果检测合法返回true
                    如果非法返回false
Author:             NFZR
************************************/
function CheckInputWhereExpression(expressionStr,inputValue)
{
 var consultStr,consultStr1;
 var inputValueTrim=trim(inputValue);

  consultStr="+,-,*,/,AND,OR,)";//不能出现在第一个

  if(consultStr.indexOf(inputValueTrim)>-1 && trim(expressionStr).length==0)
  {
    alert("无效的录入!");
    return false;
  }

  consultStr="+,-,*,/";  //不能同时出现两个
 if(consultStr.indexOf(right(expressionStr,1))>-1 && consultStr.indexOf(inputValueTrim)>-1)
 {
    alert("无效的录入!");
    return false;
 }

 consultStr="+,-,*,/";  //AND 和 OR 前不能出现的符号

 if(consultStr.indexOf(right(expressionStr,1))>-1 && (inputValueTrim=="AND" || inputValueTrim=="OR" ))
 {
    alert("无效的录入!");
   return false;
 }

consultStr="+,-,*,/,(,)";// 不能在一起的符号
consultStr1="+,-,*,/";
if(consultStr.indexOf(right(expressionStr,1))>-1 && consultStr1.indexOf(inputValueTrim) > -1)
{
  alert("无效的录入!");
  return false;
}

if(right(expressionStr,1)=="(" && inputValueTrim==")")
{
  alert("无效的录入!");
  return false;
}

consultStr="AND,OR";//不能在一起
consultStr1="AND,OR,0,1,2,3,4,5,6,7,8,9";
if(consultStr.indexOf(trim(right(expressionStr,4)))>-1 && consultStr1.indexOf(inputValueTrim)>-1)
{
  alert("无效的录入!");
  return false;
}

  return true;
}
