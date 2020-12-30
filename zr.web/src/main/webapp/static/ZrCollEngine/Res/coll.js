function numberInput(){
  if (event.keyCode < 48 || event.keyCode > 57) {
    event.returnValue = false;
  }
}


function floatInput(){
  if (event.keyCode < 45 || event.keyCode > 57){
      event.returnValue = false;
  }
  if (event.keyCode==47) {
      event.returnValue = false;
  }
}

function checkdateNull(obj,objName){
    if(obj.length==0){
	//alert("【"+objName+"】：是必填项，请填写！");
	$.messager.alert('提示','【'+objName+'】：是必填项，请填写！','warning');
 	setFocus(obj);
 	return false;
    }else{
        return checkdate(obj,objName);
    }
}

function checkdatetimeNull(obj,objName){
    if(obj.length==0){
    $.messager.alert('提示','【'+objName+'】：是必填项，请填写！','warning');
 	setFocus(obj);
 	return false;
    }else{
       return checkdatetime(obj,objName);
    }
}

function checkint(obj,objName){
   if (obj.search(/^[.0-9]+$/)==-1 && obj!="")
   {
//       alert("【"+objName+"】：只能输入数字！\n");
       $.messager.alert('提示','【'+objName+'】：只能输入数字！\n','warning');
       setFocus(obj);
       return false;
   }
   else
   {
       return true;
   }
}

function checkfloat(obj,objName)
{
  var String = obj;
  var Letters = "1234567890.";
  var i;
  var c;
  for( i = 0; i < String.length; i ++ )
  {
     c = String.charAt( i );
     if (Letters.indexOf( c ) ==-1)
     {
//      alert("【"+objName+"】：只能输入数字！\n");
      $.messager.alert('提示','【'+objName+'】：只能输入数字！\n','warning');
      setFocus(obj);
      return false;
     }
  }
  return true;
}

function checkfloatNull(obj,objName)
{
    if(obj.length==0){
    $.messager.alert('提示','【'+objName+'】：是必填项，请填写！','warning');
 	setFocus(obj);
 	return false;
    }else{
        return checkfloat(obj,objName);
    }
}

function checkintNull(obj,objName){
   if (obj=="")
   {
	   $.messager.alert('提示','【'+objName+'】：是必填项，请填写！','warning');
       setFocus(obj);
       return false;
   }
   if (obj.search(/^[.0-9]+$/)==-1)
   {

       $.messager.alert('提示','【'+objName+'】：只能输入数字！\n','warning');
       setFocus(obj);
       return false;
   }
   return true;
}

