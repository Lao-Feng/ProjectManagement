function userlist(flag){
$('#changePwdDialog').dialog({
		    title: '个人信息',
		    width: 500,
		    height: 300,
		    inline:false,
		    closed: false,
		    closable:false,
		    cache: false,
		    modal: true,
		    href: '/user/actionuser?Act=UserInfo',
		    buttons: [{
                text:'关闭',
                iconCls:'icon-cancel',
                handler:function(){
		    	$('#changePwdDialog').dialog('close');
                }
            }]
		});
}

/**
 * 登录密码修改
 */
function changePassWords(flag){
		$('#changePwdDialog').dialog({
		    title: '密码修改',
		    width: 500,
		    height: 300,
		    inline:false,
		    closed: false,
		    closable:false,
		    cache: false,
		    modal: true,
		    href: '/iframe?page=password.html',
		    buttons: [{
                text:'确定',
                iconCls:'icon-ok',
                handler:function(){
                    	var bool = $("#passwordform").form('enableValidation').form('validate');
                    	if(!bool){
                    		return;
                    	}
                    	changePassWord();
                }
            },{
                text:'取消',
                iconCls:'icon-cancel',
                handler:function(){
                	if(flag ==undefined || flag !=1){
                		$('#changePwdDialog').dialog('close');
                	}
                }
            }]
		});
}

function changePassWord(username,oldPassword,newPassword){
		var username=$("#username").val();
		var oldPassword = $("#oldPassword").val();
		var newPassword = $("#newPassword").val();
    	$.ajax({
    		type : "POST",
    		url : "/user/actionuser",
    		data :{
    			username:username,oldPassword:oldPassword,newPassword:newPassword,Act:"repassword"
    		},
    		dataType : "text",
    		success : function(data) {
                var resultStr = data.replace(/\ +/g, ""); //去掉空格
                resultStr = resultStr.replace(/[ ]/g, "");    //去掉空格
                resultStr = resultStr.replace(/[\r\n]/g, ""); //去掉回车换行
    			if(resultStr == 'fail'){
    				message("密码修改失败");
    				return;
    			}
    			if(resultStr == 'success'){
    				message("密码修改成功");
    				$('#changePwdDialog').dialog('close');
    			}

    		}
    	});
    	$('#changePwdDialog').dialog("center");
	}
