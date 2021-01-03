/**
 * 判断是否发生异常
 * @param json
 * @returns {Boolean}
 */
function areActionOrFieldErrorInJson(json) {
    var errorsExist = false;

    if ( json != null && ( json.actionErrors != null || json.fieldErrors != null ) ) {
        errorsExist = true;
    }
    return errorsExist;
}

/**
 * 判断是否发生异常
 * 
 * @param json
 * @returns {Boolean}
 */
function showActionOrFieldErrorInJson(json,callback) {
    if ( json != null && ( json.actionErrors != null || json.fieldErrors != null ) ) {
    	Dialog.alert(json.actionErrors[0]);
    	setTimeout(function(){
    		Dialog.close();
    	}, 2000);
    }else{
    	callback();
    }
}

/**
 * 格式化数字
 * @param f 值
 * @param dec 小数点后几位
 * @param isFloat 是否返回浮点数
 * @returns
 */
function tofloat(f,dec,isFloat){ 
	if(f == null || f == undefined || f == "")f = 0;
	if(dec<0)return "Error:dec<0!";     
	var result=parseInt(f)+(dec==0?"":".");     
	f-=parseInt(f);
	if(f==0)
		for(var i=0;i<dec;i++)    result+='0';     
	else{
		f += 1;
		for(var i=0;i<dec;i++)f*=10;
		var pointerPart = ""+parseInt(Math.round(f));
		result+=pointerPart.substring(1);     
	}
	if(isFloat){
		return parseFloat(result);  
	}else{
		return "&yen;"+result;  
	}
	   
}

/**
 * 格式化百分比
 * @param f 值
 * @param dec 小数点后几位
 * @returns
 */
function toPercent(f,dec){
	if(f == null || f == undefined || f == "")f = 0;
	if(dec<0)return "Error:dec<0!";     
	f = tofloat(f,dec+3,true);
	return (Math.round(f * 10000)/100).toFixed(2) + '%';
}

/**
 * 日期格式化
 * @param date 日期和字符串均可
 * @param format
 * @returns
 */
function dateFormat(date,format){
	if(date == null || date == undefined)return "";
	if(typeof date == 'string'){
		date = new Date(date.replace("T"," ").replace(/-/g,"/"));
	}
	var o = { 
		"M+" : date.getMonth()+1, //month 
		"d+" : date.getDate(), //day 
		"h+" : date.getHours(), //hour 
		"m+" : date.getMinutes(), //minute 
		"s+" : date.getSeconds(), //second 
		"q+" : Math.floor((date.getMonth()+3)/3), //quarter 
		"S" : date.getMilliseconds() //millisecond 
	};
	
	
	if(/(y+)/.test(format)) { 
		format = format.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	} 
	for(var k in o) { 
		if(new RegExp("("+ k +")").test(format)) { 
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
		} 
	} 
	return format; 

}

/**
 * 增加日期
 * @param dateStr 日期和字符串均可
 * @param addNum
 * @param type
 * @returns
 */
function addDate(date,addNum,type){
	if(date == null || date == undefined)return null;
	if(typeof date == 'string'){
		date = new Date(date.replace("T"," ").replace(/-/g,"/"));
	}
	var year = date.getFullYear();
	var month = date.getMonth();
	var day = date.getDate();
	if(type == "Y"){
		date.setFullYear(year+addNum, month, day);
		return date;
	}else if(type == "M"){
		var months = year*12+month+addNum;
		date.setFullYear(months/12, months%12, day);
		return date;
	}else if(type == "D"){
		 var time = date.getTime();
		 var addtime = addNum * 1000 * 3600 * 24;
		 time += addtime;
		 var result = new Date(time);
		 return result;
	}else{
		return null;
	}
}

/**
 * 日期查询条件区间
 * @param id
 * @param limitNum
 * @param type
 */
function datePickerDuring(id,limitNum,type,isNowLimit){
	id = id?(id + " "):"";
	$(id + '.datepicker').eq(0).datepicker('option',{
		"onSelect":function(dateText, inst){
			var maxDate = new Date();
			$(id + '.datepicker').eq(1).datepicker('option',{"minDate":dateText});
			if(limitNum){
				var newDate = addDate(dateText,limitNum,type);
				if(isNowLimit){
					if(newDate < maxDate)maxDate = newDate;
				}else{
					maxDate = newDate;
				}
			}
			$(id + '.datepicker').eq(1).datepicker('option',{"maxDate":maxDate});
		}
	}); 
	$(id + '.datepicker').eq(1).datepicker('option',{
		"onSelect":function(dateText, inst){
			$(id + '.datepicker').eq(0).datepicker('option',{"maxDate":dateText});
			if(limitNum){
				var minDate = addDate(dateText,-limitNum,type);
				$(id + '.datepicker').eq(0).datepicker('option',{"minDate":minDate});
			}
		}
	}); 
}

