//--------------------------vue -------------------------//
var $flow;
/**
 * 获取单位tree数据
 * @returns
 */
function ZRqueryUnitList(){
	var url = "bpipunit/list";
	ZRqueryList('/'+url, null, function (data) {
		$flow.unit_tree = ZRqueryUnitTree(data.list, '');
    })
}
/**
 * 选择单位tree，返回选中值
 * @returns
 */
function on_unit_onclick(data, keys){
	var field = $flow_field.split('.');
	if (keys.checkedKeys.length > 0) {
		$flow.$refs.unit_tree.setCheckedKeys([data.unitid]);
		$flow.form[field[0]][field[1]] = data.unitid;
		$flow.dialog_unit = false;
	}
}
/**
 * 获取单位、单位用户tree数据
 * @returns
 */
function ZRqueryUserList(){
	ZRqueryList('/bpipunit/list', null, function (data) {
		$flow.unit_tree = ZRqueryUnitTree(data.list, '');
		ZRqueryList('/bpipuser/list', null, function (user) {
            ZRunitAndUserList(data.list, user.list, function (list) {
            	$flow.user_tree = ZRunitAndUserTree(list, '0');
            })
        })
    })
}

/**
 * 获取数据
 * @returns
 */
function ZRqueryList(url, data, callback){
	$.post(url, data, function (data) {
        callback(data)
    }, 'json')
}
/**
 * @param 将单位转换tree格式
 * @param {*} list 
 * @param {*} parentid
 */
function ZRqueryUnitTree(list, parentid) {
    let re = new RegExp("00", "g"); //定义正则表达式
    let result = [],
        temp;
    for (let i = 0; i < list.length; i++) {
        let unitId = list[i].unitid.replace(re, "");
        if (unitId.substring(0, unitId.length - 2) == parentid) {
            let obj = list[i];
            // 递归运算
            if (list[i].unitid.length >= 2) {
                temp = ZRqueryUnitTree(list, unitId);
                if (temp.length > 0) {
                    obj.list = temp;
                }
            }
            result.push(obj);
        }
    }
    return result;
}
/**
 * @param 将单位和用户合转换tree格式
 * @param {*} unitList 
 * @param {*} userList
 * @param {*} callback
 */
function ZRunitAndUserList(unitList, userList, callback) {
    let re = new RegExp("00", "g"); //定义正则表达式
    let unit_list = [];
    if (unitList.length > 0) {
        $.each(unitList, function (i, perms) {
            let parentId = perms.unitid.replace(re, "");
            parentId = parentId.substring(0, parentId.length - 2);
            if (parentId.length >= 2) {
                parentId = parentId + "000000000000";
                parentId = parentId.substring(0, 12);
            } else {
                parentId = '0';
            }
            let json = {
                userid: perms.unitid,
                name: perms.unitname,
                unitid: parentId
            }
            unit_list.push(json);
        });
    }
    unit_list.push.apply(unit_list, userList);
    callback(unit_list);
}

/**
 * @param 将单位and用户组合转换tree格式
 * @param {*} list 
 * @param {*} parentid
 */
function ZRunitAndUserTree(list, parentid) {
    let result = [],
        temp;
    for (let i = 0; i < list.length; i++) {
        if (list[i].unitid == parentid) {
            let obj = list[i];
            // 递归运算
            temp = ZRunitAndUserTree(list, list[i].userid);
            if (temp.length > 0) {
                obj.list = temp;
            } else {
                //设置单位下没有绑定用户的情况禁用或者不展示判断 disabled: true
                if (list[i].userid.length < 16) {
                    obj.disabled = true;
                }
            }
            result.push(obj);
        }
    }
    return result;
}

//----------------------------------流程处理控制--------------------------
//保存
function doSave() {
	webform.OptCmd.value = "SAVE";
	webform.OptCmd_Name.value = "保存";
	webform.submit();
}

//移交
function devolve() {
	var mFileUrl = webform.WEBPATH.value;
	mFileUrl = mFileUrl + "/flow/openflowcfg?Act=Devolve";
	//$.get(url,{},function(src){
	var index = layer.open({
		type : 2,
		title : "选择移交人员",
		area : [ '550px', '400px' ],
		fix : false, //不固定
		maxmin : false,
		content : mFileUrl
	});
	//})
}

//选择移交的人员后返回
function devolveRe(strValue) {
	webform.Do_User_Nos.value = strValue;
	webform.OptCmd.value = "DEVOLVE";
	webform.OptCmd_Name.value = "移交";
	webform.submit();
}

//提交------
function doSubmit() {
	doSubmit_start(1);
}