function checkdate1(dateobj){
	 var today=new Date();
	 var thisyear=today.getYear();
	 var thismon=today.getMonth()+1;
	 var thisday=today.getDate();
   var dd=dateobj;
   if (dd.length==0) return true;
   var strvalue;
   var styles=0;
   for (var i=0;i<dd.length;i++){
        var  ch=dd.substring(i,i+1);
	if ((ch<"0" || ch>"9")&&(ch!="/")&&(ch!="-"))
	{
	    $.messager.alert('提示','【'+objName+'】：请不要输入非法字符！\n','warning');
//	    dateobj="";
//	    dateobj.focus();
	    return false;
        }
	if (ch=="/") styles+=1;
	if (ch=="-") styles+=10;
   }
   if ((dd.length<8)|| ((styles!=2)&&(styles!=20)&&(styles!=0)))
   {
	$.messager.alert('提示','【'+objName+'】：请按正确的格式输入日期！yyyymmdd 或 yyyy/mm/dd 或yyyy-mm-dd','warning');
//	dateobj="";
//	dateobj.focus();
	return false;
   }

   if (styles==0)
   {
	if (dd.length!=8)
	   {
		$.messager.alert('提示','【'+objName+'】：请按正确的格式yyyymmdd输入日期！','warning');
//		dateobj="";
//		dateobj.focus();
		return false;
	   }
	strvalue=dd.substring(0,4)+'-'+dd.substring(4,6)+'-'+dd.substring(6,8);
   }

   if (styles==2)
   {
	var ch1=dd.substring(4,5);
	var ch2=dd.substring(7,8);
	if (ch1!="/" || ch2!="/" || dd.length!=10)
	   {
		var msg="请按正确的格式yyyy/mm/dd输入日期！";
		$.messager.alert('提示',msg,'warning');
//		dateobj="";
//		dateobj.focus();
		return false;
	   }
	strvalue=dd.substring(0,4)+'-'+dd.substring(5,7)+'-'+dd.substring(8,10);
   }


   if (styles==20)
   {
	var ch1=dd.substring(4,5);
	var ch2=dd.substring(7,8);
	if (ch1!="-" || ch2!="-" || dd.length!=10)
	   {
		var msg="请按正确的格式yyyy-mm-dd输入日期！";
		$.messager.alert('提示',msg,'warning');
//		dateobj="";
//		dateobj.focus();
		return false;
	   }
	strvalue=dd;
   }

   dateobj=strvalue;

   var yea=parseInt(strvalue.substring(0,4),10);
   if (yea<1900)
   {
	var msg="请输入>1900的年份！";
	$.messager.alert('提示',msg,'warning');
//	dateobj="";
//	dateobj.focus();
	return false;
   }
   var mon=parseInt(strvalue.substring(5,7),10);
   if (mon>12||mon==0)
   {
	var msg="请输入正确的月份！";
	$.messager.alert('提示',msg,'warning');
//	dateobj="";
//	dateobj.focus();
	return false;
   }

   var da=parseInt(strvalue.substring(8,10),10);
   if (da>31||da==0)
   {
	var msg="请输入正确的日期！";
	$.messager.alert('提示',msg,'warning');
//	dateobj="";
//	dateobj.focus();
	return false;
   }

   if (((mon==4)||(mon==6)||(mon==9)||(mon==11))&&(da>30))
   {
	var msg="该月日期不应超过30！";
	$.messager.alert('提示',msg,'warning');
//	dateobj="";
//	dateobj.focus();
	return false;
   }
   if ((yea%4==0 && yea%100!=0) || yea%400==0 )
   {
	if ((mon==2)&&(da>30))
	   {
		var msg="该月日期不应超过30！";
		$.messager.alert('提示',msg,'warning');
//		dateobj="";
//		dateobj.focus();
		return false;
	   }
   }
   else
   {
	if ((mon==2)&&(da>30))
	   {
		var msg="该月日期不应超过30！";
		$.messager.alert('提示',msg,'warning');
//		dateobj="";
//		dateobj.focus();
		return false;
	   }
   }


   return true;
}