/**
 * 以固定宽度展示文字
 * @param obj
 * @param value
 * @param limit
 */
function showStrWithFixLength(obj,value,limit){
	if(limit == undefined || limit == null)limit = 5;
	value = $.trim(value);
	if(value.length > limit){
		$(obj).html(value.substring(0,limit)+"...");
		$(obj).attr("title",value);
	}else{
		$(obj).html(value);
	}
}

/**
 * 获取固定长度文字
 * @param value
 * @param limit
 */
function getStrWithFixLength(value,limit){
	if(limit == undefined || limit == null)limit = 5;
	value = $.trim(value);
	if(value.length > limit){
		return value.substring(0,limit)+"...";
	}else{
		return value;
	}
}

/**
 * 获取加载的表格的滚动条的实现
 * @param objId   滚动条最外层的div的id
 * @param listArray   右侧上部的模块的class 例如：面包屑导航模块 工具条模块 搜索模块 
 */
function iframeScroll(objId,listArray){
	var WindowH = $(window.parent).height();
	var totalHeight=0;
	for(var i =0;i<listArray.length;i++){
	
		totalHeight +=  $("."+listArray[i]).outerHeight()+parseInt($("."+listArray[i]).css("margin-top"))+parseInt($("."+listArray[i]).css("margin-bottom"));
		
	}
	var headerH = 100;//公共头部的固定高度
	var footerH = 32;//公共底部的固定高度
	var fastTrack = 24;//底部漂浮fastTrack固定的高度
	var oListDivH = WindowH-totalHeight-headerH-footerH-fastTrack-10;	
	
	$("#"+objId).height(oListDivH);
	$("#sideright").height(WindowH-footerH-headerH);
	$("#mainFrame").height(WindowH-footerH-headerH);
	$(".poParent").height(WindowH-footerH-headerH);
	
}
function alertIframe(objId,listArray){
	var alertWinH =$(window).height();
	var totalHeight=0;
	for(var i =0;i<listArray.length;i++){
	
		totalHeight +=  $("."+listArray[i]).outerHeight()+parseInt($("."+listArray[i]).css("margin-top"))+parseInt($("."+listArray[i]).css("margin-bottom"));
		
	}
	var oiframeH = alertWinH-totalHeight-100;
	$("#"+objId).height(oiframeH);
}

function changeJsp(count,orgname,state){
	var res = '<a name = "'+orgname+'" id="'+state+'" onclick="selectState(this)">'+count+'</a>';
	return res;
} 

function selectState(obj){
	//alert("OK");
	//var orgState = obj.attributes("id").value;
	//var orgName = obj.attributes("name").value;
	//alert(orgName+"*#*"+orgState);
	//submitseach();
}

//查找所有input并清空——马英杰
function clearAllInp(formname){ 
	var selform = document.getElementById(formname);
	var elements = selform.elements;
	for (var i = 0; i < elements.length; i++) {
  //控件的type属性,
		if ($(elements[i]).is('input')&&$(elements[i]).attr("type")=='text') 
			$(elements[i]).val("");
		
		if ($(elements[i]).is('select')){
			$(elements[i]).find("option[value='']").attr("selected",true);
		}
		
		if ($(elements[i]).is('input')&&$(elements[i]).attr("type")=='checkbox') 
		{}
		
		if ($(elements[i]).is('input')&&$(elements[i]).attr("type")=='radio') 
		{}
	}
}


//查找所有input并显示为纯文本效果——马英杰
function changeInpText(formname){ 
	var selform = document.getElementById(formname);
	var elements = selform.elements;
	for (var i = 0; i < elements.length; i++) {
  //控件的type属性,
		if ($(elements[i]).is('input')&&$(elements[i]).attr("type")=='text') {
			$(elements[i]).attr("style","border:0;background:transparent;");//text类型变成文本
			$(elements[i]).attr("readonly","readonly");//将input元素设置只读
		}
		if ($(elements[i]).is('textarea')) 
		{
			$(elements[i]).attr("style","border:0;background:transparent;");//textarea类型变成文本
			$(elements[i]).attr("readonly","readonly");//将input元素设置只读
		}
		
		if ($(elements[i]).is('input')&&$(elements[i]).attr("type")=='select') 
		{}
		
		if ($(elements[i]).is('input')&&$(elements[i]).attr("type")=='checkbox') 
		{}
		
		if ($(elements[i]).is('input')&&$(elements[i]).attr("type")=='radio') 
		{}
	}
}