function doSubmit_start(num) {
	switch (num) {
	case 1: {//开始执行提交
		var needFlow = webform.NeedNewFlow.value;
		if (needFlow.length > 0) {
			layer.msg('请先新建下列流程:\r\n' + needFlow);
			return (false);
		}
		var iswhere = webform.ISWHERE.value;
		if (iswhere == "1") {
			getiswhere(1, false);//线上有条件用到当前保存表中的值时重新设置前后步骤,成功回调doSubmit_start(2)继续执行。
		} else {
			doSubmit_start(2);
		}
	}
		;
		break;
	case 2: {
		var str_node_e = webform.M_Node_No_E.value;
		if (str_node_e.length > 10)//有多分支时
		{
			//选择一分支
			SelectNextID(str_node_e);//成功回调doSubmit_start(3)继续执行。
		} else {
			doSubmit_start(3);
		}
	}
		;
		break;
	case 3: {
		//附件提示检测---
		var AttMessage = webform.AttMessage.value;//当前步骤附件提示情况
		if (AttMessage.length > 0) {
			//检测流程线路上是否需要提示附件
			Attcheck(1, false);//检测通过直接调用doSubmit_start(4);
		} else {
			doSubmit_start(4);
		}
	}
		;
		break;

	case 4: {
		var arrlist = "";
		var str_NodeNo = webform.M_Node_No_S_E.value;
		arrlist = str_NodeNo.split(",")[0].split("/");
		str_NodeNo = arrlist[1];//得到处理人类型
		
		if (str_NodeNo == "E")//自定义选择办理部门
		{
			SelectDoDept();//选择办理部门,成功回调doSubmit_start(5);
		}
		if (str_NodeNo == "F")//自定义选择办理人员
		{
			SelectDoPsn()//自定义选择办理人员,成功回调doSubmit_start(5);
		}
		if (str_NodeNo != "E" && str_NodeNo != "F") {
			//选择处理人员,成功回调doSubmit_start(5);
			SelectDoUser();
		}
	}
		;
		break;
	case 5: {
		//layer.msg('执行到5\r\n');
		var arrlist1 = "";
		var str_NodeNo1 = webform.M_Node_No_S_E.value;
		arrlist1 = str_NodeNo1.split(",")[0].split("/");
		webform.M_Node_No_S_E.value = arrlist1[0];

		webform.OptCmd.value = "SUBMIT";
		webform.OptCmd_Name.value = "提交";
		webform.submit();
	}
		;
		break;
	default: {
	}
	}
	//----------
}

//重新设置前后步骤
function getiswhere(type, value) {
	if (type == 1)//调用
	{
		var cid = webform.Node_No_S.value;
		var strExecute_No = webform.Execute_No.value;
		var strIdentification = webform.Identification.value;
		var strOtherID = webform.OtherID.value;
		var strParentID = webform.ParentID.value;
		var strParentID1 = webform.ParentID1.value;
		var strRID = webform.RID.value;
		var strRECORDID = webform.RECORDID.value;
		var strISBRANCH = webform.ISBRANCH.value;
		var mFileUrl = "";

		mFileUrl = webform.WEBPATH.value;
		mFileUrl = mFileUrl + "/flow/openflowcfg?Act=iswhere&CID=" + cid
				+ "&Execute_No=" + strExecute_No + "&Identification="
				+ strIdentification + "&OtherID=" + strOtherID + "&ParentID="
				+ strParentID + "&ParentID1=" + strParentID1 + "&RID=" + strRID
				+ "&RECORDID=" + strRECORDID + "&ISBRANCH=" + strISBRANCH;
		layer.msg("正在处理，请等待...");
		$.get(mFileUrl, {}, function(src) {

		})
	} else {
		if (value)//为真时回调上级继续执行
		{
			doSubmit_start(2);
		}
	}
}

//重新设置前后步骤getiswhere(type,value)(回调)
function setiswhere(strValue) {
	if (strValue != "" && strValue != null) {
		var arrlist = "";
		arrlist = strValue.split("^");

		var s1 = arrlist[0];
		if (s1 == null) {
			s1 = "";
		}
		var s2 = arrlist[1];
		if (s2 == null) {
			s3 = "";
		}
		var s3 = arrlist[2];
		if (s3 == null) {
			s3 = "";
		}
		var s4 = arrlist[3];
		if (s4 == null) {
			s4 = "";
		}

		webform.M_Node_No_E.value = s1;
		webform.M_Node_No_S_E.value = s2;
		webform.S_Node_No_E.value = s3;
		webform.S_Node_No_S_E.value = s4;

		getiswhere(2, true);
	} else {
		getiswhere(2, false);
	}
}

//检测是否提示上传附件---
function Attcheck(type, value) {
	if (type == 1) {
		var str_CNodeNo = webform.Node_No_S.value;
		var str_NodeNo = webform.M_Node_No_S_E.value;
		var arrlist = "";
		arrlist = str_NodeNo.split("/");
		str_NodeNo = arrlist[0];

		var mFileUrl = webform.WEBPATH.value;
		mFileUrl = mFileUrl + "/flow/openflowcfg?Act=Attcheck";
		mFileUrl = mFileUrl + "&CNodeNo=" + str_CNodeNo + "&NodeNo="
				+ str_NodeNo;
		layer.msg("正在检测附件，请等待...");
		$.get(mFileUrl, {}, function(src) {

		})
	} else {
		if (value)//通过
		{
			doSubmit_start(4);//回调
		} else {
			var AttMessage = webform.AttMessage.value;//当前步骤附件提示情况
			if (AttMessage.length > 0) {
				layer.msg(AttMessage);
				return (false);
			}
		}
	}
}

