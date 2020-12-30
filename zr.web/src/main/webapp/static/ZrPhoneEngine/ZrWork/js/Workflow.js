//----------------------------------流程处理控制--------------------------
//保存
function doSave(){
    webform.OptCmd.value="SAVE";
    webform.OptCmd_Name.value="保存";
    webform.submit();
}
//移交
function devolve(){
    var mFileUrl ="";
    mFileUrl = webform.WEBPATH.value;
    mFileUrl = mFileUrl + "/flow/openflowcfg?Act=PhoneDevolve";
    document.getElementById('user-model-code').innerHTML="<iframe id=\"iframe0\"  name=\"iframe0\" onLoad=\"javascript:iFrameHeight();\"    scrolling=\"no\" src="+mFileUrl+" border=0 frameborder=0 width=99% ></iframe>";
    document.getElementById('user-model-button').innerHTML="" +
	 "<span class=\"am-modal-btn\" data-am-modal-cancel onclick=\"javascript:doClose();\">取消</span>"+
         "<span class=\"am-modal-btn\" data-am-modal-confirm onclick=\"javascript:getChild0();\">提交</span>";
    $('#my-actions').modal('close');//关闭按钮
    $('#user-model').modal('open');//弹出选择下一流程框
}

//提交
function doSubmit(){
  var needFlow = webform.NeedNewFlow.value;
  if (needFlow.length>0)
  {
     $("#public-alert-code").html("请先新建下列流程:\r\n"+needFlow);
     $("#public-alert").modal('open');
     return (false);
  }
  var iswhere = webform.ISWHERE.value;
  var str_node_e=webform.M_Node_No_E.value;
   getFz();
  //----------
}


//处理返回
function doReturn(){
  //后退保存数据-------
  var ISSAVE2=webform.ISSAVE2.value;
  //后退必填留言
  var ISLEAVE2=webform.ISLEAVE2.value;

  if (ISLEAVE2=="1")
  {
      var DoIdea = webform.DoIdea.value;
      if (DoIdea.length==0)
      {
         $("#public-alert-code").html("请先填写处理意见!");
         $("#public-alert").modal('open');
         return;
      }
  }

  if (ISSAVE2=="1")
  {
    var strPath = webform.FlowFormPath.value;
    if (strPath.match("/collect/collengine"))
    {
      if(!saveColl('1'))//先保存
      {
        return false; //检测保存未通过；
      }
    }


  }


  if (confirm("真的要返回吗?")){
    //活动处理前填写表单处理
    var ADDFORMPATH = webform.ADDFORMPATH.value;//活动处理前填写表单地址

    webform.OptCmd.value="RETURN";
    webform.OptCmd_Name.value="处理返回";
    webform.submit();

  }
}
//退回
function doUntread(){
  //后退必填留言
  var ISLEAVE2=webform.ISLEAVE2.value;
  if (ISLEAVE2=="1"){
      var DoIdea = webform.DoIdea.value;
      if (DoIdea.length==0){
         $("#public-alert-code").html("请先填写处理意见!");
         $("#public-alert").modal('open');
         return;
      }
  }
  var str_node_s=webform.S_Node_No_E.value;
  if (str_node_s.length>10)//有多分支时
  {
  //选择一分支
      SelectUpID(str_node_s);

  }else{
	  var arrlist ="";
	  var str_NodeNo = webform.S_Node_No_S_E.value;
	  arrlist = str_NodeNo.split("/");
	  str_NodeNo = arrlist[1];//得到处理人类型
          //选择处理人员
          SelectUpDoUser();

  }


}