function getTaActionList(){
	
}

/**
 * 杨玉帝
 * 返回
 */
function back(form,url){
	$(form).attr("action", url);
	$(form).submit();
}

var reg=/^\d+(\.\d{1,3})?$/;
/**
 * 杨玉帝
 * 格式化数字、四舍五入
 */
function forDight(Dight,How){  
    Dight = Math.round(Dight*Math.pow(10,How))/Math.pow(10,How);  
    return Dight;  
}

/**
 * 杨玉帝
 * 自动调节Iframes高度
 */
function iFrameHeight(frame) { 
	var ifm= document.getElementById(frame); 
	var subWeb = document.frames ? document.frames[frame].document : ifm.contentDocument; 
	if(ifm != null && subWeb != null) { 
		ifm.height = subWeb.body.scrollHeight; 
	} 
}

var $regexs = {
		require : /.+/,
		email : /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
		phone : /^((\(\d{2,3}\))|(\d{2,3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/,
		mobile : /^(13[0-9]|15[0|1|2|3|5|6|7|8|9]|18[2|6|8|7|9])\d{8}$/,
		url : /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"])*$/,
		
		ip : /^(0|[1-9]\d?|[0-1]\d{2}|2[0-4]\d|25[0-5]).(0|[1-9]\d?|[0-1]\d{2}|2[0-4]\d|25[0-5]).(0|[1-9]\d?|[0-1]\d{2}|2[0-4]\d|25[0-5]).(0|[1-9]\d?|[0-1]\d{2}|2[0-4]\d|25[0-5])$/,
		currency : /^\d+(\.\d+)?$/,
		number : /^\d+$/,
		zip : /^[1-9]\d{5}$/,
		qq : /^[1-9]\d{4,9}$/,
		english : /^[A-Za-z]+$/,
		chinese : /^[\u0391-\uFFE5]+$/,
		username : /^[a-zA-Z0-9_]{4,15}$/,
		integer : /^[-\+]?\d+$/,
		'double' : /^[-\+]?\d+(\.\d+)?$/,
		mobiles : /^(((\(\d{2,3}\))|(\d{2,3}\-))?((1\d{10})))(,((\(\d{2,3}\))|(\d{2,3}\-))?((1\d{10}))){0,8}$/,
		phones : /^((\(\d{2,3}\))|(\d{2,3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?(,((\(\d{2,3}\))|(\d{2,3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?){0,8}$/,
		contact : /^((((\(\d{2,3}\))|(\d{2,3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?)|(((\(\d{2,3}\))|(\d{2,3}\-))?((1\d{10}))))(,((((\(\d{2,3}\))|(\d{2,3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?)|(((\(\d{2,3}\))|(\d{2,3}\-))?((1\d{10}))))){0,8}$/,
		password : /^[a-zA-Z0-9_]{4,15}$/,
		nickName : /^[\u4e00-\u9fa5A-Za-z0-9\-\_]{2,16}$/,
		personalSign : /^[\u0391-\uFFE5]|[a-zA-Z0-9]{0-100}$/,
		cardNo : /([1-6]\d{5}(19|20)\d\d(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])\d{3}[0-9xX])|([1-6]\d{5}\d\d(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])\d{3})/
	};


/**
 * 获取当前时间 yyyy-mm-dd HH:MM:ss
 * @returns {String}
 */
function currentTime(){
    var date = new Date();
    var y = date.getFullYear();
    var m = date.getMonth()+1;
    var d = date.getDate();
    var h = date.getHours();
    var i = date.getMinutes();
    var s = date.getSeconds();
    return y+"-"+(m>9?m:("0"+m))+"-"+(d>9?d:("0"+d))+" "+(h>9?h:("0"+h))+":"+(i>9?i:("0"+i))+":"+(s>9?s:("0"+s));  
}

/**
 * 获取当前时间
 * @returns {String}yyyy-mm-dd
 */
function currentDate(){
    var date = new Date();
    var y = date.getFullYear();
    var m = date.getMonth()+1;
    var d = date.getDate();
    return y+"-"+(m>9?m:("0"+m))+"-"+(d>9?d:("0"+d));
  }

/**
 * 徐保文添加 遮罩
 * @param str
 */
function openShade(str){
	$.messager.progress({
        msg:str
    });
}

function closeShade(){
	$.messager.progress('close');
}