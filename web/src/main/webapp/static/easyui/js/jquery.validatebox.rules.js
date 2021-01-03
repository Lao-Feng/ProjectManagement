var idCard = function (value) { 
    if (value.length == 18 && 18 != value.length) return false;
    var number = value.toLowerCase();
    var d, sum = 0, v = '10x98765432', w = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2], a = '11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65,71,81,82,91';
    var re = number.match(/^(\d{2})\d{4}(((\d{2})(\d{2})(\d{2})(\d{3}))|((\d{4})(\d{2})(\d{2})(\d{3}[x\d])))$/);
    if (re == null || a.indexOf(re[1]) < 0) return false;
    if (re[2].length == 9) {
        number = number.substr(0, 6) + '19' + number.substr(6);
        d = ['19' + re[4], re[5], re[6]].join('-');
    } else d = [re[9], re[10], re[11]].join('-');
    if (!isDateTime.call(d, 'yyyy-MM-dd')) return false;
    for (var i = 0; i < 17; i++) sum += number.charAt(i) * w[i];
    return (re[2].length == 9 || number.charAt(17) == v.charAt(sum % 11));
};

var isDateTime = function (format, reObj) {
    format = format || 'yyyy-MM-dd';
    var input = this, o = {}, d = new Date();
    var f1 = format.split(/[^a-z]+/gi), f2 = input.split(/\D+/g), f3 = format.split(/[a-z]+/gi), f4 = input.split(/\d+/g);
    var len = f1.length, len1 = f3.length;
    if (len != f2.length || len1 != f4.length) return false;
    for (var i = 0; i < len1; i++) if (f3[i] != f4[i]) return false;
    for (var i = 0; i < len; i++) o[f1[i]] = f2[i];
    o.yyyy = s(o.yyyy, o.yy, d.getFullYear(), 9999, 4);
    o.MM = s(o.MM, o.M, d.getMonth() + 1, 12);
    o.dd = s(o.dd, o.d, d.getDate(), 31);
    o.hh = s(o.hh, o.h, d.getHours(), 24);
    o.mm = s(o.mm, o.m, d.getMinutes());
    o.ss = s(o.ss, o.s, d.getSeconds());
    o.ms = s(o.ms, o.ms, d.getMilliseconds(), 999, 3);
    if (o.yyyy + o.MM + o.dd + o.hh + o.mm + o.ss + o.ms < 0) return false;
    if (o.yyyy < 100) o.yyyy += (o.yyyy > 30 ? 1900 : 2000);
    d = new Date(o.yyyy, o.MM - 1, o.dd, o.hh, o.mm, o.ss, o.ms);
    var reVal = d.getFullYear() == o.yyyy && d.getMonth() + 1 == o.MM && d.getDate() == o.dd && d.getHours() == o.hh && d.getMinutes() == o.mm && d.getSeconds() == o.ss && d.getMilliseconds() == o.ms;
    return reVal && reObj ? d : reVal;
    function s(s1, s2, s3, s4, s5) {
        s4 = s4 || 60, s5 = s5 || 2;
        var reVal = s3;
        if (s1 != undefined && s1 != '' || !isNaN(s1)) reVal = s1 * 1;
        if (s2 != undefined && s2 != '' && !isNaN(s2)) reVal = s2 * 1;
        return (reVal == s1 && s1.length != s5 || reVal > s4) ? -10000 : reVal;
    }
};