//完成
function doFinish(){
  var strPath = webform.FlowFormPath.value;
  if (strPath.length==0)
  {
    if(!saveColl('1'))//先保存
    {
      return false; //检测保存未通过；
    }
  }
  var needFlow = webform.NeedNewFlow.value;
  if (needFlow.length>0)
  {
     $("#public-alert-code").html("请先新建下列流程:\r\n"+needFlow);
     $("#public-alert").modal('open');
     return (false);
  }

  var AttMessage = webform.AttMessage.value;//当前步骤附件提示情况
  if (AttMessage.length>0)
  {
	  $("#public-alert-code").html(AttMessage);
	  $("#public-alert").modal('open');
     return (false);
  }
  //活动处理前填写表单处理
  var ADDFORMPATH = webform.ADDFORMPATH.value;//活动处理前填写表单地址

  if (strPath.match("/collect/collengine"))
  {
     if(!saveColl('1'))//先保存
     {
       return false; //检测保存未通过；
     }
  }


  webform.OptCmd.value="FINISH";
  webform.OptCmd_Name.value="完成";

  webform.submit();
}
//直接提交下一步(处理人为本人时使用)
function doNext(){

  var needFlow = webform.NeedNewFlow.value;
  if (needFlow.length>0)
  {
     $("#public-alert-code").html("请先新建下列流程:\r\n"+needFlow);
     $("#public-alert").modal('open');

     return (false);
  }
  var AttMessage = webform.AttMessage.value;//当前步骤附件提示情况
  if (AttMessage.length>0)
  {
	  $("#public-alert-code").html(AttMessage);
	  $("#public-alert").modal('open');

     return (false);
  }

  //前进保存数据
  var ISSAVE1=webform.ISSAVE1.value;
  if (ISSAVE1 == "1")
  {
      var isFalse = window.CollDataTable.notre_saveColl();
      if (isFalse==true)
      {
        window.setTimeout(doNext1(),1000);
      }
      else
      {

      }

  }else
 {
   doNext1();
 }


   return;


}

function doNext1(){


  webform.OptCmd.value="SUBMIT";
  webform.OptCmd_Name.value="提交下一步";

  var str_node_e=webform.M_Node_No_E.value;
  var str_NodeNo = webform.M_Node_No_S_E.value;
  var arrlist ="";
  arrlist = str_NodeNo.split("/");
  webform.M_Node_No_S_E.value = arrlist[0];

  webform.Do_User_Nos.value = webform.UserNo.value;

  webform.submit();
}

//删除流程
function doDelete(){
  if (confirm("将删除当前流程及相关数据,确定要删除吗?")){
     webform.OptCmd.value="DELETE";
     webform.OptCmd_Name.value="删除";
     webform.submit();
  }
}
//初始化流程
function doInit(){
  if (confirm("将初始化流程,确定要初始化吗?")){
     webform.OptCmd.value="INIT";
     webform.OptCmd_Name.value="初始化";
     webform.submit();
  }
}
//收回
function TakeBack(){
  if (confirm("真的要收回重新处理吗?")){
        webform.OptCmd.value="TAKEBACK";
        webform.OptCmd_Name.value="收回";
        webform.submit();
  }
}

//关闭
function doClose(){
	$('#box').animate({'top':'-400px'},500);
}

//送部门承办人
function dodept()
{
   //前进必填留言
   var ISLEAVE1=webform.ISLEAVE1.value;
   if (ISLEAVE1=="1")//提交必填留言
   {
      var DoIdea = webform.DoIdea.value;
      if (DoIdea.length==0)
      {
         $("#public-alert-code").html("请先填写处理意见!");
         $("#public-alert").modal('open');
         return;
      }
   }

   var mFileUrl = webform.WEBPATH.value;
   mFileUrl = mFileUrl + "/flow/openflowcfg?Act=phoneDeptPsn";


   document.getElementById('user-model-code').innerHTML="<iframe id=\"iframe0\"  name=\"iframe0\" onLoad=\"javascript:iFrameHeight();\"    scrolling=\"no\" src="+mFileUrl+" border=0 frameborder=0 width=99% ></iframe>" ;
   document.getElementById('user-model-button').innerHTML="<span class=\"am-modal-btn\" data-am-modal-cancel onclick=\"javascript:doClose();\">取消</span>"+
        "<span class=\"am-modal-btn\" data-am-modal-confirm onclick=\"javascript:deptcb();\">提交</span>";
   //$('#box').animate({'top':'65px'},500);
	  $('#my-actions').modal('close');//关闭按钮
	  $('#user-model').modal('open');//弹出选择下一流程框

}
//确定承办
function deptcb(){
	var strValue=iframe0.window.GetUserNo();
	if (strValue==""||strValue==null){
	     $("#public-alert-code").html("请选择承办人员!");
	     $("#public-alert").modal('open');
	     return false;
	   }
	   webform.Do_User_Nos.value = strValue;
	   webform.OptCmd.value="SUBDEPT";
	   webform.OptCmd_Name.value="送部门承办";
	   webform.submit();
}

