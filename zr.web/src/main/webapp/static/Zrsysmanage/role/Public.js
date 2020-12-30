<!--
/**
 * 查询引擎(增加归档文件夹)
 * fileid 文件类型ID
 * @return
 */
function querFileAdd(fileid){
	parent.parent.addFile(fileid,'');//调用父页面的方法
}

/**
 * 查询引擎(编辑文件夹名称)
 * @param ID  文件夹名称ID
 * @return
 */
function querFileEidt(ID){
	parent.parent.addFile('',ID);//调用父页面的方法
}
/**
 * 查询引擎(跳转到文件选择归档页面)
 * @return
 */
function Viewclick(){
	//获取类别P1
	var P1=document.recordfrm.P1.value;
	parseButtonJs("gwgl/file/fileList.jsp?FILEID=[OA_FILENAME.FILEID]&P1="+P1);
}

function delGdwjj(){
	 var str=document.getElementsByName("chkRecord");
	 var objarray=str.length;
	 var chestr="";
	 for (var i=0;i<objarray;i++){
	     if(str[i].checked == true){
	        if (chestr==""){
	           chestr = str[i].value;
	        }else{
	           chestr = chestr + "," + str[i].value;
	        }
	      }
	 }
	 if(chestr==""){
		 alert("请选择要删除的数据!");
	 }else{
		 if(confirm("确定要删除该条数据吗?")){
			 $.ajax({
				   type: "POST", //用POST方式传输
				   dataType: "text", //数据格式:JSON
				   url: "gwgl/dbset/wjGdxz.jsp", //目标地址
				   data: "gdid="+chestr+"&P1=&TYPE=03",
				   error: function (XMLHttpRequest, textStatus, errorThrown) { },
				   success: function (strenu){
				      //刷新table
				      window.location.reload();
				   }
			});
		 }
	 }
}

/**
 * 查询引擎(获取选中的复选框主键ID)
 * @return
 */
function getCkeAllStr(ifrname){
	 var str="";
	 var main=window.frames[ifrname];
	 var str=main.document.getElementsByName("chkRecord");

	 var objarray=str.length;
	 var chestr="";
	 for (var i=0;i<objarray;i++){
	     if(str[i].checked == true){
	        if (chestr==""){
	           chestr = str[i].value;
	        }else{
	           chestr = chestr + "," + str[i].value;
	        }
	      }
	 }
	return chestr;
}


/**
 * 删除数据列表 查询引擎
 * @param ID 表主键
 * @return
 */
function delDate(ID){
	if(confirm("确定要删除该条数据吗?")){
		$.ajax({
			   type: "POST", //用POST方式传输
			   dataType: "text", //数据格式:JSON
			   url: "gwgl/dbset/deldate.jsp?del=1&DOCID="+ID+"", //目标地址
			   data: "",
			   error: function (XMLHttpRequest, textStatus, errorThrown) { },
			   success: function (msg){
			   var reg=new RegExp("\r\n","g");
			   var resultStr = msg.replace(/\ +/g, ""); //去掉空格
			     resultStr = resultStr.replace(/[ ]/g, "");    //去掉空格
			     resultStr = resultStr.replace(/[\r\n]/g, ""); //去掉回车换行
			     msg=resultStr;
		         if(msg=="true"){
		        	 parent.Dialog.alert("提示：操作成功!");
		         }else{
		        	 parent.Dialog.alert("提示：操作失败!");
		         }
		         //刷新页面
		         window.location.reload();
			   }
		   });
	}
}

/**
 * 弹出窗口 ，调用地址
 * @param id 返回ID
 * @param mc 返回名称
 * @param url 调用地址
 * @param title 窗口名称
 * @param w 窗口宽度
 * @param h 窗口高度
 * @return
 */
function openDiagCk(id,mc,url,title,w,h){
	var diag = new Dialog();
	diag.Modal = false;//没有遮盖c
	diag.Width = w;
	diag.Height = h;
	diag.Title = title;
	diag.URL = url;
	diag.show();
}

function SetWinMenu(TdObj){
        if (TdObj){
                if (TdObj.className=='Menuu'){
                           TdObj.className='Menud';
                }else{
                           TdObj.className='Menuu';

                }
        }
}