$.extend($.fn.validatebox.defaults.rules, {
	minLength : { // 判断最小长度
		validator : function(value, param) {
			return value.length >= param[0];
		},
		message : '最少输入 {0} 个字符。'
	},
	length:{validator:function(value,param){
		var len=$.trim(value).length;
			return len>=param[0]&&len<=param[1];
		},
			message:"内容长度介于{0}和{1}之间."
		},
	phone : {// 验证电话号码
		validator : function(value) {
			return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
		},
		message : '格式不正确,请使用下面格式:020-88888888'
	},
	mobile : {// 验证手机号码
		validator : function(value) {
			return /^(13|15|18)\d{9}$/i.test(value);
		},
		message : '手机号码格式不正确(正确格式如：13450774432)'
	},
	phoneOrMobile:{//验证手机或电话
		validator : function(value) {
			return /^(13|15|18)\d{9}$/i.test(value) || /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
		},
		message:'请填入手机或电话号码,如13688888888或020-8888888'
	},
	/*idcard : {// 验证身份证
		validator : function(value) {
			return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value);
		},
		validator: function(value,param){    
			 var flag= isCardID(value);  
			 return flag==true?true:false;    
		 },
		message : '身份证号码格式不正确'
	},*/
	norm : {//考核指标名称不能重复
		validator: function(value,param){    
			 //var flag= checkNorm(value);  
			 return checkNorm(value);    
		 },
		message : '考核指标名称不能重复'
	},
	idcard: {
        validator: function (value, param) {
            return idCard(value);
        },
        message:'请输入正确的身份证号码'
    },
    ecertId : {// 验证从业资格证
		validator : function(value) {
			return /^[a-zA-Z0-9]{1,19}$/i.test(value);
		},
		message:'从业资格证格式不正确'
    },
    
    pwd : {// 密码强度验证
		validator : function(value) {
			 var rank = PwdIntensity(value);
			 printIntensity(rank);
			 if(rank ==0  || rank == 1){
				 return false;
			 }else{
				 return true; 
			 }
		},
		message:'密码强度验证不安全，请重新输入密码！'
    },
    ccertId : {// 验证运输证号
		validator : function(value) {
			return /^[0-9]{12}$/i.test(value);
		},
		message:'运输证号格式不正确'
    },
    vehicleShipId : {// 验证车牌号
		validator : function(value) {
			return /^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$/i.test(value)|/^WJ[0-9A-Z]{7}$/i.test(value);
		},
		message:'车牌号格式不正确(正确如：京A1110或WJ2400001)'
    },
   NotLessThanMonth : {
	   validator : function(value, param){
			if($("#"+param[0]).val() != "" && value != ""){
				return $("#"+param[0]).val() <= value; 
			}
		},
		message : "截止日期月不能少于开始日期月"	
   },
   NotMoreThanMonth : {
	   validator : function(value, param){
			if($("#"+param[0]).val() != "" &&  value != ""){
				return $("#"+param[0]).val() >= value; 
			}
			if($("#"+param[0]).val()==""){
				return true;
			}
		},
		message : "开始日期月不能大于截止日期月"	
   },
   certificateId : {// 验证执法人员证件号
				validator : function(value) {
					return /^[a-zA-Z0-9]{1,12}$/i.test(value);
				},
				message:'执法人员证件号格式不正确'
   },
/*	notStaffNameEqual:{//验证输入框值不相等
		validator : function(value, param){
			if($("#"+param[0]).val() != "" && value != ""){
				return $("#"+param[0]).val() != value; 
			}
		},
		message : "执法人员姓名不能相同"	
	},*/
	notCertificateIdEqual:{//验证输入框值不相等
		validator : function(value, param){
			var rules = $.fn.validatebox.defaults.rules; 
			rules.notCertificateIdEqual.message="";
			if(!rules.certificateId.validator(value)){
				 rules.notCertificateIdEqual.message = rules.certificateId.message; 
	             return false; 
			}
			if($("#"+param[0]).val() != "" && value != ""){
				rules.notCertificateIdEqual.message="执法人员证件号不能相同！";
				return $("#"+param[0]).val() != value; 
			}else{
				return true;
			}
		},
		message : ''	
	},
    age:{
        validator:function(value,param){
          if(/^[1-9]\d*$/.test(value)){
            return value >= param[0] && value <= param[1];
          }else{
            return false;
          }
        },
        message:'请输入1到100之间正整数'
      },
      checkDate:{
      	validator : function(value) {
      		var systemTime=new Date().getTime(); 
      		var currentTime=new Date(value).getTime();
  			return currentTime-systemTime>0;

  		},
  		message : '截止日期大于当前日期'
      },
	floatOrInt : {// 验证是否为小数或整数
		validator : function(value) {
			return /^(\d{1,3}(,\d\d\d)*(\.\d{1,3}(,\d\d\d)*)?|\d+(\.\d+))?$/i.test(value);
		},
		message : '请输入数字，并保证格式正确'
	},
	currency : {// 验证货币
		validator : function(value) {
			return /^d{0,}(\.\d+)?$/i.test(value);
		},
		message : '货币格式不正确'
	},
	price : {//验证金额
		validator : function(value) {
		    return /^d{0,}(\.\d+)?$/i.test(value);
	    },
	    message : '输入的价格不合格'
	},

	qq : {// 验证QQ,从10000开始
		validator : function(value) {
			return /^[1-9]\d{4,9}$/i.test(value);
		},
		message : 'QQ号码格式不正确(正确如：453384319)'
	},
	integer : {// 验证整数
		validator : function(value) {
			return /^[+]?[1-9]+\d*$/i.test(value);
		},
		message : '请输入整数'
	},
	chinese : {// 验证中文
		validator : function(value) {
			return /^[\u0391-\uFFE5]+$/i.test(value);
		},
		message : '请输入中文'
	},
	english : {// 验证英语
		validator : function(value) {
			return /^[A-Za-z]+$/i.test(value);
		},
		message : '请输入英文'
	},
	unnormal : {// 验证是否包含空格和非法字符
		validator : function(value) {
			return /.+/i.test(value);
		},
		message : '输入值不能为空和包含其他非法字符'
	},
	username : {// 验证用户名
		validator : function(value) {
			return /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/i.test(value);
		},
		message : '用户名不合法（字母开头，允许6-16字节，允许字母数字下划线）'
	},
	faxno : {// 验证传真
		validator : function(value) {
//			return /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/i.test(value);
			return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
		},
		message : '传真号码不正确'
	},
	zip : {// 验证邮政编码
		validator : function(value) {
			return /^[1-9]\d{5}$/i.test(value);
		},
		message : '邮政编码格式不正确'
	},
	ip : {// 验证IP地址
		validator : function(value) {
			return /d+.d+.d+.d+/i.test(value);
		},
		message : 'IP地址格式不正确'
	},
	name : {// 验证姓名，可以是中文或英文
			validator : function(value) {
				return /^[\u0391-\uFFE5]+$/i.test(value)|/^\w+[\w\s]+\w+$/i.test(value);
			},
			message : '请输入姓名'
	},
	carNo:{
		validator : function(value){
			return /^[\u4E00-\u9FA5][\da-zA-Z]{6}$/.test(value); 
		},
		message : '车牌号码无效（例：粤J12350）'
	},
	carenergin:{
		validator : function(value){
			return /^[a-zA-Z0-9]{16}$/.test(value); 
		},
		message : '发动机型号无效(例：FG6H012345654584)'
	},
	email:{
		validator : function(value){
		return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value); 
	},
	message : '请输入有效的电子邮件账号(例：abc@126.com)'	
	},
	msn:{
		validator : function(value){
		return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value); 
	},
	message : '请输入有效的msn账号(例：abc@hotnail(msn/live).com)'
	},
	department:{
		validator : function(value){
			return /^[0-9]*$/.test(value); 
		},
		message : '请输入部门排序号(例：1)'	
	},
	equalTo:{
		validator : function(value, param){
			return $(param[0]).val() == value; 
		},
		message : '两次输入的密码不一致'	
	},range : { 
		validator : function(value, param) {
			return (value <= param[1]) && (value >= param[0]);
		},
		message : '{2}必须介于{0}-{1}'
	},
	notLessThanCurrentDate: {  
		            validator: function(value,param){  
		                if(value)  
		                 {  
		                     if(value.length > 10)  
		                     {  
		                         value = value.substring(0,10);  
		                     }  
		                     var ed_arr = value.split('-');  
		                    var selectedDate = new Date(ed_arr[0],ed_arr[1]-1,ed_arr[2]);  
		                   var currentDate = new Date();  
		                     if((currentDate.getTime() - selectedDate.getTime()) >= 0)  
		                     {  
		                         return false;  
		                   }  
		                 }  
		           return true;  
		      },  
		        message:"日期必须大于等于当前日期"  
	},
   notMoreThanCurrentDate: {  
			            validator: function(value,param){  
			                if(value)  
			                 {  
			                     if(value.length > 10)  
			                     {  
			                         value = value.substring(0,10);  
			                     }  
			                     var ed_arr = value.split('-');  
			                    var selectedDate = new Date(ed_arr[0],ed_arr[1]-1,ed_arr[2]);  
			                   var currentDate = new Date();  
			                     if((currentDate.getTime() - selectedDate.getTime()) >= 0)  
			                     {  
			                         return true;  
			                   }  
			                 }  
			           return false;  
			      },  
			        message:"日期必须小于等于当前日期"  
	}  
	//验证价格
//	$("#ipt1").keyup(function () {
//  var reg = $(this).val().match(/\d+\.?\d{0,2}/);
//  var txt = '';
//  if (reg != null) {
//      txt = reg[0];
//  }
//  $(this).val(txt);
//}).change(function () {
//  $(this).keypress();
//  var v = $(this).val();
//  if (/\.$/.test(v))
//  {
//      $(this).val(v.substr(0, v.length - 1));
//  }
//});
//
});