function checkdatetime1(dateobj){
	 var today=new Date();
	 var thisyear=today.getYear();
	 var thismon=today.getMonth()+1;
	 var thisday=today.getDate();
   var dd=dateobj;
   if (dd.length==0) return true;
   var strvalue;
   var styles=0;
   for (var i=0;i<dd.length;i++){
        var  ch=dd.substring(i,i+1);
	if ((ch<"0" || ch>"9")&&(ch!="/")&&(ch!="-")&&(ch!=" ")&&(ch!=":"))
	{
	    var msg="请不要输入非法字符！";
	    $.messager.alert('提示',msg,'warning');
//	    dateobj="";
//	    dateobj.focus();
	    return false;
        }
	if (ch=="/") styles+=1;
	if (ch=="-") styles+=10;
        if (ch==" ") styles+=10;
        if (ch==":") styles+=10;
   }
   if ((dd.length<8)|| ((styles!=2)&&(styles!=20)&&(styles!=0)&&(styles!=5)&&(styles!=50)))
   {
	var msg="请按正确的格式输入日期时间！yyyymmddhhmmss 或 yyyy/mm/dd/hh/mm/ss 或 yyyy-mm-dd hh:mm:ss 或 yyyymmdd 或 yyyy/mm/dd 或 yyyy-mm-dd";
	$.messager.alert('提示',msg,'warning');
//	dateobj="";
//	dateobj.focus();
	return false;
   }

   if (styles==0)
   {
	if (dd.length!=8)
	   {
		var msg="请按正确的格式yyyymmdd输入日期！";
		$.messager.alert('提示',msg,'warning');
//		dateobj="";
//		dateobj.focus();
		return false;
	   }
	strvalue=dd.substring(0,4)+'-'+dd.substring(4,6)+'-'+dd.substring(6,8)+' 00:00:00';
   }

   if (styles==2)
   {
	var ch1=dd.substring(4,5);
	var ch2=dd.substring(7,8);
	if (ch1!="/" || ch2!="/" || dd.length!=10)
	   {
		var msg="请按正确的格式yyyy/mm/dd输入日期！";
		$.messager.alert('提示',msg,'warning');
//		dateobj="";
//		dateobj.focus();
		return false;
	   }
	strvalue=dd.substring(0,4)+'-'+dd.substring(5,7)+'-'+dd.substring(8,10)+' 00:00:00';
   }

   if (styles==5)
   {
	var ch1=dd.substring(4,5);
	var ch2=dd.substring(7,8);
        var ch3=dd.substring(10,11);
        var ch4=dd.substring(13,14);
        var ch5=dd.substring(16,17);
	if (ch1!="/" || ch2!="/" || ch3!="/" ||ch4!="/" ||ch5!="/" || dd.length!=19)
	   {
		var msg="请按正确的格式yyyy/mm/dd/hh/mm/ss输入日期时间！";
		$.messager.alert('提示',msg,'warning');
//		dateobj="";
//		dateobj.focus();
		return false;
	   }
	strvalue=dd.substring(0,4)+'-'+dd.substring(5,7)+'-'+dd.substring(8,10)+' '+dd.substring(11,13)+':'+dd.substring(14,16)+':'+dd.substring(17,19);
   }

   if (styles==20)
   {
	var ch1=dd.substring(4,5);
	var ch2=dd.substring(7,8);
	if (ch1!="-" || ch2!="-" || dd.length!=10)
	   {
		var msg="请按正确的格式yyyy-mm-dd输入日期！";
		$.messager.alert('提示',msg,'warning');
//		dateobj="";
//		dateobj.focus();
		return false;
	   }
	strvalue=dd+' 00:00:00';
   }

   if (styles==50)
   {
	var ch1=dd.substring(4,5);
	var ch2=dd.substring(7,8);
        var ch3=dd.substring(10,11);
        var ch4=dd.substring(13,14);
        var ch5=dd.substring(16,17);
	if (ch1!="-" || ch2!="-" || ch3!=" " ||ch4!=":" ||ch5!=":" || dd.length!=19)
	   {
		var msg="请按正确的格式yyyy-mm-dd hh:mm:ss输入日期时间！";
		$.messager.alert('提示',msg,'warning');
//		dateobj="";
//		dateobj.focus();
		return false;
	   }
	strvalue=dd.substring(0,4)+'-'+dd.substring(5,7)+'-'+dd.substring(8,10)+' '+dd.substring(11,13)+':'+dd.substring(14,16)+':'+dd.substring(17,19);
   }

   dateobj=strvalue;

   var yea=parseInt(strvalue.substring(0,4),10);
   if (yea<1900)
   {
	var msg="请输入>1900的年份！";
	$.messager.alert('提示',msg,'warning');
//	dateobj="";
//	dateobj.focus();
	return false;
   }
   var mon=parseInt(strvalue.substring(5,7),10);
   if (mon>12||mon==0)
   {
	var msg="请输入正确的月份！";
	$.messager.alert('提示',msg,'warning');
//	dateobj="";
//	dateobj.focus();
	return false;
   }

   var da=parseInt(strvalue.substring(8,10),10);
   if (da>31||da==0)
   {
	var msg="请输入正确的日期！";
	$.messager.alert('提示',msg,'warning');
//	dateobj="";
//	dateobj.focus();
	return false;
   }

   if (((mon==4)||(mon==6)||(mon==9)||(mon==11))&&(da>30))
   {
	var msg="该月日期不应超过30！";
	$.messager.alert('提示',msg,'warning');
//	dateobj="";
//	dateobj.focus();
	return false;
   }
   if ((yea%4==0 && yea%100!=0) || yea%400==0 )
   {
	if ((mon==2)&&(da>30))
	   {
		var msg="该月日期不应超过30！";
		$.messager.alert('提示',msg,'warning');
//		dateobj="";
//		dateobj.focus();
		return false;
	   }
   }
   else
   {
	if ((mon==2)&&(da>30))
	   {
		var msg="该月日期不应超过30！";
		$.messager.alert('提示',msg,'warning');
//		dateobj="";
//		dateobj.focus();
		return false;
	   }
   }


   return true;
}