//检测是否提示上传附件Attcheck(type,value)(回调)
function setAttcheck(strValue) {
	if (strValue == "1") {
		Attcheck(2, false);
	} else {
		Attcheck(2, true);
	}
}

//处理返回
function doReturn() {
	//后退保存数据-------
	var ISSAVE2 = webform.ISSAVE2.value;
	//后退必填留言
	var ISLEAVE2 = webform.ISLEAVE2.value;
	if (ISLEAVE2 == "1") {
		var DoIdea = webform.DoIdea.value;
		if (DoIdea.length == 0) {
			layer.msg('请先填写处理意见！');
			return;
		}
	}

	if (ISSAVE2 == "1") {
		var strPath = webform.FlowFormPath.value;
		if (strPath.match("/collect/collengine")) {
			if (!saveColl('1'))//先保存
			{
				return false; //检测保存未通过；
			}
		}
		if (strPath.match("IndexSend.jsp")) {
			window.CollDataTable.WebSaveVersionByFileID();
		}
	}
	layer.confirm("真的要返回吗？",{title:'提示'},function(index){
		webform.OptCmd.value = "RETURN";
		webform.OptCmd_Name.value = "处理返回";
		webform.submit();
   });
}

//退回
function doUntread() {
	//后退必填留言
	var ISLEAVE2 = webform.ISLEAVE2.value;
	if (ISLEAVE2 == "1") {
		var DoIdea = webform.DoIdea.value;
		if (DoIdea.length == 0) {
			layer.msg('请先填写处理意见！');
			return;
		}
	}
	doUntread_start(1);
	return;
}

//退回特殊处理----
function doUntread_start(num) {
	switch (num) {
	case 1: {
		var str_node_s = webform.S_Node_No_E.value;
		if (str_node_s.length > 10)//有多分支时
		{
			//选择一分支,成功执行doUntread_start(2);
			SelectUpID(str_node_s)
		} else {
			doUntread_start(2);
		}
	}
		;
		break;
	case 2: {
		var arrlist = "";
		var str_NodeNo = webform.S_Node_No_S_E.value;
		arrlist = str_NodeNo.split("/");
		str_NodeNo = arrlist[1];//得到处理人类型

		if (str_NodeNo == "E")//自定义选择办理部门
		{
			SelectUpDept();//选择办理部门,成功回调doUntread_start(3);
		}
		if (str_NodeNo == "F")//自定义选择办理人员
		{
			SelectUpPsn();//自定义选择办理人员,成功回调doUntread_start(3);
		}
		if (str_NodeNo != "E" && str_NodeNo != "F") {
			//选择处理人员,成功回调doUntread_start(3);
			SelectUpDoUser();
		}
	}
		;
		break;
	case 3: {
		var arrlist = "";
		var str_NodeNo = webform.S_Node_No_S_E.value;
		arrlist = str_NodeNo.split("/");

		webform.S_Node_No_S_E.value = arrlist[0];
		webform.M_Node_No_S_E.value = arrlist[0];
		webform.OptCmd.value = "UNTREAD";
		webform.OptCmd_Name.value = "退回";
		webform.submit();
	}
		;
		break;

	default: {
	}
	}

}

//完成
function doFinish() {
	var strPath = webform.FlowFormPath.value;
	if (strPath.length == 0) {
		if (!saveColl('1'))//先保存
		{
			return false; //检测保存未通过；
		}
	}
	var needFlow = webform.NeedNewFlow.value;
	if (needFlow.length > 0) {
		layer.msg('请先新建下列流程:\r\n' + needFlow);
		return (false);
	}

	var AttMessage = webform.AttMessage.value;//当前步骤附件提示情况
	if (AttMessage.length > 0) {
		layer.msg(AttMessage);
		return (false);
	}

	if (strPath.match("/collect/collengine")) {
		if (!saveColl('1'))//先保存
		{
			//return false; //检测保存未通过；
		}
	}

	webform.OptCmd.value = "FINISH";
	webform.OptCmd_Name.value = "完成";
	//显示提交提示
	webform.submit();
}
//直接提交下一步(处理人为本人时使用)
function doNext() {

	var needFlow = webform.NeedNewFlow.value;
	if (needFlow.length > 0) {
		layer.msg('请先新建下列流程:\r\n' + needFlow);
		return (false);
	}
	var AttMessage = webform.AttMessage.value;//当前步骤附件提示情况
	if (AttMessage.length > 0) {
		layer.msg(AttMessage);
		return (false);
	}

	//前进保存数据
	var ISSAVE1 = webform.ISSAVE1.value;
	if (ISSAVE1 == "1") {
		var isFalse = window.CollDataTable.notre_saveColl();
		if (isFalse == true) {
			window.setTimeout(doNext1(), 1000);
		}

	} else {
		doNext1();
	}

	return;

}

