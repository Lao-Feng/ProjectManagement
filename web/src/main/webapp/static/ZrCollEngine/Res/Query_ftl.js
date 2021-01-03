/***
 * ftl 定义
 * 2016-10-25
 */
//js初始化
var editIndex3 = undefined;
var editIndex = undefined;

/**
 * 删除行例子
 * @return
 */
function delRowNum(tableid){
	var row = $('#'+tableid).datagrid('getSelected');
	if (row) {
	    var rowIndex = $('#'+tableid).datagrid('getRowIndex', row);
	    $('#'+tableid).datagrid('deleteRow', rowIndex);
	    //从新更新表
	    if(tableid=="list1"){
	    	allRelTableStr=$("#MAINTABLE").combobox('getValue')+".";//设置主表初始值
	    	var rows = $("#"+tableid).datagrid("getRows");
	        for(var i=0;i<rows.length;i++){
	        	//继续填充 主表英文名称
	        	if(allRelTableStr.indexOf(rows[i].NAME1_6)==-1){
	        		allRelTableStr=allRelTableStr+rows[i].NAME1_6+".";
	        	}
	        	if(allRelTableStr.indexOf(rows[i].NAME1_8)==-1){
	        		allRelTableStr=allRelTableStr+rows[i].NAME1_8+".";
	        	}
	        }
	        //更新初始条件==条件列
			loadinitWhereTableFieldInfo();
			//填充其它大文本左右选择的select字段
			loadAllRelTableFieldInfo();

			///判断是否为编辑状态，是则执行下面的语句
      	    if((document.RegFormmain.ID.value).length>5){
  		        //编辑===填充查询已选字段，编辑已经填充的表格===list7
  			    setTimeout("onInitQueryFieldDestList();",500);
  			    //编辑==填充查询已选字段（查询结果列表==显示/隐藏结果列表）===list3
  			    setTimeout("onInitResultFieldDestList();",500);
  	         }
	    }
	    //$('#'+tableid).datagrid('reload');//删除后重新加载下
	}
}

function ClearList(selectID){
	$('#'+selectID).empty();
}

//=====================公共部分    开始=====================================
//填充所有的select待选字段
function loadAllRelTableFieldInfo(){
	$.ajax({
		async: false,
        type: "GET",
        url: '/queryCfgBean/getTableFieldList?TableName='+allRelTableStr+'&flag='+false,
        dataType: "json",
        success: function(result){
        	var items = result;
            var optionval = '';
            $.each(items, function(index, item) {
            	optionval += '<option value="'+item.id+'">'+item.text+'</option>';
        	});
        	//填充查询列待选信息
            ClearList("QueryFieldSrcList");
            $("#QueryFieldSrcList").append(optionval);
            
            //填充结果列待选信息
            ClearList("resultDisFieldSrcList");
            $("#resultDisFieldSrcList").append(optionval);
            
            ClearList("resultHidFieldSrcList");
            $("#resultHidFieldSrcList").append(optionval);
            
            //填充明细待选参数字段
            ClearList("selParameterField2");
            $("#selParameterField2").append(optionval);
            
            //排序待选字段
            ClearList("SORT_FIELD");
            $("#SORT_FIELD").append(optionval);
            
            //填充到查询列表的table数据中
            addUprow7();
      	  	//填充到结果列的table数据中
      	  	addUprow4();
      	  	
      	  	//判断是否为编辑状态，是则执行下面的语句
      	  	if((document.RegFormmain.ID.value).length>5){
      		    //编辑===填充查询已选字段，编辑已经填充的表格===list7
      			setTimeout("onInitQueryFieldDestList();", 500);
      			//编辑==填充查询已选字段（查询结果列表==显示/隐藏结果列表）===list3
      			setTimeout("onInitResultFieldDestList();", 500);
      	  	}
		},
		error: function(request, status, thrown){
			layer.alert('系统错误，请稍后重试~'); 
		}
	});
}

//=====================公共部分    结束=====================================