function SetHead(TdObj){
	if (TdObj){
	   if (TdObj.className=='HeadOver'){
	   	TdObj.className='HeadOut';
	   }else{
	   	TdObj.className='HeadOver';
	   }
	}
}

function SetMenuHead(TdObj){
	if (TdObj)
	{
		if (TdObj.id!=MenuAct.value)
		{
			if (TdObj.className=='PuTbMenuAct')
			{
				TdObj.className='PuTbMenu';
			}
			else
			{
				TdObj.className='PuTbMenuAct';
			}
		}
	}
}

function SetHelpMenu(TdObj){
        if (TdObj){
                if (TdObj.className=='HelpButtonAct'){
                           TdObj.className='HelpButton';
                }else{
                           TdObj.className='HelpButtonAct';
                }
        }
}


function showtime(url){
  var strValue = window.showModalDialog(url,window,"status:no;help:no;dialogHeight:530px;dialogWidth:720px");
}

/**
 * 左侧打开word控件
 * @param RecordID
 * @return
 */
function OpenWorld(RecordID){
	if (parent.Body){
		var src="../weboffice/DocumentEdit.jsp?FileType=.doc&RecordID="+RecordID;//+"&woridstr="+filename;
        parent.Body.location.href=src;
      }else{

      }
}
/**
 * 流程界面打开 word控件
 * @return
 */
function OpenWorldFrom(RecordID){
	var src="../../weboffice/DocumentEdit.jsp?FileType=.doc&RecordID="+RecordID;//+"&woridstr="+filename;
	window.CollDataTable.location.href=src;
}
/**
 * 流程界面打开 html审核表单
 * @return
 */
function OpenHtmlFrom(src){
	window.CollDataTable.location.href=src;
}
/**
 * 打开公文审批窗口
 * @param strURL
 * @return
 */
function OpenGwglFlow(strURL){
	  strURL=strURL.substring(1,strURL.length);
	  var w,h,s;
	  var MenuWindow = null;
	  w = screen.availWidth;
	  h = screen.availHeight;
	  s = "directories=no,left=0,top=0,location=no,menubar=no,resizable=yes,scrollbars=no,status=no,toolbar=no,width=" + w + ",height=" + h;
	  //strURL=strURL.substr(1);
	  strURL=strURL+"&type=0"
	  MenuWindow = window.open(strURL,"电子公文办理系统",s);
	  MenuWindow.moveTo(0,0);
	  MenuWindow.focus();
	}

//弹出最大窗体
function OpenMenuWindow(url){
//  var w,h,s;
//  var MenuWindow = null;
//  w = screen.availWidth;
//  h = screen.availHeight;
//  s = "location=no,top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=no,status=no,width=" + w + ",height=" + h;
//  MenuWindow = window.open(strURL,"电子公文办理系统",s);
//  MenuWindow.moveTo(0,0);
//  MenuWindow.focus();

//判断浏览器版本号
  var z_browser = $.browser.version;
  var z_toolbarstatus = "yes";
  var oNewWindow;
  if(z_browser === "5.0" || z_browser === "6.0"){
	  if (z_toolbarstatus=='yes'){
		  oNewWindow = window.open(url,"win_blank","height=" + screen.availHeight + ",width=" + screen.availWidth + ",channelmode=no,status=no,resizable=yes,menubar=no,location=no,toolbar=no,scrollbars=no,top=0,left=0");
	  }else{
		  oNewWindow = window.open(url,"win_blank","height=" + screen.availHeight + ",width=" + screen.availWidth + ",channelmode=no,status=no,resizable=yes,menubar=no,location=no,toolbar=no,scrollbars=no,top=0,left=0");
	  }

  }else{
	  if (z_toolbarstatus=='yes'){
		  oNewWindow = window.open(url,"win_blank","height=" + screen.availHeight + ",width=" + screen.availWidth + ",status=no,resizable=yes,menubar=no,location=no,toolbar=no,scrollbars=no,top=0,left=0");
	  }else{
		  oNewWindow = window.open(url,"win_blank","height=" + screen.availHeight + ",width=" + screen.availWidth + ",channelmode=no,status=no,resizable=no,menubar=no,location=no,toolbar=no,scrollbars=no,top=0,left=0");
	  }
  }
}