function doNext1() {

	webform.OptCmd.value = "SUBMIT";
	webform.OptCmd_Name.value = "提交下一步";

	var str_node_e = webform.M_Node_No_E.value;
	if (str_node_e.length > 10)//有多分支时
	{
		if (!SelectNextID(str_node_e)) {
			return (false);
		}
	}

	var str_NodeNo = webform.M_Node_No_S_E.value;
	var arrlist = "";
	arrlist = str_NodeNo.split("/");
	webform.M_Node_No_S_E.value = arrlist[0];

	webform.Do_User_Nos.value = webform.UserNo.value;

	webform.submit();
}

//删除流程
function doDelete() {
	layer.confirm("将删除当前流程及相关数据,确定要删除吗?",{title:'提示'},function(index){
		webform.OptCmd.value = "DELETE";
		webform.OptCmd_Name.value = "删除";
		webform.submit();
   });
}
//初始化流程
function doInit() {
	layer.confirm("将初始化流程,确定要初始化吗?",{title:'提示'},function(index){
		webform.OptCmd.value = "INIT";
		webform.OptCmd_Name.value = "初始化";
		webform.submit();
   });
}
//收回
function TakeBack() {
	layer.confirm("真的要收回重新处理吗?",{title:'提示'},function(index){
		webform.OptCmd.value = "TAKEBACK";
		webform.OptCmd_Name.value = "收回";
		webform.submit();
   });
}

//关闭
function doClose() {
	window.close();
}

//送部门承办人
function dodept() {
	//前进必填留言
	var ISLEAVE1 = webform.ISLEAVE1.value;
	if (ISLEAVE1 == "1")//提交必填留言
	{
		var DoIdea = webform.DoIdea.value;
		if (DoIdea.length == 0) {
			layer.msg('请先填写处理意见！');
			return;
		}
	}

	var mFileUrl = webform.WEBPATH.value;
	mFileUrl = mFileUrl + "/flow/openflowcfg?Act=selectdeptpsn";
	//$.get(url,{},function(src){
	var index = layer.open({
		type : 2,
		title : "选择部门承办人员",
		area : [ '550px', '400px' ],
		fix : false, //不固定
		maxmin : false,
		content : mFileUrl
	});
	//})

}
//送部门承办选人(dodept)后返回-----
function dodeptRe(strValue) {
	webform.Do_User_Nos.value = strValue;
	webform.OptCmd.value = "SUBDEPT";
	webform.OptCmd_Name.value = "送部门承办";
	webform.submit();
}
//分送
//说明：第一个参数为指定显示传阅的活动id,第二个参数为待选择传阅人的角色编号，多个编号间用,号隔开。
//第三个参数为人员编号左包含的编码,第四个参数为是否是本部门的人,参数为空时表示选择所有的人员。
//第五个参数为是否发送短信
function dosend(showsid, roles, sunitid, isdept, isSMS) {
	var mFileUrl = webform.WEBPATH.value;
	mFileUrl = mFileUrl + "/flow/openflowcfg?Act=dosendpsn";
	var strTitleName = webform.TitleName.value;//标题
	mFileUrl = mFileUrl + "&showsid=" + showsid + "&roles=" + roles
			+ "&sunitid=" + sunitid + "&isdept=" + isdept + "&issms=" + isSMS
			+ "&TitleName=" + strTitleName;

	//正常返回dosend_re(strValue)执行
	//$.get(url,{},function(src){
	var index = layer.open({
		type : 2,
		title : "选择分送人员",
		area : [ '550px', '400px' ],
		fix : false, //不固定
		maxmin : false,
		content : mFileUrl
	});
	//})

}
//选择分送人员后调用----
function dosend_re(strValue, showsid) {
	var strExecuteNo = webform.Execute_No.value;//流程运转ID
	var strcid = webform.Node_No_S.value;//当前活动编号
	//strValue
	var arrlist = strValue.split("/");
	var users = "";
	var smsvalue = "";
	users = arrlist[0];
	if (arrlist.length == 2)//自定义短信
	{
		smsvalue = arrlist[1];
	}

	var mFileUrl = webform.WEBPATH.value;
	mFileUrl = mFileUrl + "/flow/openflowcfg?Act=doflowsend";
	mFileUrl = mFileUrl + "&ExecuteNo=" + strExecuteNo + "&users=" + users
			+ "&sid=" + showsid + "&cid=" + strcid + "&smsvalue=" + smsvalue;
	layer.msg("正在发送...");
	$.get(mFileUrl, {}, function(src) {

	})

}

//执行分送完成--
function dosendFinish() {
	webform.OptCmd.value = "SENDFINISH";
	webform.OptCmd_Name.value = "分送已阅";
	webform.submit();
}

//刷新
function doRefresh() {
	location.href = "EditFrm.jsp?Execute_No=" + webform.Execute_No.value;
}

