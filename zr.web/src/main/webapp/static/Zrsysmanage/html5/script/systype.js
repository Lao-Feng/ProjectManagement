
//弹出窗口

/**
 * 弹出提示窗口 type=2表示是html5窗口
 */
function alertTile(tile,type){
	if(type=="2"){//类型2为html窗口
		layer.alert(tile, {icon: 7});
	}else{
		alert(tile);
	}
}

/**
 * 弹出询问窗口 
 * type=2表示是html5窗口
 * tile  标题
 * quer  确认方法  例如；actionstr()
 * extstr  取消方法  例如；actionstr()
 */
function confirmTile(tile,quer,extstr,type){
	var returstr="false";//就是false
	if(type=="2"){
		layer.confirm(tile, {
			  btn: ['确认','取消'] //按钮
			}, function(){
				layer.closeAll('dialog');
				eval(quer);
			}, function(){
				eval(extstr);
			});
	}else{
		if(confirm(tile)){
			eval(quer);
		}else{
			eval(extstr);
		}
	}

}

/**
 * 表单选择操作  用户、单位选择
 * @param mFileUrl
 * @return
 */

function selectUserHtml(mFileUrl){
	var index =layer.open({
        type: 2,
        title: "用户、单位选择",
        area: ['500px', '400px'],
        shade: 0.3,
        closeBtn: 0,
        shadeClose: true,
         content: mFileUrl
    });
	setTimeout("initValue("+index+");",500);
}



function initValue(index){
	//修改弹出窗口距离顶部的位置
	$("#layui-layer"+index).css("top","50px");
	
}

/**
 * 表单选择操作  字典表选择
 * @param mFileUrl
 * @return
 */
function selectDictHtml(mFileUrl){
	layer.open({
        type: 2,
        title: false,
        area: ['382px', '312px'],
        shade: 0.3,
        closeBtn: 0,
        shadeClose: true,
         content: mFileUrl
    });
}