/**
 *
 * @param CurUrl
 * @param IsPopWin
 * @return
 */

//从菜单中打开链接
function OpenUrl(CurUrl,IsPopWin){
  if (CurUrl==""){
    history.back();
  }else{
    if (IsPopWin=="1"){
      if (CurUrl.indexOf("time.jsp")!=-1)
      {
        showtime(CurUrl);
      }
      else
      {
         OpenMenuWindow(CurUrl);
      }

    }
    else if(IsPopWin=="2")
    {
      SelectTaskUserLstQQ(CurUrl);
    }
    else{
      if (parent.Body){
        parent.Body.location.href=CurUrl;
      }else{
        location.href=CurUrl;
      }
    }
  }
}

//删除提示框
function ConfirmDel(FileUrl){
  if (confirm("是否确定删除？")){
    location.href = FileUrl;
  }
}

//提示信息框
function AlertMsg(Content){
  alert("系统提示：\r\n\r\n    "+Content);
}



//弹出最大窗体(打开流程)
function OpenFlowWindow(strURL){
  var w,h,s;
  var MenuWindow = null;
  w = screen.availWidth - 4;
  h = screen.availHeight - 10;
  s = "directories=no,left=0,top=0,location=no,menubar=no,resizable=yes,scrollbars=no,status=no,toolbar=no,width=" + w + ",height=" + h;
  strURL=strURL.substr(1);
  strURL=strURL+"&type=0"
  MenuWindow = window.open(strURL,"_blank",s);
  MenuWindow.moveTo(-4,-4);
  MenuWindow.focus();
}

//弹出最大窗体(打开流程)
function Opendatatest(id){
  var w,h,s;
  var MenuWindow = null;
  w = 800;
  h = 600;
  s = "directories=no,left=0,top=0,location=no,menubar=no,resizable=yes,scrollbars=no,status=no,toolbar=no,width=" + w + ",height=" + h;
  var strURL="ZrCollEngine/collengine.jsp?ID=WF00000009&DOCID="+id+"&READONLY=0&ATT=0&ISOPEN=1&REURL=";
  MenuWindow = window.open(strURL,"_blank",s);
  MenuWindow.moveTo(200,200);
  MenuWindow.focus();
}

//弹出最大窗体(打开流程)
function OpenCaseFlowWindow(strURL){
  var w,h,s;
  var OtherID = parent.parent.document.webform.OtherID.value;
  var ParentID = parent.parent.document.webform.ParentID1.value;
  var ParentID1 = parent.parent.document.webform.ParentID2.value;

  var MenuWindow = null;
  w = screen.availWidth - 4;
  h = screen.availHeight - 10;
  s = "directories=no,left=0,top=0,location=no,menubar=no,resizable=yes,scrollbars=no,status=no,toolbar=no,width=" + w + ",height=" + h;
  if(strURL.indexOf("OtherID=")==-1)
  {
    strURL=strURL+"&OtherID="+OtherID+"&ParentID="+ParentID+"&ParentID1="+ParentID1;
  }
  MenuWindow = window.open(strURL,"_blank",s);
  MenuWindow.moveTo(-4,-4);
  MenuWindow.focus();
}

//弹出帮助窗体
function OpenHelpWindow(strURL){
  var w,h,s;
  var MenuWindow = null;
  x = screen.availWidth-392;
  y = 135;
  MenuWindow = window.showModalDialog(strURL,window,"status:no;help:no;dialogHeight:576px;dialogWidth:388px;dialogLeft:"+x+";dialogTop:"+y);
}
//弹出新窗体
function OpenPopWindow(url){
  var name="PopWindow";
  var ShowPopWindow = null;
  var status="toolbar=no,location=No,resizable=no,directories=no,status=no,menubar=no,scrollbars=yes,width=220,height=400";
  ShowPopWindow = window.open(url,name,status);
  ShowPopWindow.moveTo(1,1);
  ShowPopWindow.focus();
}
//弹出900*700窗体
function OpenDataWindow(url){
  var name="PopWindow";
  var ShowPopWindow = null;
  var status="toolbar=no,location=No,resizable=yes,directories=no,status=no,menubar=no,scrollbars=yes,width=1000,height=680";
  ShowPopWindow = window.open(url,name,status);
  ShowPopWindow.moveTo(80,0);
  ShowPopWindow.focus();
}