//选择用户
function SelectDoUser() {
	var mFileUrl = "";
	var str_CNodeNo = webform.Node_No_S.value;
	var str_NodeNo = webform.M_Node_No_S_E.value;
	var str_ExecuteNo = webform.Execute_No.value;
	var str_OtherID = webform.OtherID.value;
	var str_ParentID = webform.ParentID.value;
	var str_ParentID1 = webform.ParentID1.value;
	var SELECTDEPTID = webform.SELECTDEPTID.value;

	var arrlist = "";
	arrlist = str_NodeNo.split("/");
	str_NodeNo = arrlist[0];

	var AttMessage = "";

	mFileUrl = webform.WEBPATH.value;
	mFileUrl = mFileUrl
			+ "/flow/openflowcfg?Act=selectuserlist&type=1&ExecuteNo="
			+ str_ExecuteNo + "&CNodeNo=" + str_CNodeNo + "&NodeNo="
			+ str_NodeNo + "&OtherID=" + str_OtherID + "&ParentID="
			+ str_ParentID + "&ParentID1=" + str_ParentID1 + "&SELECTDEPTID="
			+ SELECTDEPTID;
	//正常返回SPsnRe1执行
	//$.get(url,{},function(src){
	var index2 = layer.open({
		type : 2,
		title : "选择办理人员",
		area : [ '550px', '400px' ],
		fix : false, //不固定
		maxmin : false,
		content : mFileUrl
	});
	//})
}

//SelectDoUser选到后返回---
function SPsnRe1(strValue, msg) {
	webform.Do_User_Nos.value = strValue;
	webform.MsgType.value = msg;
	doSubmit_start(5);//正常选择到人员，调用doSubmit_start(5)继续执行

}

//选择意见
function SelectIdea() {
	var mFileUrl = webform.WEBPATH.value;
	mFileUrl = mFileUrl + "/flow/openflowcfg?Act=openflowidea";

	//$.get(url,{},function(src){
	var index = layer.open({
		type : 2,
		title : "选择个人意见",
		area : [ '550px', '400px' ],
		fix : false, //不固定
		maxmin : false,
		content : mFileUrl
	});
	//})
}

//返回选择后的意见
function selectidea() {
	var row = $('#idealist').datagrid('getSelected');
	if (row) {
		webform.DoIdea.value = row.CONTENT;
		layer.closeAll();
	} else {
		layer.msg('请先选择意见。');
	}
}

//选择用户(退回)
function SelectUpDoUser() {
	var mFileUrl = "";
	var str_CNodeNo = webform.Node_No_S.value;
	var str_NodeNo = webform.S_Node_No_S_E.value;
	var str_ExecuteNo = webform.Execute_No.value;
	var str_OtherID = webform.OtherID.value;
	var str_ParentID = webform.ParentID.value;
	var str_ParentID1 = webform.ParentID1.value;
	var SELECTDEPTID = webform.SELECTDEPTID.value;

	var arrlist = "";
	arrlist = str_NodeNo.split("/");
	str_NodeNo = arrlist[0];

	var AttMessage = "";

	mFileUrl = webform.WEBPATH.value;
	mFileUrl = mFileUrl
			+ "/flow/openflowcfg?Act=selectuserlist&type=2&ExecuteNo="
			+ str_ExecuteNo + "&CNodeNo=" + str_CNodeNo + "&NodeNo="
			+ str_NodeNo + "&OtherID=" + str_OtherID + "&ParentID="
			+ str_ParentID + "&ParentID1=" + str_ParentID1 + "&SELECTDEPTID="
			+ SELECTDEPTID;

	//正常返回SPsnUpRe1('','');执行
	//$.get(url,{},function(src){
	var index = layer.open({
		type : 2,
		title : "选择办理人员",
		area : [ '550px', '400px' ],
		fix : false, //不固定
		maxmin : false,
		content : mFileUrl
	});
	//})
}

//SelectUpDoUser选到后返回---
function SPsnUpRe1(strValue, msg) {
	webform.Do_User_Nos.value = strValue;
	webform.MsgType.value = msg;
	doUntread_start(3);//正常选择到人员，调用doUntread_start(3)继续执行
}

//选择办理部门(提交时)
function SelectDoDept() {
	var mFileUrl = webform.WEBPATH.value;
	mFileUrl = mFileUrl + "/flow/openflowcfg?Act=selectunit&type=1";
	//正常返回SPsnRe(strValue)执行
	//$.get(url,{},function(src){
	var index = layer.open({
		type : 2,
		title : "选择办理部门",
		area : [ '550px', '400px' ],
		fix : false, //不固定
		maxmin : false,
		content : mFileUrl
	});
	//})

}

//选到后返回---
function SPsnRe(strValue) {
	webform.Do_User_Nos.value = strValue;
	doSubmit_start(5);//正常选择到人员，调用doSubmit_start(5)继续执行
}

//选择办理人员(提交时)
function SelectDoPsn() {
	var str_NodeNo = webform.M_Node_No_S_E.value;
	var arrlist = str_NodeNo.split(",")[0].split("/");
	str_NodeNo = arrlist[0];//得到待处理的步骤id

	var mFileUrl = webform.WEBPATH.value;
	mFileUrl = mFileUrl + "/flow/openflowcfg?Act=selectuser&type=1&nodeno="
			+ str_NodeNo;
	//正常返回SPsnRe(strValue)执行
	//$.get(url,{},function(src){
	var index = layer.open({
		type : 2,
		title : "选择办理人员",
		area : [ '550px', '400px' ],
		fix : false, //不固定
		maxmin : false,
		content : mFileUrl
	});
	//})
}

