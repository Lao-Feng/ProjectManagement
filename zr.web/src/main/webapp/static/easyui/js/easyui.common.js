/**
 * 设置列表控件
 * datagrid  列表id
 * toolbar   工具条id
 **/
function datagrid(data,datagrid,toolbar){
	$(datagrid).datagrid({
		border:false,
		fitColumns:true,
		checkbox:true,
		fit:true,
		remoteSort:false,
		rownumbers:true,
		singleSelect:true,
		toolbar: toolbar,
		pagination:true,
		data: data
	});
}

/**
 * 提示消息
 * @param msg
 */
function message(msg){
	$.messager.show({
        title:'提示',
        msg:msg,
        timeout:5000,
        showType:'slide'
    });
}

/**
 * 获取datagrid选择行
 **/
function getSelectedRow(datagrid,msg) {
	var row = $(datagrid).datagrid('getSelected');
	if (row) {
		return row;
	} else {
		if(!msg){
			msg="请选中记录";
		}
		message(msg);
		return;
	}
}

/**
 * 获取treegrid选择行
 **/
function getTreegridSelectedRow(datagrid,msg) {
	var row = $(datagrid).treegrid('getSelected');
	if (row) {
		return row;
	} else {
		if(!msg){
			msg="请选中记录";
		}
		//$.messager.alert('提示', message, 'info');
		message(msg);
		return;
	}
}

/**
 * 格式化日期
 * @param date
 * @returns {String}
 */
function myformatter(date){
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
}
function myparser(s){
	if (!s) return new Date();
	var ss = (s.split('-'));
	var y = parseInt(ss[0],10);
	var m = parseInt(ss[1],10);
	var d = parseInt(ss[2],10);
	if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
		return new Date(y,m-1,d);
	}else {
		return new Date();
	}
}

/**
 * 格式化时间
 * @param date
 * @returns {String}
 */
function myDateTimeformatter(date){
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	var h = date.getHours();  
	var min = date.getMinutes();  
	var sec = date.getSeconds();  
	return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?('0'+h):h)+':'+(min<10?('0'+min):min)+':'+(sec<10?('0'+sec):sec);  

}
function myDateTimeparser(s){
	if (!s) return new Date();
	var y = s.substring(0,4);  
	var m =s.substring(5,7);  
	var d = s.substring(8,10);  

	var h = s.substring(11,13);  
	var min = s.substring(14,16);  
	var sec = s.substring(17,19); 

	if (!isNaN(y) && !isNaN(m) && !isNaN(d) && !isNaN(h) && !isNaN(min) && !isNaN(sec)){  
		return new Date(y,m-1,d,h,min,sec); 
	}else {
		return new Date();
	}
}


















/**
 * 设置列表控件
 * datagrid  列表id
 * toolbar   工具条id
 **/
function datagrid1(datagrid,toolbar,bool){
	$(datagrid).datagrid({
		border:false,
		fitColumns:true,
		checkbox:true,
		fit:true,
		remoteSort:false,
		rownumbers:true,
		singleSelect:true,
		sortName:'id',
		sortOrder:'asc',
		toolbar: toolbar
	});
}

/**
 * 设置列表控件
 * datagrid  列表id
 * toolbar   工具条id
 **/
function datagrid2(data,datagrid,toolbar){
	$(datagrid).datagrid({
		border:false,
		fitColumns:true,
		checkbox:true,
		fit:true,
		remoteSort:false,
		rownumbers:true,
		singleSelect:true,
		toolbar: toolbar,
		pagination:true,
		data: data
	});
}

/*********** 公共函数  ****************/
//将标签中所有input置空
function clearInput(tagid){
	$inputs = $("#"+tagid).find("input, textarea");
	$.each($inputs,function(idx, item){
		$(item).val("");
	});
}

/**
 * 销毁dialog对话框
 * @param $dialog
 * @param $div
 */
function destroy(dialog,$div){
	$("#" + dialog).dialog('destroy');
	$($div).append("<div id='" + dialog + "'></div>");
}