//*********************关联表    开始**************************************
//添加行  ==关联表
function addUprow1(tabId){
	var row={};
	var NAME1_1="",NAME1_2="",NAME1_3="",NAME1_4="",NAME1_5="",NAME1_6="",NAME1_7="",NAME1_8="",NAME1_9="",NAME1_10="";
	if(tabId=="list1"){
		row.NAME1_1=$('#CFIELD_TABLE').combobox('getText');//text
		row.NAME1_6=$('#CFIELD_TABLE').combobox('getValue');//value
		row.NAME1_2=$('#CFIELD_FIELD').combobox('getText');
		row.NAME1_7=$('#CFIELD_FIELD').combobox('getValue');
		row.NAME1_3=$('#MFIELD_TABLE').combobox('getText');
		row.NAME1_8=$('#MFIELD_TABLE').combobox('getValue');
		row.NAME1_4=$('#MFIELD_FIELD').combobox('getText');
		row.NAME1_9=$('#MFIELD_FIELD').combobox('getValue');
		row.NAME1_5=$('#JOINTYPE').combobox('getText');
		row.NAME1_10=$('#JOINTYPE').combobox('getValue');
		//继续填充 主表英文名称
		if(allRelTableStr.indexOf(row.NAME1_6)==-1){
			allRelTableStr=allRelTableStr+row.NAME1_6+".";
		}
		if(allRelTableStr.indexOf(row.NAME1_8)==-1){
			allRelTableStr=allRelTableStr+row.NAME1_8+".";
		}

		$("#"+tabId).datagrid("appendRow", row);
		//更新初始条件==条件列
		loadinitWhereTableFieldInfo();
		//填充其它大文本左右选择的select字段
		loadAllRelTableFieldInfo();
	}
	if(tabId=="list6"){
		var fieldCnName=$('#SORT_FIELD2').combobox('getText');//SORT_FIELD2
		var fieldEnName=$('#SORT_FIELD2').combobox('getValue');
		var sortCnName=$('#SORT_TYPE').combobox('getText');//SORT_TYPE
		var sortEnName=$('#SORT_TYPE').combobox('getValue');
		row.NAME6_1="1";
		row.NAME6_2=fieldCnName;
		row.NAME6_3=sortCnName;
		row.NAME6_4=fieldEnName;
		row.NAME6_5=sortEnName;
		$("#"+tabId).datagrid("appendRow", row);
	}
	if(tabId=="list5"){
		//判断是否输入参数
		if($("#txtParameterNAME").val().trim().length==0){
			$.messager.alert('提示','参数名称未录入!','warning');
			return;
		}
		if(GetMultiListSelectedNum(document.RegFormmain.ButtonDestList)!= 1){
		    $.messager.alert('提示','请选择一项已分配按钮!','warning');
		    return;
		}
		var idStr=(new Date()).valueOf();
		row.NAME5_1=idStr;
		row.NAME5_2=$("input[name='txtParameterNAME']").val();
		row.NAME5_3=$('#selParameterField').combobox('getText');
		row.NAME5_4=$('#selParameterField').combobox('getValue');
		row.NAME5_5=document.RegFormmain.ButtonDestList.value;
		$("#"+tabId).datagrid("appendRow", row);
	}
}