function checkdate(obj,objName){
  if(obj.length==0)
  {
     return true;
  }
  if(obj.length!=10)
  {
	var msg="【"+objName+"】项填入的日期不符合标准,或者无效,具体格式如下:"+"\n"+"四位年+两位月份+两位日期"+"\n"+"例:2004-08-08";
	$.messager.alert('提示',msg,'warning');
	setFocus(obj);
 	return false;
  }
 else
 {
     var c;
     var d;

     d = obj;

     var x = d.charAt(4);
     var y = d.charAt(7);
     var pa=new Object();
     pa.a=/^[0-9]$/;
     var e=pa["a"];
     var g=e.test(x);
     var h=e.test(y);
     if (g|h) {
	var msg="【"+objName+"】项填入的日期不符合标准,或者无效,具体格式如下:"+"\n"+"四位年+两位月份+两位日期"+"\n"+"例:2004-08-08";
	$.messager.alert('提示',msg,'warning');
	setFocus(obj);
	return false;
     }

     c = d.charAt(0)+d.charAt(1)+d.charAt(2)+d.charAt(3)+d.charAt(5)+d.charAt(6)+d.charAt(8)+d.charAt(9);

     var pa=new Object();
     pa.a=/^[1-2][0-9]{3}(([0][1-9])|([1][0-2]))(([0][1-9])|([1-2][0-9])|([3][0-1]))$/;
     var d=pa["a"];
     var f=d.test(c);

     if (!f) {
        var msg="【"+objName+"】项填入的日期不符合标准,或者无效,具体格式如下:"+"\n"+"四位年+两位月份+两位日期"+"\n"+"例:2004-08-08";
        $.messager.alert('提示',msg,'warning');
         setFocus(obj);
         return false;
     }

     var year = c.charAt(0)+c.charAt(1)+c.charAt(2)+c.charAt(3);
     var month = c.charAt(4)+c.charAt(5);
     var day = c.charAt(6)+c.charAt(7);

     if(((month=='04')|(month=='06')|(month=='09')|(month=='11'))&(day=='31'))
     {
	 var msg="【"+objName+"】项填入的日期\""+month+"月\"没有31号";
	 $.messager.alert('提示',msg,'warning');
	 setFocus(obj);
	 return false;
      }

    if(month=='02')
    {
        if((day=='30')|(day=='31'))
        {
            var msg="【"+objName+"】项填入的日期\"2月\"没有这个日期";
            $.messager.alert('提示',msg,'warning');
            setFocus(obj);
            return false;
	}
	else
	{
  	     if((!(((parseInt(year)%4=='0')&(parseInt(year)%100!='0'))|(parseInt(year)%400=='0')))&(day=='29'))
	     {
                  var msg="【"+objName+"】项填入的日期\""+year+"年\"不是闰年，2月没有这个日期";
                  $.messager.alert('提示',msg,'warning');
                  setFocus(obj);
                  return false;
             }
        }
      }
      return true;
 }
}