//分送
//说明：第一个参数为指定显示传阅的活动id,第二个参数为待选择传阅人的角色编号，多个编号间用,号隔开。
//第三个参数为人员编号左包含的编码,第四个参数为是否是本部门的人,参数为空时表示选择所有的人员。
//第五个参数为是否发送短信
function dosend(showsid,roles,sunitid,isdept,isSMS)
{
    var mFileUrl = webform.WEBPATH.value;
    var strTitleName = webform.TitleName.value;//标题

    mFileUrl = mFileUrl + "/flow/openflowcfg?Act=phonesendpsn&roles="+roles+"&sunitid="+sunitid+"&isdept="+isdept+"&issms="+isSMS+"&TitleName="+strTitleName;
	 document.getElementById('user-model-code').innerHTML="<iframe id=\"iframe0\"  name=\"iframe0\" onLoad=\"javascript:iFrameHeight();\"    scrolling=\"no\" src="+mFileUrl+" border=0 frameborder=0 width=99% ></iframe>";
	 document.getElementById('user-model-button').innerHTML="" +
	 "<span class=\"am-modal-btn\" data-am-modal-cancel onclick=\"javascript:doClose();\">取消</span>"+
     "<span class=\"am-modal-btn\" data-am-modal-confirm onclick=\"javascript:sendfs('"+showsid+"');\">提交</span>";
	 //$('#box').animate({'top':'65px'},500);
	  $('#my-actions').modal('close');//关闭按钮
	  $('#user-model').modal('open');//弹出选择下一流程框
}
//分送
function sendfs(showsid){
	    var strValue=iframe0.window.GetUserNo();
	    if (strValue==""||strValue==null){
		$("#public-alert-code").html("请选择人员!");
		$("#public-alert").modal('open');
	        return false;
	    }
	    var strExecuteNo = webform.Execute_No.value;//流程运转ID
	    var strcid = webform.Node_No_S.value;//当前活动编号
	    var arrlist = strValue.split("/");
	    var users="";
	    var smsvalue="";
	    users=arrlist[0];
	    if (arrlist.length==2)//自定义短信
	    {
	       smsvalue=arrlist[1];
	    }
            var mFileUrl = webform.WEBPATH.value;
            mFileUrl = mFileUrl + "/flow/openflowcfg?Act=phonedoflowsend&ExecuteNo="+strExecuteNo+"&users="+strValue+"&sid="+showsid+"&cid="+strcid+"&smsvalue="+smsvalue;
	    document.getElementById('user-model-code').innerHTML="<iframe id=\"iframe0\"  name=\"iframe0\" onLoad=\"javascript:iFrameHeight();\"    scrolling=\"no\" src="+mFileUrl+" border=0 frameborder=0 width=99% ></iframe>" ;
            document.getElementById('user-model-button').innerHTML="<span class=\"am-modal-btn\" data-am-modal-cancel onclick=\"javascript:doClose();\">确定</span>";
}

//执行分送完成--
function dosendFinish()
{
    webform.OptCmd.value="SENDFINISH";
    webform.OptCmd_Name.value="分送已阅";
    webform.submit();
}

//刷新
function doRefresh(){
  //location.href="EditFrm.jsp?Execute_No=" + webform.Execute_No.value;
}

//提示框 Value=""表示显示  Value="none"表示隐藏
function ShowWainForWin(Value){
    //显示/隐藏提交提示
    document.getElementById('wait').style.display=Value;

}