////选中后，显示在头部
//更新===关联表
function editUpRow_row(tabId){
	var row = $('#'+tabId).datagrid('getSelected');
	if (row) {
		var columns = $('#'+tabId).datagrid("options").columns; //得到columns对象
		var rows = $('#'+tabId).datagrid("getRows");//  得到rows对象
		if(tabId=="list1"){
			var rowIndex = $('#'+tabId).datagrid('getRowIndex', row);
			rows[rowIndex][columns[0][0].field]=$('#CFIELD_TABLE').combobox('getText');//text
			rows[rowIndex][columns[0][5].field]=$('#CFIELD_TABLE').combobox('getValue');//value
			rows[rowIndex][columns[0][1].field]=$('#CFIELD_FIELD').combobox('getText');
			rows[rowIndex][columns[0][6].field]=$('#CFIELD_FIELD').combobox('getValue');
			rows[rowIndex][columns[0][2].field]=$('#MFIELD_TABLE').combobox('getText');
			rows[rowIndex][columns[0][7].field]=$('#MFIELD_TABLE').combobox('getValue');
			rows[rowIndex][columns[0][3].field]=$('#MFIELD_FIELD').combobox('getText');
			rows[rowIndex][columns[0][8].field]=$('#MFIELD_FIELD').combobox('getValue');
			rows[rowIndex][columns[0][4].field]=$('#JOINTYPE').combobox('getText');
			rows[rowIndex][columns[0][9].field]=$('#JOINTYPE').combobox('getValue');
		}
		if(tabId=="list2"){
			var rowIndex = $('#'+tabId).datagrid('getRowIndex', row);
			rows[rowIndex][columns[0][0].field]=$('#selLeft').combobox('getText');//text
			rows[rowIndex][columns[0][1].field]=$('#selFIELD').combobox('getText');//value
			rows[rowIndex][columns[0][2].field]=$('#selSYMBOL').combobox('getText');
			rows[rowIndex][columns[0][3].field]=$('#txtWHEREVALUE').val();
			rows[rowIndex][columns[0][4].field]=$('#selRight').combobox('getText');
			rows[rowIndex][columns[0][5].field]=$('#selLOGIC').combobox('getText');
			rows[rowIndex][columns[0][6].field]=$('#selLeft').combobox('getValue');
			rows[rowIndex][columns[0][7].field]=$('#selFIELD').combobox('getValue').length-2;
			rows[rowIndex][columns[0][8].field]=$('#selRight').combobox('getValue');
			rows[rowIndex][columns[0][9].field]=$('#selLOGIC').combobox('getValue');
		}
		//刷新该行, 只有刷新了才有效果
		$('#'+tabId).datagrid('refreshRow', rowIndex);
	}
}
//*********************关联表    结束**************************************
//添加行  ==初始条件列表
function addUprow2(tabId){
	var row={};
	var NAME2_1="",NAME2_2="",NAME2_3="",NAME2_4="",NAME2_5="",NAME2_6="",NAME2_7="",NAME2_8="",NAME2_9="",NAME2_10="";
	if($("#txtWHEREVALUE").val().length==0){
		message("条件值未录入！");
		return false;
	}else{
		if(tabId=="list2"){
			var v8=$('#selFIELD').combobox('getValue');
			row.NAME2_1=$('#selLeft').combobox('getText');//text
			row.NAME2_2=$('#selFIELD').combobox('getText');//value
			row.NAME2_3=$('#selSYMBOL').combobox('getText');
			row.NAME2_4=$('#txtWHEREVALUE').val();
			row.NAME2_5=$('#selRight').combobox('getText');
			row.NAME2_6=$('#selLOGIC').combobox('getText');

			row.NAME2_7=$('#selLeft').combobox('getValue');
			row.NAME2_8=v8.substring(0,v8.length-2);
			row.NAME2_9=$('#selRight').combobox('getValue');
			row.NAME2_10=$('#selLOGIC').combobox('getValue');
		}
		$("#"+tabId).datagrid("appendRow", row);
	}
}
//*********************初始条件    开始**************************************
//*********************初始条件    结束**************************************
//=====================查询列    开始=======================================
//填充到查询列表的table数据中
function addUprow7(){
	$('#list7').datagrid('loadData', { total: 0, rows: [] });//清空
	if($("#QueryFieldSrcList option").size()>0){
		//--------------------------------------------
		var data = {"total":$("#QueryFieldSrcList option").size(),"rows":[]};
		//--------------------------------------------
		var NAME7_1="",NAME7_2="",NAME7_3="",NAME7_4="",NAME7_5="",NAME7_6="",NAME7_7="";
       	$("#QueryFieldSrcList option").each(function () {
            var txt = $(this).text(); //获取单个text
            var val = $(this).val(); //获取单个value
            var row7={};
       		row7.NAME7_1=txt;//text
       		row7.NAME7_2="否";
       		row7.NAME7_3=val;
       		data.rows.push(row7);
        });
       	$('#list7').datagrid('loadData', data);
    }
	//改变后，判断是否为编辑
}


