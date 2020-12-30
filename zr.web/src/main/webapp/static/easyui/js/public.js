
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

//-------------------------------------Body Menu Start----------------------------
function ClickAll(){
  if (document.recordfrm.selectall.checked){
    SelectAll();
    document.recordfrm.selectall.checked=false;
  }else{
    SelectAll()
    document.recordfrm.selectall.checked=true;
  }
}

function SelectAll(){
        var setcheck,length;
        length = document.recordfrm.elements.length ;
        if (document.recordfrm.setall.value == 0){
                document.recordfrm.setall.value = 1;
                setcheck = true;
        }else{
                document.recordfrm.setall.value = 0;
                setcheck = false;
        }
        for (var i=0; i<length; i++) {
                if (document.recordfrm.elements[i].name.indexOf("record") != -1)
                        document.recordfrm.elements[i].checked = setcheck;
        }
        return;
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