//弹出新窗体
function OpenPopUnitWindow(url,strCode,strName){
  var name="PopWindow";
  var ShowPopWindow = null;
  var status="toolbar=no,location=No,resizable=no,directories=no,status=no,menubar=no,scrollbars=yes,width=320,height=400";
  ShowPopWindow = window.open(url+"?code="+strCode+"&name="+strName,name,status);
  ShowPopWindow.moveTo(1,1);
  ShowPopWindow.focus();
}
//弹出新窗体1(用于流程节点属性设置)
function OpenPopWindow1(url){
  var name="PopWindow";
  var ShowPopWindow = null;
  var status="toolbar=no,location=No,resizable=no,directories=no,status=no,menubar=no,scrollbars=yes,width=450,height=360";
  ShowPopWindow = window.open(url,name,status);
  ShowPopWindow.moveTo(250,200);
  ShowPopWindow.focus();
}
//弹出新窗体2(用于流程节点属性设置)
function OpenPopWindow2(url){
  var name="PopWindow";
  var ShowPopWindow = null;
  var status="toolbar=no,location=No,resizable=no,directories=no,status=no,menubar=no,scrollbars=yes,width=580,height=450";
  ShowPopWindow = window.open(url,name,status);
  ShowPopWindow.moveTo(250,190);
  ShowPopWindow.focus();
}
//弹出新窗体(用于帮助设置)
function OpenPopWindow3(url,id,name1){
  var name="PopWindow";
  var ShowPopWindow = null;
  var status="toolbar=no,location=No,resizable=no,directories=no,status=no,menubar=no,scrollbars=yes,width=220,height=400";
  url=url+'?id='+id+'&name='+name1;
  ShowPopWindow = window.open(url,name,status);
  ShowPopWindow.moveTo(1,1);
  ShowPopWindow.focus();
}
//显示记录详细内容
function ShowMsgCont(mUrl){
  var ShowMsgContWin = null;
  var WinStatus = "status=no,toolbar=no,location=no,directories=no,menubar=no,scrollbars=yes,width=450,height=350";
  ShowMsgContWin = window.open(mUrl,"ShowMsgContWin",WinStatus);
  ShowMsgContWin.moveTo(1,1);
  ShowMsgContWin.focus();
}


//选择用户
var SelUserWin = null;
function SelectTaskUserLst(path,txtUserNo,txtTrueName,Is_Type){
   var strURL = path+"?strUserNo="+txtUserNo+"&strTrueName="+txtTrueName+"&Is_Type="+Is_Type;

   SelUserWin=window.open(strURL,"UserList","toolbar=no,location=no,directories=no,status=no,menubar=no,resizable=yes,scrollbars=yes,width=400,height=500");

   SelUserWin.moveTo(1,1);
   SelUserWin.focus();
}
var SelUserWin = null;

//选择用户(模态窗口打开)
function SelectUserLstModal(path,txtUserNo,txtTrueName,Type1,Type2){
  var strURL = path+"?Type1="+Type1+"&Type2="+Type2;
  var strValue = window.showModalDialog(strURL,window,"status:no;help:no;dialogHeight:485px;dialogWidth:380px");
  var strSelect = strValue.split("/");
  document.all(txtUserNo).value = strSelect[0];
  document.all(txtTrueName).value = strSelect[1];
}
//选择用户1
var SelUserWinMsg = null;
function SelectTaskUserLstMsg(path){
   var strURL = path;
   SelUserWinMsg=window.open(strURL,"UserList","toolbar=no,location=no,directories=no,status=no,menubar=no,resizable=yes,scrollbars=yes,width=400,height=500");
   SelUserWinMsg.moveTo(1,1);
   SelUserWinMsg.focus();
}
var SelUserWinMsg = null;


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
//-->