//选择行==触发事件(查询列表)
function onClickRow7(index){
	if (editIndex!= index){
		if (endEditing7()){
			$('#list7').datagrid('selectRow', index).datagrid('beginEdit', index);
			editIndex = index;
		} else {
			$('#list7').datagrid('selectRow', editIndex);
		}
	}

}

//编辑后更新==触发事件(查询列表)
function endEditing7(){
	if (editIndex == undefined){return true}
	if ($('#list7').datagrid('validateRow', editIndex)){
		$('#list7').datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
	} else {
		return false;
	}
}
//=====================查询列    开始=======================================

//=====================结果列    开始=======================================
//填充到结果列的table数据中
function addUprow4(){
	$('#list3').datagrid('loadData', { total: 0, rows: [] });//清空
	if($("#resultDisFieldSrcList option").size()>0){
		var data = {"total":$("#QueryFieldSrcList option").size(),"rows":[]};

		var NAME3_1="",NAME3_2="",NAME3_3="",NAME3_4="",NAME3_5="",NAME3_6="",NAME3_7="";
       	$("#resultDisFieldSrcList option").each(function () {
            var txt = $(this).text(); //获取单个text
            var val = $(this).val(); //获取单个value
            var row={};
       		row.NAME3_1=txt;//text
       		row.NAME3_2="120";//value
       		row.NAME3_3=val;
       		row.NAME3_4="否";
       		row.NAME3_5="居左";
       		row.NAME3_6="";//是否展示
       		row.NAME3_7="否";//显示/隐藏==显示/隐藏/都不
       		data.rows.push(row);
        });
       	$("#list3").datagrid('loadData', data);
    }
}
//初始化展示方式
var NAME6_JSON=[{"ID":"显示","NAME3_6":"显示"},{"ID":"隐藏","NAME3_6":"隐藏"}];
var NAME5_JSON=[{"ID":"居左","NAME3_5":"居左"},{"ID":"居中","NAME3_5":"居中"},{"ID":"居右","NAME3_5":"居右"}];



//编辑后更新==触发事件(结果列表)
function endEditing3(){
	if (editIndex3 == undefined){return true}
	if ($('#list3').datagrid('validateRow', editIndex3)){
		//$('#list3').datagrid('endEdit', 0);
		$('#list3').datagrid('acceptChanges');
		editIndex3 = undefined;
		return true;
	} else {
		return false;
	}
}
//选择行==触发事件(结果列表)
function onClickRow(index3){
	if (editIndex3!= index3){
		if (endEditing3()){
			//设置选中行编辑
			$('#list3').datagrid('selectRow', index3).datagrid('beginEdit', index3);
			editIndex3 = index3;
		} else {
			$('#list3').datagrid('selectRow', index3);
		}
	}
}

//=====================结果列    结束=======================================

//======================公共==============================
//表格工具栏===查询条件列表
var toolbar7 = ['-',{
	text:'上移',
	iconCls:'icon-add',
	handler:function(){MoveUp('list7');}
},'-',{
	text:'下移',
	iconCls:'icon-edit',
	handler:function(){MoveDown('list7');}
},'-',{
	text:'确定',
	iconCls:'icon-save',
	handler:function(){appendRow('list7');}
},'-'];
//appendRow('list7');
var toolbar3 = ['-',{
	text:'上移',
	iconCls:'icon-add',
	handler:function(){MoveUp('list3');}
},'-',{
	text:'下移',
	iconCls:'icon-edit',
	handler:function(){MoveDown('list3');}
},'-',{
	text:'确定',
	iconCls:'icon-save',
	handler:function(){appendRow('list3');}
},'-'];
//上移
function MoveUp(tabId) {
    var row = $("#"+tabId).datagrid('getSelected');
    var index = $("#"+tabId).datagrid('getRowIndex', row);
    mysort(index, 'up', tabId);
}