//选择办理部门(退回时)
function SelectUpDept() {
	var mFileUrl = webform.WEBPATH.value;
	mFileUrl = mFileUrl + "/flow/openflowcfg?Act=selectunit&type=2";
	//正常返回SPsnRe(strValue)执行
	//$.get(url,{},function(src){
	var index = layer.open({
		type : 2,
		title : "选择办理部门",
		area : [ '550px', '400px' ],
		fix : false, //不固定
		maxmin : false,
		content : mFileUrl
	});
	//})
}

//选到后返回---
function SPsnUpRe(strValue) {
	webform.Do_User_Nos.value = strValue;
	doUntread_start(3);//正常选择到人员，调用doUntread_start(3)继续执行
}

//选择办理人员(退回时)
function SelectUpPsn() {
	var str_NodeNo = webform.S_Node_No_S_E.value;
	var arrlist = str_NodeNo.split(",")[0].split("/");
	str_NodeNo = arrlist[0];//得到待处理的步骤id

	var mFileUrl = webform.WEBPATH.value;
	mFileUrl = mFileUrl + "/flow/openflowcfg?Act=selectuser&type=2&nodeno="
			+ str_NodeNo;
	//正常返回SPsnRe(strValue)执行
	//$.get(url,{},function(src){
	var index = layer.open({
		type : 2,
		title : "选择办理人员",
		area : [ '550px', '400px' ],
		fix : false, //不固定
		maxmin : false,
		content : mFileUrl
	});
	//})
}

//选择下一分支节点
function SelectNextID(str_E) {
	var mFileUrl = webform.WEBPATH.value;
	mFileUrl = mFileUrl + "/flow/openflowcfg?Act=selectflowactivity";
	mFileUrl = mFileUrl + "&type=1&Node_No_S=" + webform.Node_No_S.value
			+ "&Node_No_E=" + str_E;
	//$.get(url,{},function(src){
	var index = layer.open({
		type : 2,
		title : "选择流程步骤",
		area : [ '400px', '300px' ],
		fix : false, //不固定
		maxmin : false,
		content : mFileUrl
	});
	//})

}

//SelectNextID回调
function setSelectNextID(strValue) {
	webform.M_Node_No_S_E.value = strValue;
	doSubmit_start(3);
}

//选择上一分支节点
function SelectUpID(str_E) {
	var mFileUrl = webform.WEBPATH.value;
	mFileUrl = mFileUrl + "/flow/openflowcfg?Act=selectflowactivity";
	mFileUrl = mFileUrl + "&type=2&Node_No_S=" + webform.Node_No_S.value
			+ "&Node_No_E=" + str_E;
	//$.get(url,{},function(src){
	var index = layer.open({
		type : 2,
		title : "选择流程步骤[退回]",
		area : [ '550px', '400px' ],
		fix : false, //不固定
		maxmin : false,
		content : mFileUrl
	});
	//})
}

//SelectUpID回调
function setSelectUpID(strValue) {
	webform.S_Node_No_S_E.value = strValue;
	doUntread_start(2);
}

//后退
function doUp() {
	history.back();
}

//前进
function doDown() {
	history.forward();
}

//弹出新窗体(属性设置)
function OpenSetup(url) {
	var name = "PopWindow";
	var ShowPopWindow = null;
	var status = "toolbar=no,location=no,directories=no,status=no,menubar=no,resizable=yes,scrollbars=yes,width=500,height=380";
	ShowPopWindow = window.open(url, name, status);
	ShowPopWindow.moveTo(280, 200);
	ShowPopWindow.focus();
}

//弹出新窗体1(属性设置)
function OpenSetup1(url) {
	var name = "PopWindow";
	var ShowPopWindow = null;
	var status = "toolbar=no,location=no,directories=no,status=no,menubar=no,resizable=yes,scrollbars=yes,width=600,height=560";
	ShowPopWindow = window.open(url, name, status);
	ShowPopWindow.moveTo(200, 50);
	ShowPopWindow.focus();
}

//弹出打印窗口
function Openprint(url) {
	var w, h, s;
	var IndexWin = null;
	url = url + "&AID=" + webform.Node_No_S.value + "&DOCID="
			+ webform.FormID.value;

	w = screen.availWidth - 4;
	h = screen.availHeight - 10;
	s = "directories=no,left=0,top=0,location=no,menubar=no,resizable=yes,scrollbars=yes,status=yes,toolbar=no,width="
			+ w + ",height=" + h;
	IndexWin = window.open(url, "print", s);
	IndexWin.moveTo(-4, -4);
	IndexWin.focus();
}

//打开显示流程运行图示窗口
function OpenFlowMap(strURL) {
	var url = strURL;
	//$.get(url,{},function(src){
	var index = layer.open({
		type : 2,
		title : "流程步骤",
		area : [ '800px', '500px' ],
		fix : false, //不固定
		maxmin : false,
		content : url
	});
	//})
}