//选择用户
function SelectDoUser(){

  var mFileUrl ="";
  var str_CNodeNo = webform.Node_No_S.value;
  var str_NodeNo = webform.M_Node_No_S_E.value;
  var str_ExecuteNo = webform.Execute_No.value;
  var str_OtherID = webform.OtherID.value;
  var str_ParentID = webform.ParentID.value;
  var str_ParentID1 = webform.ParentID1.value;
  var SELECTDEPTID = webform.SELECTDEPTID.value;

  var arrlist ="";
  arrlist = str_NodeNo.split("/");
  str_NodeNo = arrlist[0];

  var AttMessage = webform.AttMessage.value;//当前步骤附件提示情况

  mFileUrl = webform.WEBPATH.value;
  mFileUrl = mFileUrl + "/flow/openflowcfg?Act=phoneuserlist&ExecuteNo="+ str_ExecuteNo +"&CNodeNo="+str_CNodeNo+"&NodeNo=" + str_NodeNo+"&OtherID=" + str_OtherID+"&ParentID=" + str_ParentID+"&ParentID1=" + str_ParentID1+"&Type=1&AttMessage="+AttMessage+"&SELECTDEPTID="+SELECTDEPTID;
  document.getElementById('user-model-code').innerHTML="<iframe id=\"iframe0\"  name=\"iframe0\" onLoad=\"javascript:iFrameHeight();\"    scrolling=\"no\" src="+mFileUrl+" border=0 frameborder=0 width=99% ></iframe>";
  document.getElementById('user-model-button').innerHTML="" +
	 "<span class=\"am-modal-btn\" data-am-modal-cancel onclick=\"javascript:doClose();\">取消</span>"+
     "<span class=\"am-modal-btn\" data-am-modal-confirm onclick=\"javascript:getChild();\">提交</span>";
  //$('#box').animate({'top':'65px'},500);
  $('#my-actions').modal('close');//关闭按钮
  $('#user-model').modal('open');//弹出选择下一流程框
  var slist = strValue.split("/");
  webform.Do_User_Nos.value = slist[0];
  webform.MsgType.value = slist[1];
  //return true;
}

//选择用户(退回)
function SelectUpDoUser(){
  var mFileUrl ="";
  var str_CNodeNo = webform.Node_No_S.value;
  var str_NodeNo = webform.S_Node_No_S_E.value;
  var str_ExecuteNo = webform.Execute_No.value;
  var str_OtherID = webform.OtherID.value;
  var str_ParentID = webform.ParentID.value;
  var str_ParentID1 = webform.ParentID1.value;

  var arrlist ="";
  arrlist = str_NodeNo.split("/");
  str_NodeNo = arrlist[0];

  var AttMessage = webform.AttMessage.value;//当前步骤附件提示情况

  mFileUrl = webform.WEBPATH.value;
  mFileUrl = mFileUrl + "/flow/openflowcfg?Act=phoneuserlist";

  mFileUrl = mFileUrl+"&ExecuteNo="+ str_ExecuteNo +"&CNodeNo="+str_CNodeNo+"&NodeNo=" + str_NodeNo+"&Type=1&AttMessage="+AttMessage;
  document.getElementById('user-model-code').innerHTML="<iframe id=\"iframe0\"  name=\"iframe0\" onLoad=\"javascript:iFrameHeight();\"    scrolling=\"no\" src="+mFileUrl+" border=0 frameborder=0 width=99% ></iframe>";
  document.getElementById('user-model-button').innerHTML="" +
	 "<span class=\"am-modal-btn\" data-am-modal-cancel onclick=\"javascript:doClose();\">取消</span>"+
     "<span class=\"am-modal-btn\" data-am-modal-confirm onclick=\"javascript:getPsn();\">提交</span>";
  //$('#box').animate({'top':'65px'},500);
  $('#my-actions').modal('close');//关闭按钮
  $('#user-model').modal('open');//弹出选择下一流程框
}