//下移
function MoveDown(tabId) {
    var row = $("#"+tabId).datagrid('getSelected');
    var index = $("#"+tabId).datagrid('getRowIndex', row);
    mysort(index, 'down', tabId);
}
//上线移动实现
function mysort(index, type, gridname) {
    if ("up" == type) {
        if (index != 0) {
            var toup = $('#' + gridname).datagrid('getData').rows[index];
            var todown = $('#' + gridname).datagrid('getData').rows[index - 1];
            $('#' + gridname).datagrid('getData').rows[index] = todown;
            $('#' + gridname).datagrid('getData').rows[index - 1] = toup;
            $('#' + gridname).datagrid('refreshRow', index);
            $('#' + gridname).datagrid('refreshRow', index - 1);
            $('#' + gridname).datagrid('selectRow', index - 1);
        }
    } else if ("down" == type) {
        var rows = $('#' + gridname).datagrid('getRows').length;
        if (index != rows - 1) {
            var todown = $('#' + gridname).datagrid('getData').rows[index];
            var toup = $('#' + gridname).datagrid('getData').rows[index + 1];
            $('#' + gridname).datagrid('getData').rows[index + 1] = todown;
            $('#' + gridname).datagrid('getData').rows[index] = toup;
            $('#' + gridname).datagrid('refreshRow', index);
            $('#' + gridname).datagrid('refreshRow', index + 1);
            $('#' + gridname).datagrid('selectRow', index + 1);
        }
    }
}
//确定锁定编辑行appendRow('list7');
function appendRow(tabId){
	if(tabId=="list7"){
		if (endEditing7()){
			$('#'+tabId).datagrid('selectRow', editIndex)
					.datagrid('beginEdit', editIndex);
		}
	}
	if(tabId=="list3"){
		if (endEditing3()){
			$('#'+tabId).datagrid('selectRow', editIndex3)
					.datagrid('beginEdit', editIndex3);
		}
	}
}
//======================公共==============================
//计算表达式  name:字段名称 ShowModal_main('txtWHEREVALUE')
function ShowModal_main(name){
	$('#win3').window({
	      width: 850,
	      height: 400,
	      modal: true,
	      collapsible:false,
	      href: "Config/ConValueSetup.jsp?ID=txtWHEREVALUE&name="+name+"&win=win3&strResult="+$("input[name='"+name+"']").val(),
	      title: "初始条件值"
	});
}

//点击已经分配好的按钮，将list5的数据缓存值list4，

function getDetailParameterByButton(){
	//当前选中的按钮
	var BID=$('#ButtonDestList option:selected').val();//document.RegFormmain.ButtonDestList.value;
	//获取当前list5中的数据
	var rows = $("#list5").datagrid("getRows");
    //1、比较缓存数据与当前数据，
	for(var i=0;i<rows.length;i++){
		var row={};
		row.NAME4_1=rows[i].NAME5_1;
		row.NAME4_2=rows[i].NAME5_2;
		row.NAME4_3=rows[i].NAME5_3;
		row.NAME4_4=rows[i].NAME5_4;
		row.NAME4_5=rows[i].NAME5_5;
		//重新插入
	    $("#list4").datagrid("appendRow", row);
   }
	//2、直接清空list5
	$('#list5').datagrid('loadData',{total:0,rows:[]});//清空

	//3、再重list4-->list5加载
	rows = $("#list4").datagrid("getRows");
	for(var i=rows.length-1;i>=0;i--){
		//判断是否属于当前点击的按钮
		if(BID==rows[i].NAME4_5){
			var row={};
			row.NAME5_1=rows[i].NAME4_1;
			row.NAME5_2=rows[i].NAME4_2;
			row.NAME5_3=rows[i].NAME4_3;
			row.NAME5_4=rows[i].NAME4_4;
			row.NAME5_5=rows[i].NAME4_5;
			//重新加载到list5中显示
		    $("#list5").datagrid("appendRow", row);
		    //4\加载后删除缓存
		    $('#list4').datagrid('deleteRow',i);
		}
	}
}
//按钮属性设置私有按钮
function setbutton(){
   var gid = document.RegFormmain.ID.value;
   if (gid != ""){
	   $('#win3').window({
		      width: 700,
		      height: 300,
		      modal: true,
		      collapsible:false,
		      href: "ButtonConfig/List.jsp?FID="+ gid,
		      title: "初始条件值"
		});
        //EXPRESSIONSWHERE
   }else{
	   $.messager.alert('提示','请先保存查询配置才能添加!','warning');
	   return;
   }
}



