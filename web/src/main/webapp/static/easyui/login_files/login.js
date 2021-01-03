$(document).ready(function() {
	// 绑定回车键事件
	$("body").bind('keypress',function(event){ 
		 var curKey = event.keyCode; 
		 if(curKey == 13){ 
			 $("#login").focus();
        	 $("#login").trigger("click"); 
        }
    });
	/**
	 * 绑定登录事件
	 */
	$("#login").unbind().bind('click',function() {
		var username = $("#username").textbox('getValue');
		var password = $("#password").textbox('getValue');
		if($.trim(username).length == 0){
			message("用户名不能为空");
			$("#username").focus();
			return false;
		}
		
		if($.trim(password).length == 0){
			message("密码不能为空");
			$("#password").focus();
			return false;
		}
		$("#loginform").submit();
	});
});