function getPsn(){
	var str_NodeNo = webform.S_Node_No_S_E.value;
	 //alert(type);
	arrlist = str_NodeNo.split("/");

	    //选择处理人员
		  var strValue=iframe0.window.GetUserNo();

		  if (strValue==""||strValue==null){
			    $("#public-alert-code").html("请选择人员");
			    $("#public-alert").modal('open');
			  }else{
				  var slist = strValue.split("/");
				  webform.Do_User_Nos.value = slist[0];
				  webform.MsgType.value = slist[1];
				  webform.S_Node_No_S_E.value = arrlist[0];
			      webform.OptCmd.value="UNTREAD";
			      webform.OptCmd_Name.value="退回";
			      webform.submit();
			  }


}

//选择下一分支节点
function SelectNextID(str_E){


  var strValue =webform.M_Node_No_S_E.value;

  if (strValue==""||strValue==null){
    $("#public-alert-code").html("请选择流程!");
    $("#public-alert").modal('open');
    return false;
  }
  var arrlist ="";
  arrlist = strValue.split("/");
  var strYcode = webform.M_Node_No_S_E.value;
  var arrlist1 ="";
  arrlist1 = strYcode.split(",");
  var strRvalue = "";
  for (i=0;i<arrlist1.length;i++)
  {
      if (arrlist1[i].match(arrlist[0]))
      {
         if (strRvalue.length>0)
         {
            strRvalue = arrlist[0]+"/"+arrlist1[i].split("/")[1] + "," + strRvalue;
         }else
         {
            strRvalue = arrlist[0]+"/"+arrlist1[i].split("/")[1];
         }
      }else
      {
         if (strRvalue.length>0)
         {
           strRvalue = strRvalue + "," + arrlist1[i];
         }else
         {
           strRvalue = arrlist1[i];
         }

      }
  }
  webform.M_Node_No_S_E.value = strRvalue;
  return true;
}
//选择分支节点
function SelectUpID(str_E){
  var mFileUrl = webform.WEBPATH.value;
  mFileUrl = mFileUrl+"/flow/openflowcfg?Act=phoneActivityList&Node_No_S="+ webform.Node_No_S.value +"&Node_No_E=" + str_E;

  document.getElementById('user-model-code').innerHTML="<iframe id=\"iframe0\"  name=\"iframe0\" onLoad=\"javascript:iFrameHeight();\"    scrolling=\"no\" src="+mFileUrl+" border=0 frameborder=0 width=99% ></iframe>";
  document.getElementById('user-model-button').innerHTML="" +
	 "<span class=\"am-modal-btn\" data-am-modal-cancel onclick=\"javascript:doClose();\">取消</span>"+
     "<span class=\"am-modal-btn\" data-am-modal-confirm onclick=\"javascript:getChildType();\">提交</span>";
  //$('#box').animate({'top':'65px'},500);
  $('#my-actions').modal('close');//关闭按钮
  $('#user-model').modal('open');//弹出选择下一流程框
}
function getChildType(){
	var strValue =iframe0.window.FunOk();
	if (strValue==""||strValue==null){
	    $("#public-alert-code").html("请选择流程！");
		$("#public-alert").modal('open');
	    return false;
	  }else{
		  webform.S_Node_No_S_E.value = strValue;
		  var arrlist ="";
		  var str_NodeNo = webform.S_Node_No_S_E.value;
		  arrlist = str_NodeNo.split("/");
		  webform.S_Node_No_S_E.value = arrlist[0];
                  webform.OptCmd.value="UNTREAD";
                  webform.OptCmd_Name.value="退回";
                  webform.submit();
	  }

}