//弹出最大窗体(新建流程)
function OpenCLinFlowWindow(strURL) {
	var w, h, s;

	var OtherID = document.webform.OtherID.value;
	var ParentID = document.webform.ParentID.value;
	var ParentID1 = document.webform.ParentID1.value;

	var MenuWindow = null;
	w = screen.availWidth - 4;
	h = screen.availHeight - 10;
	s = "directories=no,left=0,top=0,location=no,menubar=no,resizable=yes,scrollbars=no,status=no,toolbar=no,width="
			+ w + ",height=" + h;
	if (strURL.indexOf("OtherID=") == -1) {
		strURL = strURL + "&OtherID=" + OtherID + "&ParentID=" + ParentID
				+ "&ParentID1=" + ParentID1 + "&type=1";
	}
	MenuWindow = window.open(strURL, "_blank", s);
	MenuWindow.moveTo(-4, -4);
	MenuWindow.focus();
}

//弹出最大窗体(打开流程)
function OpenLinFlowWindow(strURL) {
	var w, h, s;

	var OtherID = document.webform.OtherID.value;
	var ParentID = document.webform.ParentID.value;
	var ParentID1 = document.webform.ParentID1.value;

	var MenuWindow = null;
	w = screen.availWidth - 4;
	h = screen.availHeight - 10;
	s = "directories=no,left=0,top=0,location=no,menubar=no,resizable=yes,scrollbars=no,status=no,toolbar=no,width="
			+ w + ",height=" + h;
	if (strURL.indexOf("OtherID=") == -1) {
		strURL = strURL + "&OtherID=" + OtherID + "&ParentID=" + ParentID
				+ "&ParentID1=" + ParentID1 + "&type=1";
	}
	MenuWindow = window.open("../../" + strURL, "_blank", s);
	MenuWindow.moveTo(-4, -4);
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
function CheckInputValueExpression(expressionStr, inputValue) {
	var consultStr, consultStr1;
	var inputValueTrim = trim(inputValue);
	consultStr = ">,>=,<,<=,!=,="; //不能出现在值表达式中
	if (consultStr.indexOf(inputValueTrim) > -1) {
		alert("无效的录入!");
		return false;
	}
	consultStr = "+,-,*,/,AND,OR,)";//不能出现在第一个

	if (consultStr.indexOf(inputValueTrim) > -1
			&& trim(expressionStr).length == 0) {
		alert("无效的录入!");
		return false;
	}

	consultStr = "+,-,*,/"; //不能同时出现两个
	if (consultStr.indexOf(right(expressionStr, 1)) > -1
			&& consultStr.indexOf(inputValueTrim) > -1) {
		alert("无效的录入!");
		return false;
	}

	consultStr = "+,-,*,/"; //AND 和 OR 前不能出现的符号

	if (consultStr.indexOf(right(expressionStr, 1)) > -1
			&& (inputValueTrim == "AND" || inputValueTrim == "OR")) {
		alert("无效的录入!");
		return false;
	}

	consultStr = "+,-,*,/,(,)";// 不能在一起的符号
	consultStr1 = "+,-,*,/";
	if (consultStr.indexOf(right(expressionStr, 1)) > -1
			&& consultStr1.indexOf(inputValueTrim) > -1) {
		alert("无效的录入!");
		return false;
	}

	if (right(expressionStr, 1) == "(" && inputValueTrim == ")") {
		alert("无效的录入!");
		return false;
	}

	consultStr = "AND,OR";//不能在一起
	consultStr1 = "AND,OR,0,1,2,3,4,5,6,7,8,9";
	if (consultStr.indexOf(trim(right(expressionStr, 4))) > -1
			&& consultStr1.indexOf(inputValueTrim) > -1
			&& trim(expressionStr).length > 0) {
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
function CheckInputWhereExpression(expressionStr, inputValue) {
	var consultStr, consultStr1;
	var inputValueTrim = trim(inputValue);

	consultStr = "+,-,*,/,AND,OR,)";//不能出现在第一个

	if (consultStr.indexOf(inputValueTrim) > -1
			&& trim(expressionStr).length == 0) {
		alert("无效的录入!");
		return false;
	}

	consultStr = "+,-,*,/"; //不能同时出现两个
	if (consultStr.indexOf(right(expressionStr, 1)) > -1
			&& consultStr.indexOf(inputValueTrim) > -1) {
		alert("无效的录入!");
		return false;
	}

	consultStr = "+,-,*,/"; //AND 和 OR 前不能出现的符号

	if (consultStr.indexOf(right(expressionStr, 1)) > -1
			&& (inputValueTrim == "AND" || inputValueTrim == "OR")) {
		alert("无效的录入!");
		return false;
	}

	consultStr = "+,-,*,/,(,)";// 不能在一起的符号
	consultStr1 = "+,-,*,/";
	if (consultStr.indexOf(right(expressionStr, 1)) > -1
			&& consultStr1.indexOf(inputValueTrim) > -1) {
		alert("无效的录入!");
		return false;
	}

	if (right(expressionStr, 1) == "(" && inputValueTrim == ")") {
		alert("无效的录入!");
		return false;
	}

	consultStr = "AND,OR";//不能在一起
	consultStr1 = "AND,OR,0,1,2,3,4,5,6,7,8,9";
	if (consultStr.indexOf(trim(right(expressionStr, 4))) > -1
			&& consultStr1.indexOf(inputValueTrim) > -1
			&& trim(expressionStr).length > 0) {
		alert("无效的录入!");
		return false;
	}

	return true;
}

/**************************新加方法2011-04-20 ****************************************/
function saveColl1(type, id) {
	if (isSave == "1") {
		return;
	}
	isSave == "1";

	webform.type.value = "";//清空
	webform.type.value = type;

	//前进必填留言
	var ISLEAVE1 = webform.ISLEAVE1.value;
	if (type == "2" && ISLEAVE1 == "1")//提交必填留言
	{
		var DoIdea = webform.DoIdea.value;
		if (DoIdea.length == 0) {
			layer.msg('请先填写留言或意见!');
			return;
		}
	}

	//前进保存数据
	var ISSAVE1 = webform.ISSAVE1.value;
	if (ISSAVE1 == "1") {

		var isFalse = window.CollDataTable.notre_saveColl();
		if (isFalse == true) {
			window.setTimeout(doSubmit1(id), 1000);
		}

	} else {
		doSubmit1(id);
	}
	return;
}

//提交
function doSubmit1(id) {
	var needFlow = webform.NeedNewFlow.value;
	if (needFlow.length > 0) {
		layer.msg("请先新建下列流程:\r\n" + needFlow)
		return (false);
	}
	var atttype = webform.ATTTYPE.value;
	if (atttype == "2" || atttype == "3")//可上传附件
	{
		var attnum1 = webform.ATTNUM.value;
		var attnum2 = document.att.document.RegForm.ATTNUM.value;
		if (attnum2 < attnum1)//附件没有上传完
		{
			var attnum3 = attnum1 - attnum2;
			webform.AttMessage.value = "请先上传附件!\r\n" + "已上传" + attnum2
					+ "个,还需上传" + attnum3 + "个。";
		} else {
			webform.AttMessage.value = "";
		}
	}
	var AttMessage = webform.AttMessage.value;//当前步骤附件提示情况
	if (AttMessage.length > 0) {
		layer.msg(AttMessage)
		return (false);
	}
	webform.M_Node_No_S_E.value = id + "/6";
	doSubmit_start(4);
}

function doUntread1(id) {
	//后退必填留言
	var ISLEAVE2 = webform.ISLEAVE2.value;
	webform.S_Node_No_S_E.value = id;
	webform.M_Node_No_S_E.value = id;

	if (ISLEAVE2 == "1") {
		var DoIdea = webform.DoIdea.value;
		if (DoIdea.length == 0) {
			layer.msg('请先填写留言或意见！');
			return;
		}
	}
	saveColl('1');//直接保存
	doUntread_start(2);
}

//确认处理(提交,多人同时处理提交时用到)
function submitFinish() {
	var strPath = webform.FlowFormPath.value;
	var strExecuteNo = webform.Execute_No.value;//流程运转ID
	var strcid = webform.Node_No_S.value;//当前活动编号

	var mFileUrl = webform.WEBPATH.value;
	mFileUrl = mFileUrl + "/flow/openflowcfg?Act=IsShow";
	mFileUrl = mFileUrl + "&type=1&ExecuteNo=" + strExecuteNo + "&cid="
			+ strcid;
	layer.msg("正在处理...");
	$.get(mFileUrl, {}, function(src) {

	})
}

//submitFinish检测返回时调用--
function submitFinishRe(strValue) {
	if (strValue == "1") {
		if (strPath.match("/collect/collengine")) {
			if (!saveColl('1'))//先保存
			{
				return false; //检测保存未通过；
			}
		}
		dosendFinish();
	} else {
		saveColl('2');
	}
}

//确认处理(退回,多人同时处理退回时用到)
function UntreadFinish() {
	var strPath = webform.FlowFormPath.value;
	var strExecuteNo = webform.Execute_No.value;//流程运转ID
	var strcid = webform.Node_No_S.value;//当前活动编号
	var mFileUrl = webform.WEBPATH.value;
	mFileUrl = mFileUrl + "/flow/openflowcfg?Act=IsShow";
	mFileUrl = mFileUrl + "&type=2&ExecuteNo=" + strExecuteNo + "&cid="
			+ strcid;
	layer.msg("正在处理...");
	$.get(mFileUrl, {}, function(src) {

	})
}

//UntreadFinish返回时调用--
function UntreadFinishRe(strValue) {
	if (strValue == "1") {
		if (strPath.match("/collect/collengine")) {
			if (!saveColl('1'))//先保存
			{
				return false; //检测保存未通过；
			}
		}
		dosendFinish();
	} else {
		doUntread();
	}
}