function checkdatetime(obj,objName){
     if(obj.length==0)
     {
        return true;
     }
     if(obj.length==10)
     {
        return checkdate(obj,objName);
     }
     if(obj.length!=19)
     {
    	 var msg="【"+objName+"】项填入的日期时间位数不正确,或者无效,具体格式如下:"+"\n"+"四位年+两位月份+两位日期+空格+两位小时+两位分钟+两位秒"+"\n"+"例:2004-08-08 08:59:59";
	 $.messager.alert('提示',msg,'warning');
	 setFocus(obj);
	  return false;
     }

     var c;
     var d;
     d = obj;
     c = d.charAt(0)+d.charAt(1)+d.charAt(2)+d.charAt(3)+d.charAt(5)+d.charAt(6)+d.charAt(8)+d.charAt(9)+d.charAt(11)+d.charAt(12)+d.charAt(14)+d.charAt(15)+d.charAt(17)+d.charAt(18);

     var pa=new Object();
     pa.a=/^[1-2][0-9]{3}(([0][1-9])|([1][0-2]))(([0][1-9])|([1-2][0-9])|([3][0-1]))(([0-1][0-9])|([2][0-3]))[0-5][0-9][0-5][0-9]$/;

     var d=pa["a"];
     var f=d.test(c);
     if(!f)
     {
         var msg="【"+objName+"'】项填入的日期时间不符合标准,或者无效,具体格式如下:"+"\n"+"四位年+两位月份+两位日期+空格+两位小时+两位分钟+两位秒"+"\n"+"例:2004-08-08 08:59:59";
         $.messager.alert('提示',msg,'warning');
         setFocus(obj);
         return false;
     }

     var year = c.charAt(0)+c.charAt(1)+c.charAt(2)+c.charAt(3);
     var month = c.charAt(4)+c.charAt(5);
     var day = c.charAt(6)+c.charAt(7);

     if(((month=='04')|(month=='06')|(month=='09')|(month=='11'))&(day=='31'))
     {
//	  alert("【"+objName+"'】项填入的日期\""+month+"月\"没有31号");
	  $.messager.alert('提示','【'+objName+'】：项填入的日期'+month+'月没有31号','warning');
	  setFocus(obj);
          return false;
     }

     if(month=='02')
     {
         if((day=='30')|(day=='31'))
         {
//             alert("【"+objName+"'】项填入的日期2月没有这个日期");
             $.messager.alert('提示','【'+objName+'】：项填入的日期2月没有这个日期','warning');
             setFocus(obj);
             return false;
         }
         else
         {
              if((!(((parseInt(year)%4=='0')&(parseInt(year)%100!='0'))|(parseInt(year)%400=='0')))&(day=='29'))
              {
//                   alert("【"+objName+"'】项填入的日期\""+year+"年\"不是闰年，2月没有这个日期");
                   $.messager.alert('提示','【'+objName+'】：项填入的日期'+year+'年不是闰年，2月没有这个日期','warning');
                   setFocus(obj);
                   return false;
              }
	  }
      }
     return true;
}

function setFocus(obj)
{
	//obj.focus();
	//obj.select();
}

function Pucker(obj,id){
   if(document.getElementById(id).style.display == ""){
      document.getElementById(id).style.display = "none";
      obj.src="ZrCollEngine/Res/expand.gif";
      obj.alt="展开内容";
   }else{
      document.getElementById(id).style.display = "";
      obj.src="ZrCollEngine/Res/shrink.gif";
      obj.alt="隐藏内容";
   }
}
//http://blog.chinajavaworld.com/entry.jspa?id=3965
function VisiableFalse(id){
   document.getElementById(id).style.display = "none";
}



function CheckRepeat(Obj1Name,Obj2,strTitle)
{

  var Obj1=document.getElementsByName(Obj1Name);

   for(i=0;i<Obj1.length;i++)
  {

     if(typeof(Obj1[i]) != "undefined" && Obj1[i].type == "hidden")
     {
        if(Obj1[i].value == Obj2.value)
         {
//           alert(strTitle+"不能一样!");
           $.messager.alert('提示','【'+strTitle+'】：不能一样!','warning');
           Obj2.value="";
         }

     }

  }
}


function OpenMatchCopy(strURL,Width,Height)
{
  var rtnValue = window.showModalDialog(strURL,window,"status:no;help:no;dialogWidth:"+Width+"px;dialogHeight:"+Height+"px");
    if (rtnValue!=null && rtnValue!="" && rtnValue!="undefined")
     {
        frmColl.COLL_PKID.value = rtnValue;
        frmColl.COLL_Action.value = "read";
        frmColl.submit();
     }
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
//得到客户端上传文件的大小
function getFileSize (fileName){
 if (fileName==""){return 0;}
 if (document.layers) {
  if (navigator.javaEnabled()) {
   var file = new java.io.File(fileName);
    if (location.protocol.toLowerCase() != "file:")
     netscape.security.PrivilegeManager.enablePrivilege("UniversalFileRead");
   return file.length();
  }else
   return -1;
 }else if (document.all) {
  window.oldOnError = window.onerror;
  window.onerror = function (err) {
   if (err.indexOf("utomation") != -1) {
    return -1;
   }else
    return 0;
  };
  var fso = new ActiveXObject("Scripting.FileSystemObject");
  var file = fso.GetFile(fileName);
  window.onerror = window.oldOnError;
  return file.Size/1024;
 }
}
//拼音快速录入
function SetSelectValue(SelectId, DictTable){
var mFileUrl ="/config/actionconfigmsg?Act=selectdict&DictTable="+DictTable+"&SelectId="+SelectId;
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