//后退
function doUp(){
   history.back();
}
//前进
function doDown(){
   history.forward();
}
//弹出新窗体(属性设置)
function OpenSetup(url){
  var name="PopWindow";
  var ShowPopWindow = null;
  var status="toolbar=no,location=no,directories=no,status=no,menubar=no,resizable=yes,scrollbars=yes,width=500,height=380";
  ShowPopWindow = window.open(url,name,status);
  ShowPopWindow.moveTo(280,200);
  ShowPopWindow.focus();
}
//弹出新窗体1(属性设置)
function OpenSetup1(url){

  var name="PopWindow";
  var ShowPopWindow = null;
  var status="toolbar=no,location=no,directories=no,status=no,menubar=no,resizable=yes,scrollbars=yes,width=600,height=560";
  ShowPopWindow = window.open(url,name,status);
  ShowPopWindow.moveTo(200,50);
  ShowPopWindow.focus();

}
//弹出打印窗口
function Openprint(url){
  var w,h,s;
  var IndexWin = null;
  url = url +"&AID="+webform.Node_No_S.value+"&DOCID="+webform.FormID.value;

  w = screen.availWidth - 4;
  h = screen.availHeight - 10;
  s = "directories=no,left=0,top=0,location=no,menubar=no,resizable=yes,scrollbars=yes,status=yes,toolbar=no,width=" + w + ",height=" + h;
  IndexWin = window.open(url,"print",s);
  IndexWin.moveTo(-4,-4);
  IndexWin.focus();
}


//打开显示流程运行图示窗口
function OpenFlowMap(strURL){
   if(navigator.userAgent.indexOf("MSIE")>0)
   {
        window.showModalDialog(strURL,window,"status:no;help:no;resizable:yes;dialogHeight:600px;dialogWidth:800px");
   }
}

//弹出最大窗体(新建流程)
function OpenCLinFlowWindow(strURL){
  var w,h,s;

  var OtherID = document.webform.OtherID.value;
  var ParentID = document.webform.ParentID.value;
  var ParentID1 = document.webform.ParentID1.value;

  var MenuWindow = null;
  w = screen.availWidth - 4;
  h = screen.availHeight - 10;
  s = "directories=no,left=0,top=0,location=no,menubar=no,resizable=yes,scrollbars=no,status=no,toolbar=no,width=" + w + ",height=" + h;
  if(strURL.indexOf("OtherID=")==-1)
  {
    strURL=strURL+"&OtherID="+OtherID+"&ParentID="+ParentID+"&ParentID1="+ParentID1+"&type=1";
  }
  MenuWindow = window.open(strURL,"_blank",s);
  MenuWindow.moveTo(-4,-4);
  MenuWindow.focus();

}

//弹出最大窗体(打开流程)
function OpenLinFlowWindow(strURL){
  var w,h,s;

  var OtherID = document.webform.OtherID.value;
  var ParentID = document.webform.ParentID.value;
  var ParentID1 = document.webform.ParentID1.value;

  var MenuWindow = null;
  w = screen.availWidth - 4;
  h = screen.availHeight - 10;
  s = "directories=no,left=0,top=0,location=no,menubar=no,resizable=yes,scrollbars=no,status=no,toolbar=no,width=" + w + ",height=" + h;
  if(strURL.indexOf("OtherID=")==-1)
  {
    strURL=strURL+"&OtherID="+OtherID+"&ParentID="+ParentID+"&ParentID1="+ParentID1+"&type=1";
  }
  MenuWindow = window.open("../../"+strURL,"_blank",s);
  MenuWindow.moveTo(-4,-4);
  MenuWindow.focus();

}