//======================备用==============================
function mysortTr(index,gridname,dowid) {
	if (index != 0) {
        var toup = $('#' + gridname).datagrid('getData').rows[index];
        var todown = $('#' + gridname).datagrid('getData').rows[dowid];
        $('#' + gridname).datagrid('getData').rows[index] = todown;
        $('#' + gridname).datagrid('getData').rows[dowid] = toup;
        $('#' + gridname).datagrid('refreshRow', index);
        $('#' + gridname).datagrid('refreshRow', dowid);
        $('#' + gridname).datagrid('selectRow', dowid);
    }
}


////初始化结果列表
//$(function(){
//		$('#list3').datagrid({
//			title:'结果列设置',
//			iconCls:'icon-edit',
//			width:500,
//			height:250,
//			singleSelect:true,
//			idField:'itemid',
//			url:'ActionJson.do?method=querlsit3&tabid=',
//			columns:[[
//				{field:'NAME1',title:'字段',width:200},
//				{field:'NAME7',title:'是否启用',width:60,editor:{type:'checkbox',options:{on:'是',off:''}}},
//				{field:'NAME6',title:'展示方式',width:100,
//					formatter:function(value){
//						for(var i=0; i<NAME6_JSON.length; i++){
//							if (NAME6_JSON[i].ID == value) return NAME6_JSON[i].NAME6;
//						}
//						return value;
//					},
//					editor:{
//						type:'combobox',
//						options:{
//							valueField:'ID',
//							textField:'NAME6',
//							data:[{"ID":"显示","NAME6":"显示"},{"ID":"隐藏","NAME6":"隐藏"},{"ID":"不用","NAME6":"不用"}],
//							required:true
//						}
//					}
//				},
//				{field:'NAME2',title:'宽度',width:80,align:'right',editor:'numberbox'},
//				{field:'NAME3',title:'FIELD',width:1,align:'right',},
//				{field:'NAME5',title:'对齐方式',width:50,align:'center',
//					formatter:function(value){
//					    for(var i=0; i<NAME5_JSON.length; i++){
//						   if (NAME5_JSON[i].ID == value) return NAME5_JSON[i].NAME5;
//					    }
//					    return value;
//				    },
//				    editor:{
//					    type:'combobox',
//					    options:{
//						    valueField:'ID',
//						    textField:'NAME5',
//						    data:[{"ID":"居左","NAME5":"居左"},{"ID":"居中","NAME5":"居中"},{"ID":"居右","NAME5":"居右"}],
//						    required:true
//					    }
//				    }
//				},
//				{field:'NAME4',title:'导出转数字',width:60,editor:{type:'checkbox',options:{on:'是',off:''}}}
//			]],
//			onBeforeEdit:function(index,row){
//				row.editing = true;
//				updateActions(index);
//			},
//			onAfterEdit:function(index,row){
//				row.editing = false;
//				updateActions(index);
//			},
//			onCancelEdit:function(index,row){
//				row.editing = false;
//				updateActions(index);
//			}
//		});
//	});
//	function updateActions(index){
//		$('#list3').datagrid('updateRow',{
//			index: index,
//			row:{}
//		});
//	}