/************************************
Purpose:	    检测录入构建的值表达式是否合法
expressionStr:	    表达式字串
inputValue :        输入的值
ReturnValue:        如果检测合法返回true
                    如果非法返回false
Author:
Date:               2007-3-8
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
if(consultStr.indexOf(trim(right(expressionStr,4)))>-1 && consultStr1.indexOf(inputValueTrim)>-1 && trim(expressionStr).length>0)
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
Author:
Date:               2007-3-8
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
if(consultStr.indexOf(trim(right(expressionStr,4)))>-1 && consultStr1.indexOf(inputValueTrim)>-1 && trim(expressionStr).length>0)
{
  alert("无效的录入!");
  return false;
}

  return true;
}


/**************************新加方法2011-04-20 ****************************************/
function saveColl1(type,id){
   if (isSave=="1")
   {return;}
   isSave=="1";
   //显示提示
  /// ShowWainForWin("");
   webform.type.value = "";//清空
   webform.type.value = type;


  //前进必填留言
  var ISLEAVE1=webform.ISLEAVE1.value;
  if (type=="2" && ISLEAVE1=="1")//提交必填留言
  {
      var DoIdea = webform.DoIdea.value;
      if (DoIdea.length==0)
      {
         $("#public-alert-code").html("请先填写留言或意见!");
         $("#public-alert").modal('open');
         return;
      }
  }


  //前进保存数据
  var ISSAVE1=webform.ISSAVE1.value;
  if (ISSAVE1 == "1")
  {

      var isFalse = window.CollDataTable.notre_saveColl();
      if (isFalse==true)
      {
        window.setTimeout(doSubmit1(id),1000);
      }
      else
      {

      }

  }else
 {
   doSubmit1(id);
 }


   return;
}




//提交
function doSubmit1(id){

  var needFlow = webform.NeedNewFlow.value;
  if (needFlow.length>0)
  {
     $("#public-alert-code").html("请先新建下列流程:\r\n"+needFlow);
		$("#public-alert").modal('open');
     return (false);
  }

  var atttype = webform.ATTTYPE.value;
  if (atttype=="2" || atttype=="3")//可上传附件
  {
  var attnum1 = webform.ATTNUM.value;
  var attnum2 = document.att.document.RegForm.ATTNUM.value;
  if (attnum2<attnum1)//附件没有上传完
  {
     var attnum3 = attnum1-attnum2;
     webform.AttMessage.value ="请先上传附件!\r\n"+"已上传"+attnum2+"个,还需上传"+attnum3+"个。";
  }else
  {
    webform.AttMessage.value ="";
  }
  }
  var AttMessage = webform.AttMessage.value;//当前步骤附件提示情况
  if (AttMessage.length>0)
  {
     $("#public-alert-code").html(AttMessage);
     $("#public-alert").modal('open');
     return (false);
  }

  webform.M_Node_No_S_E.value=id;
  var arrlist ="";
  var str_NodeNo =webform.M_Node_No_S_E.value;
  var isSelectUser = "0";

    //选择处理人员
    if (SelectDoUser()){
      isSelectUser = "1";
    }


  if (isSelectUser == "1")
  {
    webform.OptCmd.value="SUBMIT";
    webform.OptCmd_Name.value="提交";
    webform.submit();
   }
}

function doUntread1(id){
  //后退必填留言
  var ISLEAVE2=webform.ISLEAVE2.value;
  webform.S_Node_No_S_E.value = id;
  webform.M_Node_No_S_E.value = id;

  if (ISLEAVE2=="1")
  {
      var DoIdea = webform.DoIdea.value;
      if (DoIdea.length==0)
      {
         $("#public-alert-code").html("请先填写留言或意见!");
         $("#public-alert").modal('open');
         return;
      }
  }
  saveColl('1');//直接保存
  var str_node_s=webform.S_Node_No_E.value;
  if (str_node_s.length>10)//有多分支时
  {
  //选择一分支
  if (!SelectUpID(str_node_s))
  {

  return (false);
  }
  }

    //选择处理人员
    if (SelectUpDoUser()){
      webform.OptCmd.value="UNTREAD";
      webform.OptCmd_Name.value="退回";
      webform.submit();
    }

}
//确认处理(提交,多人同时处理提交时用到)
function submitFinish()
{
       var strPath = webform.FlowFormPath.value;
       if (strPath.match("/collect/collengine"))
       {
         if(!saveColl('1'))//先保存
         {
           return false; //检测保存未通过；
         }
       }
       dosendFinish();

}
//确认处理(退回,多人同时处理退回时用到)
function UntreadFinish()
{
    var strPath = webform.FlowFormPath.value;
    if (strPath.match("/collect/collengine"))
    {
      if(!saveColl('1'))//先保存
      {
         return false; //检测保存未通过；
      }
    }
    dosendFinish();
}

